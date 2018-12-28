package com.thinkerwolf.hantis.datasource;

import com.thinkerwolf.hantis.common.Factory;
import com.thinkerwolf.hantis.common.SLI;

import javax.sql.CommonDataSource;

@SLI("POOL")
public interface CommonDataSourceFactory extends Factory<CommonDataSource> {
}
