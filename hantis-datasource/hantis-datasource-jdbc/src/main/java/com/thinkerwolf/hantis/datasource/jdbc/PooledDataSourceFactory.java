package com.thinkerwolf.hantis.datasource.jdbc;

import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;

import javax.sql.CommonDataSource;

public class PooledDataSourceFactory implements CommonDataSourceFactory {
    @Override
    public CommonDataSource getObject() throws Exception {
        DBPoolDataSource ds = new DBPoolDataSource();
        return ds;
    }

    @Override
    public Class <?> getObjectType() {
        return DBPoolDataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
