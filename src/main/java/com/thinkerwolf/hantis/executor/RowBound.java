package com.thinkerwolf.hantis.executor;

import java.io.Closeable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 包装ResultSet
 * 
 * @author wukai
 *
 */
public class RowBound implements Closeable {

	private ResultSet resultSet;
	
	private static final Logger logger = LoggerFactory.getLogger(RowBound.class);
	
	public RowBound(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public synchronized ResultSet getResultSet() {
		try {
			resultSet.absolute(0);
		} catch (SQLException e) {
			throw new ExecutorException(e);
		}
		return resultSet;
	}

	@Override
	public void close() throws IOException {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Close Rowbound resultSet");
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

}
