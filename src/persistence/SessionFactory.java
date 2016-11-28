package persistence;

import java.io.IOException;
import java.io.InputStream;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;



public class SessionFactory {
	//防止指令重排
	//http://blog.csdn.net/haoel/article/details/4028232
	//http://coolshell.cn/articles/265.html
	private volatile static  SessionFactory sessionFactory;
	
	private SqlSessionFactory sqlSessionFactory;
	
	
	private SessionFactory() {
		try {
			String resource = "config/mybatis_config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getInstance() {
		if(sessionFactory == null) {
			synchronized(SessionFactory.class) {
				if(sessionFactory == null) {
					sessionFactory = new SessionFactory();
				}
			}
		}
		return sessionFactory.sqlSessionFactory;
	}
}

