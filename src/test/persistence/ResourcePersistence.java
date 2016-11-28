package test.persistence;

import java.util.Date;
import java.util.List;

import mapper.link.ResourceMapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.log4j.Logger;

import persistence.SessionFactory;
import persistence.entity.Resource;

public class ResourcePersistence {

	final static Logger logger = Logger.getLogger(ResourcePersistence.class);
	
	public static void main(String args[]) {
		testInsertWhetherDuplicate();
		testSelectByCodeAndName();
	}
	
	public static void testInsertWhetherDuplicate() {
		SqlSession session = SessionFactory.getInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
		ResourceMapper mapper = session.getMapper(ResourceMapper.class);
		Resource r = new Resource();
		r.setCreateDate(new Date());
		r.setModifyDate(new Date());
		r.setOriginalLocation("test");
		r.setOriginId("test");
		r.setResourceName("test");
		r.setResourceType("test");
		r.setUrl("test");
		int result = mapper.insertWhetherDuplicate(r);
		logger.info("insert " + result + " rows");
		session.commit();
		session.close();
	}
	
	public static void testSelectByCodeAndName() {
		SqlSession session = SessionFactory.getInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
		ResourceMapper mapper = session.getMapper(ResourceMapper.class);
		Resource r = new Resource();
		r.setResourceName("test");
		r.setOriginId("test");
		List<Resource> result = mapper.selectByCodeAndName(r);
		session.close();
		int counter = 0;
		if(result != null)
			counter = result.size();
		logger.info("retrieve from resource get " + counter + " result");
	}
}
