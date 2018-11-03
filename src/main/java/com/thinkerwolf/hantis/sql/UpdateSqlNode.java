package com.thinkerwolf.hantis.sql;

import java.util.List;

public class UpdateSqlNode extends AbstractSqlNode {

    private Class<?> parameterType;

    public UpdateSqlNode(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    @Override
    public boolean generate(Sql sql) throws Throwable {
        List<SqlNode> children = getChildren();
        if (children != null && children.size() > 0) {
            for (SqlNode sn : children) {
                sn.generate(sql);
            }
        }
        return true;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }


}
