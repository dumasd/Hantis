package com.thinkerwolf.hantis.conf.xml;

import com.thinkerwolf.hantis.common.Initializing;
import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.io.Resources;
import com.thinkerwolf.hantis.common.util.ClassUtils;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.common.util.ReflectionUtils;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.conf.HantisConfigException;
import com.thinkerwolf.hantis.conf.ShutdownHook;
import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import com.thinkerwolf.hantis.datasource.jdbc.DBUnpoolDataSource;
import com.thinkerwolf.hantis.executor.ExecutorType;
import com.thinkerwolf.hantis.orm.TableEntity;
import com.thinkerwolf.hantis.orm.annotation.Entity;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.transaction.TransactionManager;
import com.thinkerwolf.hantis.transaction.jdbc.JdbcTransactionManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thinkerwolf.hantis.common.Constants.*;

public class XMLConfig {

	private static final Pattern PLACE_HOLDER = Pattern.compile("\\$\\s*\\{.*\\}");
	private static final Pattern PROP_NAME = Pattern.compile("[^\\$\\s\\{\\}]+");

	private static final AtomicInteger sessionFacrotyId = new AtomicInteger();

	private static final String DEFAULT_SESSION_FACTORY_ID_PREFFIX = "session-factory-";

	private InputStream is;

	private Configuration configuration;

	public XMLConfig(InputStream is) {
		this(is, new Configuration());
	}

	public XMLConfig(InputStream is, Configuration configuration) {
		this.is = is;
		this.configuration = configuration;
	}

	public void parse() {
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);
			if (doc.getElementsByTagName("sessionFactories").getLength() == 0) {
				return;
			}

			// 解析props
			NodeList propsNodeList = doc.getElementsByTagName("props");
			for (int i = 0, len = propsNodeList.getLength(); i < len; i++) {
				parseProps((Element) propsNodeList.item(i));
			}

			Element sessionFactoriesEl = (Element) doc.getElementsByTagName("sessionFactories").item(0);

            // 解析transactionManager
            NodeList tmNl = doc.getElementsByTagName("transactionManager");
            parseTransactionManager(tmNl.getLength() > 0 ? (Element) tmNl.item(0) : null);

            // 解析sessionFactory，当TransactionManager未Jdbc时，只解析一个sessionFactory
            NodeList sessionFactoryNodeList = sessionFactoriesEl.getElementsByTagName("sessionFactory");
            for (int i = 0, len = sessionFactoryNodeList.getLength(); i < len; i++) {
				parseSessionFactory((Element) sessionFactoryNodeList.item(i));
                if (!configuration.getTransactionManager().isDistributed()) {
                    break;
                }
			}

            doAfterParse(configuration);

