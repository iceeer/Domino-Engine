/*
 * © Copyright IBM Corp. 2010
 * © Copyright Declan Sciolla-Lynch 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.jquery.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.faces.application.StateManager;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import lotus.domino.Database;
import lotus.domino.Session;

import org.osgi.framework.Bundle;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.Version;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.ajax.AjaxUtil;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.UniqueViewIdManager;
import com.ibm.xsp.binding.ComponentBindingObject;
import com.ibm.xsp.component.FacesPageProvider;
import com.ibm.xsp.component.FacesRefreshableComponent;
import com.ibm.xsp.component.UIScriptCollector;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.designer.context.XSPContext;
import com.jquery.library.JqueryActivator;
import com.ibm.xsp.resource.ScriptResource;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.TypedUtil;

@SuppressWarnings("deprecation")
public class JqueryUtil {

    // ==============================================================================
    // Development mode 
    // ==============================================================================
    
    private static boolean DVLP_MODE;
    static {
        DVLP_MODE = false;
        try {
            // Look for a flag
            String prop = Platform.getInstance().getProperty("xsp.jquery.dvlp"); // $NON-NLS-1$
            if(StringUtil.isEmpty(prop)) {
                // Look for a system property
                prop = System.getProperty("xsp.jquery.dvlp"); // $NON-NLS-1$
                if(StringUtil.isEmpty(prop)) {
                    // Look for a Notes.ini property
                    // The Backend classes object is not yet available when this gets initialized, so
                    // we should call the native code here.
                    // To avoid any dependency from this to the native layer, we use Java reflection. Note that 
                    // this underlying code can change at any time, so it might silently fail in the future
/*#IF 852*/
                    prop = AccessController.doPrivileged(new PrivilegedAction<String>() {
                        public String run() {
                            try {
                                //String prop = NotesContext.getCurrentUnchecked().getNotesSession().getEnvironmentString("XPagesDev");
                                NotesContext nc = NotesContext.getCurrentUnchecked();
                                if(nc!=null) {
                                    Method sm = nc.getClass().getMethod("getNotesSession"); // $NON-NLS-1$
                                    Object ns = sm.invoke(nc, (Object[])null);
                                    Method em = ns.getClass().getMethod("getEnvironmentString", String.class); // $NON-NLS-1$
                                    String prop = (String)em.invoke(ns, "jQueryDev"); // $NON-NLS-1$
                                    return prop;
                                }
                            } catch(Throwable t) {}
                            // Assume unknown
                            return null;
                        }
                    });
