package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ObjectTypeHandler extends AbstractTypeHandler<Object> {

    public ObjectTypeHandler() {
		super(Object.class);
	}

	@Override
    protected void setNotNullParameter(PreparedStatement stat, int pos, Object parameter) throws SQLException {
        stat.setObject(pos, parameter);
    }

    @Override
    protected Object getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }

    @Override
    protected Object getNotNullResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName);
    }

}