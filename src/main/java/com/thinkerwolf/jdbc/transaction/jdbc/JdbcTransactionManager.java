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
        TransactionSychronizationManager.getResource(dataSource);


        return null;
    }

    @Override
    protected boolean isExistsTransaction(Transaction transaction) {
        return false;
    }



    private static class JdbcTransaction implements Transaction {

        private JdbcResourceHolder resourceHolder;

        @Override
        public void commit() throws SQLException {

        }

        @Override
        public void rollback() throws SQLException {

        }

        @Override
        public void close() throws SQLException {

        }

    }


    private static class JdbcResourceHolder implements ResourceHolder {

        private DataSource dataSource;

        private Connection connection;

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
