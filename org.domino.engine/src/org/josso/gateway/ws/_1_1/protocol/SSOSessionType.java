/**
 * SSOSessionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.protocol;

public class SSOSessionType  implements java.io.Serializable {
    private java.lang.String id;

    private long creationTime;

    private long lastAccessTime;

    private int maxInactiveInterval;

    private java.lang.String username;

    private long accessCount;

    public SSOSessionType() {
    }

    public SSOSessionType(
           java.lang.String id,
           long creationTime,
           long lastAccessTime,
           int maxInactiveInterval,
           java.lang.String username,
           long accessCount) {
           this.id = id;
           this.creationTime = creationTime;
           this.lastAccessTime = lastAccessTime;
           this.maxInactiveInterval = maxInactiveInterval;
           this.username = username;
           this.accessCount = accessCount;
    }


    /**
     * Gets the id value for this SSOSessionType.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this SSOSessionType.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the creationTime value for this SSOSessionType.
     * 
     * @return creationTime
     */
    public long getCreationTime() {
        return creationTime;
    }


    /**
     * Sets the creationTime value for this SSOSessionType.
     * 
     * @param creationTime
     */
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Gets the lastAccessTime value for this SSOSessionType.
     * 
     * @return lastAccessTime
     */
    public long getLastAccessTime() {
        return lastAccessTime;
    }


    /**
     * Sets the lastAccessTime value for this SSOSessionType.
     * 
     * @param lastAccessTime
     */
    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }


    /**
     * Gets the maxInactiveInterval value for this SSOSessionType.
     * 
     * @return maxInactiveInterval
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }


    /**
     * Sets the maxInactiveInterval value for this SSOSessionType.
     * 
     * @param maxInactiveInterval
     */
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }


    /**
     * Gets the username value for this SSOSessionType.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this SSOSessionType.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the accessCount value for this SSOSessionType.
     * 
     * @return accessCount
     */
    public long getAccessCount() {
        return accessCount;
    }


    /**
     * Sets the accessCount value for this SSOSessionType.
     * 
     * @param accessCount
     */
    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SSOSessionType)) return false;
        SSOSessionType other = (SSOSessionType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            this.creationTime == other.getCreationTime() &&
            this.lastAccessTime == other.getLastAccessTime() &&
            this.maxInactiveInterval == other.getMaxInactiveInterval() &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            this.accessCount == other.getAccessCount();
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        _hashCode += new Long(getCreationTime()).hashCode();
        _hashCode += new Long(getLastAccessTime()).hashCode();
        _hashCode += getMaxInactiveInterval();
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        _hashCode += new Long(getAccessCount()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SSOSessionType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSessionType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "creationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastAccessTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "lastAccessTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxInactiveInterval");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "maxInactiveInterval"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "accessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
