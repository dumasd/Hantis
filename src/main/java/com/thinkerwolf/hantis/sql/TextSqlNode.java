package com.thinkerwolf.hantis.sql;

import com.thinkerwolf.hantis.common.Param;
import ognl.Ognl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class TextSqlNode extends AbstractSqlNode {
    private String text;
    private String jdbcSql;
    private List<String> expressions;

    public TextSqlNode(String text) {
        this.text = text;
        init();
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void init() {
        Matcher m = SqlNode.SPLIT.matcher(text);
        this.expressions = new ArrayList<>();
        while (m.find()) {
            Matcher arguMh = SqlNode.ARUMENT.matcher(m.group());
            arguMh.find();
            expressions.add(arguMh.group());
        }
        jdbcSql = text;
        for (String express : expressions) {
            jdbcSql = jdbcSql.replaceAll("#\\s*\\{\\s*" + express + "\\s*\\}", "?");
        }
    }

    @Override
    public boolean generate(Sql sql) throws Throwable {
        sql.appendSql(this.jdbcSql);
        for (String expression : expressions) {
            Object value = Ognl.getValue(expression, sql.getInputParameter());
            sql.appendParam(new Param(value));
        }
        return true;
    }


}
