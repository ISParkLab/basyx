package org.eclipse.basyx.components.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a BaSyx configuration for a MongoDB connection.
 * 
 * @author espen
 *
 */
public class BaSyxMongoDBConfiguration extends BaSyxConfiguration {
	// Default BaSyx SQL configuration
	public static final String DEFAULT_USER = "admin";
	public static final String DEFAULT_CONNECTIONURL = "mongodb://127.0.0.1:27017/";
	public static final String DEFAULT_DATABASE = "admin";
	public static final String DEFAULT_REGISTRY_COLLECTION = "basyxregistry";
	public static final String DEFAULT_AAS_COLLECTION = "basyxaas";
	public static final String DEFAULT_SUBMODEL_COLLECTION = "basyxsubmodel";

	public static final String USER = "dbuser";
	public static final String DATABASE = "dbname";
	public static final String CONNECTIONURL = "dbconnectionstring";
	public static final String REGISTRY_COLLECTION = "dbcollectionRegistry";
	public static final String AAS_COLLECTION = "dbcollectionAAS";
	public static final String SUBMODEL_COLLECTION = "dbcollectionSubmodels";

	// The default path for the context properties file
	public static final String DEFAULT_CONFIG_PATH = "mongodb.properties";

	// The default key for variables pointing to the configuration file
	public static final String DEFAULT_FILE_KEY = "BASYX_MONGODB";

	public static Map<String, String> getDefaultProperties() {
		Map<String, String> defaultProps = new HashMap<>();
		defaultProps.put(USER, DEFAULT_USER);
		defaultProps.put(CONNECTIONURL, DEFAULT_CONNECTIONURL);
		defaultProps.put(DATABASE, DEFAULT_DATABASE);
		defaultProps.put(REGISTRY_COLLECTION, DEFAULT_REGISTRY_COLLECTION);
		defaultProps.put(AAS_COLLECTION, DEFAULT_AAS_COLLECTION);
		defaultProps.put(SUBMODEL_COLLECTION, DEFAULT_SUBMODEL_COLLECTION);

		return defaultProps;
	}

	public BaSyxMongoDBConfiguration(Map<String, String> values) {
		super(values);
	}

	public BaSyxMongoDBConfiguration() {
		super(getDefaultProperties());
	}

	public BaSyxMongoDBConfiguration(String user, String connectionUrl, String database, String registryCollection,
			String aasCollection, String submodelCollection) {
		this();
		setUser(user);
		setConnectionUrl(connectionUrl);
		setDatabase(database);
		setRegistryCollection(registryCollection);
		setAASCollection(aasCollection);
		setSubmodelCollection(submodelCollection);
	}

	public BaSyxMongoDBConfiguration(String user, String connectionUrl, String database, String aasCollection,
			String submodelCollection) {
		this();
		setUser(user);
		setConnectionUrl(connectionUrl);
		setDatabase(database);
		setAASCollection(aasCollection);
		setSubmodelCollection(submodelCollection);
	}

	public BaSyxMongoDBConfiguration(String user, String connectionUrl, String database, String registryCollection) {
		this();
		setUser(user);
		setConnectionUrl(connectionUrl);
		setDatabase(database);
		setRegistryCollection(registryCollection);
	}

	public void loadFromDefaultSource() {
		loadFileOrDefaultResource(DEFAULT_FILE_KEY, DEFAULT_CONFIG_PATH);
	}

	public String getUser() {
		return getProperty(USER);
	}

	public void setUser(String user) {
		setProperty(USER, user);
	}

	public String getDatabase() {
		return getProperty(DATABASE);
	}

	public void setDatabase(String database) {
		setProperty(DATABASE, database);
	}

	public String getConnectionUrl() {
		return getProperty(CONNECTIONURL);
	}

	public void setConnectionUrl(String connectionUrl) {
		setProperty(CONNECTIONURL, connectionUrl);
	}

	public String getRegistryCollection() {
		return getProperty(REGISTRY_COLLECTION);
	}

	public void setRegistryCollection(String registryCollection) {
		setProperty(REGISTRY_COLLECTION, registryCollection);
	}

	public String getAASCollection() {
		return getProperty(AAS_COLLECTION);
	}

	public void setAASCollection(String aasCollection) {
		setProperty(AAS_COLLECTION, aasCollection);
	}

	public String getSubmodelCollection() {
		return getProperty(SUBMODEL_COLLECTION);
	}

	public void setSubmodelCollection(String submodelCollection) {
		setProperty(SUBMODEL_COLLECTION, submodelCollection);
	}
}
