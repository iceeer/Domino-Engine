package org.domino.engine.utility.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

public class RedirectServlet extends HttpServlet {
	/**
	 * Domino Application Servlet Demo code
	 */
	private static final long serialVersionUID = 1L;

	private static final String DOC_ID_MAP_VIEW_NAME = "vwMapID";

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();

		String strDBFileName = req.getParameter("db");// 数据库地址参数
		String strDocumentID = req.getParameter("id");// 文档标识参数
		String strNumber = req.getParameter("number");// 文档编号参数

		try {
			Session session = NotesContext.getCurrent().getCurrentSession();
			
			if (strDBFileName.equals("")) {

			} else {
				Database db = session.getDatabase(session.getServerName(),
						strDBFileName);
				if (db != null) {
					
					if ((strNumber != null)&&(!(strNumber.equals("")))) {
						View vw = db.getView("vwAllContractByNumber");
						if (vw != null) {
							Document doc = vw.getDocumentByKey(strNumber);
							if (doc != null) {
								String strURL = getDatabaseURL(db) + "/0/" + doc.getUniversalID() + "?opendocument";
								res.sendRedirect(strURL);
							} else {
								res.sendRedirect("/oa.nsf/xpPrintMessage.xsp?msgtype=error&msg=" + "文档不存在或您无权查看该文档");
							}
						} else {
							out.println("VIEW IS NOT EXIST:"
									+ "vwAllContractByNumber");
						}
					}else if (strDocumentID.equals("")) {

					} else {
						View vw = db.getView(DOC_ID_MAP_VIEW_NAME);
						if (vw != null) {
							Document doc = vw.getDocumentByKey(strDocumentID);
							if (doc != null) {
								String strURL = getDatabaseURL(db) + "/" + DOC_ID_MAP_VIEW_NAME + "/" + strDocumentID + "?opendocument";
								res.sendRedirect(strURL);
							} else {
								res.sendRedirect("/oa.nsf/xpPrintMessage.xsp?msgtype=error&msg=" + "文档不存在或您无权查看该文档");
							}
						} else {
							out.println("VIEW IS NOT EXIST:"
									+ DOC_ID_MAP_VIEW_NAME);
						}
					}
				} else {
					out.println("DB IS NOT EXIST:" + strDBFileName);
				}
			}

		} catch (Throwable t) {
			t.printStackTrace(out);
		}

		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		this.doGet(req, res);
	}
	
	/**
	 * 
	 * @param db
	 * @return
	 */
	public String getDatabaseURL(Database db) throws NotesException{
		String strURL = "";
		strURL = "/" + db.getFilePath().replace('\\', '/');
		
		return strURL;
	}

}
