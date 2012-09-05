/**
 * SSOIdentityProviderWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

public interface SSOIdentityProviderWS extends javax.xml.rpc.Service {
    public java.lang.String getSSOIdentityProviderAddress();

    public org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider getSSOIdentityProvider() throws javax.xml.rpc.ServiceException;

    public org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider getSSOIdentityProvider(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
