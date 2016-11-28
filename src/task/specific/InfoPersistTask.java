package task.specific;

import java.util.concurrent.BlockingQueue;

import mapper.link.base.BaseMapper;
import mapper.util.TypeMatcher;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.log4j.Logger;


import persistence.SessionFactory;
import persistence.entity.base.BaseModel;
import task.base.Task;

/**
 * when you want to persist your own Entity in DB, 
 * YOU NEED TO MAINTIAN relationMap BY YOURSELF
 * 
 * @author Hammer
 *
 * @param <T>
 */
public class InfoPersistTask<T extends BaseModel> extends Task implements Runnable{
	
	private static final Logger logger = Logger.getLogger(InfoPersistTask.class);
	
	private T t;
	private Class<BaseMapper<T>> c;
	
	public InfoPersistTask(BlockingQueue<Task> queue, T t) {
		this.queue = queue;
		this.t = t;
		this.c = (Class<BaseMapper<T>>) TypeMatcher.relationMap.get(t.getClass());
	}
	
	public void process() {
		insertHandleException();
	}
	
	public void insertHandleException() {
		SqlSession s = SessionFactory.getInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
		BaseMapper<T> m = s.getMapper(c);
		try{
			m.insertWhetherDuplicate(t);
			s.commit();
			logger.info("persist " + t + " success");
		} catch (Exception ex) {
			logger.error("FATAL!!! failed when persist" + t, ex);
		} finally {
			s.close();
		}
	}
	
}
