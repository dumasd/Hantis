package com.thinkerwolf.hantis.sql.xml;

import org.w3c.dom.Node;

import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.sql.UpdateSqlNode;

public class UpdateNodeHandler extends AbstractNodeHandler {

	@Override
	protected SqlNode newSqlNode(Node node) {
		return new UpdateSqlNode();
	}

}
