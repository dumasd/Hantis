package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.jdk.JdkLoggerFactory;
import com.thinkerwolf.hantis.example.*;
import com.thinkerwolf.hantis.session.*;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class SessionFactoryTest {

    @Test
    public void sessionTest() throws IOException {
        InternalLoggerFactory.setDefaultLoggerFactory(new JdkLoggerFactory());
        Resource resource = new ClassPathResource("hantis_jdbc.xml");
        Configuration cfg = new Configuration();
        cfg.config(resource.getInputStream());
        SessionFactoryBuilder sfb1 = cfg.getSessionFactoryBuilder("development1");
        SessionFactory sf1 = sfb1.build();

        Session session = sf1.openSession();
        Session session1 = sf1.openSession();
        try {
            Map<String, Object> p = new HashMap<>();
            StopWatch sw = StopWatch.start();

            List<Blog> blogs = session.selectList("tableBlog.selectOne");

            System.out.println(blogs);
            List<Blog> blogs1 = session1.selectList("tableBlog.selectOne");

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
