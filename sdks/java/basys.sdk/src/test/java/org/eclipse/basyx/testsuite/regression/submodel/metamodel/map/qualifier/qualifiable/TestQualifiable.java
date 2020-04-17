package org.eclipse.basyx.testsuite.regression.submodel.metamodel.map.qualifier.qualifiable;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.api.reference.enums.KeyElements;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.qualifiable.Constraint;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.qualifiable.Formula;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.qualifiable.Qualifiable;
import org.eclipse.basyx.submodel.metamodel.map.reference.Key;
import org.eclipse.basyx.submodel.metamodel.map.reference.Reference;
import org.junit.Test;

/**
 * Tests constructor, setter and getter of {@link Qualifiable} for their
 * correctness
 * 
 * @author haque
 *
 */
public class TestQualifiable {
	private static final Formula FORMULA1 = new Formula(Collections.singleton(new Reference(new Key(KeyElements.BLOB, true, "TestValue", IdentifierType.IRI))));
	private static final Formula FORMULA2 = new Formula(Collections.singleton(new Reference(new Key(KeyElements.ANNOTATEDRELATIONSHIPELEMENT, true, "TestValue2", IdentifierType.IRDI))));
	
	@Test
	public void testConstructor1() {
		Qualifiable qualifiable = new Qualifiable(FORMULA1);
		assertEquals(Collections.singleton(FORMULA1), qualifiable.getQualifier());
	}
	
	@Test
	public void testConstructor2() {
		Collection<Constraint> constraints = new HashSet<Constraint>();
		constraints.add(FORMULA1);
		constraints.add(FORMULA2);
		
		Qualifiable qualifiable = new Qualifiable(constraints);
		assertEquals(constraints, qualifiable.getQualifier());
	}
	
	@Test
	public void testSetQualifier() {
		Qualifiable qualifiable = new Qualifiable(FORMULA1);
		
		qualifiable.setQualifier(Collections.singleton(FORMULA2));
		assertEquals(Collections.singleton(FORMULA2), qualifiable.getQualifier());
	}
}
