package org.domino.engine.utility.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.html.HTMLHelper;
import org.domino.engine.utility.sso.SSOHelper;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Session;

/**
 * domino logout servet
 * 
 * @author admin
 * 
 */
public class LogoutServlet extends HttpServlet {
	/**
	 * Domino Application Servlet Demo code
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// TODO some log
		String strRedirectURL = req.getParameter("redirectto");

		// out.println("Miss parameter username or password!");
		String strURL = XSPHelper.buildLogoutURL(strRedirectURL);
		if (Engine.isDebug()) {
			Helper.logMessage("The actual logout url is " + strURL);
		}

		if (SSOHelper.isEnableSSO()) {
			SSOHelper.cleanDominoCookie(req, res);// unright
			PrintWriter out = res.getWriter();
			// out.println(HTMLHelper.removeCookieJS());
			out.println(HTMLHelper.getRedirectJS(strURL));
			out.close();
		} else {
			res.sendRedirect(strURL);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		this.doGet(req, res);
	}

}
