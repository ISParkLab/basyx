package org.eclipse.basyx.vab.directory.restapi;

import org.eclipse.basyx.vab.directory.api.IVABDirectoryService;
import org.eclipse.basyx.vab.directory.memory.InMemoryDirectory;
import org.eclipse.basyx.vab.exception.provider.ProviderException;
import org.eclipse.basyx.vab.exception.provider.ResourceNotFoundException;
import org.eclipse.basyx.vab.modelprovider.VABPathTools;
import org.eclipse.basyx.vab.modelprovider.api.IModelProvider;

/**
 * Connects an arbitrary IVABDirectoryService implementation to the VAB
 * 
 * @author schnicke
 */

public class DirectoryModelProvider implements IModelProvider {

	private IVABDirectoryService directory;

	/**
	 * Creates a DirectoryModelProvider wrapping an IVABDirectoryService
	 * 
	 * @param directory
	 */
	public DirectoryModelProvider(IVABDirectoryService directory) {
		super();
		this.directory = directory;
	}

	/**
	 * Creates a default DirectoryModelProvider wrapping an InMemoryDirectory
	 */
	public DirectoryModelProvider() {
		this(new InMemoryDirectory());
	}

	@Override
	public Object getModelPropertyValue(String path) throws ProviderException {
		path = VABPathTools.stripSlashes(path);
		Object obj = directory.lookup(path);
		if (obj == null) {
			throw new ResourceNotFoundException("Path " + path + " does not exist in the Registry");
		} else {
			return obj;
		}
	}

	@Override
	public void setModelPropertyValue(String path, Object newValue) throws ProviderException {
		throw new RuntimeException("Set not supported by VAB Directory");
	}

	@Override
	public void createValue(String path, Object newEntity) throws ProviderException {
		path = VABPathTools.stripSlashes(path);
		directory.addMapping(path, (String) newEntity);
	}

	@Override
	public void deleteValue(String path) throws ProviderException {
		path = VABPathTools.stripSlashes(path);
		directory.removeMapping(path);
	}

	@Override
	public void deleteValue(String path, Object obj) throws ProviderException {
		throw new RuntimeException("Delete with parameter not supported by VAB Directory");
	}

	@Override
	public Object invokeOperation(String path, Object... parameter) throws ProviderException {
		throw new RuntimeException("Invoke not supported by VAB Directory");
	}

}
