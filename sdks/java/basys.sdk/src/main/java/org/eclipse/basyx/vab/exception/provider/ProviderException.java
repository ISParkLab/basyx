package org.eclipse.basyx.vab.exception.provider;

/**
 * Used to indicate a general exception in a ModelProvider
 * 
 * @author conradi
 *
 */
public class ProviderException extends Exception {

	
	/**
	 * Version information for serialized instances
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Store message
	 */
	protected String message = null;
	
	
	/**
	 * Constructor
	 */
	public ProviderException(String msg) {
		// Store message
		message = msg;
	}
	
		
	public ProviderException(Exception e) {
		super(e);
	}


	/**
	 * Return detailed message
	 */
	@Override
	public String getMessage() {
		return message;
	}
}