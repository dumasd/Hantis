package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.sql.Sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CommonExecutor extends AbstractExecutor {

    @Override
    protected List <BatchResult> doFlushStatments(boolean isRollback) {
        return null;
    }

    @Override
    protected int doUpdate(Sql sql, Connection connection) {
        PreparedStatementBuilder builder = new PreparedStatementBuilderImpl(connection, sql.getSql(), sql.getParams());
        return execute(() -> {
        	PreparedStatement ps = null;
            try {
            	ps = builder.build();
                return ps.executeUpdate();
            } catch (Throwable e) {
                throw new ExecutorException("Update error", e);
            } finally {
            	if (ps != null) {
            		try {
						ps.close();
					} catch (SQLException e) {
						
					}
            	}
            }
        });
    }

    @Override
    public ExecutorType getType() {
        return ExecutorType.COMMON;
    }


}
