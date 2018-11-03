package com.thinkerwolf.hantis.sql.xml;

import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.WhereSqlNode;
import org.w3c.dom.Node;

public class WhereNodeHandler extends AbstractNodeHandler {

    @Override
    protected SqlNode newSqlNode(Node node) {
        return new WhereSqlNode();
    }

}
