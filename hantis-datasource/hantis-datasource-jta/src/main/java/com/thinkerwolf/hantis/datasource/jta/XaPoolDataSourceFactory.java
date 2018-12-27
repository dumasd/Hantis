package com.thinkerwolf.hantis.datasource.jta;

import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;

import javax.sql.CommonDataSource;

public class XaPoolDataSourceFactory implements CommonDataSourceFactory {
    @Override
    public CommonDataSource getObject() throws Exception {
        DBXAPoolDataSource ds = new DBXAPoolDataSource();
        return ds;
    }

    @Override
    public Class <?> getObjectType() {
        return DBXAPoolDataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
