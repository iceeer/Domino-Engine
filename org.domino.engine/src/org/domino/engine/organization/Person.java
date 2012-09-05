/**
 * 
 */
package org.domino.engine.organization;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * @author Administrator
 * 
 */
public class Person {

	/**
	 * 
	 */
	private String strPersonID = "";

	/**
	 * 
	 */
	private String strPersonName = "";

	/**
	 * 
	 */
	private String strSSOUserName = "";

	/**
	 * 
	 */
	public Person() {
	}

	/**
	 * 
	 */
	public static Person getPersonByDocument(Document docPerson) {
		Person p = new Person();

		try {
			if (docPerson != null) {
				p.setPersonID(docPerson.getItemValueString("PersonID"));
				p.setPersonName(docPerson.getItemValueString("PersonName"));
				p.setSSOUserName(docPerson.getItemValueString("SSOUserName"));

			}

		} catch (NotesException e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * 
	 */
	public static Person getPersonByID(String strPersonID) {

		Document docPerson = getPersonDoc(strPersonID);

		return getPersonByDocument(docPerson);

	}

	/**
	 * 
	 */
	public static Person getPersonBySSOUserName(String strSSOUserName) {
		
		Document docPerson = null;
		try {
			docPerson = DominoObjectHelper.getDocumentByKey(Application.getFullApplicationDB(),"vwAllPersonBySSOUserName",strSSOUserName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getPersonByDocument(docPerson);
	}

	/**
	 * 
	 */
	public Document getPersonDoc() {
		return getPersonDoc(this.getPersonID());

	}
	
	/**
	 * 
	 */
	public static Document getPersonDoc(String strPersonID) {

		Document docPerson = null;
		try {
			docPerson = DominoObjectHelper.getDocumentByKey(Application.getFullApplicationDB(),"vwAllPersonByPersonID",strPersonID);
		} catch (Exception e) {
			Helper.logError(e);
		}

		return docPerson;

	}
	
	/**
	 * @param strPersonID
	 *            the strPersonID to set
	 */
	public void setPersonID(String strPersonID) {
		this.strPersonID = strPersonID;
	}

	/**
	 * @return the strPersonID
	 */
	public String getPersonID() {
		return strPersonID;
	}

	/**
	 * 
	 * @return
	 * @throws NotesException
	 */
	public String getPersonName() {
		return strPersonName;
	}

	/**
	 * 
	 * @param setPersonName
	 */
	public void setPersonName(String setPersonName) {
		this.strPersonName = setPersonName;
	}

	/**
	 * @param strSSOUserName
	 *            要设置的 strSSOUserName
	 */
	public void setSSOUserName(String strSSOUserName) {
		this.strSSOUserName = strSSOUserName;
	}

	/**
	 * @return strSSOUserName
	 */
	public String getSSOUserName() {
		return strSSOUserName;
	}

}
