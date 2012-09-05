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
import org.domino.engine.utility.sso.SSOHelper;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Session;

/**
 * domino login servet
 * @author admin
 *
 */
public class LoginServlet extends HttpServlet {
	/**
	 * Domino Application Servlet Demo code
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		//TOOD some log
		PrintWriter out = res.getWriter();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String strRedirectURL = req.getParameter("redirectto");
		if (username == null || password == null) {
			out.println("Miss parameter username or password!");
		} else {
			if (username.trim().equals("admin")
					&& password.trim().equals("admin")) {
				out.println("Login Success");
			} else
				out.println("Login Failure");
		}

		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		this.doGet(req, res);
	}

}
