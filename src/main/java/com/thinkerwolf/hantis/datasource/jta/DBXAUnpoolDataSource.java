package com.thinkerwolf.hantis.datasource.jta;

import javax.sql.XAConnection;
import java.sql.SQLException;

public class DBXAUnpoolDataSource extends AbstractXADataSource {

    @Override
    protected XAConnection doGetXAConnection() throws SQLException {
        return xaDataSource.getXAConnection();
    }

}
