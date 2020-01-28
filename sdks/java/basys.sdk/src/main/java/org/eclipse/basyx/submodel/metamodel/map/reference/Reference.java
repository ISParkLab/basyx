package org.eclipse.basyx.submodel.metamodel.map.reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.basyx.submodel.metamodel.api.reference.IKey;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.vab.model.VABModelMap;

/**
 * Reference as described by DAAS document <br/>
 * <br/>
 * Reference to either a model element of the same or another AAS or to an
 * external entity. A reference is an ordered list of keys, each key referencing
 * an element. The complete list of keys may for example be concatenated to a
 * path that then gives unique access to an element or entity.
 * 
 * @author schnicke
 *
 */
public class Reference extends VABModelMap<Object> implements IReference {
	public static final String KEY = "keys";

	/**
	 * Constructor
	 */
	public Reference() {
		setKeys(new ArrayList<IKey>());
	}

	/**
	 * 
	 * @param key Unique reference in its name space.
	 */
	public Reference(List<IKey> keys) {
		setKeys(keys);
	}

	/**
	 * 
	 * @param key
	 *            Unique reference in its name space.
	 */
	public Reference(Key key) {
		this(Collections.singletonList(key));
	}

	/**
	 * Creates a Reference object from a map
	 * 
	 * @param obj
	 *            a Reference object as raw map
	 * @return a Reference object, that behaves like a facade for the given map
	 */
	public static Reference createAsFacade(Map<String, Object> map) {
		if (map == null) {
			return null;
		}

		Reference ret = new Reference();
		ret.setMap(map);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IKey> getKeys() {
		List<IKey> ret = new ArrayList<>();

		// Transform list of maps to set of IKey
		List<Map<String, Object>> list = (List<Map<String, Object>>) get(Reference.KEY);

		for (Map<String, Object> m : list) {
			ret.add(Key.createAsFacade(m));
		}

		return ret;
	}

	public void setKeys(List<IKey> keys) {
		// Copy the key list to make sure an actual hashmap is put inside this map
		List<IKey> copy = new ArrayList<>();
		for (IKey key : keys) {
			copy.add(new Key(key.getType(), key.isLocal(), key.getValue(), key.getIdType()));
		}
		put(Reference.KEY, copy);
	}
}
