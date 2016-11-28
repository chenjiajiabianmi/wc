package task.specific;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import persistence.entity.Resource;
import task.base.Task;
import task.basic.ResourcePersistTask;
import connection.core.PooledConnectionManager;
import connection.entity.ByteFile;
import exception.DataIncorrectException;

public class JpgDownloadTask extends ResourcePersistTask implements Runnable{
	
	private static final Logger logger = Logger.getLogger(JpgDownloadTask.class);
	
	private String targetUrl;
	
	
	private String directory = "C:\\test\\";
	
	private Resource resource;
	
	public JpgDownloadTask(Resource resource, BlockingQueue<Task> queue, String directory) {
		this.targetUrl = resource.getUrl();
		this.queue = queue;
		this.directory = directory;
		this.resource = resource;
	}
	
	public void process() {
		String name = resource.getResourceName();
		if(name == null) {
			logger.fatal("resource name is null : [" + targetUrl + "]");
			throw new DataIncorrectException();
		}
		/**
		 * 0. check if the resource has been download already
		 */
		if(checkIfExist(resource)) {
			logger.info("resource target [" + name + "] already exist");
			return;
		}
			
		ByteFile bf = PooledConnectionManager.getManager().getByte(targetUrl, 3000, 3);
		if(bf == null) {
			logger.fatal("request : [ " + targetUrl + " ] response : ByteFile instance is null");
			return;
		}
		byte[] jpg = bf.getStream();
		if(jpg == null) {
			logger.fatal("request : [ " + targetUrl + " ] response : byte[] stream is null");
			return;
		}
		
		/**
		 * 2.save source as a file
		 */
		try {
			FileUtils.writeByteArrayToFile(new File(directory + name), jpg);
		} catch (IOException e) {
			logger.fatal("file " + name + " write to " + directory + "failed", e);
			queue.offer(new JpgDownloadTask(resource, queue, directory));
			return;
		}
		logger.info("request : [" + targetUrl + "] response jpg size : [ " + jpg.length + " ] ");
		
		/**
		 * 3. persist resource info
		 */
		persistResource(resource);
	}
	
}
