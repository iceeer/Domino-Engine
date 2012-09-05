/**
 * ResolveAuthenticationAssertionResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.protocol;

public class ResolveAuthenticationAssertionResponseType  implements java.io.Serializable {
    private java.lang.String ssoSessionId;

    private java.lang.String securityDomain;

    public ResolveAuthenticationAssertionResponseType() {
    }

    public ResolveAuthenticationAssertionResponseType(
           java.lang.String ssoSessionId,
           java.lang.String securityDomain) {
           this.ssoSessionId = ssoSessionId;
           this.securityDomain = securityDomain;
    }


    /**
     * Gets the ssoSessionId value for this ResolveAuthenticationAssertionResponseType.
     * 
     * @return ssoSessionId
     */
    public java.lang.String getSsoSessionId() {
        return ssoSessionId;
    }


    /**
     * Sets the ssoSessionId value for this ResolveAuthenticationAssertionResponseType.
     * 
     * @param ssoSessionId
     */
    public void setSsoSessionId(java.lang.String ssoSessionId) {
        this.ssoSessionId = ssoSessionId;
    }


    /**
     * Gets the securityDomain value for this ResolveAuthenticationAssertionResponseType.
     * 
     * @return securityDomain
     */
    public java.lang.String getSecurityDomain() {
        return securityDomain;
    }


    /**
     * Sets the securityDomain value for this ResolveAuthenticationAssertionResponseType.
     * 
     * @param securityDomain
     */
    public void setSecurityDomain(java.lang.String securityDomain) {
        this.securityDomain = securityDomain;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResolveAuthenticationAssertionResponseType)) return false;
        ResolveAuthenticationAssertionResponseType other = (ResolveAuthenticationAssertionResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ssoSessionId==null && other.getSsoSessionId()==null) || 
             (this.ssoSessionId!=null &&
              this.ssoSessionId.equals(other.getSsoSessionId()))) &&
            ((this.securityDomain==null && other.getSecurityDomain()==null) || 
             (this.securityDomain!=null &&
              this.securityDomain.equals(other.getSecurityDomain())));
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
        if (getSsoSessionId() != null) {
            _hashCode += getSsoSessionId().hashCode();
        }
        if (getSecurityDomain() != null) {
            _hashCode += getSecurityDomain().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResolveAuthenticationAssertionResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ResolveAuthenticationAssertionResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ssoSessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ssoSessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityDomain");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "securityDomain"));
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
