package com.thinkerwolf.hantis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class DateTypeHandler extends AbstractTypeHandler<Date> {

	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Date parameter) throws SQLException {
		stat.setTimestamp(pos, new Timestamp((parameter.getTime())));
	}
	
	@Override
	protected Date getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
		if (sqlTimestamp != null) {
			return new Date(sqlTimestamp.getTime());
		}
		return null;
	}

	@Override
	protected Date getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		Timestamp sqlTimestamp = rs.getTimestamp(columnName);
		if (sqlTimestamp != null) {
			return new Date(sqlTimestamp.getTime());
		}
		return null;
	}

}