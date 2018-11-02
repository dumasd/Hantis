package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

public class TimestampTypeHandler extends AbstractTypeHandler<Timestamp> {
	
	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Timestamp parameter) throws SQLException {
		stat.setTimestamp(pos, parameter);
	}
	
	@Override
	protected Timestamp getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getTimestamp(columnIndex);
	}

	@Override
	protected Timestamp getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getTimestamp(columnName);
	}

}