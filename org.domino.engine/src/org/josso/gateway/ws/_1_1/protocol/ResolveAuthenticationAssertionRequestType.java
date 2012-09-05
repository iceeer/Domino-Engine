/**
 * ResolveAuthenticationAssertionRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.protocol;

public class ResolveAuthenticationAssertionRequestType  implements java.io.Serializable {
    private java.lang.String assertionId;

    public ResolveAuthenticationAssertionRequestType() {
    }

    public ResolveAuthenticationAssertionRequestType(
           java.lang.String assertionId) {
           this.assertionId = assertionId;
    }


    /**
     * Gets the assertionId value for this ResolveAuthenticationAssertionRequestType.
     * 
     * @return assertionId
     */
    public java.lang.String getAssertionId() {
        return assertionId;
    }


    /**
     * Sets the assertionId value for this ResolveAuthenticationAssertionRequestType.
     * 
     * @param assertionId
     */
    public void setAssertionId(java.lang.String assertionId) {
        this.assertionId = assertionId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResolveAuthenticationAssertionRequestType)) return false;
        ResolveAuthenticationAssertionRequestType other = (ResolveAuthenticationAssertionRequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.assertionId==null && other.getAssertionId()==null) || 
             (this.assertionId!=null &&
              this.assertionId.equals(other.getAssertionId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAssertionId() != null) {
            _hashCode += getAssertionId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResolveAuthenticationAssertionRequestType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ResolveAuthenticationAssertionRequestType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assertionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "assertionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
