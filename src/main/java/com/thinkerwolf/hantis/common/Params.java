package com.thinkerwolf.hantis.common;

import java.util.ArrayList;

public final class Params extends ArrayList<Param> {

	public static final Params EMPTY_PARAMS = new Params();

	private static final long serialVersionUID = -5831930412046399676L;

	public void addParam(JDBCType type, Object value) {
		add(new Param(type, value));
	}

	public void addParam(Object value) {
		add(new Param(value));
	}
	
	
	
}
