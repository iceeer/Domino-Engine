/*
 * � Copyright IBM Corp. 2010
 * � Copyright Declan Sciolla-Lynch 2011
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

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.resource.Resource;

public class JqueryRequestCustomizer implements RequestParameters.ResourceProvider {

	public static final JqueryRequestCustomizer instance = new JqueryRequestCustomizer();
	
	private static List<Resource> resources;
	
    public List<Resource> getResources(FacesContext context) throws IOException {
    	if(resources==null) {

    	}
    	return resources;
    }
}