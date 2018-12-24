package com.thinkerwolf.hantis.sql;

import java.util.List;
import java.util.regex.Pattern;

public interface SqlNode {

    Pattern SPLIT = Pattern.compile("#\\s*\\{\\s*[^#\\{\\}]*\\s*\\}");
    Pattern ARUMENT = Pattern.compile("[^#\\s\\{\\}]+");
    Pattern EMPTY_TEXT = Pattern.compile("[\\n\\t\\f\\s\\r]*");
    Pattern TRIM = Pattern.compile("[\\n\\t\\f\\r]+");

    boolean generate(Sql sql) throws Throwable;

    List<SqlNode> getChildren();

    void setChildren(List<SqlNode> children);


}
