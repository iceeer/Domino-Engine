/**
 * SSOIdentityProviderSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

public class SSOIdentityProviderSoapBindingStub extends org.apache.axis.client.Stub implements org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("resolveAuthenticationAssertion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ResolveAuthenticationAssertionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ResolveAuthenticationAssertionRequestType"), org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionRequestType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ResolveAuthenticationAssertionResponseType"));
        oper.setReturnClass(org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "ResolveAuthenticationAssertionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/resolveAuthenticationAssertion", "SSOIdentityProviderError"),
                      "org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOIdentityProviderErrorType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/resolveAuthenticationAssertion", "AssertionNotValidError"),
                      "org.josso.gateway.ws._1_1.protocol.AssertionNotValidErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertionNotValidErrorType"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("assertIdentityWithSimpleAuthentication");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AssertIdentityWithSimpleAuthenticationRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertIdentityWithSimpleAuthenticationRequestType"), org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationRequestType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertIdentityWithSimpleAuthenticationResponseType"));
        oper.setReturnClass(org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "AssertIdentityWithSimpleAuthenticationResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/assertIdentityWithSimpleAuthentication", "SSOIdentityProviderError"),
                      "org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOIdentityProviderErrorType"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("globalSignoff");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GlobalSignoffRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "GlobalSignoffRequestType"), org.josso.gateway.ws._1_1.protocol.GlobalSignoffRequestType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "GlobalSignoffResponseType"));
        oper.setReturnClass(org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "GlobalSignoffResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/globalSignoff", "SSOIdentityProviderError"),
                      "org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOIdentityProviderErrorType"), 
                      true
                     ));
        _operations[2] = oper;

    }

    public SSOIdentityProviderSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public SSOIdentityProviderSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public SSOIdentityProviderSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertIdentityWithSimpleAuthenticationRequestType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationRequestType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertIdentityWithSimpleAuthenticationResponseType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AssertionNotValidErrorType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.AssertionNotValidErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "GlobalSignoffRequestType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.GlobalSignoffRequestType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "GlobalSignoffResponseType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ResolveAuthenticationAssertionRequestType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionRequestType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "ResolveAuthenticationAssertionResponseType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOIdentityProviderErrorType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType resolveAuthenticationAssertion(org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionRequestType resolveAuthenticationAssertionRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType, org.josso.gateway.ws._1_1.protocol.AssertionNotValidErrorType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/resolveAuthenticationAssertion");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/resolveAuthenticationAssertion", "resolveAuthenticationAssertion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {resolveAuthenticationAssertionRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.AssertionNotValidErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.AssertionNotValidErrorType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType assertIdentityWithSimpleAuthentication(org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationRequestType assertIdentityWithSimpleAuthenticationRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/assertIdentityWithSimpleAuthentication");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/assertIdentityWithSimpleAuthentication", "assertIdentityWithSimpleAuthentication"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {assertIdentityWithSimpleAuthenticationRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType globalSignoff(org.josso.gateway.ws._1_1.protocol.GlobalSignoffRequestType globalSignoffRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/globalSignoff");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/IdentityProvider/globalSignoff", "globalSignoff"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {globalSignoffRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, org.josso.gateway.ws._1_1.protocol.GlobalSignoffResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
