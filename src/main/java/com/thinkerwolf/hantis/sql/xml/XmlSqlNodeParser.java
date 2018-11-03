package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.sql.SqlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlNodeParser {

    public Map<String, SqlNode> parse(InputStream is) throws Throwable {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(is);
        Element element = (Element) doc.getElementsByTagName("mapping").item(0);
        String namespace = element.getAttribute("namespace");
        Map<String, SqlNode> allSqlNode = new HashMap<>();

        NodeList selectNodeList = element.getElementsByTagName("select");
        for (int i = 0, len = selectNodeList.getLength(); i < len; i++) {
            Element el = (Element) selectNodeList.item(i);
            String id = namespace + "." + el.getAttribute("id");
            allSqlNode.put(id, NodeHandlerFactory.getNodeHandler("select").parse(el));
        }

        NodeList updateNodeList = element.getElementsByTagName("update");
        for (int i = 0, len = updateNodeList.getLength(); i < len; i++) {
            Element el = (Element) updateNodeList.item(i);
            String id = namespace + "." + el.getAttribute("id");
            allSqlNode.put(id, NodeHandlerFactory.getNodeHandler("update").parse(el));
        }
        return allSqlNode;
    }

}
