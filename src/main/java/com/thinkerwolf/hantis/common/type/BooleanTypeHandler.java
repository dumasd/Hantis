package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BooleanTypeHandler extends AbstractTypeHandler<Boolean> {
	
	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Boolean parameter) throws SQLException {
		stat.setBoolean(pos, parameter);
	}
	
	@Override
	protected Boolean getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}

	@Override
	protected Boolean getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getBoolean(columnName);
	}

}