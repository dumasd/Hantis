package com.thinkerwolf.hantis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.thinkerwolf.hantis.transaction.ConnectionUtils;
import org.junit.Test;

import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.example.Blog;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.Session;
import com.thinkerwolf.hantis.session.SessionFactory;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import com.thinkerwolf.hantis.transaction.Transaction;

@SuppressWarnings("unused")
public class JtaSessionFactoryTest {

	@Test
	public void jtaSession() throws IOException, SQLException {
		Resource resource = new ClassPathResource("hantis.xml");
		Configuration cfg = new Configuration();
		cfg.config(resource.getInputStream());
		SessionFactoryBuilder sfb1 = cfg.getSessionFactoryBuilder("development1");
		SessionFactoryBuilder sfb2 = cfg.getSessionFactoryBuilder("development2");
		SessionFactoryBuilder sfb3 = cfg.getSessionFactoryBuilder("development3");
		SessionFactory sf1 = sfb1.build();
		SessionFactory sf2 = sfb2.build();
		SessionFactory sf3 = sfb3.build();

		Session session = sf1.openSession();
		session.beginTransaction();

		Session session2 = sf2.openSession();
		Session session3 = sf3.openSession();

		try {

			StopWatch sw = StopWatch.start();
			
			Blog blog = new Blog();
			//blog.setId(11);
			blog.setTitle("hantis");
			//blog.setContent("hantis_content");
			blog.setUserId(122);
			blog.setCreateTime(new Date());

			session.create(blog);
			session2.create(blog);
			session3.create(blog);
			
			session3.commit();
			session2.commit();
			session.commit();
			System.out.println("Spend time : " + sw.end());

		} catch (Exception e) {
			session2.rollback();
			session3.rollback();
			session.rollback();
		} finally {
			session.close();
			session2.close();
			session3.close();
		}

	}

}
