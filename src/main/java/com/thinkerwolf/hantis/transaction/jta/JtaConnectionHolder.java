package com.thinkerwolf.hantis.transaction.jta;

import java.sql.Connection;

import com.thinkerwolf.hantis.transaction.ConnectionHolder;

public class JtaConnectionHolder extends ConnectionHolder {

	public JtaConnectionHolder(Connection conn) {
		super(conn);
	}

	@Override
	public Object getResource() {
		return null;
	}

}
