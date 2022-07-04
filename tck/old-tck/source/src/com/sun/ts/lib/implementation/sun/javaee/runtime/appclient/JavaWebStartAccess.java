//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0-M3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.02 at 08:06:39 PM CEST 
//


package com.sun.ts.lib.implementation.sun.javaee.runtime.appclient;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contextRoot",
    "eligible",
    "vendor",
    "jnlpDoc"
})
@XmlRootElement(name = "java-web-start-access")
public class JavaWebStartAccess
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "context-root")
    protected String contextRoot;
    protected String eligible;
    protected String vendor;
    @XmlElement(name = "jnlp-doc")
    protected JnlpDoc jnlpDoc;

    /**
     * Gets the value of the contextRoot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextRoot() {
        return contextRoot;
    }

    /**
     * Sets the value of the contextRoot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextRoot(String value) {
        this.contextRoot = value;
    }

    /**
     * Gets the value of the eligible property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEligible() {
        return eligible;
    }

    /**
     * Sets the value of the eligible property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEligible(String value) {
        this.eligible = value;
    }

    /**
     * Gets the value of the vendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendor(String value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the jnlpDoc property.
     * 
     * @return
     *     possible object is
     *     {@link JnlpDoc }
     *     
     */
    public JnlpDoc getJnlpDoc() {
        return jnlpDoc;
    }

    /**
     * Sets the value of the jnlpDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JnlpDoc }
     *     
     */
    public void setJnlpDoc(JnlpDoc value) {
        this.jnlpDoc = value;
    }

}