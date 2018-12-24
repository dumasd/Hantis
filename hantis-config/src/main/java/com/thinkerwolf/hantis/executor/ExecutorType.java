package com.thinkerwolf.hantis.executor;

public enum ExecutorType {

    COMMON,
    BATCH,
    
    ;
    public static ExecutorType getType(String name) {
    	for (ExecutorType t : ExecutorType.values()) {
    		if (t.name().equalsIgnoreCase(name)) {
    			return t;
    		}
    	}
    	return null;
    }
    
}
