package com.thinkerwolf.hantis.executor;

import java.sql.PreparedStatement;

public interface PreparedStatementBuilder extends StatementBuilder {

    PreparedStatement build() throws Exception;

}
