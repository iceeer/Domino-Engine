package org.domino.engine.database;

import org.domino.engine.Application;
import org.domino.engine.Engine;

import lotus.domino.DxlImporter;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.Stream;
import lotus.domino.View;

public class ViewDesign {
	private String viewName = "";

	

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public String getDxlFileName() {
		return "res\\dxl\\view";
	}

	

}
