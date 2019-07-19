package org.eclipse.basyx.testsuite.support.aas.vab.stub.elements;

import java.util.function.Function;

import org.eclipse.basyx.aas.api.exception.ServerException;
import org.eclipse.basyx.aas.metamodel.factory.MetaModelElementFactory;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.SubModel;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.submodelelement.operation.Operation;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.submodelelement.property.Property;

/**
 * A simple VAB submodel element that explains the development of conforming
 * submodels using the VAB hashmap provider
 * 
 * @author kuhn
 *
 */
public class SimpleAASSubmodel extends SubModel {

	private static final long serialVersionUID = 1L;

	public SimpleAASSubmodel() {
		this("SimpleAASSubmodel");
	}

	/**
	 * Constructor
	 */
	public SimpleAASSubmodel(String id) {
		// Create sub model

		setId(id);

		// Create example properties
		MetaModelElementFactory fac = new MetaModelElementFactory();

		getProperties().put("prop1", fac.create(new Property(), 123));

		// Create example operations
		getOperations().put("operation1", fac.createOperation(new Operation(), (Function<Object[], Object>) (v) -> {
			return (int) v[0] - (int) v[1];
		}));
		getOperations().put("operation2", fac.createOperation(new Operation(), (Function<Object[], Object>) (v) -> {
			return (int) v[0] + (int) v[1];
		}));

		// Create example operations
		// - Contained operation that throws native JAVA exception
		getOperations().put("operationEx1", fac.createOperation(new Operation(), (Function<Object[], Object>) (elId) -> {
			throw new NullPointerException();
		}));
		// - Contained operation that throws VAB exception
		getOperations().put("operationEx2", fac.createOperation(new Operation(), (Function<Object[], Object>) (elId) -> {
			throw new ServerException("ExType", "Exception description");
		}));

	}
}
