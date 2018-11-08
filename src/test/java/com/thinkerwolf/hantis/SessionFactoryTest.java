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
        Resource resource = new ClassPathResource("hantis.xml");
        Configuration cfg = new Configuration();
        cfg.config(resource.getInputStream());
        SessionFactoryBuilder sfb = cfg.getSessionFactoryBuilders().get("development1");
        SessionFactory sf = sfb.build();
        
        Session session = sf.openSession();
        //Session session1 = sf.openSession(true);

        try {
            Map<String, Object> p = new HashMap<>();
            StopWatch sw = StopWatch.start();
            
            for (int i = 0; i < 1000; i++) {
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
            	 
                 
            }
            
           
            
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
            session.commit();
            //session1.commit();
            
            System.out.println("Spend time : " + sw.end());
        } finally {
            session.close();
        }


    }


}
