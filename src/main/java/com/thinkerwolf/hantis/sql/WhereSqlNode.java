package com.thinkerwolf.hantis.sql;

import com.thinkerwolf.hantis.common.Params;

import java.util.List;

public class WhereSqlNode extends AbstractSqlNode {

    @Override
    public boolean generate(Sql sql) throws Throwable {
        List<SqlNode> children = getChildren();
        boolean success = false;

        StringBuilder childSqlBuilder = new StringBuilder();
        Params childParams = new Params();
        if (children != null && children.size() > 0) {
            boolean first = true;
            for (SqlNode sn : children) {
                Sql childrenSql = new Sql(sql.getInputParameter());
                if (sn.generate(childrenSql)) {
                    success = true;
                    if (first) {
                        first = false;
                        childSqlBuilder.append(childrenSql.getSql());
                    } else {
                        childSqlBuilder.append(" AND" + childrenSql.getSql());
                    }
                    childParams.addAll(childrenSql.getParams());
                }
            }
        }
        if (success) {
            sql.appendSql(" WHERE" + childSqlBuilder.toString());
            sql.appendParams(childParams);
        }
        return success;
    }

}
