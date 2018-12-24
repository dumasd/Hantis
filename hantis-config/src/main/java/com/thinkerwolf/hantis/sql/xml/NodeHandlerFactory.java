package com.thinkerwolf.hantis.sql.xml;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeHandlerFactory {

    static Map<String, NodeHandler> handlerMap = new ConcurrentHashMap<>();

    static {
        handlerMap.put("select", new SelectNodeHandler());
        handlerMap.put("update", new UpdateNodeHandler());
        handlerMap.put("where", new WhereNodeHandler());
        handlerMap.put("if", new IfNodeHandler());
    }

    public static NodeHandler getNodeHandler(String name) {
        return handlerMap.get(name);
    }

}
