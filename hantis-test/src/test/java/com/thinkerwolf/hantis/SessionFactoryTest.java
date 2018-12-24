package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.example.*;
import com.thinkerwolf.hantis.session.*;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class SessionFactoryTest {

    @Test
    public void sessionTest() throws IOException {
        Resource resource = new ClassPathResource("hantis_jdbc.xml");
        Configuration cfg = new Configuration();
        cfg.config(resource.getInputStream());
        SessionFactoryBuilder sfb1 = cfg.getSessionFactoryBuilder("development1");
        SessionFactory sf1 = sfb1.build();

        Session session = sf1.openSession();
        
        try {
            Map<String, Object> p = new HashMap<>();
            StopWatch sw = StopWatch.start();
            session.beginTransaction();


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
            session.create(blog2);

            session.commit();

            /*for (int i = 0; i < 1000; i++) {
                 p.put("id", 10000);
            	 List<Blog> list = session.selectList("tableBlog.selectOne", p);
            	 
            	 if (i == 54) {
            		 Calendar cal = Calendar.getInstance(Locale.CHINA);
            		 p.put("id", i);
                     p.put("title", "hantis_" + i);
                     p.put("content", "hantis insert_" + i);
                     p.put("createTime", cal.getTime());
                     session.update("tableBlog.insertOne", p);
                     session.commit();
                     p.remove("title");
                     p.remove("content");
                     p.remove("createTime");
                    // System.out.println(new Timestamp(cal.getTime().getTime()));
            	 } else {
            		 System.out.println(list.size());
            	 }
            	 
                 
            }*/
            
           
            
           /* p.put("id", 4);
            List<Blog> list1 = session.selectList("tableBlog.selectOne", p);
            System.out.println(list1);
            
            p.put("id", 3);
            List<Blog> list2 = session.selectList("tableBlog.selectOne", p);
            System.out.println(list2);*/
            
            /*Calendar cal = Calendar.getInstance(Locale.CHINA);
           
            for (int i = 1000; i <= 11000; i++) {
                p.put("id", i);
                p.put("title", "hantis_" + i);
                p.put("content", "hantis insert_" + i);
                p.put("createTime", cal.getTime());
                session.update("tableBlog.insertOne", p);
            }
            
            */
            
            System.out.println("Spend time : " + sw.end());
        } finally {
            session.close();
        }


    }

   // @Test
    public void autoGenerate() throws Exception {
        Resource resource = new ClassPathResource("hantis.xml");
        Configuration cfg = new Configuration();
        cfg.config(resource.getInputStream());
        SessionFactoryBuilder sfb = cfg.getSessionFactoryBuilder("development1");
        SessionFactory sf = sfb.build();
        Session session = sf.openSession();
        
        try {
        	for (int i = 0; i < 1000; i++) {
                User user = new User();
                user.setName("robot_" + i);
                session.create(user);
            }
        	session.commit();
        } finally {
            session.close();
        }


    }



}