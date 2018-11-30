package com.thinkerwolf.hantis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

public class JtaSessionFactoryTest {

	@Test
	public void jtaSession() throws IOException, SQLException {
		Resource resource = new ClassPathResource("hantis.xml");
		Configuration cfg = new Configuration();
		cfg.config(resource.getInputStream());
		SessionFactoryBuilder sfb1 = cfg.getSessionFactoryBuilders().get("development1");
		SessionFactoryBuilder sfb2 = cfg.getSessionFactoryBuilders().get("development2");
		SessionFactory sf1 = sfb1.build();
		SessionFactory sf2 = sfb2.build();

		Session session = sf1.openSession();
		Transaction transaction = session.beginTransaction();
		
		Session session2 = sf2.openSession();

		try {
			Map<String, Object> p = new HashMap<>();
			StopWatch sw = StopWatch.start();

			p.put("id", "3");
			Blog blog = session.get(Blog.class, p);
			System.out.println(blog);

			Blog blog1 = new Blog();
			blog1.setTitle("hantis");
			blog1.setContent("hantis_content");
			blog1.setUserId(122);
			blog1.setCreateTime(new Date());
			session.create(blog1);

			Blog blog2 = new Blog();
			blog2.setTitle("hantis_t");
			blog2.setContent("hantis_content_t");
			blog2.setUserId(122);
			blog2.setCreateTime(new Date());
			session2.create(blog2);

			transaction.commit();

			System.out.println("Spend time : " + sw.end());
		} finally {
			//transaction.close();
		}

	}

}
