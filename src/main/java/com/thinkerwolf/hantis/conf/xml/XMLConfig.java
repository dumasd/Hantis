package com.thinkerwolf.hantis.conf.xml;

import com.thinkerwolf.hantis.common.io.Resource;
import com.thinkerwolf.hantis.common.io.Resources;
import com.thinkerwolf.hantis.common.util.ClassUtils;
import com.thinkerwolf.hantis.common.util.PropertyUtils;
import com.thinkerwolf.hantis.common.util.StringUtils;
import com.thinkerwolf.hantis.datasource.jdbc.DBPoolDataSource;
import com.thinkerwolf.hantis.datasource.jdbc.DBUnpoolDataSource;
import com.thinkerwolf.hantis.datasource.jta.DBXAPoolDataSource;
import com.thinkerwolf.hantis.datasource.jta.DBXAUnpoolDataSource;
import com.thinkerwolf.hantis.executor.ExecutorType;
import com.thinkerwolf.hantis.session.Configuration;
import com.thinkerwolf.hantis.session.SessionFactoryBuilder;
import com.thinkerwolf.hantis.sql.SqlNode;
import com.thinkerwolf.hantis.transaction.jdbc.JdbcTransactionManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

			// 解析sessionFactory
			NodeList sessionFactoryNodeList = sessionFactoriesEl.getElementsByTagName("sessionFactory");
			for (int i = 0, len = sessionFactoryNodeList.getLength(); i < len; i++) {
				parseSessionFactory((Element) sessionFactoryNodeList.item(i));
			}

			// 解析transactionManager
			NodeList tmNl = doc.getElementsByTagName("transactionManager");
			if (tmNl.getLength() > 0) {
				parseTransactionManager((Element) tmNl.item(0));
			} else {
				// TODO 默认初始化JDBC

			}

		} catch (Exception e) {
			throw new RuntimeException("Can't parse config, have a exception", e);
		}

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
		DataSource dataSource = parseDataSource(dataSourceEl);

		// 解析mapping
		Map<String, SqlNode> sqlNodeMap = new HashMap<>();
		Element sqlsEl = (Element) el.getElementsByTagName("mappings").item(0);
		parseMappings(sqlsEl, sqlNodeMap);

		// 解析Executor
		NodeList exeNl = el.getElementsByTagName("executor");
		ExecutorType executorType = parseExecutor((Element) (exeNl.getLength() > 0 ? exeNl.item(0) : null));

		builder.setId(id);
		builder.setDataSource(dataSource);
		builder.setSqlNodeMap(sqlNodeMap);
		builder.setConfiguration(configuration);
		builder.setExecutorType(executorType);
		configuration.putSessionFactoryBuilder(builder);
		return builder;
	}

	private DataSource parseDataSource(Element el) {
		String dataSourceType = el.getAttribute("type");
		// POOL UNPOOL XAPOOL XAUNPOOL
		if (StringUtils.isEmpty(dataSourceType)) {
			throw new RuntimeException("dataSource type is null");
		}
		DataSource dataSource = null;
		if ("POOL".equals(dataSourceType)) {
			dataSource = new DBPoolDataSource();
		} else if ("UNPOOL".equals(dataSourceType)) {
			dataSource = new DBUnpoolDataSource();
		} else if ("XAPOOL".equals(dataSourceType)) {
			dataSource = new DBXAPoolDataSource();
		} else if ("XAUNPOOL".equals(dataSourceType)) {
			dataSource = new DBXAUnpoolDataSource();
		} else {
			dataSource = (DataSource) ClassUtils.newInstance(ClassUtils.forName(dataSourceType));
		}
		// NodeList propsNl = el.getElementsByTagName("property");
		Properties props = parseElementProps(el);
		// for (int i = 0, len = propsNl.getLength(); i < len; i++) {
		// Element propEl = (Element) propsNl.item(i);
		// props.setProperty(propEl.getAttribute("name").trim(),
		// getPropertyValue(propEl.getAttribute("value").trim()));
		// }
		PropertyUtils.setProperties(dataSource, props);
		return dataSource;
	}

	/**
	 * 解析sqls
	 *
	 * @param el
	 */
	private void parseMappings(Element el, Map<String, SqlNode> sqlNodeMap) {
		NodeList nl = el.getElementsByTagName("mapping");
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Element mappingEl = (Element) nl.item(i);

			// resource 解析sql xml文件
			String resource = mappingEl.getAttribute("resource");
			Resource[] resources = Resources.getResources(resource);
			for (Resource r : resources) {
				if (r.getPath().endsWith(".xml")) {
					try {
						sqlNodeMap.putAll(configuration.getParser().parse(r.getInputStream()));
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}

			// TODO scanpath 解析 orm注解

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
		String type = el.getAttribute("type");

		if (StringUtils.isEmpty(type)) {
			throw new RuntimeException("dataSource type is null");
		}
		// JDBC JTA
		if ("JDBC".equalsIgnoreCase(type)) {
			// transactionManager = new JdbcTransactionManager();
			Map<String, SessionFactoryBuilder> builderMap = configuration.getSessionFactoryBuilders();
			for (SessionFactoryBuilder builder : builderMap.values()) {
				JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();
				jdbcTransactionManager.setDataSource(builder.getDataSource());
				builder.setTransactionManager(jdbcTransactionManager);
			}
		}
		if ("JTA".equalsIgnoreCase(type)) {
			// TODO
		}

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
