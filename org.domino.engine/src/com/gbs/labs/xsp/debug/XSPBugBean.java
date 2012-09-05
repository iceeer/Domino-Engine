package com.gbs.labs.xsp.debug;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;

import lotus.domino.NotesException;
import lotus.domino.Session;

//import com.gbs.labs.dynamicdata.eventHandlers.EventHandler;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.xsp.component.UIPassThroughTag;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.component.xp.XspDiv;
import com.ibm.xsp.component.xp.XspEventHandler;
import com.ibm.xsp.component.xp.XspOutputLabel;
import com.ibm.xsp.component.xp.XspOutputLink;
import com.ibm.xsp.component.xp.XspSection;
import com.ibm.xsp.component.xp.XspTable;
import com.ibm.xsp.component.xp.XspTableCell;
import com.ibm.xsp.component.xp.XspTableRow;

public class XSPBugBean {

	private HashMap<String, WrappedViewRoot>	viewRootHandles			= new HashMap<String, WrappedViewRoot>();
	private HashMap<Integer, UIComponentBase>	componentRegistry		= new HashMap<Integer, UIComponentBase>();
	private HashMap<String, UIComponentBase>	propertiesPaneRegistry	= new HashMap<String, UIComponentBase>();
	private JsonXspGenerator					_jsonGenerator;
	private int									currentLogLevel;

	public XSPBugBean () {
		System.out.println("Created new XSPBugBean");
	}

	public Collection<WrappedViewRoot> getActiveViews () {

		/*
		 * Cleans out all null view handles and returns the resulting value collection
		 */
		HashMap<String, WrappedViewRoot> activeViews = new HashMap<String, WrappedViewRoot>();
		Iterator<WrappedViewRoot> iter = viewRootHandles.values().iterator();
		while (iter.hasNext()) {
			WrappedViewRoot view = iter.next();
			if (view.isValid()) {
				activeViews.put(view.getId(), view);
			}
		}
		viewRootHandles = activeViews;
		return activeViews.values();
	}

	public UIViewRootEx2 getCurrentView () {
		return (UIViewRootEx2) FacesContext.getCurrentInstance().getViewRoot();
	}

	public String getCurrentViewId () {
		return this.getCurrentView().getUniqueViewId();
	}

	public JsonXspFactory getJsonFactory () {
		return JsonXspFactory.instance;
	}

	public JsonXspGenerator getJsonGenerator () {
		if (_jsonGenerator == null) {
			_jsonGenerator = new JsonXspGenerator(this);
		}
		return _jsonGenerator;
	}

	public String viewToJson (String viewId) {
		return getViewById(viewId).getJson();
	}

	// public String viewToJson (String viewId, boolean selfDocument) {
	// String result;
	// result = getViewById(viewId).getJson(selfDocument);
	// return result;
	// }

