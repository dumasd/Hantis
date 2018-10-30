package com.thinkerwolf.hantis.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.thinkerwolf.hantis.common.Param;

import ognl.Ognl;

public class TextSqlNode extends AbstractSqlNode {
	private String text;
	private String jdbcSql;
	private List<String> expressions;

	public TextSqlNode(String text) {
		this.text = text;
		init();
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
		if (expressions.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String s : SqlNode.SPLIT.split(text)) {
				sb.append(s);
				sb.append(" ? ");
			}
			this.jdbcSql = sb.toString();
		} else {
			this.jdbcSql = text;
		}

	}

}
