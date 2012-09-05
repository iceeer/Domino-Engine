/**
 * SSOIdentityProviderWSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

import org.domino.engine.Engine;

public class SSOIdentityProviderWSLocator extends
		org.apache.axis.client.Service implements
		org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderWS {

	public SSOIdentityProviderWSLocator() {
	}

	public SSOIdentityProviderWSLocator(
			org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public SSOIdentityProviderWSLocator(java.lang.String wsdlLoc,
			javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for SSOIdentityProvider
	private java.lang.String SSOIdentityProvider_address = Engine.getProperty(
			"sso.josso.url", "http://10.136.238.173:8888/josso/")
			+ "services/SSOIdentityProvider";

	public java.lang.String getSSOIdentityProviderAddress() {
		return SSOIdentityProvider_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String SSOIdentityProviderWSDDServiceName = "SSOIdentityProvider";

	public java.lang.String getSSOIdentityProviderWSDDServiceName() {
		return SSOIdentityProviderWSDDServiceName;
	}

	public void setSSOIdentityProviderWSDDServiceName(java.lang.String name) {
		SSOIdentityProviderWSDDServiceName = name;
	}

	public org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider getSSOIdentityProvider()
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(SSOIdentityProvider_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getSSOIdentityProvider(endpoint);
	}

	public org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider getSSOIdentityProvider(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderSoapBindingStub _stub = new org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderSoapBindingStub(
					portAddress, this);
			_stub.setPortName(getSSOIdentityProviderWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setSSOIdentityProviderEndpointAddress(java.lang.String address) {
		SSOIdentityProvider_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		try {
			if (org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider.class
					.isAssignableFrom(serviceEndpointInterface)) {
				org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderSoapBindingStub _stub = new org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderSoapBindingStub(
						new java.net.URL(SSOIdentityProvider_address), this);
				_stub.setPortName(getSSOIdentityProviderWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException(
				"There is no stub implementation for the interface:  "
						+ (serviceEndpointInterface == null ? "null"
								: serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName,
			Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("SSOIdentityProvider".equals(inputPortName)) {
			return getSSOIdentityProvider();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName(
				"http://josso.org/gateway/ws/1.1/wsdl/soapbinding",
				"SSOIdentityProviderWS");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName(
					"http://josso.org/gateway/ws/1.1/wsdl/soapbinding",
					"SSOIdentityProvider"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("SSOIdentityProvider".equals(portName)) {
			setSSOIdentityProviderEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(
					" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
