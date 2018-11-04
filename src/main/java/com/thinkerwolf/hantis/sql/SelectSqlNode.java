package com.thinkerwolf.hantis.sql;

import java.util.List;

public class SelectSqlNode<P, T> extends AbstractSqlNode {
    /**
     * 参数类型
     */
    private Class<P> parameterType;
    /**
     * 返回类型
     */
    private Class<T> returnType;

    public SelectSqlNode(Class<P> parameterType, Class<T> returnType) {
        this.parameterType = parameterType;
        this.returnType = returnType;
    }

    @Override
    public boolean generate(Sql sql) throws Throwable {
        List<SqlNode> children = getChildren();
        if (children != null && children.size() > 0) {
            for (SqlNode sn : children) {
                sn.generate(sql);
            }
        }
        sql.setParameterType(parameterType);
        sql.setReturnType(returnType);
        return true;
    }

    public Class<P> getParameterType() {
        return parameterType;
    }

    public Class<T> getReturnType() {
        return returnType;
    }

}
