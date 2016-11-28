package task.basic;

import java.util.List;

import mapper.link.ResourceMapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import persistence.SessionFactory;
import persistence.entity.Resource;

import task.base.Task;
import task.specific.InfoPersistTask;
import test.persistence.ResourcePersistence;
import exception.DataIncorrectException;

public abstract class ResourcePersistTask extends Task{
	
	final static Logger logger = Logger.getLogger(ResourcePersistence.class);
	

	
	public boolean checkIfExist(Resource resource) {
		SqlSession session = SessionFactory.getInstance().openSession();
		try {
			List<Resource> resources = session.getMapper(ResourceMapper.class).selectByCodeAndName(resource);
			if(resources == null || resources.size() == 0)
				return false;
			if(resources.size() != 1)
				throw new DataIncorrectException();
			return true;
			
		} catch (Exception e) {
			logger.fatal("fetch resource exist info for <" + resource.getResourceName() + "> failed", e);
			return false;
		} finally {
			session.close();
		}
	}
	
	public void persistResource(Resource resource) {
		queue.offer(new InfoPersistTask<Resource>(queue, resource));
	}
}
