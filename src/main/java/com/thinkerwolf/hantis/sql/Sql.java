package com.thinkerwolf.hantis.sql;

import com.thinkerwolf.hantis.common.Param;
import com.thinkerwolf.hantis.common.Params;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql {

    private static final Map<String, Object> EMPTY_PARAMETER = new HashMap<>();

    /**
     * 原始参数
     */
    private Object inputParameter;

    private StringBuilder sqlBuilder = new StringBuilder();

    private List<Param> params = new Params();


    public Sql(Object inputParameter) {
        if (inputParameter == null) {
            this.inputParameter = EMPTY_PARAMETER;
        } else {
            this.inputParameter = inputParameter;
        }
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

//	public Configuration getConfiguration() {
//		return configuration;
//	}
//
//	public void setConfiguration(Configuration configuration) {
//		this.configuration = configuration;
//	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[SQL]" + sqlBuilder);
        sb.append(" [Params]" + params);
        return sb.toString();
    }

}
