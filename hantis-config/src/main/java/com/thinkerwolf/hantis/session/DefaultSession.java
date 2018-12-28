package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.common.SQLType;
import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.common.log.InternalLoggerFactory;
import com.thinkerwolf.hantis.common.log.Logger;
import com.thinkerwolf.hantis.executor.*;
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

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultSession implements Session {

	private Transaction transaction;

	private Executor executor;

	private SessionFactoryBuilder builder;

	private TransactionDefinition transactionDefinition;

	private TransactionManager transactionManager;

	private static final Logger logger = InternalLoggerFactory.getLogger(DefaultSession.class);

	public DefaultSession(TransactionDefinition transactionDefinition, SessionFactoryBuilder builder) {
		this.transactionDefinition = transactionDefinition;
		this.builder = builder;
		this.executor = createExecutor(builder.getExecutorType());
		this.transactionManager = builder.getConfiguration().getTransactionManager();
	}

	@Override
	public Executor getExecutor() {
		return executor;
	}

	@Override
	public void close() throws IOException {
		try {
			doTransactionClose();
			executor.close();
			builder.clearCache();
		} catch (Exception e) {
			logger.error("close error", e);
		}
	}

	@Override
	public void commit() {
		try {
			executor.flushStatments(false);
			doTransactionCommit();
		} catch (SQLException e) {
			logger.error("commit error!", e);
		}
	}

	@Override
	public void rollback() {
		try {
			executor.flushStatments(true);
			doTransactionRollback();
		} catch (SQLException e) {
			logger.error("rollback error!", e);
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
		Sql sql = generateSQL(mapping, parameter);
		return executor.queryForList(sql, (Class<T>) sql.getReturnType());
	}

	@Override
	public <T> T selectOne(String mapping, Object parameter) {
		List<T> resList = selectList(mapping, parameter);
		if (resList.size() > 1) {
			throw new RuntimeException("Too many result");
		}
		return resList.size() == 0 ? null : resList.get(0);
	}

	@Override
	public <T> T selectOne(String mapping) {
		return selectOne(mapping, null);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String mapping, Object parameter) {
		List<Map<K, V>> resList = selectList(mapping, parameter);
		if (resList.size() > 1) {
			throw new RuntimeException("Too many result");
		}
		return resList.size() == 0 ? null : resList.get(0);
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
			logger.error("Generate sql error", throwable);
			return 0;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Generate sql {" + sql.toString() +"} time(ms) : " + sw.end());
		}

		if (sn.getCache() != null) {
			sn.getCache().clear();
		}
		return executor.update(sql);
	}

	@Override
	public boolean execute(String mapping) {
		Sql sql = generateSQL(mapping, null);
		return executor.execute(sql);
	}

	@Override
	public int update(String mapping) {
		return update(mapping, null);
	}

	@Override
	public <T> List<T> getList(Class<T> clazz, Object parameter) {
		Sql sql = generateSQL(clazz, parameter, SQLType.QUERY);
		return executor.queryForList(sql, clazz);
	}

	@Override
	public <T> T get(Class<T> clazz, Object parameter) {
		List<T> resList = getList(clazz, parameter);
		if (resList.size() > 1) {
			throw new RuntimeException("Too many result");
		}
		return resList.size() == 0 ? null : resList.get(0);
	}

	@Override
	public <T> int update(T entity) {
		Sql sql = generateSQL(entity.getClass(), entity, SQLType.UPDATE);
		return executor.update(sql);
	}

	@Override
	public <T> int delete(T entity) {
		Sql sql =  generateSQL(entity.getClass(), entity, SQLType.DELETE);
		return executor.update(sql);
	}

	@Override
	public <T> int create(T entity) {
		Sql sql = generateSQL(entity.getClass(), entity, SQLType.INSERT);
		return executor.update(sql);
	}


	private Sql generateSQL(String mapping, Object parameter) {
		SqlNode sn = builder.getSqlNode(mapping);
		Sql sql = new Sql(parameter);
		StopWatch sw = StopWatch.start();
		try {
			sn.generate(sql);
		} catch (Throwable throwable) {
			logger.error("Generate sql error", throwable);
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Generate sql {" + sql.toString() +"} time(ms) : " + sw.end());
		}
		sql.setCache(sn.getCache());
		return sql;
	}

	private <T> Sql generateSQL(Class<T> entityClass, Object parameter, SQLType sqlType) {
		TableEntity tableEntity = builder.getEntityMap().get(entityClass.getName());
		if (tableEntity == null) {
			throw new RuntimeException("Table entity null");
		}
		Sql sql = null;
		switch (sqlType) {
			case QUERY:
				sql = tableEntity.parseSelectSql(parameter);
				break;
			case INSERT:
				sql = tableEntity.parseInsertSql(executor, parameter);
				break;
			case UPDATE:
				sql = tableEntity.parseUpdateSql(executor, parameter);
				break;
			case DELETE:
				sql = tableEntity.parseDeleteSql(executor, parameter);
				break;
			default:
				break;
		}
		if (sql == null) {
			throw new RuntimeException("Table entity null");
		}
		sql.setCache(tableEntity.getCache());
		return sql;
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
		executor.setSessionFactoryBuilder(builder);
		return executor;
	}

}
