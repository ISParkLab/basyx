package org.eclipse.basyx.components.sqlprovider.query;

import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.basyx.components.sqlprovider.driver.ISQLDriver;
import org.eclipse.basyx.components.tools.propertyfile.opdef.OperationDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Implement a generic SQL query
 * 
 * @author kuhn
 *
 */
public class DynamicSQLUpdate extends DynamicSQLRunner implements Consumer<Map<String,Object>> {
	private static Logger logger = LoggerFactory.getLogger(DynamicSQLUpdate.class);

	/**
	 * Store SQL query string with place holders ($x)
	 */
	protected String sqlQueryString = null;
	
	
	
	
	/**
	 * Constructor
	 */
	public DynamicSQLUpdate(ISQLDriver driver, String query) {
		// Invoke base constructor
		super(driver);

		// Store parameter count and SQL query string
		sqlQueryString         = query;
	}

	
	/**
	 * Constructor
	 */
	public DynamicSQLUpdate(String path, String user, String pass, String qryPfx, String qDrvCls, String query) {
		// Invoke base constructor
		super(path, user, pass, qryPfx, qDrvCls);
		
		// Store parameter count and SQL query string
		sqlQueryString         = query;
	}
	


	/**
	 * Execute update with given parameter
	 */
	@Override
	public void accept(Map<String,Object> parameter) {
		logger.debug("(Parameters) Running SQL update: " + parameter);

		// Apply parameter and create SQL query string
		String sqlQuery = OperationDefinition.getSQLString(sqlQueryString, parameter);
		
		logger.debug("(Query) Running SQL update:" + sqlQuery);

		// Execute SQL query
		sqlDriver.sqlUpdate(sqlQuery);
	}
}

