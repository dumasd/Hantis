package com.thinkerwolf.hantis.executor;

import java.sql.Statement;

public interface StatementBuilder {

    Statement build() throws Exception;

}
