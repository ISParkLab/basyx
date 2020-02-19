package org.eclipse.basyx.components.cfgprovider;

import java.util.Map;

import org.eclipse.basyx.components.provider.BaseConfiguredProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Asset administration shell sub model provider that exports a properties file
 * 
 * @author kuhn
 *
 */
public class CFGSubModelProvider extends BaseConfiguredProvider {

	/**
	 * Initiates a logger using the current class
	 */
	private static final Logger logger = LoggerFactory.getLogger(CFGSubModelProvider.class);

	/**
	 * Constructor
	 */
	public CFGSubModelProvider(Map<Object, Object> cfgValues) {
		// Call base constructor
		super(cfgValues);

		// Add properties
		for (String key: getConfiguredProperties(cfgValues)) {
			// Create properties
			submodelData.getDataElements().put(key.toString(), createSubmodelElement(key, cfgValues.get(key), cfgValues));
			
			// Debug output
			logger.debug("Adding configured property: "+key.toString()+" = "+cfgValues.get(key));
		}
	}
}

