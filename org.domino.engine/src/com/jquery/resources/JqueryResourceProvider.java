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

package com.jquery.resources;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.ibm.xsp.webapp.FacesResourceServlet;
import com.ibm.xsp.webapp.resources.BundleResourceProvider;
import com.jquery.library.JqueryActivator;
import com.jquery.util.JqueryUtil;

/**
 * Resources provider factory.
 * 
 * @author priand
 */
public class JqueryResourceProvider extends BundleResourceProvider {
    
    public static final String BUNDLE_RES_PATH = "/resources/web/jquery/";
    public static final String DWA_RES_PATH = "/resources/web/";
    
    public static final String EXTLIB_PREFIX = ".jquery";
    
    // Resource Path
    public static final String RESOURCE_PATH =    FacesResourceServlet.RESOURCE_PREFIX
                                                + JqueryResourceProvider.EXTLIB_PREFIX
                                                + "/";
    public static final String DOJO_PATH     =    FacesResourceServlet.RESOURCE_PREFIX
                                                + JqueryResourceProvider.EXTLIB_PREFIX;
    
    public JqueryResourceProvider() {
        super(JqueryActivator.instance.getBundle(),EXTLIB_PREFIX);
    }
    @Override
    protected boolean shouldCacheResources() {
        return !JqueryUtil.isDevelopmentMode();
    }

    @Override
    protected URL getResourceURL(HttpServletRequest request, String name) {
        String path = BUNDLE_RES_PATH+name;
        return JqueryUtil.getResourceURL(getBundle(), path);
    }
}