package com.thinkerwolf.hantis.sql;

import java.util.List;

public class SelectSqlNode extends AbstractSqlNode {
    /**
     * 参数类型
     */
    private Class<?> parameterType;
    /**
     * 返回类型
     */
    private Class<?> returnType;

    public SelectSqlNode(Class<?> parameterType, Class<?> returnType) {
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

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

}
