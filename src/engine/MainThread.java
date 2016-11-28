package engine;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import crawler.moeyo.ListPageExtractor;

import task.base.Task;
import task.basic.ExtractInfoAndLinksTask;


import engine.entity.Source;

public class MainThread {
	
	static final Logger logger = Logger.getLogger(MainThread.class);
	
	private static List<Source> sourceList = new ArrayList<Source>();
	
	public static void createBatchJob(Source source) {
		if(source.getTotalPageNum() == 0) 
			return;
		for(int i = 1; i <= source.getTotalPageNum(); i++) {
			Source element = new Source();
			element.setName(source.getName());
			element.setDirectoryBase(source.getDirectoryBase());
			String newUrl = source.getTargetUrl() + "page/" + i + "/";
			element.setTargetUrl(newUrl);
			
			sourceList.add(element);
		}
	}
	
	static {
		
		
		Source source8 = new Source();
		source8.setName("100 pages");
		source8.setDirectoryBase("C:\\moeyo\\");
		source8.setTargetUrl("http://www.moeyo.com/category/figures/");
		source8.setTotalPageNum(20);
		createBatchJob(source8);
//		
//		Source source9 = new Source();
//		source9.setName("first page");
//		source9.setDirectoryBase("C:\\moeyo\\");
//		source9.setTargetUrl("http://www.moeyo.com/category/figures");
//		sourceList.add(source9);
		
	}
	
	public static List<Source> getSourceList() {
		return sourceList;
	}
	
	public static void main(String args[]) {
		
		logger.info("initializing...");
		logger.info("-------------------task start----------------------");
		BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
		ExecutorService executor = Executors.newFixedThreadPool(40);
		for(Source source: getSourceList()) {
			Task mainTask = new ExtractInfoAndLinksTask(source.getTargetUrl(),
					queue, source.getDirectoryBase(), ListPageExtractor.getExtractor());
			Thread t = new Thread(mainTask);
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				logger.fatal("exception in main thread", e);
			}

			Task task = null;
			do {
				try {
					task = queue.poll(5,TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					logger.fatal("exception in main thread when poll from queue", e);
				}
				if (task != null)
					executor.execute(task);
			} while (Zookeeper.getInstance().getCounter() > 0);
		}
		
		executor.shutdown();
		// find out if executor is terminated
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("-------------------task finished----------------------");
		logger.info("all done");
	}
	
}
