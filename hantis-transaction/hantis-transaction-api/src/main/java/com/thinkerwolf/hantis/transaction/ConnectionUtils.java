package com.thinkerwolf.hantis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.CommonDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionUtils {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);

	public static Connection getConnection(CommonDataSource dataSource) {
		Object obj = TransactionSychronizationManager.getResource(dataSource);
        if (obj == null || !(obj instanceof ConnectionHolder)) {
            return null;
        }
        ConnectionHolder holder = (ConnectionHolder) obj;
        return holder.getConnection();
    }

	public static void commitConnection(Connection conn) throws SQLException {
		if (!conn.getAutoCommit()) {
			conn.commit();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Commit The connection is auto commit.");
			}
		}
	}

	public static void rollbackConnection(Connection conn) throws SQLException {
		if (!conn.getAutoCommit()) {
			conn.rollback();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Rollback The connection is auto commit.");
			}
		}
	}

	public static ConnectionHolder getConnectionHolder(CommonDataSource dataSource) {
		Object obj = TransactionSychronizationManager.getResource(dataSource);
		if (obj == null || !(obj instanceof ConnectionHolder)) {
			return null;
		}
		return (ConnectionHolder) obj;
	}

	public static void commitConnectionHolder(ConnectionHolder holder) throws SQLException {
		try {
            commitConnection(holder.getConnection());
        } catch (Exception e) {
            //throw new SQLException(e);
        } finally {
            clearAfterCommitOrRollback(holder);
		}
	}

	public static void rollbackConnectionHolder(ConnectionHolder holder) throws SQLException {
		try {
            rollbackConnection(holder.getConnection());
        } catch (Exception e) {
            //throw new SQLException(e);
        } finally {
            clearAfterCommitOrRollback(holder);
		}
	}

	public static void clearAfterCommitOrRollback(ConnectionHolder holder) throws SQLException {
		Connection conn = holder.getConnection();
		if (conn != null) {
			if (conn.getAutoCommit() != holder.isPreviousAutoCommit()) {
				conn.setAutoCommit(holder.isPreviousAutoCommit());
			}
			if (conn.getTransactionIsolation() != holder.getPreviousIsolationLevel()) {
				conn.setTransactionIsolation(holder.getPreviousIsolationLevel());
			}
			if (conn.isReadOnly()) {
				conn.setReadOnly(false);
			}
		}
	}

	public static void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	public static void bindConnection(CommonDataSource dataSource, Connection conn,
			TransactionManager transactionManager) {
		ResourceHolder holder;
		try {
			holder = transactionManager.createResourceHolder(conn);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		TransactionSychronizationManager.bindResource(dataSource, holder);
	}

}
