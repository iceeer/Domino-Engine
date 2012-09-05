package com.gbs.labs.xsp.debug;

import javax.faces.application.Application;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.resource.MetaDataResource;

public class Aegis extends UIOutput {
	private UIViewRootEx2	_currentView	= null;

	public Aegis () {
		// System.out.println("New AEGIS instance created!");
		XSPBugBean medusa = getMedusa();
		if (medusa != null) {
			medusa.registerViewRoot(getCurrentView());
			// medusa.getViewById(getCurrentView().getUniqueViewId()).refreshJson();
		} else {
			System.out.println("O NOES!!! Medusa is null!");
		}

		String ARRCode = "#{javascript:Medusa.getViewById(view.getUniqueViewId()).refreshJson();}";
		MethodBinding afterRenderResponse = getContextApp().createMethodBinding(ARRCode, null);
		getCurrentView().setAfterRenderResponse(afterRenderResponse);
		getCurrentView().addResource(getMetaResource());
	}

	public UIViewRootEx2 getCurrentView () {
		if (_currentView == null) {
			_currentView = (UIViewRootEx2) FacesContext.getCurrentInstance().getViewRoot();
		}
		return _currentView;
	}

	public XSPBugBean getMedusa () {
		XSPBugBean result = null;
		FacesContext context = FacesContext.getCurrentInstance();
		Object medusaChk = context.getApplication().getVariableResolver().resolveVariable(context, "Medusa");
		if (medusaChk instanceof XSPBugBean) {
			result = (XSPBugBean) medusaChk;
		}
		return result;
	}

	public Application getContextApp () {
		return FacesContext.getCurrentInstance().getApplication();
	}

	public MetaDataResource getMetaResource () {
		MetaDataResource result = new MetaDataResource();
		result.setName("xspviewid");
		ValueBinding contentBinding = getContextApp().createValueBinding("#{view.uniqueViewId}");
		result.setValueBinding("content", contentBinding);
		result.setComponent(getCurrentView());
		return result;
	}

}
