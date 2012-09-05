package org.domino.engine.utility.emailreply;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

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

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.emailreply.configuration.EmailConfiguration;

import lotus.domino.Agent;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * 
 */

/**
 * @author Administrator
 * 
 */
public class ReceiveMailTools {

	private javax.mail.Session mailSession = null;
	private Store store = null;
	private Folder folder = null;

	/**
	 * 是否开启调试，显示调试信息
	 */
	private boolean isDebug = false;

	/**
	 * 
	 */
	EmailConfiguration oEmailConfiguration = null;

	/**
	 * 
	 * @param session
	 * @throws NotesException
	 */
	public ReceiveMailTools() {
	}

	public boolean receiveMail() {
		try {
			oEmailConfiguration = new EmailConfiguration();

			// -- Get hold of a POP3 message store, and connect to it --
			store = initMailSession().getStore("pop3");
			store.connect(oEmailConfiguration.getPOP3UserName(),
					oEmailConfiguration.getPOP3Password());

			if (store.isConnected()) {

				// -- Try to get hold of the default folder --
				folder = store.getDefaultFolder();
				if (folder == null)
					throw new Exception("No default folder");
				// -- ...and its INBOX --
				folder = folder.getFolder("INBOX");
				if (folder == null)
					throw new Exception("No POP3 INBOX");
				// -- Open the folder for read only --
				// folder.open(Folder.READ_ONLY);
				// -- Open the folder for read only --
				folder.open(Folder.READ_WRITE);
				// -- Get the message wrappers and process them --
				Message[] msgs = folder.getMessages();
				for (int msgNum = 0; msgNum < msgs.length; msgNum++) {
					Message msg = msgs[msgNum];

					if (this.isDebug()) {
						printMessageFlagsInfo(msg);
					}

					String strMailSubject = msg.getSubject();
					int intMailSubjectLength = strMailSubject.length();
					if (intMailSubjectLength > 33) {// UNID LONG plus 1
						String strMailDataID = getMailDataID(strMailSubject);// 获得邮件审批数据标识
						Document docMailData = getMailDataDoc(strMailDataID);// 获得邮件审批数据文档
						if (this.isDebug()) {
							System.out
									.println("mail subject:" + strMailSubject);
							System.out.println("mail data id:" + strMailDataID);
						}
						if (docMailData != null) {
							// 判断是否有对应的邮件审批数据
							Address[] froms = msg.getFrom();
							if (froms.length == 1) {// 判断是否是一个人发出
								InternetAddress from = (InternetAddress) froms[0];
								String strSender = from.getAddress();
								if (validateSenderAddress(docMailData,
										strSender) == true) {// 判断发送人邮件地址是否有效
									String strMailBody = "";

									Object o = msg.getContent();
									// Grab the body content text
									if (o instanceof String) {
										strMailBody = (String) o;
									} else if (o instanceof Multipart) {
										// Make sure to cast to it's Multipart
										// derivative
										strMailBody = parseMultipart((Multipart) o);
									}

									docMailData.replaceItemValue(
											"ReplyMailTitle", strMailSubject);// 回复标题
									docMailData.replaceItemValue(
											"oriReplyMailBody", strMailBody);// 回复原内容
									strMailBody = doWithMailBody(strMailBody);
									docMailData.replaceItemValue(
											"ReplyMailBody", strMailBody);// 回复内容
									// docMailData.replaceItemValue("ReplyMailAttachment",
									// strMailSubject);//暂不处理邮件
									Date dtReceive = msg.getSentDate();
									docMailData.replaceItemValue(
											"ReplyMailDate", Engine
													.getSession()
													.createDateTime(dtReceive));// 回复时间
									docMailData.replaceItemValue(
											"ReplyMailAddress", strSender);// 回复邮件地址
									if (this.isDebug()) {
										System.out.println("mail body:"
												+ strMailBody);

										System.out.println("receive date:"
												+ dtReceive);
									}
									docMailData.save(true, false);

									Database dbApplication = null;

									String strDBName = docMailData
											.getItemValueString("refDocumentDBFileName");
									String strRefDocumentID = docMailData
											.getItemValueString("refDocumentUNID");
									String strRefDocumentDocumentAuthor = docMailData
											.getItemValueString("refDocumentDocumentAuthor");
									if (this.isDebug()) {
										System.out.println("Author:"
												+ strRefDocumentDocumentAuthor);
										System.out.println("ref document db:"
												+ strRefDocumentID);
										System.out.println("ref document unid:"
												+ strRefDocumentID);
									}

									dbApplication = Engine.getSession()
											.getDatabase(
													Engine.getSession()
															.getServerName(),
													strDBName);
									Document docRef = getDocumentByUNID(
											dbApplication, strRefDocumentID);

									if (docRef != null) {
										// 根据配置生成审批邮件接收对象
										Class tools = Class
												.forName(oEmailConfiguration
														.getMailReceiveToolClassName(docRef));
										Constructor conTools = tools
												.getConstructor(new Class[] { Session.class });
										MailReceiver oMailReceiver = (MailReceiver) conTools
												.newInstance(new Object[] { Engine
														.getSession() });

										oMailReceiver.setDebug(isDebug());
										if (oMailReceiver.receiveMail(
												docMailData, docRef, msg)) {
											Agent agtRun = null;
											String strAgentName = oEmailConfiguration
													.getMailReceiveSuccessAgentName(docRef);
											agtRun = dbApplication
													.getAgent(strAgentName);

											if (agtRun != null) {
												agtRun.run(docRef.getNoteID());
												agtRun.recycle();
												System.out
														.println("运行数据库["
																+ strDBName
																+ "]代理["
																+ strAgentName
																+ "]于文档["
																+ docRef.getUniversalID()
																+ "]");
											} else {
												Helper.logMessage("数据库["
														+ strDBName
														+ "]中不存在代理["
														+ strAgentName + "]");
											}

										}
									}
								}// end validateSenderAddress

							} else {
								System.out.println("the sender is not vaild");
							}

						} else {
							System.out.println("no mail data equal with email["
									+ strMailDataID + "]");
						}
					}// end 判断邮件标题长度
				}// end for

			} else {
				System.out.println("NOT CONNECT EMAIL POP3");
			}// end store.isConnected()

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 显示邮件标志信息
	 * 
	 * @param msg
	 * @throws MessagingException
	 */
	private void printMessageFlagsInfo(Message msg) throws MessagingException {
		if (msg.isSet(Flags.Flag.DELETED)) {
			System.out.println("Deleted");
		}
		if (msg.isSet(Flags.Flag.ANSWERED)) {
			System.out.println("Answered");
		}
		if (msg.isSet(Flags.Flag.DRAFT)) {
			System.out.println("Draft");
		}
		if (msg.isSet(Flags.Flag.FLAGGED)) {
			System.out.println("Marked");
		}
		if (msg.isSet(Flags.Flag.RECENT)) {
			System.out.println("Recent");
		}
		if (msg.isSet(Flags.Flag.SEEN)) {
			System.out.println("Read");
		}
		if (msg.isSet(Flags.Flag.USER)) {
			// We don't know what the user flags might be in advance
			// so they're returned as an array of strings
			String[] userFlags = msg.getFlags().getUserFlags();
			for (int j = 0; j < userFlags.length; j++) {
				System.out.println("User flag: " + userFlags[j]);
			}
		}
		if (msg.getFlags().contains(Flags.Flag.SEEN)) {
			System.out.println("SEEN");
		}
	}

	/**
	 * @param dbApplication
	 * @param strRefDocumentID
	 * @return
	 * @throws NotesException
	 */
	public Document getDocumentByUNID(Database dbApplication,
			String strRefDocumentID) {
		Document docRef = null;
		try {
			docRef = dbApplication.getDocumentByUNID(strRefDocumentID);
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return docRef;
	}

	/**
	 * 更具邮件数据标志获得邮件数据文档
	 * 
	 * @param strMailDataID
	 * @return
	 */
	private Document getMailDataDoc(String strMailDataID) {
		Document doc = null;
		try {
			if (strMailDataID.equals("")) {

			} else {
				lotus.domino.Database db = Engine.getSession()
						.getCurrentDatabase();
				lotus.domino.View view = db.getView("vwAllMailData");
				Vector v = new Vector();
				v.add(strMailDataID);
				doc = view.getDocumentByKey(v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
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

	/**
	 * @return the mailSession
	 */
	public javax.mail.Session getMailSession() {
		return mailSession;
	}

	/**
	 * 初始化邮件接收进程
	 * 
	 * @return
	 */
	private javax.mail.Session initMailSession() {
		// System.out.println("START INIT MAIL SESSION");
		try {
			Properties properties = new Properties();
			properties.put("mail.transport.protocol", "pop3");
			properties.put("mail.pop3.host", oEmailConfiguration.getPOP3Host());
			properties.put("mail.pop3.port", oEmailConfiguration.getPOP3Port());
			properties.setProperty("mail.pop3.disabletop", "true");

			javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(
							oEmailConfiguration.getPOP3UserName(),
							oEmailConfiguration.getPOP3Password());
				}
			};

			// Session mailSession = Session.getDefaultInstance(properties,
			// authenticator);
			mailSession = javax.mail.Session.getInstance(properties,
					authenticator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("END INIT MAIL SESSION");
		return mailSession;
	}

	/**
	 * 获得邮件正文第一行，忽略换行后的数据
	 * 
	 * @param strMailBody
	 * @return
	 */
	private String doWithMailBody(String strMailBody) {
		if (strMailBody.indexOf("\n") != -1) {
			System.out.println("Mail body has new line");
			return strMailBody.substring(0, strMailBody.indexOf("\n"));
		} else {
			return strMailBody;
		}
	}

	/**
	 * 获得正文内容
	 * 
	 * @param mPart
	 * @return
	 */
	private String parseMultipart(Multipart mPart) {

		try {
			// Loop through all of the BodyPart's
			for (int i = 0; i < mPart.getCount(); i++) {
				// Grab the body part
				BodyPart bp = mPart.getBodyPart(i);
				// Grab the disposition for attachments
				String disposition = bp.getDisposition();
				System.out.println("disposition : " + disposition);
				if (disposition != null
						&& (disposition.equals(BodyPart.ATTACHMENT))) {
					System.out.println("Mail have some attachment : ");

					DataHandler handler = bp.getDataHandler();
					System.out.println("file name : " + handler.getName());
					return "file name : " + handler.getName();
				} else if (disposition == null && bp instanceof MimeBodyPart) {// It's
					// not
					// an
					// attachment
					MimeBodyPart mbp = (MimeBodyPart) bp;

					// Check to see if we're in the screwy situation where
					// the message text is buried in another Multipart
					if (mbp.getContent() instanceof Multipart) {
						// Use recursion to parse the sub-Multipart
						return parseMultipart((Multipart) mbp.getContent());
					} else {
						// Time to grab and edit the body
						if (mbp.isMimeType("text/plain")) {
							// Grab the body containing the text version
							String body = (String) mbp.getContent();
							return body;

						} else if (mbp.isMimeType("text/html")) {
							// Grab the body containing the HTML version
							String body = (String) mbp.getContent();
							return body;
						}
					}
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 判断发送人邮件地址正确性
	 * 
	 * @param MailData
	 * @param strSender
	 * @return
	 */
	private boolean validateSenderAddress(Document MailData, String strSender) {
		if (this.oEmailConfiguration.needValidEmailAddress()) {// 是否需要验证地址
			boolean isValidate = false;
			try {
				String strOriSender = MailData
						.getItemValueString("SendMailReceiver");
				if (this.isDebug()) {
					System.out.println("sender:" + strSender);
					System.out.println("ori sender:" + strOriSender);
				}
				isValidate = (strOriSender.equals(strSender));
			} catch (NotesException e) {
				e.printStackTrace();
			}
			return isValidate;
		} else {
			return true;
		}
	}

	/**
	 * 从邮件标题获得邮件数据标志
	 * 
	 * @param strMailSubject
	 * @return
	 */
	private String getMailDataID(String strMailSubject) {
		String strMailDataID = "";
		try {
			String strTmpMailSubject = strMailSubject.replaceAll("\\s*", "");
			int iStart = strTmpMailSubject.lastIndexOf("[");
			int iEnd = strTmpMailSubject.lastIndexOf("]");
			if (this.isDebug()) {
				System.out.println("mail subject [ is in:" + iStart);
				System.out.println("mail subject ] is in:" + iEnd);
			}
			if ((iStart != -1) && (iEnd != -1)) {
				strMailDataID = strTmpMailSubject.substring(iStart + 1, iEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strMailDataID;
	}

	private String trimChar(char toTrim, String inString) {
		int from = 0;
		int to = inString.length();

		for (int i = 0; i < inString.length(); i++) {
			if (inString.charAt(i) != toTrim) {
				from = i;
				break;
			}
		}
		for (int i = inString.length() - 1; i >= 0; i--) {
			if (inString.charAt(i) != toTrim) {
				to = i;
				break;
			}
		}
		return inString.substring(from, to);
	}

	/**
	 * 
	 */
	public void recycle() {
		try {
			if (folder != null) {
				folder.close(true);
			}
			if (store != null) {
				store.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Helper.logError(e);
		}
	}

}
