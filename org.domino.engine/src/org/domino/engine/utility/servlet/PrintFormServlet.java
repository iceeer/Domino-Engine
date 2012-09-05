package org.domino.engine.utility.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;


import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Document;
import lotus.domino.Session;

public class PrintFormServlet extends HttpServlet {
	/**
	 * Domino Application Servlet Demo code
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
//		res.setContentType("text/html");
//		res.setHeader("Cache-Control", "no-cache");
		res.setCharacterEncoding("UTF-8");
//		res.setHeader("", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		
		PrintWriter out = res.getWriter();
		
		try {			
			req.setCharacterEncoding("UTF-8");
			String strDatabaseFileName = req.getParameter("db");
			String strUNID = req.getParameter("unid");
			if (Helper.ValidateNotEmpty(strDatabaseFileName)
					&& Helper.ValidateNotEmpty(strUNID)) {
				Session session = NotesContext.getCurrent().getCurrentSession();
				Document doc = DominoObjectHelper.getDocument(session, strDatabaseFileName,
						strUNID);
				//PrintForm printForm = new PrintForm(session, doc);

				//out.print(printForm.getHTML());
			} else {
				out.print("<error>param is wrong</error>");
			}

		} catch (Exception e) {
			e.printStackTrace(out);
		} finally {
			out.close();
		}

	}

}
