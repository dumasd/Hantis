package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.sql.IfSqlNode;
import com.thinkerwolf.hantis.sql.SqlNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class IfNodeHandler extends AbstractNodeHandler {

    @Override
    protected SqlNode newSqlNode(Node node) {
        String expression = ((Element) node).getAttribute("test");
        IfSqlNode ifSn = new IfSqlNode(expression);
        return ifSn;
    }

}
