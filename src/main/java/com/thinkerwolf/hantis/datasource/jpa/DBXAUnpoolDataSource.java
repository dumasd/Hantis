package com.thinkerwolf.hantis.datasource.jpa;

import java.sql.SQLException;

import javax.sql.XAConnection;

public class DBXAUnpoolDataSource extends AbstractXADataSource {

	@Override
	protected XAConnection doGetXAConnection() throws SQLException {
		return xaDataSource.getXAConnection();
	}

}
