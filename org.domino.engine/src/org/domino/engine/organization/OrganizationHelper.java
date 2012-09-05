/**
 * 
 */
package org.domino.engine.organization;

import lotus.domino.Database;

import org.domino.engine.Application;

/**
 * @author admin
 *
 */
public class OrganizationHelper {
	
	/**
	 * 
	 * @return
	 */
	public String getDBPath(){
		return Application.getApplicationDBPath();
	}
	
	/**
	 * 
	 * @return
	 */
	public Database getDB(){
		return Application.getApplicationDB();
	}
	
	/**
	 * 
	 * @return
	 */
	public Database getFullDB(){
		return Application.getFullApplicationDB();
	}
}
