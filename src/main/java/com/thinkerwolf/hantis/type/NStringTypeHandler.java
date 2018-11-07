package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NStringTypeHandler extends AbstractTypeHandler<String> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, String parameter) throws SQLException {
        stat.setNString(pos, parameter);
    }

    @Override
    protected String getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getNString(columnIndex);
    }

    @Override
    protected String getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getNString(columnName);
    }

}