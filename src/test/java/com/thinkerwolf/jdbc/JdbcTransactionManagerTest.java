package com.thinkerwolf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import com.thinkerwolf.jdbc.pool.DBPoolDataSource;
import com.thinkerwolf.jdbc.transaction.DefaultTransactionDefinition;
import com.thinkerwolf.jdbc.transaction.Transaction;
import com.thinkerwolf.jdbc.transaction.TransactionDefinition;
import com.thinkerwolf.jdbc.transaction.TransactionSychronizationManager;
import com.thinkerwolf.jdbc.transaction.jdbc.JdbcTransactionManager;
import com.thinkerwolf.jdbc.transaction.jdbc.JdbcTransactionManager.JdbcResourceHolder;

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
		JdbcResourceHolder holder = (JdbcResourceHolder) TransactionSychronizationManager.getResource(ds);
		Connection conn = holder.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO blog values (?, ? , ?)");
		ps.setInt(1, 7);
		ps.setString(2, "testTitle7");
		ps.setString(3, "testContnt7");
		ps.executeUpdate();

		Transaction transaction1 = transactionManager.getTransaction(definition);
		JdbcResourceHolder holder1 = (JdbcResourceHolder) TransactionSychronizationManager.getResource(ds);
		Connection conn1 = holder1.getConnection();
		PreparedStatement ps1 = conn1.prepareStatement("INSERT INTO blog values (?, ? , ?)");
		ps1.setInt(1, 8);
		ps1.setString(2, "testTitle8");
		ps1.setString(3, "testContnt8");
		ps1.executeUpdate();
		transactionManager.commit(transaction);
		transactionManager.commit(transaction1);
	}

}
