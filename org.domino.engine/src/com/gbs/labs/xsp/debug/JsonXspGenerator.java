package com.gbs.labs.xsp.debug;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator.StringGenerator;
import com.ibm.xsp.actions.ActionGroup;
import com.ibm.xsp.complex.ValueBindingObject;
import com.ibm.xsp.component.UIDataPanelBase;
import com.ibm.xsp.component.UIIncludeComposite; //import com.ibm.xsp.component.UIPassThroughTag;
import com.ibm.xsp.component.UIPassThroughTag;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.component.UIPassThroughTag.TagAttribute;
import com.ibm.xsp.model.DataSource;

public class JsonXspGenerator extends StringGenerator {
	// private JsonXspFactory _factory;
	private boolean				petrify				= false;
	private XSPBugBean			_parentBean;
	private WrappedViewRoot		_currentView;
	private StringBuilder		_output				= new StringBuilder();
	private boolean				continueReflection	= true;
	private ArrayList<Object>	reflectionRegistry	= new ArrayList<Object>();
	// private HashMap<Object, String> reflectionRegistry = new HashMap<Object, String>();
	private HashMap<Class, Map>	_ronBurgundy		= new HashMap<Class, Map>();	// he's kind of a big deal
	private boolean				_selfDocument		= false;
	private int					_logOutputLevel		= 0;

	public JsonXspGenerator (XSPBugBean contextBean) {
		super(contextBean.getJsonFactory(), true);
		this._parentBean = contextBean;
	}

	public JsonXspFactory getFactory () {
		return getParent().getJsonFactory();
	}

	public XSPBugBean getParent () {
		return _parentBean;
	}

	public void setLogOutputLevel (int level) {
		_logOutputLevel = level;
	}

	@Override
	public void out (char c) throws IOException {
		// we're overriding the out() call because we want to put this in a variable that we have access to
		_output.append(c);
	}

	@Override
	public void out (String s) throws IOException {
		// we're overriding the out() call because we want to put this in a variable that we have access to
		_output.append(s);
	}

	public void setSelfDocument (boolean arg0) {
		_selfDocument = arg0;
	}

	public boolean getSelfDocument () {
		return _selfDocument;
	}

	public String toJson (WrappedViewRoot view) {
		_currentView = view;
		String result = "";
		_output = new StringBuilder();
		getView().getLogger().trace("Creating JSON output for view " + view.getId());
		try {
			outLiteral(_currentView.getView());
		} catch (Exception e) {
			// e.printStackTrace();
			getView().getLogger().error(e);
		}
		result = _output.toString();
		getView().getLogger().trace("Created string of " + result.length() + " characters");
		return result;
	}

