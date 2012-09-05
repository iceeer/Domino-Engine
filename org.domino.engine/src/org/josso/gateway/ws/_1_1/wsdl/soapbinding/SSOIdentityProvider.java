/**
 * SSOIdentityProvider.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

public interface SSOIdentityProvider extends java.rmi.Remote {
    public org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType resolveAuthenticationAssertion(org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionRequestType resolveAuthenticationAssertionRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType, org.josso.gateway.ws._1_1.protocol.AssertionNotValidErrorType;
    public org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType assertIdentityWithSimpleAuthentication(org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationRequestType assertIdentityWithSimpleAuthenticationRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType;
    public org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType globalSignoff(org.josso.gateway.ws._1_1.protocol.GlobalSignoffRequestType globalSignoffRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType;
}
