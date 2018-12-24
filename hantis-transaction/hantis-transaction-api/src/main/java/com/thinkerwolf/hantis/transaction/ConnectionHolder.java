package com.thinkerwolf.hantis.transaction;

import java.sql.Connection;

public abstract class ConnectionHolder implements ResourceHolder {
	private int previousIsolationLevel;

	private boolean previousAutoCommit;
	
	private Connection conn;

	public ConnectionHolder(Connection conn) {
		this.conn = conn;
	}

	public Connection getConnection() {
		return conn;
	}


	public int getPreviousIsolationLevel() {
		return previousIsolationLevel;
	}

	public void setPreviousIsolationLevel(int previousIsolationLevel) {
		this.previousIsolationLevel = previousIsolationLevel;
	}

	public boolean isPreviousAutoCommit() {
		return previousAutoCommit;
	}

	public void setPreviousAutoCommit(boolean previousAutoCommit) {
		this.previousAutoCommit = previousAutoCommit;
	}

	
}