/*#ELSE
                    com.ibm.xsp.model.domino.DominoUtils.getEnvironmentString("XPagesDev");
#ENDIF*/
                }
            }
            if(StringUtil.isNotEmpty(prop)) {
                if(StringUtil.equals(prop,"true") || StringUtil.equals(prop,"1")) { // $NON-NLS-1$
                    DVLP_MODE = true;
                }
            }
        } catch(Throwable t) {
            t.printStackTrace();
            // Ok, assume not dev mode
            DVLP_MODE = false;
        }
        if(DVLP_MODE) {
            System.out.println("jQuery Plugin is running in development mode, resulting in decreased performance");
        }
    }
    
    /**
     * Check if debug mode should be applied.
     */
    public static final boolean isDevelopmentMode() {
        return DVLP_MODE;
    }
    public static final boolean isDebugJavaScript() {
        return false;
    }
    
    /**
     * Check for the runtime version.
     */
    @SuppressWarnings("deprecation") //$NON-NLS-1$
    public static final boolean isXPages852() {
        if(_852==null) {
            Version v = Version.CurrentRuntimeVersion;
            _852 = (v.getMajor()==8 && v.getMinor()==5 && v.getMicro()==2);
        }
        return _852;
    }
    private static Boolean _852;
    

    // ==============================================================================
    // ExtLib version 
    // ==============================================================================
    
    public static String getJqueryVersion() {
        try {
            String s = AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    Object o = JqueryActivator.instance.getBundle().getHeaders().get("Bundle-Version"); // $NON-NLS-1$
                    if(o!=null) {
                        return o.toString();
                    }
                    return null;
                }
            });
            if(s!=null) {
                return s;
            }
        } catch(SecurityException ex) {}
        return "";
    }
    
    
    // ==============================================================================
    // XspContext access 
    // ==============================================================================
    
    /**
     * Return the current XspContext.
     */
    public static XSPContext getXspContext() {
        return XSPContext.getXSPContext(FacesContext.getCurrentInstance());
    }
    
    /**
     * Resolve the specified variable.
     */
    public static Object resolveVariable(FacesContext facesContext, String name) {
        Object value =  facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, name);
        return value;
    }

    /**
     * Return the compositeData map for the current component.
     */
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getCompositeData(FacesContext ctx) {
        if(ctx!=null) {
            return (Map<String,Object>)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "compositeData"); // $NON-NLS-1$
        }
        return null;
    }
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getCompositeData() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return (Map<String,Object>)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "compositeData"); // $NON-NLS-1$
        }
        return null;
    }

    /**
     * Return the current database.
     */
    public static Database getCurrentDatabase(FacesContext ctx) {
        if(ctx!=null) {
            return (Database)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "database"); // $NON-NLS-1$
        }
        return null;
    }
    public static Database getCurrentDatabase() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return (Database)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "database"); // $NON-NLS-1$
        }
        return null;
    }

    /**
     * Return the current session.
     */
    public static Session getCurrentSession(FacesContext ctx) {
        if(ctx!=null) {
            return (Session)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "session"); // $NON-NLS-1$
        }
        return null;
    }
    public static Session getCurrentSession() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return (Session)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "session"); // $NON-NLS-1$
        }
        return null;
    }

    /**
     * Return the signer session.
     */
    public static Session getCurrentSessionAsSigner(FacesContext ctx) {
        if(ctx!=null) {
            return (Session)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "sessionAsSigner"); // $NON-NLS-1$
        }
        return null;
    }
    public static Session getCurrentSessionAsSigner() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return (Session)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "sessionAsSigner"); // $NON-NLS-1$
        }
        return null;
    }

    /**
     * Return the signer session with full access.
     */
    public static Session getCurrentSessionAsSignerWithFullAccess(FacesContext ctx) {
        if(ctx!=null) {
            return (Session)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "sessionAsSignerWithFullAccess"); // $NON-NLS-1$
        }
        return null;
    }
    public static Session getCurrentSessionAsSignerWithFullAccess() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return (Session)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "sessionAsSignerWithFullAccess");
        }
        return null;
    }
    
    /**
     * Return the applicationScope. 
     */
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getApplicationScope(FacesContext ctx) {
        if(ctx!=null) {
            return ctx.getExternalContext().getApplicationMap();
        }
        return null;
    }
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getApplicationScope() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return ctx.getExternalContext().getApplicationMap();
        }
        return null;
    }
    
    /**
     * Return the sessionScope. 
     */
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getSessionScope(FacesContext ctx) {
        if(ctx!=null) {
            return ctx.getExternalContext().getSessionMap();
        }
        return null;
    }
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getSessionScope() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return ctx.getExternalContext().getSessionMap();
        }
        return null;
    }
    
    /**
     * Return the requestScope. 
     */
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getRequestScope(FacesContext ctx) {
        if(ctx!=null) {
            return ctx.getExternalContext().getRequestMap();
        }
        return null;
    }
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getRequestScope() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if(ctx!=null) {
            return ctx.getExternalContext().getRequestMap();
        }
        return null;
    }
    
    /**
     * Return the viewScope. 
     */
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getViewScope(FacesContext ctx) {
        UIViewRoot root = ctx.getViewRoot();
        if(root instanceof UIViewRootEx) {
            return ((UIViewRootEx)root).getViewMap();
        }
        return null;
    }
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getViewScope() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        UIViewRoot root = ctx.getViewRoot();
        if(root instanceof UIViewRootEx) {
            return ((UIViewRootEx)root).getViewMap();
        }
        return null;
    }

    
    // ==============================================================================
    // Data conversion 
    // ==============================================================================
    
    public static String asString(Object v, String defaultValue) {
        if(v!=null) {
            return v.toString();
        }
        return defaultValue;
    }
    public static String asString(Object v) {
        return asString(v, null);
    }
    
    public static int asInteger(Object v, int defaultValue) {
        if(v!=null) {
            if(v instanceof Number) {
                return ((Number)v).intValue();
            }
            if(v instanceof String) {
                return Integer.valueOf((String)v);
            }
            if(v instanceof Boolean) {
                return ((Boolean)v) ? 1 : 0;
            }
        }
        return defaultValue;
    }
    public static int asInteger(Object v) {
        return asInteger(v, 0);
    }
    
    public static long asLong(Object v, long defaultValue) {
        if(v!=null) {
            if(v instanceof Number) {
                return ((Number)v).longValue();
            }
            if(v instanceof String) {
                return Long.valueOf((String)v);
            }
            if(v instanceof Boolean) {
                return ((Boolean)v) ? 1L : 0L;
            }
        }
        return defaultValue;
    }
    public static long asLong(Object v) {
        return asLong(v, 0);
    }
    
    public static double asDouble(Object v, double defaultValue) {
        if(v!=null) {
            if(v instanceof Number) {
                return ((Number)v).doubleValue();
            }
            if(v instanceof String) {
                return Double.valueOf((String)v);
            }
            if(v instanceof Boolean) {
                return ((Boolean)v) ? 1.0 : 0.0;
            }
        }
        return defaultValue;
    }
    public static double asDouble(Object v) {
        return asDouble(v, 0);
    }
    
    public static boolean asBoolean(Object v, boolean defaultValue) {
        if(v!=null) {
            if(v instanceof Boolean) {
                return ((Boolean)v).booleanValue();
            }
            if(v instanceof String) {
                return Boolean.valueOf((Boolean)v);
            }
            if(v instanceof Number) {
                return ((Number)v).intValue()!=0;
            }
            return true;
        }
        return defaultValue;
    }
    public static boolean asBoolean(Object v) {
        return asBoolean(v, false);
    }

    
    // ==============================================================================
    // Property access helpers 
    // ==============================================================================

    public static String getStringProperty(XSPContext ctx, String propName, String defaultValue) {
        String v = ctx.getProperty(propName);
        if(v==null) {
            return defaultValue;
        }
        return v;
    }
    public static String getStringProperty(XSPContext ctx, String propName) {
        return getStringProperty(ctx, propName, null);
    }
    
    public static int getIntProperty(XSPContext ctx, String propName, int defaultValue) {
        String v = ctx.getProperty(propName);
        if(v==null) {
            return defaultValue;
        }
        return Integer.valueOf(v);
    }
    public static int getIntProperty(XSPContext ctx, String propName) {
        return getIntProperty(ctx, propName, 0);
    }
    
    public static boolean getBooleanProperty(XSPContext ctx, String propName, boolean defaultValue) {
        String v = ctx.getProperty(propName);
        if(v==null) {
            return defaultValue;
        }
        return Boolean.valueOf(v);
    }
    public static boolean getBooleanProperty(XSPContext ctx, String propName) {
        return getBooleanProperty(ctx, propName, false);
    }

    
    // ==============================================================================
    // Map member access 
    // ==============================================================================

    public static String getString(Map<String,Object> map, String propName, String defaultValue) {
        Object v = map!=null ? map.get(propName) : null;
        return asString(v,defaultValue);
    }
    public static String getString(Map<String,Object> map, String propName) {
        Object v = map!=null ? map.get(propName) : null;
        return asString(v);
    }
    
    public static int getInteger(Map<String,Object> map, String propName, int defaultValue) {
        Object v = map!=null ? map.get(propName) : null;
        return asInteger(v,defaultValue);
    }
    public static int getInteger(Map<String,Object> map, String propName) {
        Object v = map!=null ? map.get(propName) : null;
        return asInteger(v);
    }
    
    public static long getLong(Map<String,Object> map, String propName, long defaultValue) {
        Object v = map!=null ? map.get(propName) : null;
        return asLong(v,defaultValue);
    }
    public static long getLong(Map<String,Object> map, String propName) {
        Object v = map!=null ? map.get(propName) : null;
        return asLong(v);
    }
    
    public static double getDouble(Map<String,Object> map, String propName, double defaultValue) {
        Object v = map!=null ? map.get(propName) : null;
        return asDouble(v,defaultValue);
    }
    public static double getDouble(Map<String,Object> map, String propName) {
        Object v = map!=null ? map.get(propName) : null;
        return asDouble(v);
    }
    
    public static boolean getBoolean(Map<String,Object> map, String propName, boolean defaultValue) {
        Object v = map!=null ? map.get(propName) : null;
        return asBoolean(v,defaultValue);
    }
    public static boolean getBoolean(Map<String,Object> map, String propName) {
        Object v = map!=null ? map.get(propName) : null;
        return asBoolean(v);
    }
    
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static Map<String,Object> getMap(Map<String,Object> map, String propName) {
        if(map!=null) {
            Map<String,Object> v = (Map<String,Object>)map.get(propName);
            return v;
        }
        return null;
    }
    
    // ==============================================================================
    // Style utility 
    // ==============================================================================

    public static String concatStyleClasses(String s1, String s2) {
        if(StringUtil.isNotEmpty(s1)) {
            if(StringUtil.isNotEmpty(s2)) {
                return s1 + " " + s2;
            }
            return s1;
        } else {
            if(StringUtil.isNotEmpty(s2)) {
                return s2;
            }
            return "";
        }
    }

    public static String concatStyles(String s1, String s2) {
        if(StringUtil.isNotEmpty(s1)) {
            if(StringUtil.isNotEmpty(s2)) {
                return s1 + ";" + s2;
            }
            return s1;
        } else {
            if(StringUtil.isNotEmpty(s2)) {
                return s2;
            }
            return "";
        }
    }
    
    public static String concatPath(String path1, String path2, char sep) {
        if(StringUtil.isEmpty(path1)) {
            return path2;
        }
        if(StringUtil.isEmpty(path2)) {
            return path1;
        }
        StringBuilder b = new StringBuilder();
        if(path1.charAt(path1.length()-1)==sep) {
            b.append(path1,0,path1.length()-1);
        } else {
            b.append(path1);
        }
        b.append(sep);
        if(path2.charAt(0)==sep) {
            b.append(path2,1,path2.length());
        } else {
            b.append(path2);
        }
        return b.toString();
        //853...
        //PathUtil.concat(path1,path2,sep);
    }
    
    
    public static String getPageXspUrl(String pageName) {
        if(StringUtil.isNotEmpty(pageName)) {
            if(!pageName.startsWith("/")) {
                pageName = "/" + pageName;
            }
//          if(pageName.startsWith("/")) {
//              pageName = pageName.substring(1);
//          }
            if(!pageName.endsWith(".xsp")) {
                pageName = pageName + ".xsp";
            }
            return pageName;
        }
        return null;
    }

    public static String getPageLabel(String pageName) {
        if(StringUtil.isNotEmpty(pageName)) {
            int pos = pageName.lastIndexOf('/');
            if(pos>=0) {
                if(pos+1<pageName.length()) {
                    pageName = pageName.substring(pos+1);
                } else {
                    pageName = "";
                }
            }
            if(pageName.endsWith(".xsp")) {
                pageName = pageName.substring(0,pageName.length()-4);
            }
            return pageName;
        }
        return null;
    }

    
    // ==============================================================================
    // Calculate the client id from an id 
    // ==============================================================================
    
    /**
     * Calculate the client ID of a component, giving its id.
     * The the id parameter is already a client ID, then it is returned as is.
     * @return the clientId, or not if the component does not exist
     */
    public static String getClientId(FacesContext context, UIComponent start, String id, boolean forRefresh) {
        if(StringUtil.isNotEmpty(id)) {
            // If it is a client id, then return it
            if(id.indexOf(NamingContainer.SEPARATOR_CHAR)>=0) {
                return id;
            }
            // Else, find the component and return its client id
            UIComponent c = FacesUtil.getComponentFor(start, id);
            if(c!=null) {
                if(forRefresh && c instanceof FacesRefreshableComponent) {
                    return ((FacesRefreshableComponent)c).getNonChildClientId(context);
                }
                return c.getClientId(context);
            }
        }
        return null;
    }
    
    
    // ==============================================================================
    // Ajax utility
    // ==============================================================================

    /**
     * Compose the URL for an Ajax partial refresh request related. 
     */
    public static String getPartialRefreshUrl(FacesContext context, UIComponent component) {
        ExternalContext ctx = context.getExternalContext();
        String contextPath = ctx.getRequestContextPath();
        String servletPath = ctx.getRequestServletPath();

        StringBuilder b = new StringBuilder();
        b.append(contextPath);
        b.append(servletPath);
        
        // Add the component id
        String ajaxId = component.getClientId(context);
        b.append('?');
        b.append(AjaxUtil.AJAX_COMPID);
        b.append("=");
        b.append(ajaxId);
        
        // Add the view specific id
        String vid = UniqueViewIdManager.getUniqueViewId(context.getViewRoot());
        if(StringUtil.isNotEmpty(vid)) {
            b.append('&');
            b.append(AjaxUtil.AJAX_VIEWID);
            b.append("=");
            b.append(vid);
        }
        
        return b.toString();
    }   

    
    // ==============================================================================
    // Create a valid JavaScript function name from an HTML id 
    // ==============================================================================
    
    public static String encodeJSFunctionName(String id) {
        StringBuilder b = new StringBuilder();
        int len = id.length();
        for(int i=0; i<len; i++) {
            char c = id.charAt(i);
            if(c==':' || c=='-') {
                b.append('_');
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }
    
    // ==============================================================================
    // Exception 
    // ==============================================================================
    
    public static FacesExceptionEx newException(String msg, Object... params) {
        String text = StringUtil.format(msg,params);
        return new FacesExceptionEx(text);
    }
    
    public static FacesExceptionEx newException(Throwable t, String msg, Object... params) {
        String text = StringUtil.format(msg,params);
        return new FacesExceptionEx(text,t);
    }
    

    
    // ==============================================================================
    // State management  
    // ==============================================================================
    
    /**
     * Save the state of the view if it requires it.
     */
    public static void saveViewState(FacesContext context) {
/*#IF 852*/
        // Temporary implementation that works with N/D 852 until a utility
        // method is made available by the core.
        boolean saveState = false;
        
        UIViewRoot root = context.getViewRoot();
        if(root instanceof UIViewRootEx) {
            saveState = ((UIViewRootEx)root).shouldSaveState(context);
        }
        if(saveState) {
            StateManager stateManager = context.getApplication().getStateManager();
            StateManager.SerializedView state = stateManager.saveSerializedView(context);
            TypedUtil.getRequestMap(context.getExternalContext()).put("com.ibm.xsp.ViewState", state); // $NON-NLS-1$
        }
/*#ELSE
        FacesUtil.saveViewState(context);
#ENDIF*/
    }

    
    // ==============================================================================
    // Reading resource from the library or one of its fragments  
    // ==============================================================================
    
    public static URL getResourceURL(Bundle bundle, String path) {
        int fileNameIndex = path.lastIndexOf('/');
        String fileName = path.substring(fileNameIndex+1);
        path = path.substring(0, fileNameIndex+1);
        // see http://www.osgi.org/javadoc/r4v42/org/osgi/framework/Bundle.html
        //  #findEntries%28java.lang.String,%20java.lang.String,%20boolean%29
        Enumeration<?> urls = bundle.findEntries(path, fileName, false/*recursive*/);
        if( null != urls && urls.hasMoreElements() ){
            URL url = (URL) urls.nextElement();
            if( null != url ){
                return url;
            }
        }
        return null; // no match, 404 not found.
    }

    
    // ================================================================
    // Handling parameters
    // ================================================================

    public static void pushParameters(FacesContext context, Map<String,String> parameters) {
        // Push the parameters to the request scope
        if(parameters!=null) { // TODO why? the request params are already in the requestScope.
            Map<String, Object> req = TypedUtil.getRequestMap(context.getExternalContext());
            for (Map.Entry<String, String> e : parameters.entrySet()) {
                req.put(e.getKey(), e.getValue());
            }
        }
    }
    
    public static String readParameter(FacesContext context, String name) {
        String value = (String)context.getExternalContext().getRequestMap().get(name);
        if(StringUtil.isEmpty(value)) {
            value = (String)context.getExternalContext().getRequestParameterMap().get(name);
        }
        return value;
    }
    
    
    // ==============================================================================
    // Assigning a binding bean to a component  
    // ==============================================================================

    public static void assignBindingProperty(FacesContext context, String bindingExpr, UIComponent component) {
        ValueBinding binding = ((ApplicationEx)context.getApplication()).createValueBinding(bindingExpr);
        if( binding.isReadOnly(context) ){
            return;
        }
        if( binding instanceof ComponentBindingObject ){
            ((ComponentBindingObject)binding).setComponent(component);
        }
        binding.setValue(context, component);
        component.setValueBinding("binding", binding); //$NON-NLS-1$
    }

    /**
     * Return the UIComponent with the specified id, starting from a particular
     * component. Same as {@link FacesUtil#getComponentFor(UIComponent, String)}, 
     * except the 8.5.2 FacesUtil method didn't find "for" components that
     * were within facets. Provided as a workaround for SPR#MKEE86YD5L.
     * 
     * @designer.publicmethod
     */
    static public UIComponent getComponentFor(UIComponent start, String id) {
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        
        // 1- Look for a component with this id in the current PageProvider
        // (PageProvider being a Page/Custom Control)
        // We cannot go directly to the PageProvider as we have components, like the
        // repeat control, which are creating multiple controls with the same id.
        // So we do it by browsing the hierarchy while excluding the latest already checked.
        // Not that this is not looking inside included pages.
        UIComponent lastCheck = null;
        for( UIComponent c=start; c!=null; c=c.getParent() ) {
            UIComponent found = _findComponentFor(c, id, lastCheck);
            if(found!=null) {
                return found;
            }
            if(c instanceof FacesPageProvider) {
                break;
            }
            lastCheck = c;
        }
        
        
        // 2- Fallback plan to maintain compatibility
        // It should almost never come here but just in case of...
        lastCheck = null;
        for (UIComponent parent = start; parent != null; parent = parent.getParent()) {
            if (parent.getChildCount() > 0 || parent.getFacetCount() > 0) {
                if (parent instanceof NamingContainer) {
                    UIComponent c = _findComponent(parent, id, lastCheck);
                    if (c != null) {
                        return c;
                    }
                    lastCheck = c;
                }
            }
        }
        return null;
    }
    static private UIComponent _findComponentFor(UIComponent component, String id, UIComponent excludeComponent) {
        if (id.equals(component.getId())) {
            return component;
        }
        
        // search facets first, then children
        int facetCount = component.getFacetCount();
        if( facetCount > 0 ){
            for (UIComponent next : TypedUtil.getFacets(component).values()) {
                if (next!=excludeComponent && !(next instanceof FacesPageProvider)) {
                    next = _findComponentFor(next, id, excludeComponent);
                    if (next != null)
                        return next;
                }
            }
        }
        
        // We call this function to compute the count, as getChildren() creates the List object
        int count = component.getChildCount();
        if(count>0) {
            List<?> children = component.getChildren();
            for( int i=0; i<count; i++) {
                UIComponent next = (UIComponent)children.get(i);
                if (next!=excludeComponent && !(next instanceof FacesPageProvider)) {
                    next = _findComponentFor(next, id, excludeComponent);
                    if (next != null)
                        return next;
                }
            }
        }
        return null;
    }
    static private UIComponent _findComponent(UIComponent component, String id, UIComponent excludeComponent) {
        // Look for the exact component id 
        if (id.equals(component.getId())) {
            return component;
        }
        // search facets first, then children
        int facetCount = component.getFacetCount();
        if( facetCount > 0 ){
            for(UIComponent next : TypedUtil.getFacets(component).values() ){
                if (next!=excludeComponent) {
                    next = _findComponent(next, id, excludeComponent);
                    if (next != null) {
                        return next;
                    }
                }
            }
        }
        
        // We call this function to compute the count, as getChildren() creates the List object
        int count = component.getChildCount();
        if(count>0) {
            List<?> children = component.getChildren();
            for( int i=0; i<count; i++) {
                UIComponent next = (UIComponent)children.get(i);
                if (next!=excludeComponent) {
                    next = _findComponent(next, id, excludeComponent);
                    if (next != null) {
                        return next;
                    }
                }
            }
        }
        return null;
    }

    
    // ==============================================================================
    // Render a pending action  
    // ==============================================================================

    public static void addPendingScript(FacesContext context, String script) {
        // Note that this should change with the new feature in 853 for contributing script!
        if(AjaxUtil.isAjaxPartialRefresh(context)) {
            boolean isRendering = AjaxUtil.isRendering(context);
            AjaxUtil.setRendering(context, true);
            try {
                ScriptResource r = new ScriptResource();
                r.setClientSide(true);
                r.setContents(script);
                ((UIViewRootEx)context.getViewRoot()).addEncodeResource(r);
                return;
            } finally {
                AjaxUtil.setRendering(context, isRendering);
            }
        }
        UIScriptCollector sc = UIScriptCollector.find();
        sc.addScript(script);
    }
}