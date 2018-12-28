package com.thinkerwolf.hantis.conf.xml;

import com.thinkerwolf.hantis.cache.Cache;
import com.thinkerwolf.hantis.cache.CacheFactory;
import com.thinkerwolf.hantis.common.Initializing;
import com.thinkerwolf.hantis.common.ServiceLoader;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.io.Resources;
import com.thinkerwolf.hantis.common.util.ClassUtils;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.common.util.ReflectionUtils;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.common.xml.XNode;
import com.thinkerwolf.hantis.common.xml.XPathParser;
import com.thinkerwolf.hantis.conf.HantisConfigException;
import com.thinkerwolf.hantis.conf.ShutdownHook;
import com.thinkerwolf.hantis.datasource.CommonDataSourceFactory;
import com.thinkerwolf.hantis.executor.ExecutorType;
import com.thinkerwolf.hantis.orm.TableEntity;
import com.thinkerwolf.hantis.orm.annotation.Entity;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.transaction.TransactionManager;
import com.thinkerwolf.hantis.transaction.TransactionManagerFactory;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class XMLConfig {

	private static final AtomicInteger sessionFacrotyId = new AtomicInteger();

	private static final String DEFAULT_SESSION_FACTORY_ID_PREFFIX = "session-factory-";

	//private InputStream is;

	private Configuration configuration;

	private XPathParser xPathParser;

	public XMLConfig(InputStream is) {
		this(is, new Configuration());
	}

	public XMLConfig(InputStream is, Configuration configuration) {
		//this.is = is;
		this.configuration = configuration;
		try {
			this.xPathParser = XPathParser.getParser(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void parse() {
		try {
			parseVariables();
			parseTransactionManager();
			parseSessionFactory();

            doAfterParse(configuration);
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(configuration));

        } catch (Throwable e) {
            throw new HantisConfigException("Can't parse config, have a exception", e);
        }

	}

    private void doAfterParse(Configuration configuration) throws Throwable {
		TransactionManager transactionManager = configuration.getTransactionManager();
        if (!transactionManager.isDistributed()) {
        	SessionFactoryBuilder builder = configuration.getAllSessionFactoryBuilder().get(0);
        	PropertyUtils.setProperty(transactionManager, "dataSource", (DataSource) builder.getDataSource());
		}
        configuration.getTransactionManager().afterPropertiesSet();
    }

	private void parseVariables() {
		XNode xNode = xPathParser.evalNode("/configuration/props", xPathParser.getDoc());
		if (xNode != null) {
			xPathParser.setVariables(xNode.getChildrenAsProperties());
		}
	}

	private void parseTransactionManager() {
		XNode xNode = xPathParser.evalNode("/configuration/sessionFactories/transactionManager", xPathParser.getDoc());
		if (xNode == null) {
			throw new HantisConfigException("Not find TransactionManager config!");
		}
		String type = xNode.getStringAttrubute("type");
		Properties properties = xNode.getChildrenAsProperties();
		if (StringUtils.isEmpty(type)) {
			throw new HantisConfigException("TransactionManager type is null");
		}
		TransactionManagerFactory factory = ServiceLoader.getService(type, TransactionManagerFactory.class);
		TransactionManager transactionManager;
		try {
			transactionManager = factory.getObject();
		} catch (Exception e) {
			throw new HantisConfigException(e);
		}
		PropertyUtils.setProperties(transactionManager, properties);
		configuration.setTransactionManager(transactionManager);
	}

	private void parseSessionFactory() {
		List<XNode> xNodes = xPathParser.evalNodeList("/configuration/sessionFactories/sessionFactory", xPathParser.getDoc());
		if (xNodes == null || xNodes.size() == 0) {
			throw new HantisConfigException("Not find SessionFactory config!");
		}

		try {
			for (XNode xNode : xNodes) {
				String id = xNode.getStringAttrubute("id");
				if (StringUtils.isEmpty(id)) {
					id = DEFAULT_SESSION_FACTORY_ID_PREFFIX + sessionFacrotyId.incrementAndGet();
				}
				SessionFactoryBuilder builder = new SessionFactoryBuilder();
				CommonDataSource ds = dataSource(xNode.evalNode("dataSource"));
				ExecutorType executorType = executorType(xNode.evalNode("executor"));
				Map<String, SqlNode> sqlNodeMap = sqlNodes(xNode.evalNode("mappings"));
				Map<String, TableEntity<?>> entityMap = tableEntities(xNode.evalNode("mappings"));
				
				CacheFactory cacheFactory = cacheFactory(xNode.evalNode("cache"));

				if (cacheFactory != null) {
					if (sqlNodeMap.size() > 0) {
						Cache cache = cacheFactory.getObject();
						for (SqlNode sn : sqlNodeMap.values()) {
							sn.setCache(cache);
						}
					}
					if (entityMap.size() > 0) {
						Cache cache = cacheFactory.getObject();
						for (TableEntity<?> te : entityMap.values()) {
							te.setCache(cache);
						}
					}
				}

				builder.setId(id);
				builder.setDataSource(ds);
				builder.setSqlNodeMap(sqlNodeMap);
				builder.setEntityMap(entityMap);
				builder.setConfiguration(configuration);
				builder.setExecutorType(executorType);
				configuration.putSessionFactoryBuilder(builder);
				if (!configuration.getTransactionManager().isDistributed()) {
					break;
				}
			}

		} catch (Throwable e) {
			throw new HantisConfigException(e);
		}

	}

	private CacheFactory cacheFactory(XNode xNode) {
		if (xNode == null) {
			 return null;
		}
		Boolean enable = true;
		enable = xNode.getBoolAttrubute("enable");
		enable = enable == null ? true : enable;
		if (!enable) {
			return null;
		}
		String type = xNode.getStringAttrubute("type");
		if (StringUtils.isEmpty(type)) {
			throw new HantisConfigException("Cache type is null");
		}
		CacheFactory factory = ServiceLoader.getService(type, CacheFactory.class);
		factory.setProperties(xNode.getChildrenAsProperties());
		return factory;
	}

	private CommonDataSource dataSource(XNode xNode) {
		String type = xNode.getStringAttrubute("type");
		if (StringUtils.isEmpty(type)) {
			throw new HantisConfigException("Not find dataSource config!");
		}
		CommonDataSourceFactory dsFactory = ServiceLoader.getService(type, CommonDataSourceFactory.class);
		try {
			CommonDataSource ds = dsFactory.getObject();
			PropertyUtils.setProperties(ds, xNode.getChildrenAsProperties());
			if (ds instanceof Initializing) {
				((Initializing) ds).afterPropertiesSet();
			}
			return ds;
		} catch (Throwable e) {
			throw new HantisConfigException(e);
		}
	}

	private Map<String, SqlNode> sqlNodes(XNode xNode) {
		Map<String, SqlNode> map = new HashMap<>();
		List<XNode> xNodes = xNode.evalNodeList("mapping");
		for (XNode xn : xNodes) {
			// resource 解析sql xml文件
			if (xn.hasAttribute("resource")) {
				String resource = xn.getStringAttrubute("resource");
				Resource[] resources = Resources.getResources(resource);
				for (Resource r : resources) {
					if (r.getPath().endsWith(".xml")) {
						try {
							map.putAll(configuration.getParser().parse(r.getInputStream()));
						} catch (Throwable e) {
							throw new HantisConfigException(e);
						}
					}
				}
			}
		}
		return map;
	}

	private Map<String, TableEntity<?>> tableEntities(XNode xNode) {
		Map<String, TableEntity<?>> map = new HashMap <>();
		List<XNode> xNodes = xNode.evalNodeList("mapping");
		for (XNode xn : xNodes) {
			// 解析TableEntity
			if (xn.hasAttribute("class")) {
				String clazz = xn.getStringAttrubute("class");
				parseAnnotationEntity(ClassUtils.forName(clazz), map);
			}

			if (xn.hasAttribute("package")) {
				String packageName = xn.getStringAttrubute("package");
				Set<Class<?>> set = ClassUtils.scanClasses(packageName);
				for (Class<?> clazz : set) {
					parseAnnotationEntity(clazz, map);
				}
			}
		}
		return map;
	}

	private ExecutorType executorType(XNode xNode) {
		if (xNode == null) {
			return ExecutorType.COMMON;
		}
		return ExecutorType.getType(xNode.getStringAttrubute("type"));
	}


	private void parseAnnotationEntity(Class<?> clazz, Map<String, TableEntity<?>> map) {
		if (ReflectionUtils.getAnnotation(clazz, Entity.class) != null) {
			map.put(clazz.getName(), new TableEntity<>(clazz, configuration.getNameHandler()));
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}
