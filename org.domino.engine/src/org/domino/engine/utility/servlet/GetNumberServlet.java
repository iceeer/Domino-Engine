package org.domino.engine.utility.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.domino.xsp.module.nsf.NotesContext;

import lotus.domino.Session;

import org.domino.engine.utility.number.NumberFactory;

public class GetNumberServlet extends HttpServlet {
    /**
     * Domino Application Servlet Demo code
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        Map mapParameter = req.getParameterMap();
        String strNumberConfID = req.getParameter("type");
        
        try { 
        	if(strNumberConfID.equals("")){
        		out.print("<error>Type param is wrong</error>");
        	}else{            	
                Session session = NotesContext.getCurrent().getCurrentSession(); 
                
                NumberFactory factory = new NumberFactory(session);
                
                out.print(factory.getNumber(strNumberConfID, mapParameter, true)); 
        	}

        } catch (Throwable t) { 
            t.printStackTrace(out); 
        } 

        out.close();
    }

}
