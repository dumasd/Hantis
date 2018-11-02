package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ByteTypeHandler extends AbstractTypeHandler<Byte> {
	
	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Byte parameter) throws SQLException {
		stat.setByte(pos, parameter);
	}
	
	@Override
	protected Byte getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}

	@Override
	protected Byte getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getByte(columnName);
	}

}