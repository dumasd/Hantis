package com.thinkerwolf.hantis.common;

/**
 * 列名转换处理器
 *
 * @author wukai
 */
public interface NameHandler {

	/**
	 * 列名转化成属性名
	 *
	 * @param columnName
	 *            数据库列名
	 * @return String
	 */
	String convertToPropertyName(String columnName);

	/**
	 * 属性名转化成列名
	 *
	 * @param propertyName
	 *            属性名
	 * @return String
	 */
	String convertToColumnName(String propertyName);

	/**
	 * 类名转化成表名
	 * 
	 * @param className
	 *            类名
	 * @return
	 */
	String convertToTableName(String className);

	/**
	 * 表名转化成类名
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	String convertToClassName(String tableName);

	/**
	 * 处理器名称
	 *
	 * @return String
	 */
	String name();

}
