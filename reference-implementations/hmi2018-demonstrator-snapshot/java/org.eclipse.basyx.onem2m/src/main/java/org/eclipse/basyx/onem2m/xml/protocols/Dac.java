//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.16 at 09:48:33 AM CET 
//


package org.eclipse.basyx.onem2m.xml.protocols;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.onem2m.org/xml/protocols}regularResource"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dae" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="dap" type="{http://www.onem2m.org/xml/protocols}listOfURIs"/&gt;
 *         &lt;element name="dal" type="{http://www.onem2m.org/xml/protocols}timestamp" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dae",
    "dap",
    "dal"
})
public class Dac
    extends RegularResource
{

    protected boolean dae;
    @XmlList
    @XmlElement(required = true)
    protected List<String> dap;
    protected String dal;

    /**
     * Gets the value of the dae property.
     * 
     */
    public boolean isDae() {
        return dae;
    }

    /**
     * Sets the value of the dae property.
     * 
     */
    public void setDae(boolean value) {
        this.dae = value;
    }

    /**
     * Gets the value of the dap property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dap property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDap().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDap() {
        if (dap == null) {
            dap = new ArrayList<String>();
        }
        return this.dap;
    }

    /**
     * Gets the value of the dal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDal() {
        return dal;
    }

    /**
     * Sets the value of the dal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDal(String value) {
        this.dal = value;
    }

}