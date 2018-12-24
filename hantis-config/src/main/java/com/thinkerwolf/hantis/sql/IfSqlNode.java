package com.thinkerwolf.hantis.sql;

import ognl.Ognl;

import java.util.List;

public class IfSqlNode extends AbstractSqlNode {

    private String expression;

    public IfSqlNode(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean generate(Sql sql) throws Throwable {
        Object value = Ognl.getValue(expression, sql.getInputParameter());
        if (value == null || !(value instanceof Boolean)) {
            throw new RuntimeException("Expression error " + expression);
        }
        boolean b = (boolean) value;
        if (b) {
            List<SqlNode> children = getChildren();
            if (children != null && children.size() > 0) {
                for (SqlNode sn : children) {
                    sn.generate(sql);
                }
            }
        }
        return b;
    }

}
