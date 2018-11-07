package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class IntegerTypeHandler extends AbstractTypeHandler<Integer> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, Integer parameter) throws SQLException {
        stat.setInt(pos, parameter);
    }

    @Override
    protected Integer getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    @Override
    protected Integer getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getInt(columnName);
    }

}