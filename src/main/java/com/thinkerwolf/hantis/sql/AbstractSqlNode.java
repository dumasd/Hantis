package com.thinkerwolf.hantis.sql;

import java.util.List;

public abstract class AbstractSqlNode implements SqlNode {

	private List<SqlNode> children = null;
	
	@Override
	public void setChildren(List<SqlNode> children) {
		this.children = children;
	}
	
	@Override
	public List<SqlNode> getChildren() {
		return children;
	}

	@Override
	public abstract boolean generate(Sql sql) throws Throwable;

}
