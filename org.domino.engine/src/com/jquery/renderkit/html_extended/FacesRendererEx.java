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

package com.jquery.renderkit.html_extended;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

import com.jquery.util.JqueryUtil;
import com.ibm.xsp.renderkit.FacesRenderer;
import com.ibm.xsp.util.JSUtil;

public abstract class FacesRendererEx extends FacesRenderer {

	protected Object getProperty(int prop) {
		return null;
	}

	public static final boolean DEBUG = JqueryUtil.isDevelopmentMode();
	
	protected void newLine(ResponseWriter w) throws IOException {
		JSUtil.writeln(w);
	}
	
	protected void newLine(ResponseWriter w, String comment) throws IOException {
		if(DEBUG && comment!=null) {
			w.writeComment(comment);
		}
		JSUtil.writeln(w);
	}	
}
