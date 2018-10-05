package org.eclipse.basyx.aas.impl.resources.basic;

import java.util.Collection;
import java.util.Map;

import org.eclipse.basyx.aas.api.resources.basic.IContainerProperty;

/**
 * Maps a class to its corresponding DataType
 * 
 * @author schnicke
 *
 */
public class DataTypeMapping {
	public static DataType map(Class<?> c) {
		if (c == int.class || c == Integer.class) {
			return DataType.INTEGER;
		} else if (c == void.class || c == Void.class) {
			return DataType.VOID;
		} else if (c == boolean.class || c == Boolean.class) {
			return DataType.BOOLEAN;
		} else if (c == float.class || c == Float.class) {
			return DataType.FLOAT;
		} else if (c == double.class || c == Double.class) {
			return DataType.DOUBLE;
		} else if (c == char.class || c == Character.class) {
			return DataType.CHARACTER;
		} else if (c == String.class) {
			return DataType.STRING;
		} else if (Map.class.isAssignableFrom(c)) {
			return DataType.MAP;
		} else if (Collection.class.isAssignableFrom(c)) {
			return DataType.COLLECTION;
		} else if (IContainerProperty.class.isAssignableFrom(c)) {
			return DataType.CONTAINER;
		} else {
			return DataType.REFERENCE;
		}
	}
}