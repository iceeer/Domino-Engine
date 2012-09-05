﻿/**
 * AssertIdentityWithSimpleAuthenticationRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.protocol;

public class AssertIdentityWithSimpleAuthenticationRequestType  implements java.io.Serializable {
    private java.lang.String securityDomain;

    private java.lang.String username;

    private java.lang.String password;

    public AssertIdentityWithSimpleAuthenticationRequestType() {
    }

    public AssertIdentityWithSimpleAuthenticationRequestType(
           java.lang.String securityDomain,
           java.lang.String username,
           java.lang.String password) {
           this.securityDomain = securityDomain;
           this.username = username;
           this.password = password;
    }


    /**
     * Gets the securityDomain value for this AssertIdentityWithSimpleAuthenticationRequestType.
     * 
     * @return securityDomain
     */
    public java.lang.String getSecurityDomain() {
        return securityDomain;
    }


    /**
     * Sets the securityDomain value for this AssertIdentityWithSimpleAuthenticationRequestType.
     * 
     * @param securityDomain
     */
    public void setSecurityDomain(java.lang.String securityDomain) {
        this.securityDomain = securityDomain;
    }


    /**
     * Gets the username value for this AssertIdentityWithSimpleAuthenticationRequestType.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this AssertIdentityWithSimpleAuthenticationRequestType.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the password value for this AssertIdentityWithSimpleAuthenticationRequestType.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this AssertIdentityWithSimpleAuthenticationRequestType.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AssertIdentityWithSimpleAuthenticationRequestType)) return false;
        AssertIdentityWithSimpleAuthenticationRequestType other = (AssertIdentityWithSimpleAuthenticationRequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.securityDomain==null && other.getSecurityDomain()==null) || 
             (this.securityDomain!=null &&
              this.securityDomain.equals(other.getSecurityDomain()))) &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword())));
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
        if (getSecurityDomain() != null) {
            _hashCode += getSecurityDomain().hashCode();
        }
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AssertIdentityWithSimpleAuthenticationRequestType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertIdentityWithSimpleAuthenticationRequestType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityDomain");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "securityDomain"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "password"));
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
