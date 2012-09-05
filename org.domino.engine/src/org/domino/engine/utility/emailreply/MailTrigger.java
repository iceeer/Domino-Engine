package org.domino.engine.utility.emailreply;


import java.lang.reflect.Constructor;
import org.domino.engine.Engine;
import org.domino.engine.utility.emailreply.configuration.EmailConfiguration;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;

/**
 * 
 */

/**
 * @author iceeer
 * 
 *         修改记录： 沈慧斌 20101027 类文件过大，将类细分成DocumentInfo的继承类
 */
public class MailTrigger {

	/**
	 * 当前进程对象
	 */
	private Session session = null;

	/**
	 * 是否开启调试，显示调试信息
	 */
	private boolean isDebug = false;

	/**
	 * 默认构造函数
	 */
	public MailTrigger() {
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public boolean doit(Document doc) {
		try {
			if (this.isDebug()) {// 显示触发文档（doc）信息
				System.out.println(doc.getUniversalID());
				System.out.println(doc
						.getItemValueString("refDocumentDBFileName"));
				System.out.println(doc.getItemValueString("refDocumentUNID"));
				System.out.println(doc.getItemValueString("EnableEmailReply"));
			}

			// 获得触发文档对应审批文档（docRef）
			String strRefDatabaseFileName = doc
					.getItemValueString("refDocumentDBFileName");
			String strRefDocumentUNID = doc
					.getItemValueString("refDocumentUNID");
			Database dbRef = Engine.getSession().getDatabase(
					Engine.getSession().getServerName(), strRefDatabaseFileName);// 对应审批文档所在数据库
			Document docRef = dbRef.getDocumentByUNID(strRefDocumentUNID);// 对应审批文档

			// 根据配置生成审批邮件发送对象
			EmailConfiguration oEmailConfiguration = new EmailConfiguration();
			Class tools = Class.forName(oEmailConfiguration
					.getMailSendToolClassName(docRef));
			Constructor conTools = tools
					.getConstructor(new Class[] { Session.class });
			MailSender oMailSender = (MailSender) conTools
					.newInstance(new Object[] { Engine.getSession() });

			oMailSender.setDebug(isDebug());
			oMailSender.doit(doc, docRef);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * @param isDebug
	 *            the isDebug to set
	 */
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	/**
	 * @return the isDebug
	 */
	public boolean isDebug() {
		return isDebug;
	}

}
