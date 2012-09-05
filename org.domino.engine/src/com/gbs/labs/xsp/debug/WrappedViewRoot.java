package com.gbs.labs.xsp.debug;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.w3c.dom.Document;

import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.model.DataSource;

public class WrappedViewRoot {
	public String								_owner;
	private XSPBugBean							_parentBean			= null;
	private UIViewRootEx2						_root;
	private Session								_session;
	// private ViewRootConsole console = new ViewRootConsole();
	private FacesContext						_facesContext;
	private HashMap<Integer, UIComponentBase>	componentRegistry	= new HashMap<Integer, UIComponentBase>();
	private HashMap<Integer, DataSource>		dataSourceRegistry	= new HashMap<Integer, DataSource>();
	private HashMap<UIComponentBase, String>	componentIdRegistry	= new HashMap<UIComponentBase, String>();

	private String								_jsonCache			= "";
	private XspCommonsLogging					_logger;

	public WrappedViewRoot (FacesContext context, UIViewRootEx2 view, XSPBugBean parent) throws NotesException {
		_root = view;

		_facesContext = context;
		_parentBean = parent;
		_session = (Session) context.getApplication().getVariableResolver().resolveVariable(context, "session");
		_owner = _session.getEffectiveUserName();
	}

	public Session getSession () {
		try {
			@SuppressWarnings("unused")
			boolean chk = _session.isValid();
		} catch (Exception e) {
			_session = (Session) _facesContext.getApplication().getVariableResolver().resolveVariable(_facesContext, "session");
		}
		return _session;
	}

	// public WrappedViewRoot (XSPBugBean parent, UIViewRootEx2 view) {
	// System.out.println("Creating new view root " + view.getUniqueViewId());
	// try {
	// root = view;
	// // facesContext = FacesContext.getCurrentInstance();
	// _parentBean = parent;
	// //
	// // owner = session.getEffectiveUserName();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public XspCommonsLogging getLogger () {
		if (_logger == null) {
			_logger = new XspCommonsLogging();
			_logger.setView(this);
		}
		return _logger;
	}

	public String getJson () {
		// System.out.println("Sending json for " + getId());
		// _facesContext = FacesContext.getCurrentInstance();
		String out = _jsonCache.length() == 0 ? refreshJson() : _jsonCache;
		System.out.println("Sent json for " + getId() + ": " + _jsonCache.length());
		return out;
	}

	// public String getJson (boolean selfDocument) {
	// System.out.println("Sending json for " + getId());
	// _facesContext = FacesContext.getCurrentInstance();
	// String out = _jsonCache.length() == 0 ? refreshJson(selfDocument) : _jsonCache;
	// System.out.println("Sent json for " + getId() + ": " + _jsonCache.length());
	// return out;
	// }

	public void setJson (String json) {
		_jsonCache = json;
	}

	public XspCommonsLogging getConsole () {
		return getLogger();
	}

	public String refreshJson () {
		// _jsonCache = "";
		System.out.println("Forcing JSON refresh for " + getId());
		String result = "";
		_jsonCache = getParent().viewToJson(this);
		// System.out.println("Generated JSON of length: " + _jsonCache.length());
		result = _jsonCache;
		return result;
	}

	// public String refreshJson (boolean selfDocument) {
	// _jsonCache = "";
	// String result = "";
	// _jsonCache = getParent().viewToJson(this, selfDocument);
	// return result;
	// }

	public XSPBugBean getParent () {
		return _parentBean;
	}

	public int getChildCount () {
		return getView().getChildCount();
	}

	@SuppressWarnings("unchecked")
	public List<UIComponentBase> getChildren () {
		List<UIComponentBase> children = getView().getChildren();
		return children;
	}

	public UIComponentBase getComponent (int componentId) {
		return componentRegistry.get(componentId);
	}

	public UIComponentBase getComponent (String clientId) {
		UIComponentBase result = null;
		if (componentIdRegistry.containsValue(clientId)) {
			for (Map.Entry<UIComponentBase, String> curEntry : componentIdRegistry.entrySet()) {
				if (curEntry.getValue().equals(clientId)) {
					result = curEntry.getKey();
				}
			}
		}
		return result;
	}

	public DataSource getDataSource (int id) {
		return dataSourceRegistry.get(id);
	}

	public FacesContext getFacesContext () {
		return _facesContext;
	}

	public String getId () {
		return getView().getUniqueViewId();
	}

	public String getOwner () {
		return _owner;
	}

	public UIViewRootEx2 getView () {
		return _root;
	}

	public boolean isValid () {
		return (getView() != null);
	}

	public void registerComponent (UIComponentBase component) {
		try {
			if (!componentRegistry.containsKey(component.hashCode())) {
				componentRegistry.put(component.hashCode(), component);
			}
		} catch (Exception e) {
			System.out.println("Attempt to register component failed badly. :(");
			// e.printStackTrace();
		}
	}

	public String getClientId (UIComponentBase component) {
		if (componentIdRegistry.containsKey(component)) {
			// System.out.println("returning CACHED client id: " + componentIdRegistry.get(component));
			return componentIdRegistry.get(component);
		} else {
			try {
				// System.out.println("Cache MISSED! Attempting to get the client id");
				String id = component.getClientId(getFacesContext());
				// System.out.println("Got client id: " + id);
				componentIdRegistry.put(component, id);
				return id;
				// return "";
			} catch (Exception e) {
				return "";
			}
		}
	}

	public void registerDataSource (DataSource source) {
		dataSourceRegistry.put(source.hashCode(), source);
	}

	public Object sendConsoleCommand (String expression) {
		Object returnValue = null;
		FacesContext context = FacesContext.getCurrentInstance();
		ValueBinding value = context.getApplication().createValueBinding("#{" + expression + "}");
		if (value instanceof com.ibm.xsp.binding.ValueBindingEx) {
			((com.ibm.xsp.binding.ValueBindingEx) value).setComponent(this.getView());
			((com.ibm.xsp.binding.ValueBindingEx) value).setTransient(true);
		}
		returnValue = value.getValue(context);
		return returnValue;
	}

	public Object sendConsoleCommand (String expression, UIComponentBase contextComponent) {
		Object returnValue = null;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ValueBinding value = context.getApplication().createValueBinding("#{" + expression + "}");
			if (value instanceof com.ibm.xsp.binding.ValueBindingEx) {
				((com.ibm.xsp.binding.ValueBindingEx) value).setComponent(contextComponent);
				((com.ibm.xsp.binding.ValueBindingEx) value).setTransient(true);
			}
			returnValue = value.getValue(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public Object sendConsoleCommand (String expression, int componentId) {
		return this.sendConsoleCommand(expression, this.getComponent(componentId));
	}

	// @Deprecated
	// public String toJson () {
	// /*
	// * Restoring this method temporarily so I can test other stuff with an approach I know works until the new approach is working
	// */
	// System.out.println("Calling " + this.getClass().getName() + ".toJson()");
	// String output = "";
	// try {
	// MetaComponentNode meta = new MetaComponentNode();
	// meta.setNode(_root, this);
	// System.out.println("Done building meta structure. Attempting JSON serialization...");
	// // JsonJavaFactory factory = JsonJavaFactory.instance;
	// JSONObject json = meta.toJson(new JSONObject());
	// output = json.toJSONString();
	// System.out.println("Serialization complete.  Happy inspecting!");
	// // Logger.log("JSON output: "+output);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return output;
	// }

	public String toXML (PrintWriter s) {
		String output = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.newDocument();
			xml.setTextContent("<view/>");
			output = xml.getTextContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

}
