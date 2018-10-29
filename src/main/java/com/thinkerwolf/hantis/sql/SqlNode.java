package com.thinkerwolf.hantis.sql;

import java.util.List;

public interface SqlNode {

	String parse() throws Throwable;

	List<SqlNode> setChildren(List<SqlNode> children);
	
}
