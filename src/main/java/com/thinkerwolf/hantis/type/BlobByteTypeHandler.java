package com.thinkerwolf.hantis.type;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobByteTypeHandler extends AbstractTypeHandler<Byte[]> {

	public BlobByteTypeHandler() {
		super(Byte[].class);
	}

	@Override
	protected void setNotNullParameter(PreparedStatement stat, int pos, Byte[] parameter) throws SQLException {
		ByteArrayInputStream bis = new ByteArrayInputStream(BlobByteUtils.convertToPrimitiveByteArray(parameter));
		stat.setBinaryStream(pos, bis);
	}

	@Override
	protected Byte[] getNotNullResult(ResultSet rs, int columnIndex) throws SQLException {
		return BlobByteUtils.convertToPackageByteArray(rs.getBytes(columnIndex));
	}

	@Override
	protected Byte[] getNotNullResult(ResultSet rs, String columnName) throws SQLException {
		return BlobByteUtils.convertToPackageByteArray(rs.getBytes(columnName));
	}

	private static class BlobByteUtils {

		public static byte[] convertToPrimitiveByteArray(Byte[] bytes) {
			byte[] bs = new byte[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				bs[i] = bytes[i];
			}
			return bs;
		}

		public static Byte[] convertToPackageByteArray(byte[] bytes) {
			Byte[] bs = new Byte[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				bs[i] = bytes[i];
			}
			return bs;
		}

	}

}