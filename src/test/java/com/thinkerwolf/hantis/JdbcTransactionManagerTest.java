package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import com.thinkerwolf.hantis.transaction.DefaultTransactionDefinition;
import com.thinkerwolf.hantis.transaction.Transaction;
import com.thinkerwolf.hantis.transaction.TransactionDefinition;
import com.thinkerwolf.hantis.transaction.TransactionSychronizationManager;
import com.thinkerwolf.hantis.transaction.jdbc.JdbcConnectionHolder;
import com.thinkerwolf.hantis.transaction.jdbc.JdbcTransactionManager;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTransactionManagerTest {

    @Test
    public void test() throws SQLException {
        final DBPoolDataSource ds = new DBPoolDataSource();
        ds.setDriver("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("123");
        ds.setMaxConn(100);
        ds.setMinConn(2);

        JdbcTransactionManager transactionManager = new JdbcTransactionManager();
        transactionManager.setDataSource(ds);
        TransactionDefinition definition = new DefaultTransactionDefinition();

        Transaction transaction = transactionManager.getTransaction(definition);
        JdbcConnectionHolder holder = (JdbcConnectionHolder) TransactionSychronizationManager.getResource(ds);
        Connection conn = holder.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE blog SET title = ?, content = ? WHERE id = ?");
        ps.setString(1, "testTitle9");
        ps.setString(2, "testContnt9");
        ps.setInt(3, 9);
        ps.executeUpdate();

        Transaction transaction1 = transactionManager.getTransaction(definition);
        JdbcConnectionHolder holder1 = (JdbcConnectionHolder) TransactionSychronizationManager.getResource(ds);
        Connection conn1 = holder1.getConnection();
        PreparedStatement ps1 = conn1.prepareStatement("UPDATE blog SET title = ?, content = ? WHERE id = ?");
        ps1.setString(1, "testTitle10");
        ps1.setString(2, "testContnt10");
        ps1.setInt(3, 10);
        ps1.executeUpdate();
        transactionManager.commit(transaction);
        transactionManager.commit(transaction1);
    }

}
