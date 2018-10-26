package com.thinkerwolf.hantis.sql;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {

	T process(ResultSet rs) throws Throwable;
	
}
