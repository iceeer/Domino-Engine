package org.domino.engine.example.blank.component.orangebox;

import javax.faces.component.UIComponentBase;

public class UIOrangeBox extends UIComponentBase {

	public UIOrangeBox() {
		super();
		setRendererType("com.example.blank.OrangeBox");
	}

	@Override
	public String getFamily() {
		return "com.example.blank.container";
	}
}
