package org.eclipse.basyx.aas.metamodel.facades;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.basyx.aas.metamodel.hashmap.aas.SubModel;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.identifier.IdentifierType;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.HasDataSpecification;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.HasSemantics;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.Identifiable;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.haskind.HasKind;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.qualifiable.Constraint;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.qualifiable.Qualifiable;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.reference.Key;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.reference.Reference;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.reference.enums.KeyElements;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.reference.enums.KeyType;

/**
 * Facade class that supports the development and access of sub models using
 * IRDI (International Registration Data Identifier) semantic definitions
 * 
 * @author kuhn
 *
 */
public class SubmodelFacadeIRDISemantics extends SubmodelFacade {

	/**
	 * Constructor without arguments - create a sub model with all meta properties
	 * empty / set to default values
	 */
	public SubmodelFacadeIRDISemantics() {
		// Create sub model
		super(new SubModel());
	}

	/**
	 * Sub model constructor for sub models that conform to a globally defined
	 * semantics with IRDI (International Registration Data Identifier)
	 * 
	 * Create an instance sub model with all meta properties empty / set to default
	 * values
	 * 
	 * @param semantics
	 *            String that describes the sub model semantics e.g. its type (e.g.
	 *            basys.semantics.transportsystem)
	 * @param idType
	 *            Submodel ID type
	 * @param id
	 *            Sub model ID according to idType
	 * @param idShort
	 *            Short ID of the sub model (e.g. "subsystemTopology")
	 * @param category
	 *            Additional coded meta information regarding the element type that
	 *            affects expected existence of attributes (e.g.
	 *            "transportSystemTopology")
	 * @param description
	 *            Descriptive sub model description (e.g. "This is a machine
	 *            readable description of the transport system topology")
	 * @param constraint
	 *            The collection of qualifiers of this sub model (e.g.
	 *            ["plant.maintransport", "maintransport."])
	 * @param dataSpecification
	 *            Sub model data specification
	 * @param kind
	 *            Sub model kind
	 * @param version
	 *            Sub model version
	 * @param revision
	 *            Sub model revision
	 */
	public SubmodelFacadeIRDISemantics(String semantics, String idType, String id, String idShort, String category, String description, Constraint constraint, HasDataSpecification dataSpecification, String kind, String version,
			String revision) {
		// Create sub model
		super(new SubModel(new HasSemantics(new Reference(Collections.singletonList(new Key(KeyElements.GlobalReference, false, semantics, KeyType.IRDI)))),
				new Identifiable(version, revision, idShort, category, description, IdentifierType.Custom, id), 
				new Qualifiable(constraint), 
				dataSpecification,
				new HasKind(kind)));
	}

	/**
	 * Sub model constructor for sub models that conform to a globally defined
	 * semantics with IRDI (International Registration Data Identifier) <br />
	 * Create an instance sub model with all meta properties empty / set to default
	 * values
	 * 
	 * @param semantics
	 *            String that describes the sub model semantics e.g. its type (e.g.
	 *            basys.semantics.transportsystem)
	 * @param idType
	 *            Submodel ID type
	 * @param id
	 *            Sub model ID according to idType
	 * @param idShort
	 *            Short ID of the sub model (e.g. "subsystemTopology")
	 * @param category
	 *            Additional coded meta information regarding the element type that
	 *            affects expected existence of attributes (e.g.
	 *            "transportSystemTopology")
	 * @param description
	 *            Descriptive sub model description (e.g. "This is a machine
	 *            readable description of the transport system topology")
	 * @param constraint
	 *            The collection of qualifiers of this sub model (e.g.
	 *            ["plant.maintransport", "maintransport."])
	 * @param dataSpecification
	 *            Sub model data specification
	 * @param kind
	 *            Sub model kind
	 * @param version
	 *            Sub model version
	 * @param revision
	 *            Sub model revision
	 */
	public SubmodelFacadeIRDISemantics(String semantics, String idType, String id, String idShort, String category, String description, Collection<Constraint> qualifier, Constraint constraint,
			HasDataSpecification dataSpecification, String kind, String version, String revision) {
		// Create sub model
		super(new SubModel(new HasSemantics(new Reference(Collections.singletonList(new Key(KeyElements.GlobalReference, false, semantics, KeyType.IRDI)))),
						new Identifiable(version, revision, idShort, category, description, idType, id), 
						new Qualifiable(qualifier), 
						dataSpecification, 
						new HasKind(kind)));
	}
}
