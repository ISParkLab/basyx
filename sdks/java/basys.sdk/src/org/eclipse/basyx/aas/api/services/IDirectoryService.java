package org.eclipse.basyx.aas.api.services;

import java.util.Map;



/**
 * Directory service SDK interface.
 * 
 * @author kuhn
 *
 */
public interface IDirectoryService {

	
	/**
	 * Add a mapping to directory
	 */
	public void addMapping(String key, String value);

	
	/**
	 * Remove a mapping from directory
	 */
	public void removeMapping(String key);
	
	
	/**
	 * Get mappings
	 */
	public Map<String, String> getMappings();


	/**
	 * Lookup method maps key "id" to value
	 */
	public String lookup(String id);
}
