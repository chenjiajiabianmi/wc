package test.task.specific;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import persistence.entity.Resource;
import task.base.Task;
import task.specific.InfoPersistTask;

public class InfoPersistTaskTestCase {
	
	public static void main(String args[]) {
		BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
		Resource r = new Resource();
		r.setCreateDate(new Date());
		r.setModifyDate(new Date());
		r.setOriginId("test");
		r.setResourceName("test");
		r.setResourceType("test");
		r.setUrl("http://test");
		Task t = new InfoPersistTask(queue, r);
		t.run();
	}

}
