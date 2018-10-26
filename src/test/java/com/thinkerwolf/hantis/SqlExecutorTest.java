package com.thinkerwolf.hantis;

import java.util.List;

import org.junit.Test;

import com.thinkerwolf.hantis.common.JDBCType;
import com.thinkerwolf.hantis.common.Params;
import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import com.thinkerwolf.hantis.sql.SqlExecutor;

public class SqlExecutorTest {

	@Test
	public void testJdbcExecutor() throws Throwable {
		final DBPoolDataSource ds = new DBPoolDataSource();
		ds.setDriver("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC");
		ds.setUsername("root");
		ds.setPassword("123");
		ds.setMaxConn(100);
		ds.setMinConn(2);

		SqlExecutor sqlExe = new SqlExecutor();
		sqlExe.setDataSource(ds);
		Params params = new Params();
		params.addParam(JDBCType.INTEGER, 3);
		List<Blog> blogs = sqlExe.queryForList("select * from blog where id = ? ", params, Blog.class);
		System.out.println(blogs);

	}

}
