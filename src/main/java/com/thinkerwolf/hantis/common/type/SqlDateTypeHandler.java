package com.thinkerwolf.hantis.common.type;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlDateTypeHandler extends AbstractTypeHandler<Date> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, Date parameter) throws SQLException {
        stat.setDate(pos, parameter);
    }

    @Override
    protected Date getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

    @Override
    protected Date getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getDate(columnName);
    }

}