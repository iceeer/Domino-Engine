/**
 * 
 */
package org.domino.engine.utility.servlet;

import lotus.domino.NotesFactory;
import lotus.domino.Session;

import org.domino.engine.Helper;

import com.ibm.domino.xsp.module.nsf.NotesContext;

/**
 * @author admin
 *
 */
public class ServletHelper {
	
	/**
	 * 
	 * @return
	 */
	public static Session getSession() {
		return NotesContext.getCurrent().getCurrentSession();
	}
	
}
