package org.eclipse.basyx.submodel.factory.xml.converters.submodelelement.dataelement;

import java.util.Map;

import org.eclipse.basyx.submodel.factory.xml.XMLHelper;
import org.eclipse.basyx.submodel.factory.xml.converters.submodelelement.SubmodelElementXMLConverter;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.dataelement.IRange;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.Range;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Parses &lt;aas:range&gt; and builds the Range object from it <br>
 * Builds &lt;aas:range&gt; form a given Range object
 * 
 * @author conradi
 *
 */
public class RangeXMLConverter extends SubmodelElementXMLConverter {

	public static final String RANGE = "aas:range";
	public static final String MIN = "aas:min";
	public static final String MAX = "aas:max";
	
	
	/**
	 * Parses a Map containing the content of XML tag &lt;aas:range&gt;
	 * 
	 * @param xmlObject the Map with the content of XML tag &lt;aas:range&gt;
	 * @return the parsed Range
	 */
	public static Range parseRange(Map<String, Object> xmlObject) {
		String valueType = XMLHelper.getString(xmlObject.get(VALUE_TYPE));
		String min = XMLHelper.getString(xmlObject.get(MIN));
		String max = XMLHelper.getString(xmlObject.get(MAX));
		Range range = new Range(valueType, min, max);
		populateSubmodelElement(xmlObject, range);
		return range;
	}
	
	
	
	
	/**
	 * Builds the &lt;aas:range&gt; XML tag for a Range
	 * 
	 * @param document the XML document
	 * @param range the IRange to build the XML for
	 * @return the &lt;aas:range&gt; XML tag for the given Range
	 */
	public static Element buildRange(Document document, IRange range) {
		Element rangeRoot = document.createElement(RANGE);
		populateSubmodelElement(document, rangeRoot, range);
		
		String valueType = range.getValueType();
		if(valueType != null) {
			Element valueTypeRoot = document.createElement(VALUE_TYPE);
			valueTypeRoot.appendChild(document.createTextNode(valueType));
			rangeRoot.appendChild(valueTypeRoot);
		}
		
		Object minObj = range.getMin();
		String min = minObj == null ? null : minObj.toString();
		if(min != null) {
			Element minRoot = document.createElement(MIN);
			minRoot.appendChild(document.createTextNode(min));
			rangeRoot.appendChild(minRoot);
		}
		
		Object maxObj = range.getMax();
		String max = maxObj == null ? null : maxObj.toString();
		if(max != null) {
			Element maxRoot = document.createElement(MAX);
			maxRoot.appendChild(document.createTextNode(max));
			rangeRoot.appendChild(maxRoot);
		}
		
		return rangeRoot;
	}
	
}