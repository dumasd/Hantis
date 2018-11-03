package com.thinkerwolf.hantis.session;

import com.thinkerwolf.hantis.common.type.TypeHandlerRegistry;
import com.thinkerwolf.hantis.sql.xml.XmlSqlNodeParser;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project configuration
 *
 * @author wukai
 */
public class Configuration {
    /**
     * 全局属性
     */
    private Properties props = new Properties();

    private XmlSqlNodeParser parser = new XmlSqlNodeParser();

    private Map<String, SessionFactoryBuilder> sessionFactoryBuilders = new ConcurrentHashMap<>();

    private TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public XmlSqlNodeParser getParser() {
        return parser;
    }

    public void putSessionFactoryBuilder(SessionFactoryBuilder builder) {
        sessionFactoryBuilders.putIfAbsent(builder.getId(), builder);
    }

    public Map<String, SessionFactoryBuilder> getSessionFactoryBuilders() {
        return sessionFactoryBuilders;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

}
