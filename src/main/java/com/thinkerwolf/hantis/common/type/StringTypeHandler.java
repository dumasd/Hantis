package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StringTypeHandler extends AbstractTypeHandler<String> {
	
	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, String parameter) throws SQLException {
		stat.setString(pos, parameter);
	}
	
	@Override
	protected String getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	protected String getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}

}