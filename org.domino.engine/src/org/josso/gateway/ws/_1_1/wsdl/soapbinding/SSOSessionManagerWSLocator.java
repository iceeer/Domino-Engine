/**
 * SSOSessionManagerWSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

import org.domino.engine.Engine;

public class SSOSessionManagerWSLocator extends org.apache.axis.client.Service implements org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerWS {

    public SSOSessionManagerWSLocator() {
    }


    public SSOSessionManagerWSLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SSOSessionManagerWSLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SSOSessionManager
	private java.lang.String SSOSessionManager_address = Engine.getProperty(
			"sso.josso.url", "http://10.136.238.173:8888/josso/")
			+ "services/SSOSessionManager";

    public java.lang.String getSSOSessionManagerAddress() {
        return SSOSessionManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SSOSessionManagerWSDDServiceName = "SSOSessionManager";

    public java.lang.String getSSOSessionManagerWSDDServiceName() {
        return SSOSessionManagerWSDDServiceName;
    }

    public void setSSOSessionManagerWSDDServiceName(java.lang.String name) {
        SSOSessionManagerWSDDServiceName = name;
    }

    public org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManager getSSOSessionManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SSOSessionManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSSOSessionManager(endpoint);
    }

    public org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManager getSSOSessionManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerSoapBindingStub _stub = new org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getSSOSessionManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSSOSessionManagerEndpointAddress(java.lang.String address) {
        SSOSessionManager_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManager.class.isAssignableFrom(serviceEndpointInterface)) {
                org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerSoapBindingStub _stub = new org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerSoapBindingStub(new java.net.URL(SSOSessionManager_address), this);
                _stub.setPortName(getSSOSessionManagerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SSOSessionManager".equals(inputPortName)) {
            return getSSOSessionManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding", "SSOSessionManagerWS");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding", "SSOSessionManager"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SSOSessionManager".equals(portName)) {
            setSSOSessionManagerEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
