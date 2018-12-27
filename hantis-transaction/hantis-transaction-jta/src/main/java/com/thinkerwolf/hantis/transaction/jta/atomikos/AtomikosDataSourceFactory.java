package com.thinkerwolf.hantis.transaction.jta.atomikos;

import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;

import javax.sql.CommonDataSource;

public class AtomikosDataSourceFactory implements CommonDataSourceFactory {
    @Override
    public CommonDataSource getObject() throws Exception {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        return ds;
    }

    @Override
    public Class <?> getObjectType() {
        return AtomikosDataSourceBean.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
