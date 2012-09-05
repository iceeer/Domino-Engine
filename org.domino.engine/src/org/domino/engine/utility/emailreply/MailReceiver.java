package org.domino.engine.utility.emailreply;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataContentHandler;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.domino.engine.Engine;
import org.domino.engine.utility.emailreply.configuration.EmailConfiguration;

import lotus.domino.Agent;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;

/**
 * 
 */

/**
 * @author Administrator
 * 
 */
public class MailReceiver {

	/**
	 * 是否开启调试，显示调试信息
	 */
	private boolean isDebug = false;

	/**
	 * 
	 */
	private EmailConfiguration oEmailConfiguration = null;

	/**
	 * 
	 * @param session
	 * @throws NotesException
	 */
	public MailReceiver() {
	}


	/**
	 * 
	 * @param session
	 * @param docEmailCfg
	 */
	public MailReceiver(Document docEmailCfg) {
		try {
			// 获得邮件审批配置对象
			if (docEmailCfg == null) {
				setEmailConfiguration(new EmailConfiguration());
			} else {
				setEmailConfiguration(new EmailConfiguration(
						docEmailCfg));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean receiveMail(Document docMailData, Document docRef,
			Message msg) {
		boolean result = false;
		try {

			boolean isT = false;// 邮件审批人是否是当前处理人
			boolean blNeedJCZH = false;// 是否需要解除暂缓

			String strSender = docMailData
					.getItemValueString("SendMailReceiver");//邮件发送地址
			String strRefDocumentDocumentAuthor = docMailData
					.getItemValueString("refDocumentDocumentAuthor");//审批邮件数据中的处理人
			String strMailBody = docMailData
					.getItemValueString("ReplyMailBody");//邮件回复内容

			// CHECK ref document stat
			String strISZH = docRef.getItemValueString("fldISZH");//是否暂缓状态
			Vector vDocumentAuthor = docRef.getItemValue("DocumentAuthors");//审批文档当前处理人

			// 判断暂缓人是不是邮件审批人
			String strZHpeople = "";
			Vector vZHPeople = docRef.getItemValue("fldZHPeople");
			if (vZHPeople.size() > 0) {
				for (int i = 0; i < vZHPeople.size(); i++) {
					strZHpeople = (String) vZHPeople.elementAt(i);
					if (docMailData.getItemValueString(
							"refDocumentDocumentAuthor").equals(strZHpeople)) {
						blNeedJCZH = true;
						strISZH = "";
					}
				}
			}

			//判断处理人是否有效
			if (vDocumentAuthor.size() > 0) {
				for (int i = 0; i < vDocumentAuthor.size(); i++) {
					// System.out.println(vDocumentAuthor.elementAt(i));
					if (docMailData.getItemValueString(
							"refDocumentDocumentAuthor").equals(
							(String) vDocumentAuthor.elementAt(i))) {
						isT = true;
					}
				}
			}
			if (!isT) {// 如果邮件审批人不是当前处理人
				// log
				SendMailHelper tool = new SendMailHelper();
				docMailData.appendItemValue("MailDataWorkLog",
						EmailHelper.ERROR_CHANGE_NAME);
				docMailData.save(true, false);
				javax.mail.internet.MimeMessage mmsg = tool.createMimeMessage(
						strSender, docRef.getItemValueString("fldSubject")
								+ " 审批结果", "[处理人已变更]");
				tool.sendMail(mmsg);
				msg.setFlag(Flags.Flag.DELETED, true);//删除邮件
			} else if (strISZH.equals("yes")) {
				SendMailHelper tool = new SendMailHelper();
				docMailData.appendItemValue("MailDataWorkLog", "文档被["
						+ strZHpeople + "]暂缓");
				docMailData.save(true, false);
				javax.mail.internet.MimeMessage mmsg = tool.createMimeMessage(
						strSender, docRef.getItemValueString("fldSubject")
								+ " 审批结果", "文档被[" + strZHpeople + "]暂缓");
				tool.sendMail(mmsg);
				msg.setFlag(Flags.Flag.DELETED, true);//删除邮件
			} else {

				
				docRef.replaceItemValue("TriggerUserName",
						strRefDocumentDocumentAuthor);

				// 解除暂缓状态
				if (blNeedJCZH) {
					docRef.replaceItemValue("fldISZH", "no");
					docRef.replaceItemValue("fldZHPeople", "");
					// TODO 记录解除暂缓流程记录
				}
				
				//填写意见
				lotus.domino.RichTextItem rtitem = null;
				if (docRef.hasItem("fldAttitude")) {
					Item item = docRef.getFirstItem("fldAttitude");

					if (item.getType() == Item.RICHTEXT) {
						rtitem = (lotus.domino.RichTextItem) item;
					} else {
						item.remove();
						rtitem = docRef.createRichTextItem("fldAttitude");
					}
				} else {
					rtitem = docRef.createRichTextItem("fldAttitude");
				}
				rtitem.appendText(strMailBody);
				
				//保存审批文档
				docRef.save(true, false);

				msg.setFlag(Flags.Flag.DELETED, true);//删除邮件
				result = true;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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

	public void setEmailConfiguration(EmailConfiguration oEmailConfiguration) {
		this.oEmailConfiguration = oEmailConfiguration;
	}

	public EmailConfiguration getEmailConfiguration() {
		return oEmailConfiguration;
	}

}
