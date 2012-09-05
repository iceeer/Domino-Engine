package org.domino.engine.labelbox;

import javax.faces.component.UIComponentBase;

public class UILabelBox extends UIComponentBase {

	public UILabelBox() {
		super();
		setRendererType("org.domino.engine.labelbox");
	}

	@Override
	public String getFamily() {
		return "org.domino.engine.container";
	}
}
