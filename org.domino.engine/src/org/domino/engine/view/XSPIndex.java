/**
 * 
 */
package org.domino.engine.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Document;
import lotus.domino.Session;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.report.BaseReport;
import org.domino.engine.utility.sso.LtpaToken;
import org.domino.engine.utility.sso.LtpaTokenConfig;
import org.domino.engine.view.indexdo.IIndexDo;

/**
 * @author admin
 * 
 */
public class XSPIndex {

	/**
	 * 
	 */
	public XSPIndex() {
	}

	public void load() {
		if (Engine.isDebug()) {
			Helper.logMessage("load XSPIndex");
		}
		
		String strDoAction = XSPHelper.readParameter("do");
		
		if(Helper.ValidateNotEmpty(strDoAction)){
			String strClassName = Engine.getProperty("do." + strDoAction);
			try{
				if(Engine.isDebug()){
					Helper.logMessage("int class:" + strClassName);
				}
				Class<IIndexDo> action = (Class<IIndexDo>) Class.forName(strClassName);
				Constructor<IIndexDo> conTools = action
						.getConstructor(new Class[] { });
				IIndexDo indexDo = (IIndexDo) conTools
						.newInstance(new Object[] { });
				indexDo.doAction();
			}catch(Exception e){
				PrintWriter out;
				try {
					out = XSPHelper.getResponse().getWriter();
					out.println(e);
				} catch (IOException e1) {
					Helper.logError(e1);
				}
			}
		}else{
			// Goto the configed index page
			XSPHelper.getXSPContext().redirectToPage(XSPHelper.getPageXspUrl(Engine.getProperty("index.page.name","/JobNeedTodoView.xsp")));
		}	
		
	}

}
