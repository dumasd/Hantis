package com.thinkerwolf.hantis.executor;

import java.sql.SQLException;
import java.sql.Statement;

class CommonStatmentExecuteCallback implements StatementExecuteCallback<Boolean>{

    private StatementBuilder builder;
    private String sql;

    public CommonStatmentExecuteCallback(StatementBuilder builder, String sql) {
        this.builder = builder;
        this.sql = sql;
    }

    @Override
    public Boolean execute() {
        Statement ps = null;
        try {
            ps = builder.build();
            return ps.execute(sql);
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
