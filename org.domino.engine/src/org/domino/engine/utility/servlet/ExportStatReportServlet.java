package org.domino.engine.utility.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Session;

import org.domino.engine.utility.report.ReportFactory;

public class ExportStatReportServlet extends HttpServlet {
	/**
	 * Domino Application Servlet Demo code
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();

		try {
			Session session = NotesContext.getCurrent().getCurrentSession();
			
			response.setHeader("Cache-Control", "no-cache");
			//response.setContentType("application/vnd.ms-excel;charset=utf-8");
			//response.setHeader("Content-Disposition", "attachment; filename=sampleName.xls");
			out.print("中文");
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
