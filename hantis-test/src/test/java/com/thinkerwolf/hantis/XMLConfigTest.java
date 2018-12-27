package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.io.ClassPathResource;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.xml.XNode;
import com.thinkerwolf.hantis.common.xml.XPathParser;
import com.thinkerwolf.hantis.conf.xml.XMLConfig;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import java.io.IOException;
import java.util.List;

public class XMLConfigTest {

    @Test
    public void xmlConfig() throws IOException {
        Resource resource = new ClassPathResource("jdbc-config.xml");
        XMLConfig xmlConf = new XMLConfig(resource.getInputStream());
        xmlConf.parse();
    }

    @Test
    public void xmlParser() {
        try {
            Resource resource = new ClassPathResource("hantis.xml");
            XPathParser parser = XPathParser.getParser(resource.getInputStream());
            XPath xpath = parser.getXpath();


            XPathExpression expr = xpath.compile("//sessionFactory");
            NodeList nodeList = (NodeList) expr.evaluate(parser.getDoc(), XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.println(nodeList.item(i));
            }

            String s = (String) xpath.evaluate("//@id", parser.getDoc());
            System.out.println(s);

            parser.setVariables( parser.evalNode("/configuration/props", parser.getDoc()).getChildrenAsProperties());

           // System.out.println(parser.getVariables());

            XNode xNode = parser.evalNode("/configuration/sessionFactories/sessionFactory/dataSource", parser.getDoc());
            System.out.println( xNode.getChildrenAsProperties());

            System.out.println(xNode.evalNodeList("property").size());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
