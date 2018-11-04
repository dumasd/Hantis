package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.example.*;
import com.thinkerwolf.hantis.session.*;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            p.put("id", 3);
            List<Blog> list = session.selectList("tableBlog.selectOne", p);
            System.out.println(list);

            StopWatch sw = StopWatch.start();
            for (int i = 1000; i <= 11000; i++) {
                p.put("id", i);
                p.put("title", "hantis_" + i);
                p.put("content", "hantis insert_" + i);
                session.update("tableBlog.insertOne", p);
            }
            session.commit();
            System.out.println("Insert spend time : " + sw.end());
            //session1.commit();
            // batch 5116   nobatch 7074
        } finally {
            session.close();
        }


    }


}
