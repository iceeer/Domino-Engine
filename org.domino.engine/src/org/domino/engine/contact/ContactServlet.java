package org.domino.engine.contact;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Session;

public class ContactServlet extends HttpServlet {
	/**
	 * Domino Application Servlet Demo code
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();

		try {
			Session session = NotesContext.getCurrent().getCurrentSession();
			out.print("Hello:" + session.getEffectiveUserName());
		} catch (Throwable t) {
			t.printStackTrace(out);
		}

		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		this.doGet(req, res);
	}

}
