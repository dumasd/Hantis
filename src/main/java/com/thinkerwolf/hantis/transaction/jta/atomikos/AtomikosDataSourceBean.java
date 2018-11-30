package com.thinkerwolf.hantis.transaction.jta.atomikos;

public class AtomikosDataSourceBean extends com.atomikos.jdbc.AtomikosDataSourceBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8039889604601505028L;

	private String resourceName;

	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		setUniqueResourceName(resourceName);
	}

}
