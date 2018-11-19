package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.transaction.Transaction;

public interface SessionFactory {

    Session openSession();

    Session openSession(boolean autoCommit);

    Transaction openTransaction();

    Session openSessionWithoutTransaction();

}
