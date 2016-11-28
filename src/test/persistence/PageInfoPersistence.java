package test.persistence;

import java.util.Date;
import java.util.List;

import mapper.link.PageInfoMapper;
import mapper.link.ResourceMapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.log4j.Logger;

import persistence.SessionFactory;
import persistence.entity.PageInfo;
import persistence.entity.Resource;

public class PageInfoPersistence {

	final static Logger logger = Logger.getLogger(PageInfoPersistence.class);
	
	public static void main(String args[]) {
		testInsertWhetherDuplicate();
		testSelectByCode();
	}
	
	public static void testInsertWhetherDuplicate() {
		SqlSession session = SessionFactory.getInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
		PageInfoMapper mapper = session.getMapper(PageInfoMapper.class);
		PageInfo r = new PageInfo();
		r.setCreateDate(new Date());
		r.setModifyDate(new Date());
		r.setUniqueId("test");
		r.setTitle("test");
		r.setUrl("http://test");
		int result = mapper.insertWhetherDuplicate(r);
		logger.info("insert " + result + " rows");
		session.commit();
		session.close();
	}
	
	public static void testSelectByCode() {
		SqlSession session = SessionFactory.getInstance().openSession(TransactionIsolationLevel.READ_COMMITTED);
		PageInfoMapper mapper = session.getMapper(PageInfoMapper.class);
		PageInfo r = new PageInfo();
		r.setUniqueId("test");
		List<PageInfo> result = mapper.selectByCode(r);
		session.close();
		int counter = 0;
		if(result != null)
			counter = result.size();
		logger.info("retrieve from resource get " + counter + " result");
	}
}
