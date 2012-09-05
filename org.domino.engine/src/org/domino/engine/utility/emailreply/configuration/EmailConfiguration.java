/**
 * 
 */
package org.domino.engine.utility.emailreply.configuration;

import lotus.domino.Document;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.FormulaTool;
import org.domino.engine.utility.emailreply.EmailHelper;

import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * @author iceeer
 * 
 */
public class EmailConfiguration {

	/*
	 * 邮件配置文档对象
	 */
	private Document docEmailCfg = null;

	/**
	 * 默认构造函数
	 */
	public EmailConfiguration() {
		this.docEmailCfg = getFirstEmailConfiguration();
	}

	/**
	 * 默认构造函数
	 */
	public EmailConfiguration(String strCfgID) {
		// TODO 实现
	}

	/**
	 * 默认构造函数
	 */
	public EmailConfiguration(Document docEmailCfg) {
		this.docEmailCfg = docEmailCfg;
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	private static Document getFirstEmailConfiguration() {
		Document docCfg = null;

		try {
			View vwCfg = DominoObjectHelper.getView(Application.getSettingDB(),
					EmailHelper.VIEW_NAME_OF_ENABLE_EMAIL_CONFIGURATION);
			docCfg = vwCfg.getFirstDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return docCfg;
	}

	/**
	 * @return the POP3UserName
	 */
	public String getPOP3Host() {
		try {
			return docEmailCfg.getItemValueString("cfgPOP3Host");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the POP3Port
	 */
	public String getPOP3Port() {
		try {
			return docEmailCfg.getItemValueString("cfgPOP3Port");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the POP3UserName
	 */
	public String getPOP3UserName() {
		try {
			return docEmailCfg.getItemValueString("cfgPOP3UserName");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the POP3Password
	 */
	public String getPOP3Password() {
		try {
			return docEmailCfg.getItemValueString("cfgPOP3Password");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the SMTPUserName
	 */
	public String getSMTPUserName() {
		try {
			return docEmailCfg.getItemValueString("cfgSMTPUserName");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the SMTPPassword
	 */
	public String getSMTPPassword() {
		try {
			return docEmailCfg.getItemValueString("cfgSMTPPassword");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the SMTPHost
	 */
	public String getSMTPHost() {
		try {
			return docEmailCfg.getItemValueString("cfgSMTPHost");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @return the SMTPPort
	 */
	public String getSMTPPort() {
		try {
			return docEmailCfg.getItemValueString("cfgSMTPPort");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 是否需要验证邮箱地址
	 * @return
	 */
	public boolean needValidEmailAddress() {
		try {
			return (docEmailCfg.getItemValueString("cfgValidEmailAddress") == "yes");
		} catch (NotesException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	/**
	 * @return the MailAddress
	 */
	public String getMailAddress() {
		try {
			return docEmailCfg.getItemValueString("cfgMailAddress");
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获得邮件审批表单标识
	 */
	private String getMailFormConfigurationID(Document doc) {
		try {
			String strMailFormConfigurationIDFormula = docEmailCfg
					.getItemValueString("MailFormConfigurationID");
			return FormulaTool.getFormulaValue(Engine.getSession(),
					strMailFormConfigurationIDFormula, doc);
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获得邮件审批表单配置对象
	 */
	public MailFormConfiguration getMailFormConfiguration(Document doc) {
		MailFormConfiguration oMailFormConfiguration = null;
		try {
			String strMailFormConfigurationID = getMailFormConfigurationID(doc);
			
			if(strMailFormConfigurationID.equals("")){
				System.out.println("邮件审批表单配置标识为空");
			}else{
				View vwAllMailFormConfiguration = Engine.getSession().getCurrentDatabase().getView("vwAllMailFormConfiguration");
				Document docMailFormConfiguration = vwAllMailFormConfiguration.getDocumentByKey(strMailFormConfigurationID);
				if(docMailFormConfiguration!=null){
					oMailFormConfiguration = new MailFormConfiguration(docMailFormConfiguration);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oMailFormConfiguration;
	}

	/**
	 * 获得邮件接收工具类名
	 */
	public String getMailReceiveToolClassName(Document doc) {
		try {
			String strMailReceiveToolClassNameFormula = docEmailCfg
					.getItemValueString("cfgMailReceiveToolClassName");
			return FormulaTool.getFormulaValue(Engine.getSession(),
					strMailReceiveToolClassNameFormula, doc);
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获得邮件发送工具类名
	 */
	public String getMailSendToolClassName(Document doc) {
		try {
			String strMailSendToolClassNameFormula = docEmailCfg
					.getItemValueString("cfgMailSendToolClassName");
			return FormulaTool.getFormulaValue(Engine.getSession(),
					strMailSendToolClassNameFormula, doc);
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获得接收成功时调用代理名
	 */
	public String getMailReceiveSuccessAgentName(Document doc) {
		try {
			String strReceiveSuccessAgentNameFormula = docEmailCfg
					.getItemValueString("cfgReceiveSuccessAgentName");
			return FormulaTool.getFormulaValue(Engine.getSession(),
					strReceiveSuccessAgentNameFormula, doc);
		} catch (NotesException e) {
			e.printStackTrace();
			return "";
		}
	}
}
