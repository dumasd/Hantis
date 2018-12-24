package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import org.junit.Test;

import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.*;

public class DBDataSourceTest {

    private static UncaughtExceptionHandler threadExceptionHandler = new UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread t, Throwable e) {

        }
    };

    public static void main(String[] args) throws SQLException {
        new DBDataSourceTest().testConn();
    }

    @Test
    public void testConn() throws SQLException {
        final DBPoolDataSource ds = new DBPoolDataSource();
        ds.setDriver("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("1234");
        ds.setMaxConn(100);
        ds.setMinConn(2);

        Connection connection = ds.getConnection();
        connection.setAutoCommit(false);

        long startMillis = System.currentTimeMillis();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO blog (id, title, content) VALUES (?, ?, ?)");
        for (int i = 10; i <= 1010; i++) {

            ps.setInt(1, i);
            ps.setString(2, "nonbatch_title_" + i);
            ps.setString(3, "nonbatch_content_" + i);
            ps.addBatch();
        }
        ps.executeBatch();
        connection.commit();
        System.out.println("Time : " + (System.currentTimeMillis() - startMillis));
        // nonbatch 671ms   batch 425





        /*int threadNum = 100;
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

                }

                ;

            };
            t.setUncaughtExceptionHandler(threadExceptionHandler);
            t.start();
        } */

    }
}
