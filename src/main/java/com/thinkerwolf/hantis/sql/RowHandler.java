package com.thinkerwolf.hantis.sql;

import java.sql.ResultSet;

/**
 * 返回结果行处理器
 * 
 * @author wukai
 *
 */
public interface RowHandler<T> {
	
	T processRow(ResultSet rs) throws Throwable;
	
}
