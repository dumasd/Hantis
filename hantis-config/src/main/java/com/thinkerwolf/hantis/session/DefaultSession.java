package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.executor.BatchExecutor;
import com.thinkerwolf.hantis.executor.CommonExecutor;
import com.thinkerwolf.hantis.executor.Executor;
import com.thinkerwolf.hantis.executor.ExecutorType;
import com.thinkerwolf.hantis.orm.TableEntity;
import com.thinkerwolf.hantis.sql.Sql;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.transaction.ConnectionHolder;
import com.thinkerwolf.hantis.transaction.ConnectionUtils;
import com.thinkerwolf.hantis.transaction.Transaction;
import com.thinkerwolf.hantis.transaction.TransactionDefinition;
import com.thinkerwolf.hantis.transaction.TransactionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultSession implements Session {

	private Transaction transaction;

	private Executor executor;

	private SessionFactoryBuilder builder;

	private TransactionDefinition transactionDefinition;

	private TransactionManager transactionManager;

	private static final Logger logger = LoggerFactory.getLogger(DefaultSession.class);

	public DefaultSession(TransactionDefinition transactionDefinition, SessionFactoryBuilder builder) {
		this.transactionDefinition = transactionDefinition;
		this.builder = builder;
		this.executor = createExecutor(builder.getExecutorType());
		this.transactionManager = builder.getConfiguration().getTransactionManager();
	}

	@Override
	public void close() throws IOException {
		try {
			doTransactionClose();
			executor.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void commit() {
		try {
			executor.doBeforeCommit();
			doTransactionCommit();
			executor.doAfterCommit();
		} catch (SQLException e) {
			logger.error("commit", e);
		}
	}

	@Override
	public void rollback() {
		try {
			executor.doBeforeRollback();
			doTransactionRollback();
			executor.doAfterRollback();
		} catch (SQLException e) {
			logger.error("rollback", e);
		}
	}

	private void doTransactionCommit() throws SQLException {
		if (transaction != null) {
			transactionManager.commit(transaction);
			return;
		}
		if (transactionManager.isDistributed()) { 
			return;
		}
		ConnectionHolder holder = ConnectionUtils.getConnectionHolder(builder.getDataSource());
		if (holder != null) {
			ConnectionUtils.commitConnectionHolder(holder);
		}
	}

	private void doTransactionRollback() throws SQLException {
		if (transaction != null) {
			transactionManager.rollback(transaction);
			return;
		}
		if (transactionManager.isDistributed()) { 
			return;
		}
		ConnectionHolder holder = ConnectionUtils.getConnectionHolder(builder.getDataSource());
		if (holder != null) {
			ConnectionUtils.rollbackConnectionHolder(holder);
		}
	}

	private void doTransactionClose() throws SQLException {
		if (transaction != null) {
			transaction.close();
			return;
		}
		if (transactionManager.isDistributed()) { 
			return;
		}
		Connection conn = ConnectionUtils.getConnection(builder.getDataSource());
		ConnectionUtils.closeConnection(conn);
	}

	@Override
	public Transaction beginTransaction() {
		return beginTransaction(transactionDefinition);
	}

	@Override
	public Transaction beginTransaction(TransactionDefinition transactionDefinition) {
		this.transaction = transactionManager.getTransaction(transactionDefinition);
		return transaction;
	}

	@Override
	public <T> List<T> selectList(String mapping) {
		return selectList(mapping, null);
	}

	@Override
	public <T> List<T> selectList(String mapping, Object parameter) {
		SqlNode sn = builder.getSqlNode(mapping);
		Sql sql = new Sql(parameter);
		try {
			sn.generate(sql);
		} catch (Throwable throwable) {
			return null;
		}
		return executor.queryForList(sql.getSql(), sql.getParams(), (Class<T>) sql.getReturnType());
	}

	@Override
	public <T> T selectOne(String mapping, Object parameter) {
		SqlNode sn = builder.getSqlNode(mapping);
		Sql sql = new Sql(parameter);
		StopWatch sw = StopWatch.start();
		try {
			sn.generate(sql);
		} catch (Throwable throwable) {
			return null;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Generate sql {" + sql.toString() +"} time(ms) : " + sw.end());
		}
		return executor.queryForOne(sql.getSql(), sql.getParams(), (Class<T>) sql.getReturnType());
	}

	@Override
	public <T> T selectOne(String mapping) {
		return selectOne(mapping, null);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String mapping, Object parameter) {
		SqlNode sn = builder.getSqlNode(mapping);
		Sql sql = new Sql(parameter);
		try {
			sn.generate(sql);
		} catch (Throwable e) {
			return null;
		}
		return (Map<K, V>) executor.queryForOne(sql.getSql(), sql.getParams());
	}

	@Override
	public <K, V> Map<K, V> selectMap(String mapping) {
		return selectMap(mapping, null);
	}

	@Override
	public int update(String mapping, Object parameter) {
		SqlNode sn = builder.getSqlNode(mapping);
		Sql sql = new Sql(parameter);
		StopWatch sw = StopWatch.start();
		try {
			sn.generate(sql);
		} catch (Throwable throwable) {
			return 0;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Generate sql {" + sql.toString() +"} time(ms) : " + sw.end());
		}
		return executor.update(sql.getSql(), sql.getParams());
	}

	@Override
	public int update(String mapping) {
		return update(mapping, null);
	}

	private Executor createExecutor(ExecutorType executorType) {
		executorType = executorType == null ? ExecutorType.COMMON : executorType;
		Executor executor;
		switch (executorType) {
		case COMMON:
			executor = new CommonExecutor();
			break;
		case BATCH:
			executor = new BatchExecutor();
			break;
		default:
			executor = new CommonExecutor();
			break;
		}
		executor.setDataSource(builder.getDataSource());
		executor.setConfiguration(builder.getConfiguration());
		// executor.setSessionFactoryBuilder(builder);
		return executor;
	}

	@Override
	public <T> List<T> getList(Class<T> clazz, Object parameter) {
		TableEntity tableEntity = getTableEntity(clazz);
		Sql sql = tableEntity.parseSelectSql(parameter);
		return executor.queryForList(sql.getSql(), sql.getParams(), clazz);
	}

	@Override
	public <T> T get(Class<T> clazz, Object parameter) {
		TableEntity tableEntity = getTableEntity(clazz);
		Sql sql = tableEntity.parseSelectSql(parameter);
		return executor.queryForOne(sql.getSql(), sql.getParams(), clazz);
	}

	@Override
	public <T> int update(T entity) {
		TableEntity tableEntity = getTableEntity(entity.getClass());
		Sql sql = tableEntity.parseUpdateSql(executor, entity);
		return executor.update(sql.getSql(), sql.getParams());
	}

	@Override
	public <T> int delete(T entity) {
		TableEntity tableEntity = getTableEntity(entity.getClass());
		Sql sql = tableEntity.parseDeleteSql(executor, entity);
		return executor.update(sql.getSql(), sql.getParams());
	}

	@Override
	public <T> int create(T entity) {
		TableEntity tableEntity = getTableEntity(entity.getClass());
		Sql sql = tableEntity.parseInsertSql(executor, entity);
		return executor.update(sql.getSql(), sql.getParams());
	}

	private TableEntity getTableEntity(Class<?> clazz) {
		TableEntity tableEntity = builder.getEntityMap().get(clazz.getName());
		if (tableEntity == null) {
			throw new RuntimeException("Table entity null");
		}
		return tableEntity;
	}

}
