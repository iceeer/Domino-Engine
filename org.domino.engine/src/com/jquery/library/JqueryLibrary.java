/*
 * © Copyright IBM Corp. 2010
 * © Copyright Declan Sciolla-Lynch 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.jquery.library;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ibm.commons.extension.ExtensionManager;
import com.jquery.library.JqueryFragment;
import com.ibm.xsp.library.AbstractXspLibrary;

public class JqueryLibrary extends AbstractXspLibrary {

	private List<JqueryFragment> fragments;

	public JqueryLibrary() {
	}

	public String getLibraryId() {
        return "com.jquery.library";
    }

    public boolean isDefault() {
		return true;
	}

    @Override
	public String getPluginId() {
        return "com.jquery";
    }
    
    @Override
	public String[] getXspConfigFiles() {
        String[] files = new String[] {
                "META-INF/jquery.xsp-config",
            };
        List<JqueryFragment> fragments = getJqueryFragments();
        for( JqueryFragment fragment: fragments) {
        	files = fragment.getXspConfigFiles(files);
        }
        return files;
    }
    
    @Override
	public String[] getFacesConfigFiles() {
        String[] files = new String[] {
                "META-INF/jquery-faces-config.xml",
            };
        List<JqueryFragment> fragments = getJqueryFragments();
        for( JqueryFragment fragment: fragments) {
        	files = fragment.getFacesConfigFiles(files);
        }
        return files;
    }

    private List<JqueryFragment> getJqueryFragments() {
    	if(fragments==null) {
            List<JqueryFragment> frags = ExtensionManager.findServices(null,
                    JqueryFragment.class.getClassLoader(),
                    JqueryFragment.EXTENSION_NAME, 
                    JqueryFragment.class);
            Collections.sort(frags, new Comparator<JqueryFragment>() {
                public int compare(JqueryFragment o1, JqueryFragment o2) {
                    String className1 = null == o1? "null":o1.getClass().getName(); //$NON-NLS-1$
                    String className2 = null == o2? "null":o2.getClass().getName(); //$NON-NLS-1$
                    return className1.compareTo(className2);
                }
            });
            fragments = frags;
    	}
		return fragments;
	}
	
    public void installResources() {
        List<JqueryFragment> fragments = getJqueryFragments();
        for( JqueryFragment fragment: fragments) {
        	fragment.installResources();
        }
    }

}
