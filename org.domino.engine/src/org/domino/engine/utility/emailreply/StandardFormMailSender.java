package org.domino.engine.utility.emailreply;

import java.util.Date;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.emailreply.MailSender;
import org.domino.engine.utility.emailreply.SendMailHelper;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * @author iceeer
 * 
 */
public class StandardFormMailSender extends MailSender{


	/**
	 * 强制发送判断值，"1"就是强制发送
	 */
	String strEnableEmailReply = "0";

	/**
	 * 默认构造函数
	 */
	public StandardFormMailSender() {
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public boolean doit(Document doc, Document docRef) {
		try {
			

			strEnableEmailReply = doc.getItemValueString("EnableEmailReply");// 是否强制发送

			Database dbEmailData = Application.getFullApplicationDB();// 邮件审批数据所在数据库

			// 获得要发送审批邮件的人员
			Vector vDocumentAuthor = docRef.getItemValue("DocumentAuthors");
			if (doc.getItemValueString("RestrictReceiver").trim().equals("")) {
			} else {// 如果限定了发送人员，则取限定人员
				vDocumentAuthor = doc.getItemValue("RestrictReceiver");
			}

			if (vDocumentAuthor.size() > 0) {// 判断审批文档处理人域
				SendMailHelper tool = new SendMailHelper(Engine.getSession());
				for (int i = 0; i < vDocumentAuthor.size(); i++) {

					String strDocumentAuthor = (String) vDocumentAuthor
							.elementAt(i);
					String strMailReceiver = getExtEmailByName(strDocumentAuthor);
					if ((!checkEMailAutoReplyIsEnable(strDocumentAuthor)) // 判断处理人是否开启了邮件审批
							|| (strMailReceiver.trim().equals(""))) {// 判断处理人是否设置了邮件审批用的外部邮件地址
						doc.replaceItemValue("TriggerStat", "3");// 设置触发文档状态为忽略
						String strErrorLog = strDocumentAuthor + "未设置外部邮件";// 设置错误日记信息
						if (this.isDebug()) {
							System.out.println(strErrorLog);
						}
						// 记录日记
						if (doc.hasItem("TriggerLog")) {
							Item item = doc.getFirstItem("TriggerLog");
							Vector v = new Vector();
							v.addElement(strErrorLog);
							item.appendToTextList(v);
						} else {
							doc.replaceItemValue("TriggerLog", strErrorLog);
						}
						doc.save();// 保存触发文档
					} else {// 处理人启用了邮件审批
						// 创建邮件审批数据
						Document docEmailData = dbEmailData.createDocument();
						docEmailData.replaceItemValue("Form", "fmMailData");
						docEmailData.appendItemValue("MailDataID", docEmailData
								.getUniversalID());
						docEmailData.appendItemValue("refDocumentDBFileName",
								doc
								.getItemValueString("refDocumentDBFileName"));// 对应数据库地址
						docEmailData.appendItemValue("refDocumentUNID",
								doc
								.getItemValueString("refDocumentUNID"));// 对应文档UNID
						docEmailData.appendItemValue(
								"refDocumentDocumentAuthor", strDocumentAuthor);// 对应文档处理人
						String strMailTitle = docRef
								.getItemValueString("fldSubject")
								+ "审批[" + docEmailData.getUniversalID() + "]";// 审批邮件标题
						docEmailData.appendItemValue("SendMailTitle",
								strMailTitle);
						String strMailBody = this.getDocContentHTML(null, docRef);// 审批邮件内容
						docEmailData.appendItemValue("SendMailBody",
								strMailBody);
						docEmailData.appendItemValue("SendMailReceiver",
								strMailReceiver);// 接收人
						docEmailData.appendItemValue("SendMailDate",
								Engine.getSession().createDateTime(new Date()));// 发送时间

						// 判断邮件正文是否为空，如果为空无需发送邮件
						if ((Helper.ValidateNotEmpty(strMailBody))
								&& (!(strMailBody
										.equals("<fieldset></fieldset>")))) {// 邮件正文为空
							
							javax.mail.internet.MimeMessage msg = tool
									.createMimeMessage(docEmailData);// 创建邮件对象
							Multipart mp = new MimeMultipart();// 创建邮件部件对象

							// 设置邮件正文
							MimeBodyPart part = new MimeBodyPart();// 创建邮件部件正文
							part.setDataHandler(new DataHandler(new String(
									strMailBody.getBytes(), "GBK"),
									"text/html;charset=gb2312"));// 中文特殊处理
							mp.addBodyPart(part);

							// 设置邮件附件
							if (docRef.hasEmbedded()) {// 判断审批文档是否有附件
								// Create and fill first part
								// MimeBodyPart p2 = new MimeBodyPart();
								// p2.setText("请查看附件");
								// mp.addBodyPart(p2);

								Vector v = null;
								if (this.isDebug() == true) {
									System.out
											.println("\""
													+ docRef
															.getItemValueString("fldSubject")
													+ "\" has embedded objects.");
								}
								v = docRef.getEmbeddedObjects();
								if (v.isEmpty()) {
									if (this.isDebug() == true) {
										System.out
												.println("\tEmbedded object is an attachment.");// 显示文档嵌入对象是否为附件
									}
									Vector vAttachmentNames = docRef
											.getItemValue("AttachmentNames");

									for (int j = 0; j < vAttachmentNames.size(); j++) {// 判断附件数量
										String strAttachmentName = (String) vAttachmentNames
												.elementAt(j);
										if (this.isDebug() == true) {
											System.out
													.println(strAttachmentName);// 显示附件名
										}

										EmbeddedObject eo = docRef
												.getAttachment(strAttachmentName);
										doWithEmbeddedObject(docEmailData, mp,
												eo);
									}
								} else
									for (int j = 0; j < v.size(); j++) {
										EmbeddedObject eo = (EmbeddedObject) v
												.elementAt(j);
										doWithEmbeddedObject(docEmailData, mp,
												eo);
									}
							}
							msg.setHeader("Content-Transfer-Encoding",
									"Quoted-Printable");// QP编码
							// Set Multipart as the message's content
							msg.setContent(mp);

							tool.sendMail(msg);// 发送审批邮件
						}
						docEmailData.save(true, false);// 保存邮件审批数据
					}
				}
				tool.recycle();
			}// END 判断审批文档处理人域

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 获得个人设置中设置的外部邮箱地址
	 * 
	 * @param strDocumentAuthor
	 * @return
	 */
	private String getExtEmailByName(String strDocumentAuthor) {
		String strMail = "";
		try {
			Session session = NotesFactory.createSession();
			Database db = session.getDatabase(session.getServerName(),
					"OA\\Utility\\PersonProfile.nsf");
			View view = db.getView("vwAllPersonProfileByID");
			Document doc = view.getDocumentByKey(strDocumentAuthor, true);
			if (doc != null)
				strMail = doc.getItemValueString("ExtMail");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strMail;
	}

	/**
	 * 获得个人设置中设置的外部邮箱地址
	 * 
	 * @param strDocumentAuthor
	 * @return
	 */
	private boolean checkEMailAutoReplyIsEnable(String strDocumentAuthor) {
		boolean IsEnable = false;

		if (strEnableEmailReply.equals("1")) {// 判断是否强制发送
			IsEnable = true;
		} else {

			try {
				Session session = NotesFactory.createSession();
				Database db = session.getDatabase(session.getServerName(),
						"OA\\Utility\\PersonProfile.nsf");
				View view = db.getView("vwAllPersonProfileByID");
				Document doc = view.getDocumentByKey(strDocumentAuthor, true);
				if (doc != null) {
					IsEnable = ((doc
							.getItemValueString("EMailAutoReplyIsEnable"))
							.equals("1"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return IsEnable;
	}

	/**
	 * 
	 * @param docEmailData
	 * @param mp
	 * @param eo
	 * @throws NotesException
	 * @throws MessagingException
	 */
	private void doWithEmbeddedObject(Document docEmailData, Multipart mp,
			EmbeddedObject eo) throws NotesException, MessagingException {
		if (this.isDebug()) {
			System.out.println("\t" + eo.getName() + " of " + eo.getClassName()
					+ " type:" + eo.getType());
		}
		if (eo.getType() == EmbeddedObject.EMBED_ATTACHMENT) {
			String strFileName = eo.getName();
			String strFilePath = Helper.GetTempDirectory() + strFileName;
			eo.extractFile(strFilePath);
			setFileAsAttachment(mp, strFilePath);
			EmailHelper.attachFileToRichTextItem(docEmailData, "SendMailAttachment",
					strFilePath, strFileName);
		}
	}

	/**
	 * Set a file as an attachment. Uses JAF FileDataSource.
	 * 
	 * @param mp
	 * @param filename
	 * @throws MessagingException
	 */
	public static void setFileAsAttachment(Multipart mp, String filename)
			throws MessagingException {

		// Create second part
		MimeBodyPart p2 = new MimeBodyPart();

		// Put a file in the second part
		FileDataSource fds = new FileDataSource(filename);
		p2.setDataHandler(new DataHandler(fds));
		p2.setFileName(fds.getName());
		sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
		p2
				.setFileName("=?GBK?B?" + enc.encode(fds.getName().getBytes())
						+ "?=");

		mp.addBodyPart(p2);
	}

	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 */
	public String getDocContentHTML(Document docEmailCfg, Document docRef) {
		String strHTML = "";
		try {
			strHTML = super.getDocContentHTML(docEmailCfg, docRef);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.isDebug()) {
			Helper.logMessage(strHTML, docRef);
			System.out.println(strHTML);
		}
		return strHTML;
	}


}
