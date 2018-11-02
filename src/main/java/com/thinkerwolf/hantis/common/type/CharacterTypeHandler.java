package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterTypeHandler extends AbstractTypeHandler<Character> {
	
	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Character parameter) throws SQLException {
		stat.setString(pos, parameter.toString());
	}

	@Override
	protected Character getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		String s = rs.getString(columnIndex);
		if (s.length() > 0) {
			return s.charAt(0);
		}
		return null;
	}

	@Override
	protected Character getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		String s = rs.getString(columnName);
		if (s.length() > 0) {
			return s.charAt(0);
		}
		return null;
	}

}