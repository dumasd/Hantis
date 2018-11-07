package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.common.StopWatch;
import com.thinkerwolf.hantis.executor.BatchExecutor;
import com.thinkerwolf.hantis.executor.CommonExecutor;
import com.thinkerwolf.hantis.executor.Executor;
import com.thinkerwolf.hantis.executor.ExecutorType;
import com.thinkerwolf.hantis.sql.Sql;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.transaction.Transaction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class DefaultSession implements Session {

	private Transaction transaction;

	// private DataSource dataSource;

	private Executor executor;

	private SessionFactoryBuilder builder;

	private static final Logger logger = LoggerFactory.getLogger(DefaultSession.class);

	public DefaultSession(Transaction transaction, SessionFactoryBuilder builder) {
		this.transaction = transaction;
		this.builder = builder;
		// this.dataSource = builder.getDataSource();
		// 单个Session持有一个executor
		this.executor = createExecutor(builder.getExecutorType());
	}

	@Override
	public void close() throws IOException {
		try {
			transaction.close();
            executor.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
	}

	@Override
	public void commit() {
		try {
			executor.doBeforeCommit();
			transaction.commit();
            executor.doAfterCommit();
        } catch (SQLException e) {
            logger.error("commit", e);
        }
	}

	@Override
	public void rollback() {
		try {
			executor.doBeforeRollback();
			transaction.rollback();
            executor.doAfterRollback();
        } catch (SQLException e) {
			e.printStackTrace();
		}
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
			logger.debug(sql.toString() + " 生成SQL消耗时间：" + sw.end());
		}
		return executor.queryForOne(sql.getSql(), sql.getParams(), (Class<T>) sql.getReturnType());
	}

	@Override
	public <T> T selectOne(String mapping) {
		return selectOne(mapping, null);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String mapping, Object parameter) {
		return null;
	}

	@Override
	public <K, V> Map<K, V> selectMap(String mapping) {
		return null;
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
			logger.debug(sql.toString() + " 生成SQL消耗时间：" + sw.end());
		}
		// System.out.println("update" + sql);
		return executor.update(sql.getSql(), sql.getParams());
	}

	@Override
	public int update(String mapping) {
		return update(mapping, null);
	}

	private Executor createExecutor(ExecutorType executorType) {
		executorType = executorType == null ? ExecutorType.COMMON : executorType;
		Executor executor = null;
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
		return executor;
	}

}
