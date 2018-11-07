package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class FloatTypeHandler extends AbstractTypeHandler<Float> {

    @Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, Float parameter) throws SQLException {
        stat.setFloat(pos, parameter);
    }

    @Override
    protected Float getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }

    @Override
    protected Float getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getFloat(columnName);
    }

}