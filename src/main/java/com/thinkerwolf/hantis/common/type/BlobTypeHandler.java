package com.thinkerwolf.hantis.common.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BlobTypeHandler extends AbstractTypeHandler<byte[]> {

	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, byte[] parameter) throws SQLException {
		ByteArrayInputStream bis = new ByteArrayInputStream(parameter);
		stat.setBinaryStream(pos, bis);
	}

	@Override
	protected byte[] getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		try {
			InputStream is = rs.getBinaryStream(columnIndex);
			ByteArrayOutputStream bas = new ByteArrayOutputStream();
			int n = 0;
			byte[] b = new byte[1024 * 4];
			while (-1 != (n = is.read(b))) {
				bas.write(b, 0, n);
			}
			return bas.toByteArray();
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

	@Override
	protected byte[] getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		try {
			InputStream is = rs.getBinaryStream(columnName);
			ByteArrayOutputStream bas = new ByteArrayOutputStream();
			int n = 0;
			byte[] b = new byte[1024 * 4];
			while (-1 != (n = is.read(b))) {
				bas.write(b, 0, n);
			}
			return bas.toByteArray();
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

}