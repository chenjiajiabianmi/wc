package task.basic;


import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import persistence.entity.Resource;
import persistence.entity.base.BaseModel;
import task.base.Task;
import task.specific.InfoPersistTask;
import task.specific.JpgDownloadTask;
import connection.core.PooledConnectionManager;
import crawler.base.Extractor;
import crawler.entity.base.ExtractedElements;
import crawler.entity.base.ResourceElement;
import crawler.entity.base.SeedElement;



/**
 * this is the most essential implement for a web page fetch process,
 * you could implement this class and only focus on extract info from the web page
 * 
 * when create a new ExtractInfoAndLinksTask instance
 * you need to pass
 * 1. url -- for the page you want to process
 * 2. queue -- the 'core task queue' which save all task for a thread-pool consumer
 * 3. directoryBase -- if you need to save any resource from this page, like JPG
 * you may want to specific the folder to save them
 * 4. extractor -- this is what you need to do!! 
 * the specific extractor to extract info from this web page,
 * you should test until you sure there is no exception
 * 
 * after create your own extractor, and new a ExtractInfoAndLinksTask instance,
 * it will do the following works
 * 1. download the content from 'url'
 * 2. extract data from the content using your EXTRACTOR, returns a ExtractedElements
 * 3. persist page info saved in List<BaseModel>, here you need to specific your own Entity to store the info,
 * your entity should extends BaseModel
 * 4. fetch new links you extract from the content, which will saved in List<SeedElement>
 * 5. save resource like 'JPG', which you should give the resource link and save them in List<ResourceElement>
 * 
 * note that 
 * 3 will create new InfoPersistTask, 
 * 4 will create new ExtractInfoAndLinksTask, 
 * 5 will create new ResourcePersistTask, and the ResourcePersistTask will create its' own InfoPersistTask
 * 
 * @author Hammer
 *
 */
public class ExtractInfoAndLinksTask extends Task implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ExtractInfoAndLinksTask.class);
	
	/**
	 * the URL for current page task 
	 */
	protected String targetUrl;
	
	/**
	 * where your save the resource file 
	 * when you don not want to save any
	 * pass 'null'
	 */
	protected String directoryBase;
	
	/**
	 * the extractor for current page
	 * which you MUST implemented by yourself
	 */
	protected Extractor extractor;
	
	public ExtractInfoAndLinksTask(String url, BlockingQueue<Task> queue, String directoryBase, Extractor extractor) {
		targetUrl = url;
		// core task queue
		this.queue = queue;
		this.directoryBase = directoryBase;
		this.extractor = extractor; 
	}
	
	public void process() {
		String html = null;
		/**
		 * 1. get HTML content from remote, when failed end task by return 'null'
		 */
		html = downloadContent();
		if(html == null) {
			logger.error("request to <" + targetUrl + "> failed, response is 'null'");
			return;
		}
		/**
		 * 2. elements extracted from HTML content, include:
		 * 		a. page info
		 * 		b. internal links as new target
		 * 		c. resource to be saved
		 * 		d. info to be persist in DB
		 */
		ExtractedElements elements = extractor.extractDataModel(targetUrl, html);
		if (null == elements) {
			logger.fatal("elements extraction failed on request : <" + targetUrl + ">");
			return;
		}
		//fetch flag in return elements to decide if content should be discard
		if (elements.isShouldBeDiscard()) {
			logger.info("request for details element should be discard : [" + targetUrl + "]");
			return;
		}
		
		persistenceInfo(elements);
		
		/**
		 * 3. get links from current page, initiate new tasks for each link
		 */
		fetchInsidePages(elements);

		/**
		 * 4. get resource links from current page, initiate resource saving tasks
		 */
		saveResources(elements);
	}
	
	/**
	 * you can change the (retry times) and (frequency for retry)
	 * 
	 * @return
	 */
	protected String downloadContent() {
		return PooledConnectionManager.getManager().getString(targetUrl,3000,3);
	}
	
	
	protected void persistenceInfo(ExtractedElements elements) {
		if(elements == null || elements.getPersistInfos() == null || elements.getPersistInfos().size() == 0)
			return;
		for(BaseModel entity: elements.getPersistInfos()) {
			queue.offer(new InfoPersistTask<BaseModel>(queue, entity));
		}
	}
	
	protected void fetchInsidePages(ExtractedElements elements) {
		if(elements == null || elements.getSeeds() == null || elements.getSeeds().size() == 0)
			return;
		
		for(SeedElement seed: elements.getSeeds()) {
			if(seed.getDirectoryAppender() == null) {
				queue.offer(new ExtractInfoAndLinksTask(seed.getTargetUrl(), queue, directoryBase, seed.getExtractor()));
			} else {
				// if resource need to be saved in the new page, maybe you need a new directory base to save them
				String folderPath = directoryBase + seed.getDirectoryAppender();
				queue.offer(new ExtractInfoAndLinksTask(seed.getTargetUrl(), queue, folderPath, seed.getExtractor()));

			}
		}
	}
	
	protected void saveResources(ExtractedElements elements) {
		if(elements == null || elements.getResources() == null || elements.getResources().size() == 0)
			return;
		createDirectory();
		for(ResourceElement resource: elements.getResources()) {
			Resource persistResource = new Resource();
			persistResource.setOriginId(resource.getOriginId());
			persistResource.setResourceName(resource.getResourceName());
			persistResource.setResourceType(resource.getResourceType());
			persistResource.setUrl(resource.getUrl());
			persistResource.setOriginalLocation(resource.getOriginalLocation());
			queue.offer(new JpgDownloadTask(persistResource, queue, directoryBase));
		}
	}
	
	// if this is necessary ?
	private void createDirectory() {
	}
	
}
