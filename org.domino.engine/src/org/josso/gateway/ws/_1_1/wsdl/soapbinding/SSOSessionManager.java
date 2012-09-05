/**
 * SSOSessionManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

public interface SSOSessionManager extends java.rmi.Remote {
    public org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType accessSession(org.josso.gateway.ws._1_1.protocol.AccessSessionRequestType accessSessionRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType, org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType;
    public org.josso.gateway.ws._1_1.protocol.SessionResponseType getSession(org.josso.gateway.ws._1_1.protocol.SessionRequestType sessionRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType, org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType;
}
