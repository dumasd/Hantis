package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ShortTypeHandler extends AbstractTypeHandler<Short> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, Short parameter) throws SQLException {
        stat.setShort(pos, parameter);
    }

    @Override
    protected Short getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getShort(columnIndex);
    }

    @Override
    protected Short getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getShort(columnName);
    }

}