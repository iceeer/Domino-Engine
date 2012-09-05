package org.domino.engine.example.blank.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class ExampleBlankLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
		return "org.domino.engine.example.blank.library";
	}

	public String getPluginId() {
		return "org.domino.engine.example.blank";
	}

	@Override
	public String[] getFacesConfigFiles() {
		return new String[]{
				"META-INF/orangeBox-faces-config.xml",
		};
	}

	@Override
	public String[] getXspConfigFiles() {
		return new String[]{
				"META-INF/orangeBox.xsp-config",
		};
	}
}