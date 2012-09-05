package com.gbs.labs.xsp.debug;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import com.ibm.xsp.component.xp.XspEventHandler;

public class EventHandler {
	private String	id;
	private String	event;
	private boolean	submit;
	private String	refreshMode;
	private String	refreshId;
	private boolean	immediate	= false;
	private String	methodExpression;
	private boolean	debug		= false;
	private String	onConmpleteExpression;
	private String	script;

	public EventHandler (String parentId, String parm_event, boolean parm_submit, String parm_refreshMode, String parm_refreshId,
			boolean parm_immediate, String parm_methodExpression, String parm_onCompleteExpression, String parm_script) {
		this.setId("event_" + parm_event + "_" + parentId);
		this.setEvent(parm_event);
		this.setSubmit(parm_submit);
		this.setRefreshMode(parm_refreshMode);
		this.setRefreshId(parm_refreshId);
		this.setImmediate(parm_immediate);
		this.setMethodExpression(parm_methodExpression);
		this.setOnCompleteExpression(parm_onCompleteExpression);
		this.setScript(parm_script);
	}

	private EventHandler setId (String parm_id) {
		this.id = parm_id;
		return this;
	}

	private EventHandler setEvent (String parm_event) {
		this.event = parm_event;
		return this;
	}

	private EventHandler setSubmit (boolean parm_submit) {
		this.submit = parm_submit;
		return this;
	}

	private EventHandler setRefreshMode (String parm_refreshMode) {
		this.refreshMode = parm_refreshMode;
		return this;
	}

	private EventHandler setRefreshId (String parm_refreshId) {
		this.refreshId = parm_refreshId;
		return this;
	}

	private EventHandler setImmediate (boolean parm_immediate) {
		this.immediate = parm_immediate;
		return this;
	}

	private EventHandler setMethodExpression (String parm_methodExpression) {
		this.methodExpression = parm_methodExpression;
		return this;
	}

	private EventHandler setOnCompleteExpression (String parm_onCompleteExpression) {
		this.onConmpleteExpression = parm_onCompleteExpression;
		return this;
	}

	private EventHandler setScript (String parm_script) {
		this.script = parm_script;
		return this;
	}

	public String getEvent () {
		return this.event;
	}

	public XspEventHandler generateXSP (UIComponent uiComponent) {

		MethodBinding methodBinding;
		XspEventHandler xspEventHandler = new XspEventHandler();
		xspEventHandler.setId(this.id);

		if (this.methodExpression != null) {
			methodBinding = FacesContext.getCurrentInstance().getApplication().createMethodBinding(this.methodExpression, null);
			if (methodBinding instanceof com.ibm.xsp.binding.MethodBindingEx) {
				((com.ibm.xsp.binding.MethodBindingEx) methodBinding).setComponent(uiComponent);

			} else {
				this.debug = true;
				if (debug)
					System.out.println("METHOD BINDING created for EventHandler " + this.event + " got type "
							+ methodBinding.getClass().getName());
				this.debug = false;
			}
			xspEventHandler.setAction(methodBinding);
		}

		/*
		 * ExecuteScriptAction action = new ExecuteScriptAction(); action.setComponent(xspEventHandler); action.setScript(methodBinding);
		 * xspEventHandler.setAction(action);
		 */
		xspEventHandler.setSubmit(this.submit);
		xspEventHandler.setEvent(this.event);
		if (this.script != null) {
			methodBinding = FacesContext.getCurrentInstance().getApplication().createMethodBinding(this.script, null);
			xspEventHandler.setScript(methodBinding);
		}

		if (this.refreshMode != null) {
			xspEventHandler.setRefreshMode(this.refreshMode);
			if (this.refreshId != null) {
				xspEventHandler.setRefreshId(this.refreshId);
			}
		}
		xspEventHandler.setImmediate(this.immediate);
		if (this.onConmpleteExpression != null) {
			xspEventHandler.setOnComplete(this.onConmpleteExpression);
		}
		return xspEventHandler;
	}

}