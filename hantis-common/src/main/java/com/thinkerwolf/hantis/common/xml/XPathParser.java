package com.thinkerwolf.hantis.common.xml;

import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.io.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * XPath parser
 */
public class XPathParser {



    private Document doc;
    private Element root;
    private XPath xpath;
    private Properties variables = new Properties();

    public static XPathParser getParser(InputStream is) throws IOException, SAXException, ParserConfigurationException {
        XPathParser parser = new XPathParser();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(is);
        parser.doc = doc;
        parser.root = doc.getDocumentElement();
        parser.xpath = XPathFactory.newInstance().newXPath();
        return parser;
    }

    public static XPathParser getParser(String path)  throws IOException, SAXException, ParserConfigurationException {
        Resource resource = Resources.getResource(path);
        return getParser(resource.getInputStream());
    }

    public Document getDoc() {
        return doc;
    }

    public Element getRoot() {
        return root;
    }

    public XPath getXpath() {
        return xpath;
    }

    public Properties getVariables() {
        return variables;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public XNode evalNode(String expression, Object item) {
        Node node = (Node) evaluate(expression, item, XPathConstants.NODE);
        if (node == null) {
            return null;
        }
        XNode xNode = new XNode(node, this, variables);
        return xNode;
    }

    public List <XNode> evalNodeList(String expression, Object item) {
        NodeList nodeList = (NodeList) evaluate(expression, item, XPathConstants.NODESET);
        List<XNode> xNodes = new ArrayList<>(nodeList.getLength());
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                XNode xNode = new XNode(nodeList.item(i), this, variables);
                xNodes.add(xNode);
            }
        }
        return xNodes;
    }

    public String evalString(String expression, Object item) {
        return String.valueOf(evaluate(expression, item, XPathConstants.STRING));
    }

    public Boolean evalBoolean(String expression, Object item) {
        return Boolean.valueOf(evalString(expression, item));
    }

    public Short evalShort(String expression, Object item) {
        return Short.valueOf(evalString(expression, item));
    }

    public Integer evalInteger( String expression, Object item) {
        return Integer.valueOf(evalString(expression, item));
    }

    public Long evalLong(String expression, Object item) {
        return Long.valueOf(evalString(expression, item));
    }

    public Float evalFloat(String expression, Object item) {
        return Float.valueOf(evalString(expression, item));
    }

    public Double evalDouble(String expression, Object item) {
        return Double.valueOf(evalString(expression, item));
    }

    public Object evaluate(String expression, Object item, QName qName) {
        try {
            return xpath.evaluate(expression, item, qName);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("evaluate error", e);
        }
    }


}
