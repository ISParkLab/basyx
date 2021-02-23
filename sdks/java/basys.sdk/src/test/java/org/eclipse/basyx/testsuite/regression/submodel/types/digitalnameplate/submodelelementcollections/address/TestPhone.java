package org.eclipse.basyx.testsuite.regression.submodel.types.digitalnameplate.submodelelementcollections.address;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.basyx.aas.metamodel.exception.MetamodelConstructionException;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.HasSemantics;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangString;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Referable;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.MultiLanguageProperty;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetypedef.PropertyValueTypeDef;
import org.eclipse.basyx.submodel.types.digitalnameplate.enums.PhoneType;
import org.eclipse.basyx.submodel.types.digitalnameplate.submodelelementcollections.address.Phone;
import org.eclipse.basyx.vab.exception.provider.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests createAsFacade and isValid of {@link Phone} for their
 * correctness
 * 
 * @author haque
 *
 */
public class TestPhone {
	public static final String IDSHORT = "testPhoneId";
	public static MultiLanguageProperty telephone = new MultiLanguageProperty(Phone.TELEPHONENUMBERID);
	public static Property typeOfTelephone = new Property(Phone.TYPEOFTELEPHONEID, PropertyValueTypeDef.String);
	private Map<String, Object> phoneMap = new HashMap<String, Object>();
	
	@Before
	public void buildPhone() {
		telephone.setValue(new LangStrings(new LangString("DE", "0631123456")));
		typeOfTelephone.setValue(PhoneType.HOME);
		List<ISubmodelElement> elements = new ArrayList<ISubmodelElement>();
		elements.add(telephone);
		elements.add(typeOfTelephone);
		phoneMap.put(Referable.IDSHORT, IDSHORT);
		phoneMap.put(HasSemantics.SEMANTICID, Phone.SEMANTICID);
		phoneMap.put(Property.VALUE, elements);
	}
	
	@Test
	public void testCreateAsFacade() {
		Phone phoneFromMap = Phone.createAsFacade(phoneMap);
		assertEquals(Phone.SEMANTICID, phoneFromMap.getSemanticId());
		assertEquals(telephone, phoneFromMap.getTelephoneNumber());
		assertEquals(typeOfTelephone, phoneFromMap.getTypeOfTelephone());
		assertEquals(IDSHORT, phoneFromMap.getIdShort());
	}
	
	@Test (expected = MetamodelConstructionException.class)
	public void testCreateAsFacadeExceptionIdShort() {
		phoneMap.remove(Referable.IDSHORT);
		Phone.createAsFacade(phoneMap);
	}
	
	@Test (expected = ResourceNotFoundException.class)
	public void testCreateAsFacadeExceptionTelephoneNumber() {
		List<ISubmodelElement> newElements = new ArrayList<ISubmodelElement>();
		newElements.add(typeOfTelephone);
		phoneMap.put(Property.VALUE, newElements);
		Phone.createAsFacade(phoneMap);
	}
}