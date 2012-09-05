package org.domino.engine.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ibm.commons.extension.ExtensionManager;
import com.ibm.xsp.library.AbstractXspLibrary;
import com.jquery.library.JqueryFragment;

public class EngineLibrary extends AbstractXspLibrary {

	private List<JqueryFragment> fragments;

	public String getLibraryId() {
		return "org.domino.engine.library";
	}

	public String getPluginId() {
		return "org.domino.engine";
	}

	public boolean isDefault() {
		return true;
	}

	@Override
	public String[] getFacesConfigFiles() {
		String[] files = new String[] { "META-INF/labelBox-faces-config.xml",
				"META-INF/menu-faces-config.xml",
				"META-INF/jquery-faces-config.xml", };
		List<JqueryFragment> fragments = getJqueryFragments();
		for (JqueryFragment fragment : fragments) {
			files = fragment.getFacesConfigFiles(files);
		}
		return files;
	}

	@Override
	public String[] getXspConfigFiles() {
		String[] files = new String[] { "META-INF/labelBox.xsp-config",
				"META-INF/menu.xsp-config", "META-INF/jquery.xsp-config", };

		List<JqueryFragment> fragments = getJqueryFragments();
		for (JqueryFragment fragment : fragments) {
			files = fragment.getXspConfigFiles(files);
		}
		return files;
	}

	private List<JqueryFragment> getJqueryFragments() {
		if (fragments == null) {
			List<JqueryFragment> frags = ExtensionManager.findServices(null,
					JqueryFragment.class.getClassLoader(),
					JqueryFragment.EXTENSION_NAME, JqueryFragment.class);
			Collections.sort(frags, new Comparator<JqueryFragment>() {
				public int compare(JqueryFragment o1, JqueryFragment o2) {
					String className1 = null == o1 ? "null" : o1.getClass().getName(); //$NON-NLS-1$
					String className2 = null == o2 ? "null" : o2.getClass().getName(); //$NON-NLS-1$
					return className1.compareTo(className2);
				}
			});
			fragments = frags;
		}
		return fragments;
	}

	public void installResources() {
		List<JqueryFragment> fragments = getJqueryFragments();
		for (JqueryFragment fragment : fragments) {
			fragment.installResources();
		}
	}
}