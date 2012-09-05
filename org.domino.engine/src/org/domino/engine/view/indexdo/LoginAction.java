/**
 * 
 */
package org.domino.engine.view.indexdo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.XSPHelper;

/**
 * @author admin
 * 
 */
public class LoginAction implements IIndexDo {

	/**
	 * 
	 */
	public LoginAction() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.domino.engine.view.indexdo.IIndexDo#doAction()
	 */
	@Override
	public boolean doAction() {
		try {
			//TOOD some log
			HttpServletRequest req = XSPHelper.getRequest();
			HttpServletResponse res = XSPHelper.getResponse();
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String strRedirectURL = req.getParameter("redirectto");
			if (username == null || password == null) {
				// out.println("Miss parameter username or password!");
				String strURL = XSPHelper.buildLoginURL(strRedirectURL);
				if (Engine.isDebug()) {
					Helper.logMessage("The actual login url is " + strURL);
				}

				res.sendRedirect(strURL);
			} else {
				// TODO ?
				// if (username.trim().equals("admin")
				// && password.trim().equals("admin")) {
				// out.println("Login Success");
				// } else
				// out.println("Login Failure");
			}
			return true;
		} catch (Exception e) {
			Helper.logError(e);
		}
		return false;
	}

}
