/**
 * SSOSessionManagerSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.josso.gateway.ws._1_1.wsdl.soapbinding;

public class SSOSessionManagerSoapBindingStub extends org.apache.axis.client.Stub implements org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManager {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("accessSession");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AccessSessionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AccessSessionRequestType"), org.josso.gateway.ws._1_1.protocol.AccessSessionRequestType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AccessSessionResponseType"));
        oper.setReturnClass(org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "AccessSessionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/accessSession", "SSOSessionError"),
                      "org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSessionErrorType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/accessSession", "NoSuchSessionError"),
                      "org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "NoSuchSessionErrorType"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getSession");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SessionRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SessionRequestType"), org.josso.gateway.ws._1_1.protocol.SessionRequestType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SessionResponseType"));
        oper.setReturnClass(org.josso.gateway.ws._1_1.protocol.SessionResponseType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "SessionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/getSession", "SSOSessionError"),
                      "org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSessionErrorType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/getSession", "NoSuchSessionError"),
                      "org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType",
                      new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "NoSuchSessionErrorType"), 
                      true
                     ));
        _operations[1] = oper;

    }

    public SSOSessionManagerSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public SSOSessionManagerSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public SSOSessionManagerSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AccessSessionRequestType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.AccessSessionRequestType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "AccessSessionResponseType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "NoSuchSessionErrorType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SessionRequestType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.SessionRequestType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SessionResponseType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.SessionResponseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSessionErrorType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/protocol", "SSOSessionType");
            cachedSerQNames.add(qName);
            cls = org.josso.gateway.ws._1_1.protocol.SSOSessionType.class;
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

    public org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType accessSession(org.josso.gateway.ws._1_1.protocol.AccessSessionRequestType accessSessionRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType, org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/accessSession");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/accessSession", "accessSession"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {accessSessionRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, org.josso.gateway.ws._1_1.protocol.AccessSessionResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.josso.gateway.ws._1_1.protocol.SessionResponseType getSession(org.josso.gateway.ws._1_1.protocol.SessionRequestType sessionRequest) throws java.rmi.RemoteException, org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType, org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/getSession");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://josso.org/gateway/ws/1.1/wsdl/soapbinding/SessionManager/getSession", "getSession"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.josso.gateway.ws._1_1.protocol.SessionResponseType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.josso.gateway.ws._1_1.protocol.SessionResponseType) org.apache.axis.utils.JavaUtils.convert(_resp, org.josso.gateway.ws._1_1.protocol.SessionResponseType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.SSOSessionErrorType) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType) {
              throw (org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
