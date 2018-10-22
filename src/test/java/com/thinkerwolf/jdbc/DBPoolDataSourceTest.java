package com.thinkerwolf.jdbc;

import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.thinkerwolf.jdbc.pool.DBPoolDataSource;

public class DBPoolDataSourceTest {

	private static UncaughtExceptionHandler threadExceptionHandler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread t, Throwable e) {

		}
	};

	@Test
	public void testConn() throws SQLException {
		final DBPoolDataSource ds = new DBPoolDataSource();
		ds.setDriver("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
		ds.setUsername("root");
		ds.setPassword("123");
		ds.setMaxConn(100);
		ds.setMinConn(2);
		
		int threadNum = 200;
		while (threadNum-- > 0) {
			final int n = threadNum;
			Thread t = new Thread() {
				public void run() {
					Connection conn = null;
					try {
						conn = ds.getConnection();
						conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
						PreparedStatement ps = conn.prepareStatement("select * from blog where id = ?");
						ps.setInt(1, 1);
						ResultSet rs = ps.executeQuery();
						while (rs.next()) {
							System.out.println(rs.getInt(1) + n);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException e) {
								// ignore
							}
						}
					}

				};

			};
			t.setUncaughtExceptionHandler(threadExceptionHandler);
			t.start();
		}

	}

	public static void main(String[] args) throws SQLException {
		new DBPoolDataSourceTest().testConn();
	}
}
