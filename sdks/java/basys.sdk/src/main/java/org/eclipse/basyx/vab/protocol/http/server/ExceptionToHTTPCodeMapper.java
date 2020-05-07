package org.eclipse.basyx.vab.protocol.http.server;

import org.eclipse.basyx.vab.exception.provider.MalformedRequestException;
import org.eclipse.basyx.vab.exception.provider.ProviderException;
import org.eclipse.basyx.vab.exception.provider.ResourceAlreadyExistsException;
import org.eclipse.basyx.vab.exception.provider.ResourceNotFoundException;

/**
 * Maps Exceptions from providers to HTTP-Codes
 * 
 * @author conradi
 *
 */
public class ExceptionToHTTPCodeMapper {

	
	/**
	 * Maps ProviderExceptions to HTTP-Codes
	 * 
	 * @param e The thrown ProviderException
	 * @return HTTP-Code
	 */
	public static int mapFromException(ProviderException e) {

		if (e instanceof MalformedRequestException) {
			return 400;
		} else if(e instanceof ResourceAlreadyExistsException) {
			return 422;
		} else if(e instanceof ResourceNotFoundException) {
			return 404;
		}
		return 500;
		
	}
	
	/**
	 * Maps HTTP-Codes to ProviderExceptions
	 * 
	 * @param statusCode The received HTTP-code
	 * @return the corresponding ProviderException
	 */
	public static ProviderException mapToException(int statusCode) {
		
		switch(statusCode) {
		case 400:
			return new MalformedRequestException("Response-code: " + statusCode);
		case 422:
			return new ResourceAlreadyExistsException("Response-code: " + statusCode);
		case 404:
			return new ResourceNotFoundException("Response-code: " + statusCode);
		default:
			return new ProviderException("Response-code: " + statusCode);
		}
		
	}
	
}
