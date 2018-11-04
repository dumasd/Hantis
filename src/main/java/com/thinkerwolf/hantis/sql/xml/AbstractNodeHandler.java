package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.TextSqlNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNodeHandler implements NodeHandler {

    @Override
    public SqlNode parse(Node el) {
        SqlNode sn = newSqlNode(el);
        List<SqlNode> children = new ArrayList<>();
        NodeList nodeList = el.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                if (!SqlNode.EMPTY_TEXT.matcher(node.getTextContent()).matches()) {
                    TextSqlNode tsNode = new TextSqlNode(
                            node.getTextContent().replaceAll(SqlNode.TRIM.pattern(), "").trim());
                    children.add(tsNode);
                }
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                NodeHandler handler = NodeHandlerFactory.getNodeHandler(node.getNodeName());
                if (handler == null) {
                    throw new RuntimeException("handler null");
                }
                children.add(handler.parse(node));
            }
        }
        sn.setChildren(children);
        return sn;
    }

    protected abstract SqlNode newSqlNode(Node node);

}
