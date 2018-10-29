package com.thinkerwolf.hantis.common;

/**
 * 列名转换处理器
 * 
 * @author wukai
 *
 */
public interface NameHandler {

	/**
	 * 列名转化成属性名
	 * 
	 * @param columnName
	 * @return
	 */
	String convertToPropertyName(String columnName);

	/**
	 * 属性名转化成列名
	 * 
	 * @param propertyName
	 * @return
	 */
	String convertToColumnName(String propertyName);

	/**
	 * 处理器名称
	 * 
	 * @return
	 */
	String name();

}
