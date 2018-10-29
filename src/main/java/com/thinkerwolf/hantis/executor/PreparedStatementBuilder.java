package com.thinkerwolf.hantis.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface PreparedStatementBuilder {

	PreparedStatement build(Connection connection, String sql) throws Throwable;
	
}
