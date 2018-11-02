package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.conf.xml.XMLConfig;
import com.thinkerwolf.hantis.example.Blog;
import com.thinkerwolf.hantis.session.Session;
import com.thinkerwolf.hantis.session.SessionFactory;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionFactoryTest {

    @Test
    public void sessionTest() throws IOException {
        Resource resource = new ClassPathResource("hantis.xml");
        XMLConfig xmlConfig = new XMLConfig(resource.getInputStream());
        xmlConfig.parse();
        SessionFactoryBuilder sfb = xmlConfig.getConfiguration().getSessionFactoryBuilders().get("development1");
        SessionFactory sf = sfb.build();
        Session session = sf.openSession();
        
        try {
            Map<String, Object> p = new HashMap<>();
            p.put("id", 3);
            List<Blog> list =  session.selectList("tableBlog.selectOne", p);
            System.out.println(list);

        } finally {
            session.close();
        }


    }



}
