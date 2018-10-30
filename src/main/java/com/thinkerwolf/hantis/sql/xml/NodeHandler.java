package com.thinkerwolf.hantis.sql.xml;

import org.w3c.dom.Node;

import com.thinkerwolf.hantis.sql.SqlNode;

public interface NodeHandler {
	
	
	SqlNode parse(Node el); 
	
}
