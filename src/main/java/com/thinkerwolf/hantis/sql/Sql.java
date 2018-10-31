package com.thinkerwolf.hantis.sql;

import java.util.List;

import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.common.Params;

public class Sql {
	/**
	 * 原始参数
	 */
	private Object inputParameter;
	
	private StringBuilder sqlBuilder = new StringBuilder();
	
	private List<Param> params = new Params();
	
	
	
	public Sql(Object inputParameter) {
		this.inputParameter = inputParameter;
	}
	
	public String getSql() {
		return sqlBuilder.toString();
	}

	public List<Param> getParams() {
		return params;
	}

	public void appendSql(String s) {
		sqlBuilder.append(" " + s + " ");
	}

	public void appendParam(Param param) {
		this.params.add(param);
	}

	public void appendParams(List<Param> params) {
		this.params.addAll(params);
	}

	public Object getInputParameter() {
		return inputParameter;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[SQL]" + sqlBuilder);
		sb.append(" [Params]" + params);
		return sb.toString();
	}

}
