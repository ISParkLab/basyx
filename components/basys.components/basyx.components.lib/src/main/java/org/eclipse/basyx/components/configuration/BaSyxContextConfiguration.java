package org.eclipse.basyx.components.configuration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.basyx.vab.protocol.http.server.BaSyxContext;

/**
 * Represents a BaSyx http servlet configuration for a BaSyxContext,
 * that can be loaded from a properties file.
 * 
 * @author espen
 *
 */
public class BaSyxContextConfiguration extends BaSyxConfiguration {
	// Default BaSyx Context configuration
	public static final String DEFAULT_CONTEXTPATH = "/basys.sdk";
	public static final String DEFAULT_DOCBASE = System.getProperty("java.io.tmpdir");
	public static final String DEFAULT_HOSTNAME = "localhost";
	public static final int DEFAULT_PORT = 4000;

	public static final String CONTEXTPATH = "contextPath";
	public static final String DOCBASE = "contextDocPath";
	public static final String HOSTNAME = "contextHostname";
	public static final String PORT = "contextPort";

	// The default path for the context properties file
	public static final String DEFAULT_CONFIG_PATH = "context.properties";

	// The default key for variables pointing to the configuration file
	public static final String DEFAULT_FILE_KEY = "BASYX_CONTEXT";

	public static Map<String, String> getDefaultProperties() {
		Map<String, String> defaultProps = new HashMap<>();
		defaultProps.put(CONTEXTPATH, DEFAULT_CONTEXTPATH);
		defaultProps.put(DOCBASE, DEFAULT_DOCBASE);
		defaultProps.put(HOSTNAME, DEFAULT_HOSTNAME);
		defaultProps.put(PORT, Integer.toString(DEFAULT_PORT));
		return defaultProps;
	}

	public BaSyxContextConfiguration() {
		super(getDefaultProperties());
	}

	public BaSyxContextConfiguration(Map<String, String> values) {
		super(values);
	}

	public BaSyxContextConfiguration(int port, String contextPath) {
		this();
		setPort(port);
		setContextPath(contextPath);
	}

	public BaSyxContextConfiguration(String contextPath, String docBasePath, String hostname, int port) {
		this();
		setContextPath(contextPath);
		setDocBasePath(docBasePath);
		setHostname(hostname);
		setPort(port);
	}

	public void loadFromDefaultSource() {
		loadFileOrDefaultResource(DEFAULT_FILE_KEY, DEFAULT_CONFIG_PATH);
	}

	public BaSyxContext createBaSyxContext() {
		String reqContextPath = getContextPath();
		String reqDocBasePath = getDocBasePath();
		String hostName = getHostname();
		int reqPort = getPort();
		return new BaSyxContext(reqContextPath, reqDocBasePath, hostName, reqPort);
	}

	public String getContextPath() {
		return getProperty(CONTEXTPATH);
	}

	public void setContextPath(String contextPath) {
		setProperty(CONTEXTPATH, contextPath);
	}

	public String getDocBasePath() {
		return getProperty(DOCBASE);
	}

	public void setDocBasePath(String docBasePath) {
		setProperty(DOCBASE, docBasePath);
	}

	public String getHostname() {
		return getProperty(HOSTNAME);
	}

	public void setHostname(String hostname) {
		setProperty(HOSTNAME, hostname);
	}

	public int getPort() {
		return Integer.parseInt(getProperty(PORT));
	}

	public void setPort(int port) {
		setProperty(PORT, Integer.toString(port));
	}

	public String getUrl() {
		return "http://" + getHostname() + ":" + getPort() + "/" + getContextPath();
	}
}
