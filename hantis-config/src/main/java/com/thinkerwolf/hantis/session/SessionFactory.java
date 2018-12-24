package com.thinkerwolf.hantis.session;

public interface SessionFactory {

    Session openSession();

    Session openSession(boolean autoCommit);

}
