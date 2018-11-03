package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.datasource.jta.DBXAPoolDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBXADataSourceTest {
    @Test
    public void testPool() throws SQLException {
        DBXAPoolDataSource ds = new DBXAPoolDataSource();
        ds.setDriver("com.mysql.cj.jdbc.Driver");
        ds.setMaxConn(100);
        ds.setMinConn(2);
        ds.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("url",
                "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        properties.put("user", "root");
        properties.put("password", "123");
        ds.setXaProperties(properties);

        Connection conn1 = ds.getXAConnection().getConnection();
        conn1.prepareStatement("UPDATE blog SET title = 'jta_5', content = 'jta test_5' WHERE id = 3")
                .execute();
        //conn1.commit();

    }

}
