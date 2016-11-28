package connection.core;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;

import org.apache.http.NoHttpResponseException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import connection.constants.Constant;
import connection.entity.ByteFile;
import connection.entity.ConManagerConfig;
import exception.TimeoutException;


public class PooledConnectionManager {
	
	final static Logger logger = Logger.getLogger(PooledConnectionManager.class);
	
	private static final int timeOut = 10 * 1000;
	
	private static CloseableHttpClient client = null;
	
	private final static Object syncLock = new Object();
	
	private static PooledConnectionManager manager;
	
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();

	/**
	 * create singleton httpclient with parameter in config
	 * @param config
	 */
	private PooledConnectionManager(ConManagerConfig config) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(config.getMaxTotal());
		cm.setDefaultMaxPerRoute(config.getMaxPerRoute());
//		cm.setMaxPerRoute(config.maxRoute);
		client = HttpClients.custom().setConnectionManager(cm).build();
	}
	
	/**
	 * create singleton httpclient with default parameter
	 */
	private PooledConnectionManager() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		client = HttpClients.custom().setConnectionManager(cm).build();
	}
	
	/**
	 * get singleton httpclient
	 * 
	 * @return
	 */
	public static PooledConnectionManager getManager() {
		if(client == null) {
			synchronized(syncLock) {
				if(client == null) {
					manager = new PooledConnectionManager();
				}
			}
		}
		return manager;
	}
	
	public static PooledConnectionManager getManager(ConManagerConfig config) {
		if(client == null) { 
			synchronized(syncLock) {
				if(client == null) {
					manager = new PooledConnectionManager(config);
				}
			}
		}
		return manager;
	}
	
	/**
	 * get simple HTML 
	 * when encounter 3 types exception
	 * we sure the file exist and failed is caused by pool network
	 * we maybe would like to retry:
	 * 1. SocketTimeoutException
	 * 2. TruncatedChunkException
	 * 3. ConnectionClosedException
	 * 4. NoHttpResponseException
	 * 
	 * @param url
	 * @return
	 */
	public String getString(String url) {
		HttpGet request = new HttpGet(url);
		request.setConfig(requestConfig);
		CloseableHttpResponse response = null; 
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String status = response.getStatusLine().toString();
			String fileType = entity.getContentType().getValue();
			logger.info("request url : [" + url + "], response status : [" + status + "], file-type : [" + fileType + "]");

            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return result;
		} catch (SocketTimeoutException e) {
			logger.error("timeout on request : [" + url + "]", e);
			throw new TimeoutException();
		} catch (NoHttpResponseException e) {
			// maybe server id done, or connection lost
			logger.fatal("no respond from url : [" + url + "]", e);
			throw new TimeoutException();
		} catch (TruncatedChunkException e) {
			// did not get all the parts of current HTML
			//org.apache.http.TruncatedChunkException: Truncated chunk ( expected size: 8106; actual size: 6261)
			logger.error("html content lost partially for url: [" + url + "]", e);
			throw new TimeoutException();
		} catch (ConnectionClosedException e) {
			// server not responding when sending part of the HTML content
			//org.apache.http.ConnectionClosedException: Premature end of Content-Length delimited message body (expected: 1900; received: 1548
			logger.error("html fetching encounter connection closed : [" + url + "]", e);
			throw new TimeoutException();
		} catch (IOException e) {
			logger.fatal("unknow IO ex for request : [" + url + "]", e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (TimeoutException e) {
            	logger.error("timeout when closing response return by : [" + url + "]", e);
            	throw new TimeoutException();
        	} catch (IOException e) {
        		logger.fatal("unknow IO ex when closing response teturn by : [" + url + "]", e);
             }
        }
        return null;
	}
	
	/**
	 * retry for #{times} with #{timeout} for #{url}
	 * when encounter timeout ex, counter will not be reduce
	 * when encounter unknown ex, counter reduce
	 * 
	 * @param url
	 * @param timeout
	 * @param times
	 * @return
	 */
	public String getString(String url, int timeout, int times) {
		String html = null;
		boolean exFlag = false;
		do{
			try{
				html = getString(url);
			} catch (TimeoutException e) {
				// if the failure is cause by timeout, the retry time should not be reduce
				logger.error("retry request : [" + url + "] for [ " + times + " ]", e);
				times++;
				exFlag = true;
			} catch (Exception e) {
				logger.error("fatal error encounter when request to : [" + url + "]", e);
				exFlag = true;
			}
			// consider then exception encountered, may be HTML is not null and also not correct
			if(html != null && !exFlag)
				return html;
			// reset flag to normal
			exFlag = false;
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException ex) {
				logger.fatal("thread hault ex for request : [" + url + "] fatal error", ex);
			}
			times--;
			
		} while (times > 0);
		// when no respond HTML fetched from url, it's an unusual exception 
		logger.fatal("WARNNING!! failed on request : [" + url + "]");
		return null;
	}
	
	/**
	 * get file by byte[], usually fetch JPG, MP4
	 * 1. when fetch success, return byteFile
	 * 2. when fetched resource encounter a type mismatch, return byteFile with null stream
	 * 3. when any exception encountered, return null
	 * exception include: 
	 * a.socket time out, 
	 * b.connection closed unexpected, 
	 * c.no respond from target server,
	 * d.for other exception, which is unexpected, should not return time out exception
	 * 
	 * @param url
	 * @return
	 */
	public ByteFile getByte(String url) {
		HttpGet request = new HttpGet(url);
		request.setConfig(requestConfig);
		CloseableHttpResponse response = null; 
		ByteFile bf = new ByteFile();
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String status = response.getStatusLine().toString();
			String fileType = entity.getContentType().getValue();
			logger.info("request url : [" + url + "], response status : [" + status + "], file-type : [" + fileType + "]");

			if (!Constant.FILE_TYPES.contains(fileType)) {
				logger.info("file type not matched : " + url);
				bf.setStream(null);
				bf.setTypeMismatch(true);
				return bf;
			}
            byte[] result = EntityUtils.toByteArray(entity);
            EntityUtils.consume(entity);
            bf.setStream(result);
            bf.setTypeMismatch(false);
            return bf;
		} catch (SocketTimeoutException e) {
			logger.error("timeout request : [" + url + "]", e);
			throw new TimeoutException();
		} catch (ConnectionClosedException e) {
			// unfinished source file, when handle stream file, wracked file should be retrieved again
			// org.apache.http.ConnectionClosedException: Premature end of Content-Length delimited message body
			logger.error("unfinished request : [" + url + "]", e);
			throw new TimeoutException();
		} catch (NoHttpResponseException e) {
			//org.apache.http.NoHttpResponseException: www.google.co.jp:80 failed to respond
			logger.error("no response for : [" + url + "]", e);
			throw new TimeoutException();
        } catch (IOException e) {
        	logger.fatal("unknow io ex for request : [" + url + "]", e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
            	logger.fatal("unknow io ex for request : " + url, e);
            	throw new TimeoutException();
            }
        }
		return null;
        
	}
	
	/**
	 * retry request for #{url} with times #{times} and timeout #{timeout}
	 * 1. when response is successfully returned, return the byte[]
	 * 2. when there is a time out exception, increase the times counter, retry request again
	 * 3. when 
	 * 
	 * @param url
	 * @param timeout
	 * @param times
	 * @return
	 */
	public ByteFile getByte(String url, int timeout, int times) {
		ByteFile bf = new ByteFile();
		
		boolean exFlag = false;
		do{
			try{
				bf = getByte(url);
			} catch (TimeoutException e) {
				logger.error("retry request : [" + url + "] timeout", e);
				// when failure is caused by timeout, time should not be reduced
				times++;
				exFlag = true;
			} catch (Exception e) {
				logger.fatal("unexpected response for request [" + url + "]", e);
				exFlag = true;
			}
			// when resource is not null also no exception, so the source file consider to be complete
			if(bf != null && !exFlag)
				return bf;
			
			// reset exception flag to normal
			exFlag = false;
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException ex) {
				logger.fatal("thread hault exception for request : [" + url + "] fatal error", ex);
			}
			logger.info("retry request : [" + url + "] for <" + times + ">");
			times--;
		} while (times > 0);
		logger.fatal("fail to get respond from request : [" + url + "]");
		return null;
	}
	

}