            Runtime.getRuntime().addShutdownHook(new ShutdownHook(configuration));

        } catch (Throwable e) {
            throw new HantisConfigException("Can't parse config, have a exception", e);
        }

	}

    private void doAfterParse(Configuration configuration) throws Throwable {
        for (SessionFactoryBuilder builder : configuration.getAllSessionFactoryBuilder()) {
            if (!configuration.getTransactionManager().isDistributed()) {
                ((JdbcTransactionManager) configuration.getTransactionManager()).setDataSource((DataSource) builder.getDataSource());
            }
        }
        configuration.getTransactionManager().afterPropertiesSet();
    }


	private void parseProps(Element el) {
		NodeList propNode = el.getChildNodes();
		for (int i = 0, len = propNode.getLength(); i < len; i++) {
			Node node = propNode.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element propEl = (Element) node;
			configuration.getProps().setProperty(propEl.getAttribute("name").trim(),
					propEl.getAttribute("value").trim());
		}
	}

	private SessionFactoryBuilder parseSessionFactory(Element el) {
		String id = el.getAttribute("id");
		if (StringUtils.isEmpty(id)) {
			id = DEFAULT_SESSION_FACTORY_ID_PREFFIX + sessionFacrotyId.incrementAndGet();
		}
		// 解析DataSource
		SessionFactoryBuilder builder = new SessionFactoryBuilder();
		Element dataSourceEl = (Element) el.getElementsByTagName("dataSource").item(0);
		CommonDataSource dataSource = parseDataSource(dataSourceEl);

        if (dataSource instanceof Initializing) {
            try {
                ((Initializing) dataSource).afterPropertiesSet();
            } catch (Throwable throwable) {
                throw new HantisConfigException(throwable);
            }
        }

		// 解析mapping
		Map<String, SqlNode> sqlNodeMap = new HashMap<>();
		Map<String, TableEntity<?>> tableEntityMap = new HashMap<>();
		Element sqlsEl = (Element) el.getElementsByTagName("mappings").item(0);
		parseMappings(sqlsEl, sqlNodeMap, tableEntityMap);

		// 解析Executor
		NodeList exeNl = el.getElementsByTagName("executor");
		ExecutorType executorType = parseExecutor((Element) exeNl.item(0));

		builder.setId(id);
		builder.setDataSource(dataSource);
		builder.setSqlNodeMap(sqlNodeMap);
		builder.setEntityMap(tableEntityMap);
		builder.setConfiguration(configuration);
		builder.setExecutorType(executorType);
		configuration.putSessionFactoryBuilder(builder);


        return builder;
    }

	private CommonDataSource parseDataSource(Element el) {
		String dataSourceType = el.getAttribute("type");
		if (StringUtils.isEmpty(dataSourceType)) {
			throw new HantisConfigException("dataSource type is null");
		}
        CommonDataSource dataSource;
        if (ALIAS_POOL.equals(dataSourceType)) {
            dataSource = new DBPoolDataSource();
		} else if (ALIAS_UNPOOL.equals(dataSourceType)) {
			dataSource = new DBUnpoolDataSource();
		} else if (ALIAS_ATOMIKOS.equals(dataSourceType)) {
			dataSource = (DataSource) ClassUtils.newInstance(DATASOUCE_ATOMIKOS_NAME);
		} else {
			dataSource = (DataSource) ClassUtils.newInstance(dataSourceType);
		}
		Properties props = parseElementProps(el);
		PropertyUtils.setProperties(dataSource, props);
		return dataSource;
	}

	/**
	 * 解析sqls
	 *
	 * @param el
	 */
	private void parseMappings(Element el, Map<String, SqlNode> sqlNodeMap,
			Map<String, TableEntity<?>> tableEntityMap) {
		NodeList nl = el.getElementsByTagName("mapping");
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Element mappingEl = (Element) nl.item(i);

			// resource 解析sql xml文件
			if (mappingEl.hasAttribute("resource")) {
				String resource = mappingEl.getAttribute("resource");
				Resource[] resources = Resources.getResources(resource);
				for (Resource r : resources) {
					if (r.getPath().endsWith(".xml")) {
						try {
							sqlNodeMap.putAll(configuration.getParser().parse(r.getInputStream()));
						} catch (Throwable e) {
                            throw new HantisConfigException(e);
                        }
                    }
				}
			}

			// 解析TableEntity
			if (mappingEl.hasAttribute("class")) {
				String clazz = mappingEl.getAttribute("class");
				parseAnnotationEntity(ClassUtils.forName(clazz), tableEntityMap);
			}

			if (mappingEl.hasAttribute("package")) {
				String packageName = mappingEl.getAttribute("package");
				Set<Class<?>> set = ClassUtils.scanClasses(packageName);
				for (Class<?> clazz : set) {
					parseAnnotationEntity(clazz, tableEntityMap);
				}
			}

		}
	}

	private void parseAnnotationEntity(Class<?> clazz, Map<String, TableEntity<?>> map) {
		if (ReflectionUtils.getAnnotation(clazz, Entity.class) != null) {
			map.put(clazz.getName(), new TableEntity<>(clazz, configuration.getNameHandler()));
		}
	}

	private ExecutorType parseExecutor(Element el) {
		if (el != null) {
			return ExecutorType.getType(el.getAttribute("type"));
		} else {
			return null;
		}
	}

	private String getPropertyValue(String originValue) {
		if (PLACE_HOLDER.matcher(originValue).find()) {
			Matcher m = PROP_NAME.matcher(originValue);
			m.find();
			String propName = m.group().trim();
			originValue = configuration.getProps().getProperty(propName);
		}
		return originValue;
	}

	private void parseTransactionManager(Element el) {
        if (el == null) {
            configuration.setTransactionManager(new JdbcTransactionManager());
            return;
        }
        String type = el.getAttribute("type");

		if (StringUtils.isEmpty(type)) {
			throw new HantisConfigException("TransactionManager type is null");
		}
		Properties props = parseElementProps(el);

		TransactionManager transactionManager = null;
		if (ALIAS_JDBC.equalsIgnoreCase(type)) {
			transactionManager = new JdbcTransactionManager();
		} else if (ALIAS_ATOMIKOS.equalsIgnoreCase(type)) {
			transactionManager = ClassUtils.newInstance(TRANSACTIONMANAGER_ATOMIKOS_NAME);
		} else {
			transactionManager = ClassUtils.newInstance(type);
		}
		PropertyUtils.setProperties(transactionManager, props);
		configuration.setTransactionManager(transactionManager);
	}

	private Properties parseElementProps(Element el) {
		NodeList propsNl = el.getElementsByTagName("property");
		Properties props = new Properties();
		for (int i = 0, len = propsNl.getLength(); i < len; i++) {
			Element propEl = (Element) propsNl.item(i);
			Object value = null;
			if (propEl.hasAttribute("value")) {
				value = getPropertyValue(propEl.getAttribute("value").trim());
			} else {
				NodeList l = propEl.getElementsByTagName("props");
				if (l.getLength() > 0) {
					value = parseElementProps((Element) l.item(0));
				}
			}
			if (value != null)
				props.put(propEl.getAttribute("name").trim(), value);
		}
		return props;
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}
