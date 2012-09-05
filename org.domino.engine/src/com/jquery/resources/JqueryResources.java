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

import java.util.IdentityHashMap;

import javax.faces.context.FacesContext;

import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.resource.Resource;
import com.jquery.util.JqueryUtil;

/**
 * Shared ExtLib Resources.
 * 
 * @author priand
 *
 */
public class JqueryResources {

    public static void addEncodeResources(FacesContext context, Resource[] resources) {
        UIViewRootEx rootEx = (UIViewRootEx)context.getViewRoot();
        addEncodeResources(rootEx,resources);
    }
    
    public static void addEncodeResources(UIViewRootEx rootEx, Resource[] resources) {
        if(resources!=null) {
            for(int i=0; i<resources.length; i++) {
                addEncodeResource(rootEx,resources[i]);
            }
        }
    }

    public static void addEncodeResource(FacesContext context, Resource resource) {
        UIViewRootEx rootEx = (UIViewRootEx)context.getViewRoot();
        addEncodeResource(rootEx,resource);
    }
    
    @SuppressWarnings("unchecked") // $NON-NLS-1$
    public static void addEncodeResource(UIViewRootEx rootEx, Resource resource) {
        if(JqueryUtil.isXPages852()) {
            IdentityHashMap<Resource, Boolean> m = (IdentityHashMap<Resource, Boolean>)rootEx.getEncodeProperty("extlib.EncodeResource"); // $NON-NLS-1$
            if(m==null) {
                m = new IdentityHashMap<Resource, Boolean>();
            } else {
                if(m.containsKey(resource)) {
                    return;
                }
            }
            m.put(resource, Boolean.TRUE);
        }
        rootEx.addEncodeResource(resource);
    }

    public static final String jqueryLocalJS = "/.ibmxspres/.jquery/jquery.min.js"; // $NON-NLS-1$
     
}