package com.thinkerwolf.hantis.common.xml;

import com.thinkerwolf.hantis.common.util.PropertyUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XNode {

    private static final Properties empty_variables = new Properties();

    private Node node;
    private XPathParser xPathParser;
    /** 全局参数 */
    private Properties variables;
    /** 节点属性 */
    private Properties attrubutes;

    public XNode(Node node, XPathParser xPathParser, Properties variables) {
        this.node = node;
        this.xPathParser = xPathParser;
        this.variables = variables == null ? empty_variables : variables;
        this.attrubutes = parseVariables();
    }

    private Properties parseVariables() {
        Properties properties = new Properties();
        NamedNodeMap namedNodeMap = node.getAttributes();
        if (namedNodeMap != null) {
            for (int i = 0; i < namedNodeMap.getLength(); i++) {
                Node node = namedNodeMap.item(i);
                properties.put(node.getNodeName(), PropertyUtils.getPropertyValue(variables, node.getNodeValue().trim()));
            }
        }
        return properties;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean hasAttribute(String name) {
        return attrubutes.containsKey(name);
    }

    public String getStringAttrubute(String name) {
        return attrubutes.getProperty(name);
    }

    public String getStringAttrubute(String name, String def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return value;
        }
    }

    public Boolean getBoolAttrubute(String name) {
        return getBoolAttrubute(name, null);
    }

    public Boolean getBoolAttrubute(String name, Boolean def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return Boolean.valueOf(value);
        }
    }

    public Short getShortAttrubute(String name) {
        return getShortAttrubute(name, null);
    }

    public Short getShortAttrubute(String name, Short def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return Short.valueOf(value);
        }
    }

    public Integer getIntAttrubute(String name) {
        return getIntAttrubute(name, null);
    }

    public Integer getIntAttrubute(String name, Integer def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return Integer.valueOf(value);
        }
    }

    public Long getLongAttrubute(String name) {
        return getLongAttrubute(name, null);
    }

    public Long getLongAttrubute(String name, Long def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return Long.valueOf(value);
        }
    }

    public Float getFloatAttrubute(String name) {
        return getFloatAttrubute(name, null);
    }

    public Float getFloatAttrubute(String name, Float def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return Float.valueOf(value);
        }
    }

    public Double getDoubleAttrubute(String name) {
        return getDoubleAttrubute(name, null);
    }

    public Double getDoubleAttrubute(String name, Double def) {
        String value = getStringAttrubute(name);
        if (value == null) {
            return def;
        } else {
            return Double.valueOf(value);
        }
    }

    public XNode evalNode(String expression) {
        return xPathParser.evalNode(expression, node);
    }

    public List <XNode> evalNodeList(String expression) {
        return xPathParser.evalNodeList(expression, node);
    }

    public String evalString(String expression) {
        return xPathParser.evalString(expression, node);
    }

    public Boolean evalBoolean(String expression) {
        return xPathParser.evalBoolean(expression, node);
    }

    public Short evalShort(String expression) {
        return xPathParser.evalShort(expression, node);
    }

    public Integer evalInteger( String expression) {
        return xPathParser.evalInteger(expression, node);
    }

    public Long evalLong(String expression) {
        return xPathParser.evalLong(expression, node);
    }

    public Float evalFloat(String expression) {
        return xPathParser.evalFloat(expression, node);
    }

    public Double evalDouble(String expression) {
        return xPathParser.evalDouble(expression, node);
    }

    public List<XNode> getChildren() {
        List<XNode> children = new ArrayList <>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                children.add(new XNode(node, xPathParser, variables));
            }
        }
        return children;
    }

    public XNode getFirstChild() {
       return  getChildren().size() > 0 ? getChildren().get(0) : null;
    }

    public Properties getChildrenAsProperties() {
        Properties properties = new Properties();
        for (XNode xNode : getChildren()) {
            String nodeName = xNode.getNode().getNodeName();
            if ("property".equals(nodeName) && xNode.hasAttribute("name")) {
                String name = xNode.getStringAttrubute("name");
                Object value = null;
                if (xNode.hasAttribute("value")) {
                    value = PropertyUtils.getPropertyValue(variables, xNode.getStringAttrubute("value"));
                } else {
                     XNode firstChild = xNode.getFirstChild();
                     if (firstChild != null && "props".equals(firstChild.getNode().getNodeName())) {
                         value = firstChild.getChildrenAsProperties();
                     }
                }
                if (value != null) {
                    properties.put(name, value);
                }
            }
        }
        return properties;
    }



}
