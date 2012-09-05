/**
 * 
 */
package org.domino.engine.view.indexdo;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.html.HTMLHelper;
import org.domino.engine.utility.log.ScopeMessage;
import org.domino.engine.utility.sso.SSOHelper;

/**
 * @author admin
 * 
 */
public class MessageAction implements IIndexDo {

	/**
	 * 
	 */
	public MessageAction() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.domino.engine.view.indexdo.IIndexDo#doAction()
	 */
	@Override
	public boolean doAction() {
		try {
			HttpServletRequest req = XSPHelper.getRequest();
			HttpServletResponse res = XSPHelper.getResponse();
			res.setContentType("text/html");
			
			PrintWriter out = res.getWriter();
			out.println(ScopeMessage.buildDisplayHTML());

		} catch (Exception e) {
			Helper.logError(e);
		}
		return false;
	}

}
