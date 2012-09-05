package org.domino.engine.menu;

import javax.faces.component.UIComponentBase;

public class MenuControl extends UIComponentBase {

	public MenuControl() {
		super();
		setRendererType("org.domino.engine.menu");
	}

	@Override
	public String getFamily() {
		return "org.domino.engine.container";
	}
}