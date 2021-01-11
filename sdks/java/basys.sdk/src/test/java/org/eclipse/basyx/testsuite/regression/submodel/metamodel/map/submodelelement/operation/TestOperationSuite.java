package org.eclipse.basyx.testsuite.regression.submodel.metamodel.map.submodelelement.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import org.eclipse.basyx.submodel.metamodel.api.submodelelement.operation.IAsyncInvocation;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.operation.IOperation;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.operation.IOperationVariable;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.Operation;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.OperationExecutionErrorException;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.OperationVariable;
import org.eclipse.basyx.vab.exception.provider.WrongNumberOfParametersException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for IOperation
 * 
 * @author conradi
 *
 */
public abstract class TestOperationSuite {
	
	protected static final String IN_VALUE = "inValue";
	protected static final String OUT_VALUE = "outValue";
	protected static final String INOUT_VALUE = "inOutValue";
	protected static final Collection<OperationVariable> IN = Arrays.asList(
			new OperationVariable(new Property("testIn1", IN_VALUE)),
			new OperationVariable(new Property("testIn2", IN_VALUE)));
	protected static final Collection<OperationVariable> OUT = Collections.singletonList(new OperationVariable(new Property("testId2", OUT_VALUE)));;
	protected static final Collection<OperationVariable> INOUT = Collections.singletonList(new OperationVariable(new Property("testId3", INOUT_VALUE)));;
	
	protected static final Function<Object[], Object> FUNC = (Function<Object[], Object>) v -> {
		return (int)v[0] + (int)v[1];
	};
	
	protected static final Function<Object[], Object> EXCEPTION_FUNC = (Function<Object[], Object>) v -> {
		throw new NullPointerException();
	};
	
	protected IOperation operation;
	protected IOperation operationException;

	/**
	 * Converts an Operation into the IOperation to be tested
	 */
	protected abstract IOperation prepareOperation(Operation operation);
	
	@Before
	public void setup() {
		Operation op1 = new Operation(IN, OUT, INOUT, FUNC);
		op1.setIdShort("op1");
		operation = prepareOperation(op1);

		Operation op2 = new Operation(IN, OUT, INOUT, EXCEPTION_FUNC);
		op2.setIdShort("op2");
		operationException = prepareOperation(op2);
	}
	
	@Test
	public void testInvoke() throws Exception {
		assertEquals(5, operation.invoke(2, 3));
	}
	
	@Test
	public void testInvokeException() throws Exception {
		try {
			// Ensure the operation is invoked directly
			operationException.invoke(1, 2);
			fail();
		} catch (Exception e) {
			// Exceptions from ConnectedOperation are wrapped in ProviderException
			assertTrue(e instanceof NullPointerException
					|| e.getCause() instanceof NullPointerException);
		}
	}
	
	@Test
	public void testInvokeWithSubmodelElements() {
		Property param1 = new Property("testIn1", 2);
		Property param2 = new Property("testIn2", 4);
		SubmodelElement[] result = operation.invoke(param1, param2);
		assertEquals(1, result.length);
		assertEquals(6, result[0].getValue());
	}

	@Test
	public void testInvokeParametersException() throws Exception {
		try {
			operation.invoke(1);
			fail();
		} catch (Exception e) {
			// Exceptions from ConnectedOperation are wrapped in ProviderException
			assertTrue(e instanceof WrongNumberOfParametersException
					|| e.getCause() instanceof WrongNumberOfParametersException);
		}
	}

	@Test
	public void testInvokeAsync() throws Exception {
		AsyncOperationHelper helper = new AsyncOperationHelper();
		IOperation operation = prepareOperation(helper.getAsyncOperation());

		IAsyncInvocation invocation = operation.invokeAsync(3, 2);
		
		assertFalse(invocation.isFinished());
		
		helper.releaseWaitingOperation();

		assertTrue(invocation.isFinished());
		assertEquals(5, invocation.getResult());
	}
	
	@Test
	public void testInvokeExceptionAsync() throws Exception {
		AsyncOperationHelper helper = new AsyncOperationHelper();
		IOperation operationException = prepareOperation(helper.getAsyncExceptionOperation());
		IAsyncInvocation invocation = operationException.invokeAsync();
		assertFalse(invocation.isFinished());
		
		helper.releaseWaitingOperation();
		
		try {
			invocation.getResult();
			fail();
		} catch (OperationExecutionErrorException e) {
		}

	}
	
	@Test
	public void testInputVariables() {
		Collection<IOperationVariable> inputVariables = operation.getInputVariables();
		assertEquals(2, inputVariables.size());
		Object value = getValueFromOpVariable(inputVariables);
		assertEquals(IN_VALUE, value);
	}

	@Test
	public void testOutputVariables() {
		Collection<IOperationVariable> outputVariables = operation.getOutputVariables();
		assertEquals(1, outputVariables.size());
		Object value = getValueFromOpVariable(outputVariables);
		assertEquals(OUT_VALUE, value);
	}

	@Test
	public void testInOutputVariables() {
		Collection<IOperationVariable> inoutVariables = operation.getInOutputVariables();
		assertEquals(1, inoutVariables.size());
		Object value = getValueFromOpVariable(inoutVariables);
		assertEquals(INOUT_VALUE, value);
	}
	
	/**
	 * Gets the Value from the OperationVariable in a collection
	 */
	private Object getValueFromOpVariable(Collection<IOperationVariable> vars) {
		IOperationVariable var = new ArrayList<>(vars).get(0);
		return var.getValue().getValue();
	}
	
}
