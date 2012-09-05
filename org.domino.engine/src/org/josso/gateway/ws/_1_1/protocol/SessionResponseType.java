/**
 * SessionResponseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.protocol;

public class SessionResponseType  implements java.io.Serializable {
    private org.josso.gateway.ws._1_1.protocol.SSOSessionType SSOSession;

    public SessionResponseType() {
    }

    public SessionResponseType(
           org.josso.gateway.ws._1_1.protocol.SSOSessionType SSOSession) {
           this.SSOSession = SSOSession;
    }


    /**
     * Gets the SSOSession value for this SessionResponseType.
     * 
     * @return SSOSession
     */
    public org.josso.gateway.ws._1_1.protocol.SSOSessionType getSSOSession() {
        return SSOSession;
    }


    /**
     * Sets the SSOSession value for this SessionResponseType.
     * 
     * @param SSOSession
     */
    public void setSSOSession(org.josso.gateway.ws._1_1.protocol.SSOSessionType SSOSession) {
        this.SSOSession = SSOSession;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SessionResponseType)) return false;
        SessionResponseType other = (SessionResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.SSOSession==null && other.getSSOSession()==null) || 
             (this.SSOSession!=null &&
              this.SSOSession.equals(other.getSSOSession())));
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
        if (getSSOSession() != null) {
            _hashCode += getSSOSession().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SessionResponseType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SessionResponseType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SSOSession");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSession"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSessionType"));
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
