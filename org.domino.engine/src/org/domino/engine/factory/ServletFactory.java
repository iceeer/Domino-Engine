package org.domino.engine.factory;

import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.IServletFactory;
import com.ibm.designer.runtime.domino.adapter.ServletMatch;
import lotus.domino.Session;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.foundation.PropertyLoader;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class ServletFactory implements IServletFactory {

	private static final String DEFAULT_SERVLET_CLASS = "org.domino.engine.oa.servlet.HelloWorldServlet";
	private static final String DEFAULT_SERVLET_NAME = "It is a default servlet";

	private ComponentModule module;

	/**
	 * 
	 */
	public void init(ComponentModule module) {
		this.module = module;
	}

	/**
	 * 
	 */
	public ServletMatch getServletMatch(String contextPath, String path)
			throws ServletException {
		//System.out.println("contextPath:" + contextPath);
		//System.out.println("path:" + path);

		String servletPath = "";
		if (path.equals("")) {
			// TODO path错误提示
		} else {
			String strLookupName = path.substring(path.lastIndexOf("/") + 1);
			if (strLookupName.equals("")) {
				// TODO strLookupName错误提示
			} else {

				// 获得Servet类名
				String strServletClass = Helper.getProperty("servlet.class."
						+ strLookupName, DEFAULT_SERVLET_CLASS);
				String strServletName = Helper.getProperty("servlet.name."
						+ strLookupName, DEFAULT_SERVLET_NAME);
				
				// 创建Servet对象
				Servlet servlet = module.createServlet(strServletClass,
						strServletName, null);

				return new ServletMatch(servlet, servletPath, path);
			}
		}

		return null;
	}
}
