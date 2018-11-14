package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NullValueTypeHandler extends AbstractTypeHandler<Object> {

	public NullValueTypeHandler() {
		super(Object.class);
	}

	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Object parameter) throws SQLException {
		
	}

	@Override
	protected Object getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return null;
	}

	@Override
	protected Object getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return null;
	}

}
