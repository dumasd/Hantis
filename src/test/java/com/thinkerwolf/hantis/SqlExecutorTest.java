package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.Params;
import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import com.thinkerwolf.hantis.example.Blog;
import com.thinkerwolf.hantis.executor.SqlExecutor;
import com.thinkerwolf.hantis.type.JDBCType;

import org.junit.Test;

import java.util.List;
import java.util.Map;

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

		String sql = "select * from blog where id = ? ";

		Map<String, Object> m = sqlExe.queryForOne(sql, params);
		List<Blog> blogs = sqlExe.queryForList("select * from blog where id = ? ", params, Blog.class);
		System.out.println(blogs);
		System.out.println(m);

	}

}
