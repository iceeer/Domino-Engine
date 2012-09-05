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
import org.domino.engine.utility.sso.SSOHelper;

/**
 * @author admin
 * 
 */
public class LogoutAction implements IIndexDo {

	/**
	 * 
	 */
	public LogoutAction() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.domino.engine.view.indexdo.IIndexDo#doAction()
	 */
	@Override
	public boolean doAction() {
		// TOOD some log
		try {
			HttpServletRequest req = XSPHelper.getRequest();
			HttpServletResponse res = XSPHelper.getResponse();
			String strRedirectURL = req.getParameter("redirectto");

			// out.println("Miss parameter username or password!");
			String strURL = XSPHelper.buildLogoutURL(strRedirectURL);
			if (Engine.isDebug()) {
				Helper.logMessage("The actual logout url is " + strURL);
			}

			if (SSOHelper.isEnableSSO()) {
				SSOHelper.cleanDominoCookie(req, res);// unright
				PrintWriter out = res.getWriter();
				out.println(HTMLHelper.removeCookieJS());
				out.println(HTMLHelper.getRedirectJS(strURL));
				// out.close();
			} else {
				res.sendRedirect(strURL);
			}

		} catch (Exception e) {
			Helper.logError(e);
		}
		return false;
	}

}
