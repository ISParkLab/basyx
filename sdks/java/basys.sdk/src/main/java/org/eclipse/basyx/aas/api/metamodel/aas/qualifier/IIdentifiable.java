package org.eclipse.basyx.aas.api.metamodel.aas.qualifier;

import org.eclipse.basyx.aas.api.metamodel.aas.identifier.IIdentifier;

/**
 * Interface for Identifiable
 * 
 * @author rajashek
 *
*/

public interface IIdentifiable  extends IReferable {
	public IAdministrativeInformation getAdministration();
	
	public IIdentifier getIdentification();
}