	public String toJson (XspCommonsLogging logger) {
		_currentView = logger.getView();
		System.out.println("Log console requested for view " + getView().getId());
		_output = new StringBuilder();
		String result = "";
		try {
			outLiteral(logger);
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = _output.toString();
		return result;
	}

	// public String toJson (String viewId) throws IOException, JsonException {
	// String result = "";
	// _output = new StringBuilder();
	// _currentView = getParent().getViewById(viewId);
	// outLiteral(_currentView);
	// result = _output.toString();
	// return result;
	// }

	private WrappedViewRoot getView () {
		return _currentView;
	}

	@Override
	public void outLiteral (Object obj) throws IOException, JsonException {
		if (getFactory().isUIComponent(obj))
			outUIComponentLiteral(obj);
		else if (getFactory().isDatasource(obj))
			outDatasourceLiteral(obj);
		// else if (getFactory().isTagAtribute(obj))
		// outTagAttributeLiteral(obj);
		else if (getFactory().isLocale(obj))
			outLocaleLiteral(obj);
		else if (getFactory().isIterator(obj))
			outIteratorLiteral(obj);
		else if (getFactory().isValueBinding(obj))
			outValueBindingLiteral(obj);
		else if (getFactory().isDate(obj))
			outDateLiteral(obj);
		else if (getFactory().isLog(obj))
			outLog(obj);
		else if (getFactory().isConsoleEvent(obj))
			outConsoleEvent(obj);
		else if (getFactory().isStackTraceElement(obj))
			outStringLiteral(obj.toString());
		// This next bit is rather weak. We have to duplicate the branching from the super class
		// because we want to ultimately fail to our very generic reflection handler,
		// instead of just outputing NOTHING - which is probably why we're having so many JSON
		// syntax failures, now that I think about it!
		//
		// UPDATE: yes, that was exactly it. If we had a Map where the value was an unknown object,
		// the parent JsonGenerator would simply generate no output for the object. Thus we had JSON like
		// {"firstKey":"firstValue","secondKey":,"thirdKey":"thirdValue"}
		//
		// JSON validators get very cranky if there is simply NO value for the second property!
		//
		// UPDATE 2: And it turns out we needed to fix our iterator processor as well!
		else if (getFactory().isNull(obj))
			out("null");
		else if (getFactory().isString(obj))
			outStringLiteral(getFactory().getString(obj));
		else if (getFactory().isNumber(obj))
			outNumberLiteral(getFactory().getNumber(obj));
		else if (getFactory().isBoolean(obj))
			outBooleanLiteral(getFactory().getBoolean(obj));
		else if (getFactory().isObject(obj))
			outObject(obj);
		else if (getFactory().isArray(obj))
			outArrayLiteral(obj);
		else
			outGenericLiteral(obj);
	}

	public void outLog (Object obj) throws IOException, JsonException {
		XspCommonsLogging curLog = (XspCommonsLogging) obj;
		// The Map will consist of timestamp keys and console event values;
		if (this._logOutputLevel > 0) {
			outLiteral(curLog.getConsole(_logOutputLevel));
		} else {
			outLiteral(curLog.getConsole());
		}
	}

	public void outDateLiteral (Object obj) throws IOException, JsonException {
		Date curDate = (Date) obj;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		outLiteral(sdf.format(curDate));
	}

	public void outConsoleEvent (Object obj) throws IOException, JsonException {
		ConsoleEvent curEvent = (ConsoleEvent) obj;
		HashMap<String, Object> eventMap = new HashMap<String, Object>();
		eventMap.put("type", curEvent.getType());
		eventMap.put("message", curEvent.getMessage());
		eventMap.put("time", curEvent.getTime());
		if (curEvent.getStackTrace() != null) {
			eventMap.put("stackTrace", curEvent.getStackTrace());
		}
		outLiteral(eventMap);
	}

	public void outIteratorLiteral (Object obj) throws IOException, JsonException {
		Iterator<?> localIterator = (Iterator<?>) obj;
		if (!localIterator.hasNext()) {
			outLiteral("- EMPTY -");
		} else {
			for (Iterator<?> iterator = localIterator; localIterator.hasNext();) {
				outLiteral(iterator.next());
			}
		}
	}

	public void outGenericLiteral (Object obj) throws IOException, JsonException {
		getView().getLogger().debug("Attempting reflection on " + obj.getClass().getName());
		TreeMap<String, Object> outMap = new TreeMap<String, Object>();
		outMap.put("typeName", getSourceTag(obj));
		appendReflection(obj, outMap);
		try {
			if (outMap.isEmpty()) {
				outLiteral("- no properties discovered -");
			} else {
				outLiteral(outMap);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			getView().getLogger().error("Failed reflection on " + obj.getClass().getName(), e);
		}

	}

	public void outLocaleLiteral (Object obj) throws IOException, JsonException {
		getView().getLogger().debug("Attempting locale output");
		// Again, totally weak that we have to do an explicit handler, except that if we do reflection,
		// we're constantly getting the entire list of ISO countries and crap. And it does so recursively. :-/
		TreeMap<String, Object> outMap = new TreeMap<String, Object>();
		outMap.put("typeName", "Locale");
		Locale localLocale = (Locale) obj;
		outMap.put("country", localLocale.getCountry());
		outMap.put("displayName", localLocale.getDisplayName());
		outMap.put("language", localLocale.getLanguage());
		try {
			outLiteral(outMap);
		} catch (Exception e) {
			// e.printStackTrace();
			getView().getLogger().error("Failed locale output", e);
		}

	}

	public void outValueBindingLiteral (Object obj) throws IOException, JsonException {
		ValueBinding curVB = (ValueBinding) obj;
		outLiteral(curVB.getExpressionString());
	}

	public void outUIComponentLiteral (Object obj) {
		try {
			TreeMap<String, Object> componentMap = new TreeMap<String, Object>();
			componentMap.put("typeName", getSourceTag(obj));
			UIComponentBase curUI = (UIComponentBase) obj;
			getView().registerComponent(curUI); // we're maintaining a component registry so we'll be able to perform evals
			// against it from the plug later
			componentMap.put("getClientId", getView().getClientId(curUI));

			componentMap.put("serverId", curUI.getId());
			appendReflection(obj, componentMap);
			if (curUI instanceof UIDataPanelBase) {
				if (!((UIDataPanelBase) curUI).getData().isEmpty()) {
					componentMap.put("data", ((UIDataPanelBase) curUI).getData());
				}
			}
			if (curUI instanceof UIPassThroughTag) {
				// I really hate that we're doing a branch for getTagAttributes, but for some weird reason,
				// it's a List<XspTagAttributes> instead of just a Map<String, String>.
				// Even a Map<String, XspTagAttribute> would make more sense,
				// since the Attribute name itself is obviously the key.
				// Maybe this is another request for Philippe & Co.
				//
				// UPDATE: of course. The attribute and value can both be defined by EL. *sigh*
				List<TagAttribute> taList = ((UIPassThroughTag) curUI).getTagAttributes();
				if (taList != null && !taList.isEmpty()) {
					TreeMap<Object, Object> attribMap = new TreeMap<Object, Object>();
					for (TagAttribute curAttrib : ((UIPassThroughTag) curUI).getTagAttributes()) {
						Object name;
						Object value;
						try {
							name = curAttrib.getName();
						} catch (EvaluationException e) {
							// TODO: account for EL
							// There is no obvious way to discover the EL. XspTagAttribute doesn't have a getValueBinding method
							name = "- unknown EL -";
						} catch (Exception e) {
							name = e.getLocalizedMessage();
						}
						try {
							value = curAttrib.getValue();
						} catch (EvaluationException e) {
							value = "- unknown EL -";
						} catch (Exception e) {
							value = e.getLocalizedMessage();
						}
						attribMap.put(name, value);
					}
					componentMap.put("getTagAttributes", attribMap);
				}
			}
			if (!curUI.getFacets().isEmpty()) {
				componentMap.put("facets", curUI.getFacets());
			}
			if (!curUI.getChildren().isEmpty()) {
				componentMap.put("children", curUI.getChildren());
			}
			if (curUI instanceof UIViewRootEx2) {
				// System.out.println("Building output for " + ((UIViewRootEx2) curUI).getUniqueViewId());
				// System.out.println("clientId is " + getView().getClientId(curUI));
				// System.out.println("Number of children is " + curUI.getChildCount());

			}
			try {
				outLiteral(componentMap);
				if (curUI instanceof UIViewRootEx2) {
					// System.out.println("Building output for " + ((UIViewRootEx2) curUI).getUniqueViewId());
					// System.out.println("clientId is " + getView().getClientId(curUI));
					// System.out.println("Number of children is " + curUI.getChildCount());
					// System.out.println("length of output is " + _output.length());
				}
			} catch (Exception e) {
				System.out.println("Failed out literal on component map");
				// e.printStackTrace();
				getView().getLogger().error("Failed out literal on component map", e);
			}
		} catch (Exception e) {
			System.out.println("WHOA NELLY!!! WTF just happened!?!");
			// e.printStackTrace();
			getView().getLogger().error("WHOA NELLY!!! WTF just happened!?!", e);
		}

	}

	public void outDatasourceLiteral (Object obj) throws IOException, JsonException {
		TreeMap<String, Object> sourceMap = new TreeMap<String, Object>();
		sourceMap.put("typeName", getSourceTag(obj));
		DataSource curData = (DataSource) obj;
		sourceMap.put("serverId", curData.getVar());
		getView().registerDataSource(curData);
		// appendCommonValueBindings(curData, sourceMap);
		appendReflection(curData, sourceMap);
		outLiteral(sourceMap);
	}

	private String getSourceTag (Object parmObj) {
		String result = parmObj.getClass().getName();
		for (EXspClassTagMap curTag : EXspClassTagMap.values()) {
			if (curTag.getTag(parmObj) != null) {
				result = curTag.getTag(parmObj);
				break;
			}
		}
		if (result.equals("xc:custom")) {
			UIIncludeComposite curInclude = (UIIncludeComposite) parmObj;
			String ctrlSrc = curInclude.getPageName();
			ctrlSrc = ctrlSrc.replace(".xsp", "");
			ctrlSrc = ctrlSrc.substring(1);
			result = "xc:" + ctrlSrc;
		}
		return result;
	}

	private Object getMethodDescription (Method crystal) {
		TreeMap<String, Object> methodMap = new TreeMap<String, Object>();

		String[] parameterList = new String[crystal.getParameterTypes().length];
		for (int i = 0; i < parameterList.length; i++) {
			parameterList[i] = crystal.getParameterTypes()[i].getCanonicalName();
		}
		methodMap.put("parameters", parameterList);

		String[] throwList = new String[crystal.getExceptionTypes().length];
		for (int i = 0; i < throwList.length; i++) {
			throwList[i] = crystal.getExceptionTypes()[i].getCanonicalName();
		}
		methodMap.put("throws", throwList);

		String returnType = crystal.getReturnType() == null ? "void" : crystal.getReturnType().getName();
		methodMap.put("returns", returnType);

		String declaringClass = crystal.getDeclaringClass().getName();
		methodMap.put("from", declaringClass);

		return methodMap;
	}

	private Object getClassReflection (Class<?> sanDiego) {
		if (!_ronBurgundy.containsKey(sanDiego)) {
			TreeMap<String, Object> methodMap = new TreeMap<String, Object>();
			for (Method crystal : sanDiego.getMethods()) {
				methodMap.put(crystal.getName(), getMethodDescription(crystal));
			}
			_ronBurgundy.put(sanDiego, methodMap);
		}
		return _ronBurgundy.get(sanDiego);
	}

	public void appendReflection (Object obj, Map<String, Object> argMap) {
		// Wait, TCM references in the variable names? That's right, I went there.
		// http://www.thecrystalmethod.com for all your coding-music needs.
		String kennyJ = "";
		reflectionRegistry.add(obj);
		if (continueReflection) {
			try {
				for (Method crystal : obj.getClass().getMethods()) {
					Object scottyK;
					String bindingNameGuess = "";
					kennyJ = crystal.getName();
					if (kennyJ.startsWith("is") && kennyJ.length() > 3) {
						bindingNameGuess = crystal.getName().substring(2, 3).toLowerCase();
						bindingNameGuess += crystal.getName().substring(3);
					} else if (kennyJ.startsWith("get") && kennyJ.length() > 4) {
						bindingNameGuess = crystal.getName().substring(3, 4).toLowerCase();
						bindingNameGuess += crystal.getName().substring(4);
					}
					if (isMethodOK(crystal)) {
						try {
							scottyK = invokeMethod(obj, crystal);
							if (scottyK == null) {
								argMap.put(kennyJ, "");
							} else {
								// TODO: This is pretty weak. Because the JSON output seems to be highly sensitive when it's dealing with
								// certain kinds of inputs, we're going to inspect the results of the invokation a little bit first.
								// The first obvious item is that anything that's part of the base language like String, Integer, or Boolean
								// is pretty safe. So we'll just bypass any other checks and output that.
								if (scottyK.getClass().getName().startsWith("java.lang")) {
									argMap.put(kennyJ, scottyK);
								} else if (reflectionRegistry.contains(scottyK)) {
									// We also maintain a registry of what we've added to the output. This is because we walk the Children,
									// but children also have .getParent() and .getComponent() methods which return stuff higher up in the
									// tree.
									// Can you say infinite loop? Of course you can. And we don't want our debugger to start throwing
									// OutOfMemory errors, now do we?
									getView().getLogger().trace(
											"Prevented a feedback loop when invoking " + crystal.getName() + " on a "
													+ obj.getClass().getName());
								} else if (scottyK.getClass().getName().startsWith("com.gbs")) {
									// TODO: Yes, this is a special exception for GBS's specific packages
									// We should find a better way to handle this.
									reflectionRegistry.add(scottyK);
									argMap.put(kennyJ, scottyK.getClass().getName());
								} else if (scottyK.getClass().getName().startsWith("lotus.domino.local")) {
									// TODO: Let's not reflect on the internal classes for Domino.
									// That way lies madness!
									reflectionRegistry.add(scottyK);
									argMap.put(kennyJ, scottyK.getClass().getName());
								} else if (scottyK.getClass().getName().startsWith("com.ibm.jscript")) {
									// TODO: Apparently, we'll also get out of control with the jscript packages from IBM
									// as there seems to be a monster amount of internal interpretation that happens with them
									reflectionRegistry.add(scottyK);
									argMap.put(kennyJ, scottyK.getClass().getName());
								} else {
									reflectionRegistry.add(scottyK);
									// Note to readers: debugging a debugger SUCKS!
									// No, I don't really need all this stupid branching,
									// it's just that something somewhere is returning results
									// that generate JSON errors. GRRRRRRRRRRRRRRRRRRR!!
									if (scottyK instanceof Map) {
										if (!((Map) scottyK).isEmpty()) argMap.put(kennyJ, scottyK);
										// argMap.put(crystal.getName(), "MAP: " + result.getClass() + " " + ((Map) result).toString());
									} else if (scottyK instanceof Iterator) {
										argMap.put(kennyJ, scottyK);
										// argMap.put(crystal.getName(), "ITERATOR: " + result.getClass() + " " + ((Iterator)
										// result).toString());
									} else if (scottyK instanceof List) {
										if (!((List) scottyK).isEmpty()) argMap.put(kennyJ, scottyK);
										// argMap.put(crystal.getName(), "LIST: " + result.getClass() + " " + ((List) result).size());
									} else if (scottyK.getClass().isArray()) {
										argMap.put(kennyJ, scottyK);
										// argMap.put(crystal.getName(), "ARRAY: " + result.getClass() + " " + ((List) result).size());
									} else if (scottyK instanceof ActionGroup) {
										argMap.put(crystal.getName(), ((ActionGroup) scottyK).getActions());
										// if (getSelfDocument()) argMap.put("methods", this.getClassReflection(obj.getClass()));
									} else if (scottyK instanceof MethodBinding) {
										argMap.put(crystal.getName(), scottyK.toString());
										// if (getSelfDocument()) argMap.put("methods", this.getClassReflection(obj.getClass()));
									} else {
										// TODO: There are a lot of output results that generate JSON content that coughs up blood.
										// right now we've put those methods in the Exclude list. This is extremely suboptimal
										argMap.put(kennyJ, scottyK);
										// if (getSelfDocument()) argMap.put("methods", this.getClassReflection(obj.getClass()));
									}
								}
							}
						} catch (EvaluationException e) {
							// Odds are if we threw an error on attempting to invoke, it's because
							// we had a value binding that can no longer be resolved.
							// It'll get picked up automatically in our value binding checks.
							argMap.put(kennyJ, "- Linked to value binding -");
						} catch (Exception e) {
							argMap.put(kennyJ, e.getLocalizedMessage());
						}
					} else {
						getView().getLogger().trace("Ignoring METHOD: " + crystal.getName() + " in CLASS: " + obj.getClass().getName());
					}
					// We can only guess at the name used for the binding for now.
					// Perhaps we'll get some mechanism in the future to interrogate ALL the
					// ValueBindings for a given component.
					// Wouldn't THAT be a day?
					if (bindingNameGuess.length() > 0) {
						Object binding = getValueBinding(obj, bindingNameGuess);
						if (binding != null) {
							argMap.put("Binding for " + bindingNameGuess, binding);
						}
					}
				}
			} catch (Exception e) {
				// continueReflection = false;
				e.printStackTrace();
				getView().getLogger().error(
						"Error appending generic reflection on " + obj.getClass().getName() + " Last method attempted: " + kennyJ, e);
			}
		}

	}

	private ValueBinding getValueBinding (Object obj, String bindingName) {
		ValueBinding result = null;
		if (obj instanceof UIComponentBase) {
			result = ((UIComponentBase) obj).getValueBinding(bindingName);
		} else if (obj instanceof DataSource) {
			//result = ((DataSource) obj).getValueBinding(bindingName);
			result = ((ValueBindingObject) obj).getValueBinding(bindingName);
		}
		return result;
	}

	private Object invokeMethod (Object obj, Method crystal) throws Exception {
		Object result = null;
		try {
			result = crystal.invoke(obj, null);
		} catch (IllegalArgumentException e) {
			result = e.getMessage();
		} catch (IllegalAccessException e) {
			result = e.getMessage();
		} catch (InvocationTargetException e) {
			result = e.getMessage();
		}
		return result;
	}

	private boolean isMethodOK (Method crystal) {
		try {
			if (crystal.getReturnType() == null) return false;
			if (crystal.getReturnType().getName().equals("void")) return false;
			if (crystal.getParameterTypes().length > 0) return false;
			if (crystal.getName().startsWith("_")) return false;
			for (EExcludedMethods badName : EExcludedMethods.values()) {
				if (badName.toString().equals(crystal.getName())) return false;
			}
			return true;
		} catch (Exception e) {
			getView().getLogger().error("Error checking validity of method " + crystal.getName(), e);
			return false;
		}
	}

}
