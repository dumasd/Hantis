package com.thinkerwolf.hantis.executor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class QueryStatementExecuteCallback<T> implements StatementExecuteCallback<T> {
    private PreparedStatementBuilder builder;
    private ResultSetHandler<T> listHandler;

    public QueryStatementExecuteCallback(PreparedStatementBuilder builder, ResultSetHandler<T> listHandler) {
        this.builder = builder;
        this.listHandler = listHandler;
    }

    @Override
    public T execute() {
        PreparedStatement ps = null;
        try {
            ps = builder.build();
            RowBound rowBound = new RowBound(ps.executeQuery());
            return listHandler.process(rowBound.getResultSet());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
