/**
 * 
 */
package org.domino.engine.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.String;

import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.html.HTMLHelper;
import org.domino.engine.utility.sso.SSOHelper;
import org.osgi.framework.Bundle;

import com.ibm.commons.util.StringUtil;
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
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.component.xp.XspEventHandler;
import com.ibm.xsp.designer.context.ServletXSPContext;
import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.resource.ScriptResource;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.TypedUtil;

import lotus.domino.Database;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

/**
 * @author iceeer
 * 
 */
public class XSPHelper {

	/**
	 * 获得上下文
	 * @return
	 */
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * 获得应用
	 * @return
	 */
	public static Application getApplication() {
		return FacesContext.getCurrentInstance().getApplication();
	}

	/**
	 * 获得页面
	 * @return
	 */
	public static UIViewRootEx2 getUIViewRootEx2() {
		return (UIViewRootEx2)FacesContext.getCurrentInstance().getViewRoot();
	}

	/**
	 * 
	 * @return
	 */
	public static ExternalContext getExternalContext() {
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
		return oFacesContext != null ? oFacesContext.getExternalContext()
				: null;
	}

	/**
	 * The method creates a {@link javax.faces.el.ValueBinding} from the
	 * specified value binding expression and returns its current value.<br>
	 * <br>
	 * If the expression references a managed bean or its properties and the
	 * bean has not been created yet, it gets created by the JSF runtime.
	 * 
	 * @param ref
	 *            value binding expression, e.g. #{Bean1.property}
	 * @return value of ValueBinding throws
	 *         javax.faces.el.ReferenceSyntaxException if the specified
	 *         <code>ref</code> has invalid syntax
	 */
	public static Object getBindingValue(String ref) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		return application.createValueBinding(ref).getValue(context);
	}

	/**
	 * The method creates a {@link javax.faces.el.ValueBinding} from the
	 * specified value binding expression and sets a new value for it.<br>
	 * <br>
	 * If the expression references a managed bean and the bean has not been
	 * created yet, it gets created by the JSF runtime.
	 * 
	 * @param ref
	 *            value binding expression, e.g. #{Bean1.property}
	 * @param newObject
	 *            new value for the ValueBinding throws
	 *            javax.faces.el.ReferenceSyntaxException if the specified
	 *            <code>ref</code> has invalid syntax
	 */
	public static void setBindingValue(String ref, Object newObject) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ValueBinding binding = application.createValueBinding(ref);
		binding.setValue(context, newObject);
	}

	/**
	 * The method returns the value of a global JavaScript variable.
	 * 
	 * @param varName
	 *            variable name
	 * @return value
	 * @throws javax.faces.el.EvaluationException
	 *             if an exception is thrown while resolving the variable name
	 */
	public static Object getVariableValue(String varName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getVariableResolver()
				.resolveVariable(context, varName);
	}

	/**
	 * Finds an UIComponent by its component identifier in the current component
	 * tree.
	 * 
	 * @param compId
	 *            the component identifier to search for
	 * @return found UIComponent or null
	 * 
	 * @throws NullPointerException
	 *             if <code>compId</code> is null
	 */
	public static UIComponent findComponent(String compId) {
		return findComponent(FacesContext.getCurrentInstance().getViewRoot(),
				compId);
	}

	/**
	 * Finds an UIComponent by its component identifier in the component tree
	 * below the specified <code>topComponent</code> top component.
	 * 
	 * @param topComponent
	 *            first component to be checked
	 * @param compId
	 *            the component identifier to search for
	 * @return found UIComponent or null
	 * 
	 * @throws NullPointerException
	 *             if <code>compId</code> is null
	 */
	public static UIComponent findComponent(UIComponent topComponent,
			String compId) {
		if (compId == null)
			throw new NullPointerException(
					"Component identifier cannot be null");

		if (compId.equals(topComponent.getId()))
			return topComponent;

		if ((topComponent.getChildCount() + topComponent.getFacetCount()) > 0) {

			for (Iterator i = topComponent.getFacetsAndChildren(); i.hasNext();) {
				UIComponent foundComponent = findComponent(
						(UIComponent) i.next(), compId);
				if (foundComponent != null)
					return foundComponent;
			}
		}

		// if (topComponent.getChildCount() > 0) {
		// List childComponents = topComponent.getChildren();
		//
		// for (Object currChildComponent : childComponents) {
		// UIComponent foundComponent = findComponent(
		// (UIComponent) currChildComponent, compId);
		// if (foundComponent != null)
		// return foundComponent;
		// }
		// }
		//
		// if (topComponent.getFacetCount() > 0) {
		// List childComponents = topComponent.getFacets();
		//
		// for (Object currChildComponent : childComponents) {
		// UIComponent foundComponent = findComponent(
		// (UIComponent) currChildComponent, compId);
		// if (foundComponent != null)
		// return foundComponent;
		// }
		// }

		return null;
	}

	/**
	 * 获得控件的客户端id
	 * 
	 * @param compId
	 * @return
	 */
	public static String getClientId(String compId) {
		UIComponent cmp = findComponent(compId);
		if (cmp != null) {
			return cmp.getClientId(FacesContext.getCurrentInstance());
		} else {
			return "";
		}
	}

	/**
	 * 获得控件的客户端id
	 * 
	 * @param topComponent
	 * @param compId
	 * @return
	 */
	public static String getClientId(UIComponent topComponent, String compId) {
		UIComponent cmp = findComponent(topComponent, compId);
		if (cmp != null) {
			return getClientId(cmp);
		} else {
			return "";
		}
	}

	/**
	 * 获得控件的客户端id
	 * 
	 * @param cmp
	 * @return
	 */
	public static String getClientId(UIComponent cmp) {
		if (cmp != null) {
			return cmp.getClientId(FacesContext.getCurrentInstance());
		} else {
			return "";
		}
	}

	/**
	 * 获得事件的客户端id
	 * 
	 * @param compId
	 * @param eventType
	 * @return
	 */
	public static String getEventId(String compId, String eventType) {
		UIComponent topComponent = findComponent(compId);
		if (topComponent != null) {
			if (topComponent.getChildCount() > 0) {
				List childComponents = topComponent.getChildren();

				for (Object evt : childComponents) {
					if (evt instanceof XspEventHandler) {
						if (((XspEventHandler) evt).getEvent() == eventType) {
							return getClientId((UIComponent) evt);
						}
					}
				}
			}
		}
		return "";
	}

	/**
	 * Returns the current Notes session instance of the Javascript engine.
	 * 
	 * @return Session
	 */
	public static Session getCurrentSession() {
		Session session = null;
		try {
			session = (Session) getVariableValue("session");
		} catch (Exception ve) {
			Helper.logError("get variable session error ");
			try {
				return NotesContext.getCurrent().getCurrentSession();
			} catch (Exception ne) {
				Helper.logError("get session from notes context error ");
				return null;
			}
		}
		if(session == null){
			Helper.logError("can not get the session");
		}
		return session;
	}

	/**
	 * 
	 * @return
	 */
	public static Session getSessionAsSigner() {
		Session session = null;
		try {
			session = (Session) getVariableValue("sessionAsSigner");
		} catch (Exception ve) {
			Helper.logError("get variable sessionAsSigner error ");
			try {
				return NotesContext.getCurrent().getSessionAsSigner();
			} catch (Exception ne) {
				Helper.logError("get sessionAsSigner from notes context error ");
				return null;
			}
		}
		if(session == null){
			Helper.logError("can not get the sessionAsSigner");
		}
		return session;
	}

	/**
	 * Return the applicationScope.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getApplicationScope(FacesContext ctx) {
		return ctx.getExternalContext().getApplicationMap();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getApplicationScope() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getExternalContext().getApplicationMap();
	}

	/**
	 * Return the sessionScope.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSessionScope(FacesContext ctx) {
		return ctx.getExternalContext().getSessionMap();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSessionScope() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getExternalContext().getSessionMap();
	}

	public static Object getSessionScopeValue(String strVarName) {
		Map<String, Object> map = getSessionScope();
		if (map.containsKey(strVarName)) {
			return map.get(strVarName);
		} else {
			return null;
		}
	}

	public static String getSessionScopeValueString(String strVarName) {
		Map<String, Object> map = getSessionScope();
		if (map.containsKey(strVarName)) {
			return (String) map.get(strVarName);
		} else {
			return "";
		}
	}

	public static boolean setSessionScopeValue(String strVarName,
			Object strValue) {
		Map<String, Object> map = getSessionScope();
		map.put(strVarName, strValue);
		return true;
	}

	public static boolean setSessionScopeValueString(String strVarName,
			String strValue) {
		Map<String, Object> map = getSessionScope();
		map.put(strVarName, strValue);
		return true;
	}

	/**
	 * Return the requestScope.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestScope(FacesContext ctx) {
		return ctx.getExternalContext().getRequestMap();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestScope() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getExternalContext().getRequestMap();
	}

	public static Object getRequestScopeValue(String strVarName) {
		Map<String, Object> map = getRequestScope();
		if (map.containsKey(strVarName)) {
			return map.get(strVarName);
		} else {
			return null;
		}
	}

	public static String getRequestScopeValueString(String strVarName) {
		Map<String, Object> map = getRequestScope();
		if (map.containsKey(strVarName)) {
			return (String) map.get(strVarName);
		} else {
			return "";
		}
	}

	public static boolean setRequestScopeValue(String strVarName,
			Object strValue) {
		Map<String, Object> map = getRequestScope();
		map.put(strVarName, strValue);
		return true;
	}

	public static boolean setRequestScopeValueString(String strVarName,
			String strValue) {
		Map<String, Object> map = getRequestScope();
		map.put(strVarName, strValue);
		return true;
	}

	/**
	 * Return the viewScope.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getViewScope(FacesContext ctx) {
		UIViewRoot root = ctx.getViewRoot();
		if (root instanceof UIViewRootEx) {
			return ((UIViewRootEx) root).getViewMap();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getViewScope() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		UIViewRoot root = ctx.getViewRoot();
		if (root instanceof UIViewRootEx) {
			return ((UIViewRootEx) root).getViewMap();
		}
		return null;
	}

	public static Object getViewScopeValue(String strVarName) {
		Map<String, Object> map = getViewScope();
		if (map.containsKey(strVarName)) {
			return map.get(strVarName);
		} else {
			return null;
		}
	}

	public static String getViewScopeValueString(String strVarName) {
		Map<String, Object> map = getViewScope();
		if (map.containsKey(strVarName)) {
			return (String) map.get(strVarName);
		} else {
			return "";
		}
	}

	public static boolean setViewScopeValue(String strVarName, Object strValue) {
		Map<String, Object> map = getViewScope();
		map.put(strVarName, strValue);
		return true;
	}

	public static boolean setViewScopeValueString(String strVarName,
			String strValue) {
		Map<String, Object> map = getViewScope();
		map.put(strVarName, strValue);
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public static Session getSessionAsSignerWithFullAccess() {
		Session session = null;
		try {
			 session = (Session) getVariableValue("sessionAsSignerWithFullAccess");
		} catch (Exception ve) {
			Helper.logError("get variable sessionAsSignerWithFullAccess error ");
			try {
				return NotesContext.getCurrent().getSessionAsSignerFullAdmin();
			} catch (Exception ne) {
				Helper.logError("get sessionAsSignerWithFullAccess from notes context error ");
				try {
					return NotesFactory.createSessionWithFullAccess();
				} catch (Exception e) {
					Helper.logError("notesfactory createSessionWithFullAccess error");
					return null;
				}
			}

		}
		if(session == null){
			Helper.logError("can not get the SessionAsSignerWithFullAccess");
		}
		return session;
	}

	/**
	 * Returns the current Notes database instance of the Javascript engine.
	 * 
	 * @return Database
	 */
	public static Database getCurrentDatabase() {
		return (Database) getVariableValue("database");
	}

	/**
	 * Return the compositeData map for the current component.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCompositeData(FacesContext ctx) {
		return (Map<String, Object>) ctx.getApplication().getVariableResolver()
				.resolveVariable(ctx, "compositeData");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCompositeData() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		return (Map<String, Object>) ctx.getApplication().getVariableResolver()
				.resolveVariable(ctx, "compositeData");
	}

	/**
	 * Method to check whether we are in an XPages application
	 * 
	 * @return true, if XPages app
	 */
	public static boolean isXPagesContext() {
		try {
			Class<?> ctxClazz = Class
					.forName("com.ibm.domino.xsp.module.nsf.NotesContext");
			Method m = ctxClazz
					.getMethod("getCurrentUnchecked", (Class[]) null);
			Object ctxInstance = m.invoke((Object) null, (Object[]) null);
			if (ctxInstance != null) {
				if (getXspContextSession() != null)
					return true;
			}
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (SecurityException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
	}

	/**
	 * Returns the Session of the current XPages request
	 * 
	 * @return session, null in case of errors
	 */
	public static Session getXspContextSession() {
		try {
			Class<?> ctxClazz = Class
					.forName("com.ibm.domino.xsp.module.nsf.NotesContext");
			Method m = ctxClazz.getMethod("getCurrent", (Class[]) null);
			Object ctxInstance = m.invoke((Object) null, (Object[]) null);
			Method m2 = ctxInstance.getClass().getMethod("getCurrentSession",
					(Class[]) null);
			Session session = (Session) m2.invoke(ctxInstance, (Object[]) null);
			return session;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

	public static String getStringProperty(XSPContext ctx, String propName,
			String defaultValue) {
		String v = ctx.getProperty(propName);
		if (v == null) {
			return defaultValue;
		}
		return v;
	}

	public static String getStringProperty(XSPContext ctx, String propName) {
		return getStringProperty(ctx, propName, null);
	}

	public static int getIntProperty(XSPContext ctx, String propName,
			int defaultValue) {
		String v = ctx.getProperty(propName);
		if (v == null) {
			return defaultValue;
		}
		return Integer.valueOf(v);
	}

	public static int getIntProperty(XSPContext ctx, String propName) {
		return getIntProperty(ctx, propName, 0);
	}

	public static boolean getBooleanProperty(XSPContext ctx, String propName,
			boolean defaultValue) {
		String v = ctx.getProperty(propName);
		if (v == null) {
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

	public static String getString(Map<String, Object> map, String propName,
			String defaultValue) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asString(v, defaultValue);
	}

	public static String getString(Map<String, Object> map, String propName) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asString(v);
	}

	public static int getInteger(Map<String, Object> map, String propName,
			int defaultValue) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asInteger(v, defaultValue);
	}

	public static int getInteger(Map<String, Object> map, String propName) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asInteger(v);
	}

	public static long getLong(Map<String, Object> map, String propName,
			long defaultValue) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asLong(v, defaultValue);
	}

	public static long getLong(Map<String, Object> map, String propName) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asLong(v);
	}

	public static double getDouble(Map<String, Object> map, String propName,
			double defaultValue) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asDouble(v, defaultValue);
	}

	public static double getDouble(Map<String, Object> map, String propName) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asDouble(v);
	}

	public static boolean getBoolean(Map<String, Object> map, String propName,
			boolean defaultValue) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asBoolean(v, defaultValue);
	}

	public static boolean getBoolean(Map<String, Object> map, String propName) {
		Object v = map != null ? map.get(propName) : null;
		return DataHelper.asBoolean(v);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(Map<String, Object> map,
			String propName) {
		if (map != null) {
			Map<String, Object> v = (Map<String, Object>) map.get(propName);
			return v;
		}
		return null;
	}

	// ==============================================================================
	// Style utility
	// ==============================================================================

	public static String concatStyleClasses(String s1, String s2) {
		if (StringUtil.isNotEmpty(s1)) {
			if (StringUtil.isNotEmpty(s2)) {
				return s1 + " " + s2;
			}
			return s1;
		} else {
			if (StringUtil.isNotEmpty(s2)) {
				return s2;
			}
			return "";
		}
	}

	public static String concatStyles(String s1, String s2) {
		if (StringUtil.isNotEmpty(s1)) {
			if (StringUtil.isNotEmpty(s2)) {
				return s1 + ";" + s2;
			}
			return s1;
		} else {
			if (StringUtil.isNotEmpty(s2)) {
				return s2;
			}
			return "";
		}
	}

	// ==============================================================================
	// Calculate the client id from an id
	// ==============================================================================

	/**
	 * Calculate the client ID of a component, giving its id. The the id
	 * parameter is already a client ID, then it is returned as is.
	 * 
	 * @return the clientId, or not if the component does not exist
	 */
	public static String getClientId(FacesContext context, UIComponent start,
			String id, boolean forRefresh) {
		if (StringUtil.isNotEmpty(id)) {
			// If it is a client id, then return it
			if (id.indexOf(NamingContainer.SEPARATOR_CHAR) >= 0) {
				return id;
			}
			// Else, find the component and return its client id
			UIComponent c = FacesUtil.getComponentFor(start, id);
			if (c != null) {
				if (forRefresh && c instanceof FacesRefreshableComponent) {
					return ((FacesRefreshableComponent) c)
							.getNonChildClientId(context);
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
	public static String getPartialRefreshUrl(FacesContext context,
			UIComponent component) {
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
		if (StringUtil.isNotEmpty(vid)) {
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
		for (int i = 0; i < len; i++) {
			char c = id.charAt(i);
			if (c == ':' || c == '-') {
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
		String text = StringUtil.format(msg, params);
		return new FacesExceptionEx(text);
	}

	public static FacesExceptionEx newException(Throwable t, String msg,
			Object... params) {
		String text = StringUtil.format(msg, params);
		return new FacesExceptionEx(text, t);
	}

	// ==============================================================================
	// State management
	// ==============================================================================

	/**
	 * Save the state of the view if it requires it.
	 */
	public static void saveViewState(FacesContext context) {
		/* #IF 852 */
		// Temporary implementation that works with N/D 852 until a utility
		// method is made available by the core.
		boolean saveState = false;

		UIViewRoot root = context.getViewRoot();
		if (root instanceof UIViewRootEx) {
			saveState = ((UIViewRootEx) root).shouldSaveState(context);
		}
		if (saveState) {
			StateManager stateManager = context.getApplication()
					.getStateManager();
			StateManager.SerializedView state = stateManager
					.saveSerializedView(context);
			TypedUtil.getRequestMap(context.getExternalContext()).put(
					"com.ibm.xsp.ViewState", state);
		}
		/*
		 * #ELSE FacesUtil.saveViewState(context); #ENDIF
		 */
	}

	// ==============================================================================
	// Reading resource from the library or one of its fragments
	// ==============================================================================

	public static URL getResourceURL(Bundle bundle, String path) {
		int fileNameIndex = path.lastIndexOf('/');
		String fileName = path.substring(fileNameIndex + 1);
		path = path.substring(0, fileNameIndex + 1);
		// see http://www.osgi.org/javadoc/r4v42/org/osgi/framework/Bundle.html
		// #findEntries%28java.lang.String,%20java.lang.String,%20boolean%29
		Enumeration<?> urls = bundle
				.findEntries(path, fileName, false/* recursive */);
		if (null != urls && urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			if (null != url) {
				return url;
			}
		}
		return null; // no match, 404 not found.
	}

	// ================================================================
	// Handling parameters
	// ================================================================

	@SuppressWarnings("unchecked")
	public static void pushParameters(FacesContext context,
			Map<String, String> parameters) {
		// Push the parameters to the request scope
		if (parameters != null) {
			Map req = context.getExternalContext().getRequestMap();
			for (Iterator it = parameters.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				req.put(e.getKey(), e.getValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void pushParameters(String strName, String strValue) {
		FacesContext context = FacesContext.getCurrentInstance();
		pushParameters(context, strName, strValue);
	}

	@SuppressWarnings("unchecked")
	public static void pushParameters(FacesContext context, String strName,
			String strValue) {
		// Push the parameters to the request scope
		if (strName != null) {
			Map req = context.getExternalContext().getRequestMap();
			req.put(strName, strValue);
		}
	}

	@SuppressWarnings("unchecked")
	public static void pushParameters(Map<String, String> parameters) {
		FacesContext context = FacesContext.getCurrentInstance();
		pushParameters(context, parameters);
	}

	public static String readParameter(FacesContext context, String name) {
		String value = (String) context.getExternalContext().getRequestMap()
				.get(name);
		if (StringUtil.isEmpty(value)) {
			value = (String) context.getExternalContext()
					.getRequestParameterMap().get(name);
		}
		return value;
	}

	public static String readParameter(String name) {
		FacesContext context = FacesContext.getCurrentInstance();
		return readParameter(context, name);
	}

	// ==============================================================================
	// Assigning a binding bean to a component
	// ==============================================================================

	public static void assignBindingProperty(FacesContext context,
			String bindingExpr, UIComponent component) {
		ValueBinding binding = ((ApplicationEx) context.getApplication())
				.createValueBinding(bindingExpr);
		if (binding.isReadOnly(context)) {
			return;
		}
		if (binding instanceof ComponentBindingObject) {
			((ComponentBindingObject) binding).setComponent(component);
		}
		binding.setValue(context, component);
		component.setValueBinding("binding", binding); //$NON-NLS-1$
	}

	// ==============================================================================
	// Assigning a binding bean to a component
	// ==============================================================================

	public static void assignBindingProperty(String bindingExpr,
			UIComponent component) {
		FacesContext context = FacesContext.getCurrentInstance();
		assignBindingProperty(context, bindingExpr, component);
	}

	/**
	 * Return the UIComponent with the specified id, starting from a particular
	 * component. Same as {@link FacesUtil#getComponentFor(UIComponent, String)}
	 * , except the 8.5.2 FacesUtil method didn't find "for" components that
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
		// We cannot go directly to the PageProvider as we have components, like
		// the
		// repeat control, which are creating multiple controls with the same
		// id.
		// So we do it by browsing the hierarchy while excluding the latest
		// already checked.
		// Not that this is not looking inside included pages.
		UIComponent lastCheck = null;
		for (UIComponent c = start; c != null; c = c.getParent()) {
			UIComponent found = _findComponentFor(c, id, lastCheck);
			if (found != null) {
				return found;
			}
			if (c instanceof FacesPageProvider) {
				break;
			}
			lastCheck = c;
		}

		// 2- Fallback plan to maintain compatibility
		// It should almost never come here but just in case of...
		lastCheck = null;
		for (UIComponent parent = start; parent != null; parent = parent
				.getParent()) {
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

	static private UIComponent _findComponentFor(UIComponent component,
			String id, UIComponent excludeComponent) {
		if (id.equals(component.getId())) {
			return component;
		}

		// search facets first, then children
		int facetCount = component.getFacetCount();
		if (facetCount > 0) {
			for (UIComponent next : TypedUtil.getFacets(component).values()) {
				if (next != excludeComponent
						&& !(next instanceof FacesPageProvider)) {
					next = _findComponentFor(next, id, excludeComponent);
					if (next != null)
						return next;
				}
			}
		}

		// We call this function to compute the count, as getChildren() creates
		// the List object
		int count = component.getChildCount();
		if (count > 0) {
			List<?> children = component.getChildren();
			for (int i = 0; i < count; i++) {
				UIComponent next = (UIComponent) children.get(i);
				if (next != excludeComponent
						&& !(next instanceof FacesPageProvider)) {
					next = _findComponentFor(next, id, excludeComponent);
					if (next != null)
						return next;
				}
			}
		}
		return null;
	}

	static private UIComponent _findComponent(UIComponent component, String id,
			UIComponent excludeComponent) {
		// Look for the exact component id
		if (id.equals(component.getId())) {
			return component;
		}
		// search facets first, then children
		int facetCount = component.getFacetCount();
		if (facetCount > 0) {
			for (UIComponent next : TypedUtil.getFacets(component).values()) {
				if (next != excludeComponent) {
					next = _findComponent(next, id, excludeComponent);
					if (next != null) {
						return next;
					}
				}
			}
		}

		// We call this function to compute the count, as getChildren() creates
		// the List object
		int count = component.getChildCount();
		if (count > 0) {
			List<?> children = component.getChildren();
			for (int i = 0; i < count; i++) {
				UIComponent next = (UIComponent) children.get(i);
				if (next != excludeComponent) {
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
		if (AjaxUtil.isAjaxPartialRefresh(context)) {
			ScriptResource r = new ScriptResource();
			r.setClientSide(true);
			r.setContents(script);
			((UIViewRootEx) context.getViewRoot()).addEncodeResource(r);
			return;
		}
		UIScriptCollector sc = UIScriptCollector.find();
		sc.addScript(script);
	}

	public static String getPageXspUrl(String pageName) {
		if (StringUtil.isNotEmpty(pageName)) {
			if (!pageName.startsWith("/")) {
				pageName = "/" + pageName;
			}
			// if(pageName.startsWith("/")) {
			// pageName = pageName.substring(1);
			// }
			if (!pageName.endsWith(".xsp")) {
				pageName = pageName + ".xsp";
			}
			return pageName;
		}
		return null;
	}

	public static String getPageLabel(String pageName) {
		if (StringUtil.isNotEmpty(pageName)) {
			int pos = pageName.lastIndexOf('/');
			if (pos >= 0) {
				if (pos + 1 < pageName.length()) {
					pageName = pageName.substring(pos + 1);
				} else {
					pageName = "";
				}
			}
			if (pageName.endsWith(".xsp")) {
				pageName = pageName.substring(0, pageName.length() - 4);
			}
			return pageName;
		}
		return null;
	}

	/*
	 * Get the current url("/folder/file.nsf/213.xsp?pp=1")
	 */
	public static String getRequestContextPath() {
		ExternalContext context = XSPHelper.getExternalContext();
		if (context != null) {
			HttpServletRequest request = (HttpServletRequest) context
					.getRequest();
			String activePage = "";

			String queryString = "?" + request.getQueryString();
			String requestURI = request.getRequestURI();
			if (queryString.equals("?")) {
				queryString = "";
			}

			activePage = requestURI + queryString;
			return activePage;
		} else {
			return "";
		}
	}

	/*
	 * Get the current db path("/folder/file.nsf")
	 */
	public static String getContextPath() {
		ExternalContext context = XSPHelper.getExternalContext();
		if (context != null) {
			return context.getRequestContextPath();
		} else {
			return "";
		}
	}

	/*
	 * Get the current host url("http://localhost.com")
	 */
	public static String getHostURL() {
		HttpServletRequest request = XSPHelper.getRequest();
		return request.getScheme()
				+ "://"
				+ request.getServerName()
				+ (request.getServerPort() == 80 ? "" : (":" + request
						.getServerPort()));
	}

	/*
	 * Get the current page name("/213.xsp")
	 */
	public static String getRequestServletPath() {
		ExternalContext context = XSPHelper.getExternalContext();
		if (context != null) {
			return context.getRequestServletPath();
		} else {
			return "";
		}
	}

	/**
	 * 获得当前页
	 * 
	 * @return
	 */
	public static ServletXSPContext getXSPContext() {
		return (ServletXSPContext) XSPHelper.getVariableValue("context");
	}

	/**
	 * 获得文档数据源对象
	 * 
	 * @param strDataName
	 * @return
	 */
	public static DominoDocument getDominoDocument(String strDataName) {
		return (DominoDocument) XSPHelper.getVariableValue(strDataName);
	}

	/**
	 * 获得视图数据源对象
	 * 
	 * @param strDataName
	 * @return
	 */
	public static lotus.domino.local.View getDominoView(String strDataName) {
		return (lotus.domino.local.View) XSPHelper
				.getVariableValue(strDataName);
	}

	/**
	 * 生成常用域
	 * 
	 * @param doc
	 * @return
	 */
	public static boolean buildCommonField(Session session, DominoDocument doc) {
		try {
			doc.replaceItemValue("$ID",
					DominoObjectHelper.buildStaticID(doc.getDocument()));
			doc.replaceItemValue("$CreatedBy",
					DominoObjectHelper.getCurrentUserName(session));
			doc.replaceItemValue("$Created", session.createDateTime(new Date()));
		} catch (Exception e) {
			Helper.logError(e);
		}
		return true;
	}

	/**
	 * 带原参数重定向至其他页面
	 * 
	 * @param strPageName
	 * @return
	 */
	public static String redirectToPageWithParam(String strPageName) {
		return getRequestContextPath().replaceAll(getRequestServletPath(),
				getPageXspUrl(strPageName));
	}

	/**
	 * 删除某参数后重定向至其他页面
	 * @param strParmName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String redirectWithoutParm(String strParmName) {
		StringBuilder sbURL = new StringBuilder();
		HttpServletRequest request = getRequest();
		String strParm = request.getQueryString();
		if (!strParm.isEmpty()) {
			String strToReplace = strParmName + "="
					+ XSPHelper.readParameter(strParmName);
			strParm = strParm.replaceAll(strToReplace, "");

			sbURL.append(request.getRequestURL().append("?")).append(strParm);
		} else {
			sbURL.append(request.getRequestURL());
		}
		return sbURL.toString();
	}

	/**
	 * 获得回复对象
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	/**
	 * 获得请求对象
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	/**
	 * 获得当前页面 http://ip/folder/file.nsf/page.xsp?param=value
	 * @return
	 */
	public static String getFullURL() {
		HttpServletRequest request = getRequest();
		String strParm = request.getQueryString();
		if (Helper.ValidateNotEmpty(strParm)) {
			return request.getRequestURL().append("?").append(strParm)
					.toString();
		} else {
			return request.getRequestURL().toString();
		}

	}

	/**
	 * 重定向
	 * @param strURL
	 */
	public static void redirect(String strURL) {
		try {
			getExternalContext().redirect(strURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Domino默认登录链接
	 * 
	 * @param session
	 * @return
	 */
	public static String buildLoginURL() {

		String strURL = XSPHelper.getHostURL();

		strURL = strURL + DominoObjectHelper.DEFAULT_NAME_DB_URL
				+ DominoObjectHelper.DEFAULT_LOGIN_URL_PARAM;

		if (Engine.isDebug()) {
			Helper.logMessage("The default login url:" + strURL);
		}

		return strURL;
	}

	/**
	 * 组合登录链接
	 * 
	 * @param session
	 * @return
	 */
	public static String buildLoginURL(String strRedirectURL) {
		if (!Helper.ValidateNotNull(strRedirectURL)) {
			strRedirectURL = XSPHelper.getFullURL();
		}

		if (org.domino.engine.Application.isSSOLoginForm()
				&& SSOHelper.isEnableSSO()) {
			return SSOHelper.getSSOUrl() + "signon/login.do?josso_back_to="
					+ strRedirectURL;
		}

		String strURL = XSPHelper.getContextPath();

		if (strURL.equals("")) {// 判断是否能获得当前数据库路径
			strURL = DominoObjectHelper.DEFAULT_NAME_DB_URL
					+ DominoObjectHelper.DEFAULT_LOGIN_URL_PARAM;
		} else {
			strURL += DominoObjectHelper.DEFAULT_LOGIN_URL_PARAM;
		}

		strURL += ("&redirectto=" + strRedirectURL);

		if (Engine.isDebug()) {
			Helper.logMessage("The login url:" + strURL);
		}

		return strURL;
	}

	/**
	 * 组合注销链接
	 * 
	 * @param session
	 * @return
	 */
	public static String buildLogoutURL(String strRedirectURL) {
		if (!Helper.ValidateNotNull(strRedirectURL)) {
			strRedirectURL = XSPHelper.getHostURL();
		}

		if (org.domino.engine.Application.isSSOLoginForm()
				&& SSOHelper.isEnableSSO()) {
			return SSOHelper.getSSOUrl() + "signon/logout.do?josso_back_to="
					+ strRedirectURL;
		}

		String strURL = XSPHelper.getContextPath();

		if (strURL.equals("")) {// 判断是否能获得当前数据库路径
			strURL = DominoObjectHelper.DEFAULT_NAME_DB_URL
					+ DominoObjectHelper.DEFAULT_LOGOUT_URL_PARAM;
		} else {
			strURL += DominoObjectHelper.DEFAULT_LOGOUT_URL_PARAM;
		}

		strURL += ("&redirectto=" + strRedirectURL);

		if (Engine.isDebug()) {
			Helper.logMessage("The logout url:" + strURL);
		}

		return strURL;
	}

	/**
	 * 当前文件夹 /folder/
	 * @return
	 */
	public static String getCurrentFolderURL() {
		String strCurrentDBURL = getContextPath();
		return strCurrentDBURL.substring(0, strCurrentDBURL.lastIndexOf("/")+1);
	}

}
