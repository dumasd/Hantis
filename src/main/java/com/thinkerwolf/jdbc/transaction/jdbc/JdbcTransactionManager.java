package com.thinkerwolf.jdbc.transaction.jdbc;

import com.thinkerwolf.jdbc.transaction.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransactionManager extends AbstractTransactionManager {

    private DataSource dataSource;

    @Override
    protected Transaction doGetTransaction(TransactionDefinition defination) {
        // 根据dataSource作为key
        JdbcResourceHolder holder = (JdbcResourceHolder) TransactionSychronizationManager.getResource(dataSource);
        JdbcTransaction jdbcTransaction = new JdbcTransaction(holder);
        return jdbcTransaction;
    }

    @Override
    protected boolean isExistsTransaction(Transaction transaction) {
        JdbcTransaction jdbcTransaction = (JdbcTransaction) transaction;
        return jdbcTransaction.getResourceHolder() != null;
    }



    private static class JdbcTransaction implements Transaction {

        private JdbcResourceHolder resourceHolder;

        public JdbcTransaction(JdbcResourceHolder resourceHolder) {
            this.resourceHolder = resourceHolder;
        }

        @Override
        public void commit() throws SQLException {

        }

        @Override
        public void rollback() throws SQLException {

        }

        @Override
        public void close() throws SQLException {

        }

        public JdbcResourceHolder getResourceHolder() {
            return resourceHolder;
        }

    }


    private static class JdbcResourceHolder implements ResourceHolder {

        private DataSource dataSource;

        private Connection connection;

        public JdbcResourceHolder(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public Object getResource() {
            return dataSource;
        }

        public Connection getConnection() {
            if (connection == null) {
                try {
                    connection = dataSource.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return connection;
        }
    }




}