	public String viewToJson (WrappedViewRoot view) {
		// System.out.println("converting view to JSON" + view.getId());
		String result = "";
		try {
			result = getJsonGenerator().toJson(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// public String viewToJson (WrappedViewRoot view, boolean selfDocument) {
	// String result = "";
	// try {
	// getJsonGenerator().setSelfDocument(selfDocument);
	// result = getJsonGenerator().toJson(view);
	// getJsonGenerator().setSelfDocument(false);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	public WrappedViewRoot getViewById (String viewId) {
		return viewRootHandles.get(viewId);
	}

	// public void log (String output) {
	// // this.getViewById(this.getCurrentViewId()).getConsole().log(output);
	// this.log(output, true);
	// }
	//
	// public void log (String output, boolean includeTrace) {
	// this.getViewById(this.getCurrentViewId()).getLogger().log(output, includeTrace);
	// }

	public void setCurrentLogLevel (int currentLogLevel) {
		this.currentLogLevel = currentLogLevel;
	}

	public XspCommonsLogging getLog () {
		XspCommonsLogging logger = this.getViewById(this.getCurrentViewId()).getLogger();
		logger.setCurrentLogLevel(this.currentLogLevel);
		return logger;
	}

	public boolean registerViewRoot (UIViewRootEx2 view) {
		// System.out.println("Registering view root " + view.getUniqueViewId());
		boolean result = false;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			WrappedViewRoot root = new WrappedViewRoot(context, view, this);
			viewRootHandles.put(view.getUniqueViewId(), root);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public UIComponentBase renderNode (UIComponentBase node, UIComponentBase parentNode) {
		int childCount = node.getChildCount();
		UIPassThroughTag li = new UIPassThroughTag();
		li.setTag("li");
		parentNode.getChildren().add(li);
		UIComponentBase domNode;
		if (childCount > 0) {
			XspSection childrenSection = new XspSection();
			domNode = childrenSection;
			childrenSection.setHeader(node.getClass().getName());
			childrenSection.setClosed(true);
			li.getChildren().add(childrenSection);
			XspDiv container = new XspDiv();
			childrenSection.getChildren().add(container);
			UIPassThroughTag ul = new UIPassThroughTag();
			ul.setTag("ul");
			container.getChildren().add(ul);
			for (int i = 0; i < childCount; i++) {
				this.renderNode((UIComponentBase) node.getChildren().get(i), container);
			}
		} else {
			XspOutputLink nodeLink = new XspOutputLink();
			domNode = nodeLink;
			nodeLink.setText(node.getClass().getName());
			li.getChildren().add(nodeLink);
		}
		componentRegistry.put(domNode.hashCode(), node);
		XspEventHandler eventHandler = new EventHandler(domNode.getId(), "onclick", true, "partial", "nodeProperties", true,
				"#{javascript:XSPBugBean.renderNodeProperties(" + domNode.hashCode() + ");}", null, null).generateXSP(domNode);
		eventHandler.setExecMode("partial");
		eventHandler.setOnStart("window.parent.XSPBug.highlightNode(\"" + node.getClientId(FacesContext.getCurrentInstance()) + "\");");
		domNode.getChildren().add(eventHandler);
		return node;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public UIComponentBase renderNodeProperties (int hashCode) {
		// System.out.println("Loading properties for node " + hashCode);
		UIComponentBase node = componentRegistry.get(hashCode);
		// System.out.println("Loading properties for node of type  "
		// + node.getClass().getName());
		try {
			UIComponent propertiesPane = propertiesPaneRegistry.get(((UIViewRootEx2) FacesContext.getCurrentInstance().getViewRoot())
					.getUniqueViewId());
			propertiesPane.getChildren().clear();
			XspTable propertyTable = new XspTable();
			propertyTable.setStyleClass("propertyGrid");
			propertiesPane.getChildren().add(propertyTable);
			this.renderNodeProperty("Component Type:", node.getClass().getName(), propertyTable);
			// this.renderNodeProperty("Rendered:", node.isRendered(), propertyTable);
			this.renderNodeProperty("ID:", node.getId(), propertyTable);
			this.renderNodeProperty("Client ID:", node.getClientId(FacesContext.getCurrentInstance()), propertyTable);
			this.renderNodeProperty("Renderer Type:", node.getRendererType(), propertyTable);
			if (node instanceof UIInput) {
				if (node.getValueBinding("value") != null) {
					this.renderNodeProperty("Value:", node.getValueBinding("value").getExpressionString(), propertyTable);
				} else {
					this.renderNodeProperty("Value:", ((UIInput) node).getValue(), propertyTable);
				}
			} else if (node instanceof UIOutput) {
				if (node.getValueBinding("value") != null) {
					this.renderNodeProperty("Value:", node.getValueBinding("value").getExpressionString(), propertyTable);
				} else {
					this.renderNodeProperty("Value:", ((UIOutput) node).getValue(), propertyTable);
				}
			}
		} catch (EvaluationException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return node;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	private void renderNodeProperty (String propertyName, Object propertyValue, XspTable propertyTable) {
		XspTableRow propertyRow = new XspTableRow();
		XspTableCell propertyLabelCell = new XspTableCell();
		propertyRow.getChildren().add(propertyLabelCell);
		XspOutputLabel propertyLabel = new XspOutputLabel();
		propertyLabel.setValue(propertyName);
		propertyLabelCell.getChildren().add(propertyLabel);
		XspTableCell propertyValueCell = new XspTableCell();
		propertyRow.getChildren().add(propertyValueCell);
		XspOutputLabel propertyValueLabel = new XspOutputLabel();
		if (propertyValue == null) {
			propertyValueLabel.setValue("null");
		} else {
			propertyValueLabel.setValue(propertyValue.toString());
		}
		propertyValueCell.getChildren().add(propertyValueLabel);
		propertyTable.getChildren().add(propertyRow);
	}

	@Deprecated
	public UIComponentBase renderView (String viewId, UIComponentBase parentNode, UIComponentBase propertiesPane, boolean includeViewNode) {
		propertiesPaneRegistry.put(((UIViewRootEx2) FacesContext.getCurrentInstance().getViewRoot()).getUniqueViewId(), propertiesPane);
		WrappedViewRoot view = viewRootHandles.get(viewId);
		return this.renderView(view, parentNode, includeViewNode);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public UIComponentBase renderView (WrappedViewRoot view, UIComponentBase parentNode, boolean includeViewNode) {
		UIPassThroughTag li = new UIPassThroughTag();
		li.setTag("li");
		parentNode.getChildren().add(li);
		XspDiv container = new XspDiv();
		if (includeViewNode) {
			XspSection childrenSection = new XspSection();
			childrenSection.setHeader(view.getId());
			childrenSection.setClosed(true);
			li.getChildren().add(childrenSection);
			childrenSection.getChildren().add(container);
		} else {
			li.getChildren().add(container);
		}
		for (int i = 0; i < view.getChildCount(); i++) {
			this.renderNode(view.getChildren().get(i), (container));
		}
		return parentNode;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public UIComponentBase renderViews (UIComponentBase parentNode, UIComponentBase propertiesPane) {
		// Logger.log("Calling " + this.getClass().getName() + ".renderViews()");
		propertiesPaneRegistry.put(((UIViewRootEx2) FacesContext.getCurrentInstance().getViewRoot()).getUniqueViewId(), propertiesPane);
		UIPassThroughTag ul = new UIPassThroughTag();
		ul.setTag("ul");
		parentNode.getChildren().add(ul);
		Iterator<WrappedViewRoot> views = this.getActiveViews().iterator();
		while (views.hasNext()) {
			WrappedViewRoot view = views.next();
			this.renderView(view, ul, true);
		}
		return parentNode;
	}

}
