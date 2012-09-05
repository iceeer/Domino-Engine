/**
 * 
 */
package org.domino.engine.utility.emailreply.configuration;


import lotus.domino.Document;

import org.domino.engine.Engine;
import org.domino.engine.utility.FormulaTool;
import lotus.domino.NotesException;

/**
 * @author iceeer
 * 
 */
public class MailFormRowConfiguration{

	/*
	 * 邮件行配置文档对象
	 */
	private Document docMailFormRowConfiguration = null;

	/**
	 * 默认构造函数
	 */
	public MailFormRowConfiguration() {
	}

	/**
	 * 默认构造函数
	 */
	public MailFormRowConfiguration(
			Document docMailFormRowConfiguration) {
		this.docMailFormRowConfiguration = docMailFormRowConfiguration;
	}

	/**
	 * @return the FormRowConfigurationID
	 */
	public String getFormRowConfigurationID() {
		try {
			return docMailFormRowConfiguration
					.getItemValueString("MailFormRowConfigurationID");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the FormRowContent
	 */
	public String getFormRowContent(Document doc) {
		try {
			String strFormRowContentFormula = docMailFormRowConfiguration
					.getItemValueString("MailFormRowContent");
			return FormulaTool.getFormulaValue(Engine.getSession(),
					strFormRowContentFormula, doc);
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	
}
