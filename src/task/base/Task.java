package task.base;

import java.util.concurrent.BlockingQueue;


import org.apache.log4j.Logger;

import engine.Zookeeper;

/**
 * all task must extends this Task.class!!!
 * we maintain a counter to judge if the task in queue has been all done,
 * if you not extends your own *Task from Task.class
 * this could cause fatal failure 
 * 
 * @author Hammer
 *
 */
public abstract class Task implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Task.class);
	
	protected BlockingQueue<Task> queue;
	
	/**
	 * before a task has been create, increate the counter
	 */
	{
		Integer size = Zookeeper.getInstance().increaseCounter();
		logger.info("size of the task pool is <" + size + "> " );
	}
	
	public void run() {
		try {
			process();
		} catch (Exception e) {
			logger.fatal("base task exception -- fatal !!!", e);
		} finally {
			decreaseCounter();
		}
	}
	
	public abstract void process();
	
	/**
	 * after the counter has been finished,
	 * even an exception has been encountered,
	 * reduce the counter
	 */
	public void decreaseCounter() {
		Integer size = Zookeeper.getInstance().decreaseCounter();
		logger.info("size of the task pool after a task has been finished <" + size + "> ; queue size <" + queue.size() + ">");
	}

}
