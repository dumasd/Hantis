package com.thinkerwolf.hantis.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.common.NameHandler;
import com.thinkerwolf.hantis.common.io.Resources;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import com.thinkerwolf.hantis.type.JDBCType;
import com.thinkerwolf.hantis.type.TypeHandler;
import com.thinkerwolf.hantis.type.TypeHandlerRegistry;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class TableInfo {
	private String tableNames;
	private String packageName;
	private String outputLocation;

	public String getTableNames() {
		return tableNames;
	}

	public void setTableNames(String tableNames) {
		this.tableNames = tableNames;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getOutputLocation() {
		return outputLocation;
	}

	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}

	private static final String tableInfoSql = "select COLUMN_NAME, COLUMN_COMMENT, DATA_TYPE, EXTRA, COLUMN_KEY from information_schema.COLUMNS where table_name = '{tableName}' and table_schema = '{schameName}'";
	private static TypeHandlerRegistry registry = new TypeHandlerRegistry();
	public static String DICTIONARY_TEMPLATE = "classpath:META-INF/template";
	public static String TEMPLATE_NAME = "entity.ftl";

	public static void generate(TableInfo tableInfo, DBInfo dbInfo) throws Exception {
		NameHandler nameHandler = new DefaultNameHandler();

		final DBPoolDataSource ds = new DBPoolDataSource();
		ds.setDriver(dbInfo.getDriver());
		ds.setUrl(dbInfo.getUrl());
		ds.setUsername(dbInfo.getUser());
		ds.setPassword(dbInfo.getPassword());
		ds.setMaxConn(10);
		ds.setMinConn(2);

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setDirectoryForTemplateLoading(new File(Resources.getResource(DICTIONARY_TEMPLATE).getRealPath()));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template template = cfg.getTemplate(TEMPLATE_NAME);
		Connection conn = ds.getConnection();
		for (String tableName : tableInfo.getTableNames().split(",")) {
			if (StringUtils.isNotEmpty(tableName.trim())) {
				Map<String, Object> map = getFreemarkerData(conn, dbInfo.getSchameName(), tableName, nameHandler);
				map.put("packageName", tableInfo.getPackageName());
				String fileName = tableInfo.getOutputLocation() + "/" + map.get("className") + ".java";
				Writer out = new OutputStreamWriter(new FileOutputStream(fileName));
				template.process(map, out);
			}
		}
	}

	private static Map<String, Object> getFreemarkerData(Connection conn, String schameName, String tableName,
			NameHandler nameHandler) {
		Map<String, Object> map = new HashMap<>();
		map.put("className", nameHandler.convertToClassName(tableName));
		map.put("tableName", tableName);
		String sql = tableInfoSql.replaceAll("\\{tableName\\}", tableName).replaceAll("\\{schameName\\}", schameName);
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<ColumnInfo> columnInfos = new ArrayList<>();
			List<String> importList = new ArrayList<>();
			while (rs.next()) {
				ColumnInfo ci = new ColumnInfo();
				ci.setColumnName(rs.getString(1));
				ci.setFieldName(nameHandler.convertToPropertyName(rs.getString(1)));
				ci.setComment(rs.getString(2));
				if ("PRI".equals(rs.getString(5))) {
					ci.setPrimaryKey(true);
				}
				if (rs.getString(4).contains("auto_increment")) {
					ci.setAutoIncrement(true);
				}
				TypeHandler<?> handler = registry.getHandler(JDBCType.getJDBCType(rs.getString(3)));

				Class<?> classType = handler.getType();
				if (!classType.getName().startsWith("java.lang") && classType.getName().contains(".")) {
					importList.add(classType.getName());
				}
				ci.setField(classType.getSimpleName());
				columnInfos.add(ci);
			}
			map.put("columnInfos", columnInfos);
			map.put("importList", importList);
			return map;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
