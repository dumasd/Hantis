package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.conf.xml.XMLConfig;
import org.junit.Test;

import java.io.IOException;

public class XMLConfigTest {

    @Test
    public void xmlConfig() throws IOException {
        Resource resource = new ClassPathResource("jdbc-config.xml");
        XMLConfig xmlConf = new XMLConfig(resource.getInputStream());
        xmlConf.parse();
    }


}
