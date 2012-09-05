/**
 * 
 */
package org.domino.engine.utility.emailreply.configuration;

import org.apache.ecs.Element;
import org.apache.ecs.xhtml.table;
import org.apache.ecs.xhtml.tbody;

import lotus.domino.Document;
import lotus.domino.DocumentCollection;

import org.domino.engine.Engine;
import org.domino.engine.utility.FormulaTool;
import org.domino.engine.utility.emailreply.EmailHelper;

import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewEntryCollection;

/**
 * @author iceeer
 * 
 */
public class MailFormConfiguration {

	/*
	 * 邮件配置文档对象
	 */
	private Document docMailFormConfiguration = null;

	/**
	 * 默认构造函数
	 */
	public MailFormConfiguration() {
	}

	/**
	 * 默认构造函数
	 */
	public MailFormConfiguration(Document docMailFormConfiguration) {
		this.docMailFormConfiguration = docMailFormConfiguration;
	}

	/**
	 * @return the FormConfigurationID
	 */
	public String getFormConfigurationID() {
		try {
			return docMailFormConfiguration
					.getItemValueString("FormConfigurationID");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the FormTitle
	 */
	public String getFormTitle(Document doc) {
		try {
			String strFormTitleFormula = docMailFormConfiguration
					.getItemValueString("FormTitle");
			return FormulaTool.getFormulaValue(Engine.getSession(),
					strFormTitleFormula, doc);
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the Form Table Html
	 */
	public String getFormTableHTML(Document docRef) {
		String strHTML = "";
		try {
			View vwAllMailFormRowConfiguration = Engine.getSession()
					.getCurrentDatabase()
					.getView("vwAllMailFormRowConfiguration");
			vwAllMailFormRowConfiguration.setAutoUpdate(false);
			ViewEntryCollection vec = vwAllMailFormRowConfiguration
					.getAllEntriesByKey(getFormConfigurationID());
			if (vec.getCount() > 0) {
				Document doc;
				ViewEntry entry;
				ViewEntry tmpentry;

				table oTable = new table();
				oTable.addAttribute("align", "left");

				tbody oTBody = new tbody();
				oTable.addElement(oTBody);
				entry = vec.getFirstEntry();

				while (entry != null) {
					doc = entry.getDocument();
					MailFormRowConfiguration oMailFormRowConfiguration = new MailFormRowConfiguration(
							doc);
					oTBody.addElement(EmailHelper
							.buildOneLineDataTr(oMailFormRowConfiguration
									.getFormRowContent(docRef)));

					tmpentry = vec.getNextEntry();
					entry.recycle();
					doc.recycle();
					entry = tmpentry;

				}
				vwAllMailFormRowConfiguration.setAutoUpdate(true);
				strHTML = oTable.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strHTML;
	}

}
