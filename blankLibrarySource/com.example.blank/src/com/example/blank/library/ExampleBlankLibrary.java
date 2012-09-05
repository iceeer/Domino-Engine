package com.example.blank.library;

import com.ibm.xsp.library.AbstractXspLibrary;

public class ExampleBlankLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
		return "com.example.blank.library";
	}

	public String getPluginId() {
		return "com.example.blank";
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