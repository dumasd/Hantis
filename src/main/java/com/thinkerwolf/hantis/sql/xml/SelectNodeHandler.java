package com.thinkerwolf.hantis.sql.xml;

import org.w3c.dom.Node;

import com.thinkerwolf.hantis.sql.SelectSqlNode;
import com.thinkerwolf.hantis.sql.SqlNode;

public class SelectNodeHandler extends AbstractNodeHandler {

	@Override
	protected SqlNode newSqlNode(Node node) {
		return new SelectSqlNode();
	}

}
