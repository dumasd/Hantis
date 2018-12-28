package com.thinkerwolf.hantis.sql;

import com.thinkerwolf.hantis.cache.Cache;
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

    private Cache cache;

    /**
     * 参数类型
     */
    private Class<?> parameterType;
    /**
     * 返回类型
     */
    private Class<?> returnType;

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

    public Sql appendSql(String s) {
        sqlBuilder.append(s + " ");
        return this;
    }

    public Sql appendParam(Param param) {
        this.params.add(param);
        return this;
    }

    public Sql appendParams(List<Param> params) {
        this.params.addAll(params);
        return this;
    }

    public Object getInputParameter() {
        return inputParameter;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
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
        sb.append("[SQL]{" + getSql());
        sb.append(", " + params);
        sb.append("}");
        return sb.toString();
    }

}
