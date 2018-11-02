package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DoubleTypeHandler extends AbstractTypeHandler<Double> {
	
	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Double parameter) throws SQLException {
		stat.setDouble(pos, parameter);
	}
	
	@Override
	protected Double getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getDouble(columnIndex);
	}

	@Override
	protected Double getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getDouble(columnName);
	}

}