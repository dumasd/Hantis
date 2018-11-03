package com.thinkerwolf.hantis.executor;

import java.sql.PreparedStatement;

public interface PreparedStatementBuilder {

    PreparedStatement build() throws Throwable;

}
