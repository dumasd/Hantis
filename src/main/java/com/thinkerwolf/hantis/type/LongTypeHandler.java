package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LongTypeHandler extends AbstractTypeHandler<Long> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, Long parameter) throws SQLException {
        stat.setLong(pos, parameter);
    }

    @Override
    protected Long getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }

    @Override
    protected Long getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getLong(columnName);
    }

}