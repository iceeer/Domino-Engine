package org.domino.engine.utility.emailreply;

import java.util.Date;
import java.util.Properties;
import javax.mail.internet.MimeMessage;

import org.domino.engine.Helper;
import org.domino.engine.utility.emailreply.configuration.EmailConfiguration;

import lotus.domino.Document;
import lotus.domino.NotesException;

/**
 * 
 */

/**
 * 邮件发送助理类
 * @author Administrator
 * 
 */
public class SendMailHelper {

	/**
	 * 
	 */
	private javax.mail.Session mailSession = null;

	/**
	 * 
	 */
	private javax.mail.Transport transport = null;

	/**
	 * 
	 */
	EmailConfiguration oEmailConfiguration = null;

	/**
	 * 
	 * @param session
	 * @throws NotesException
	 */
	public SendMailHelper() throws NotesException {
	}

	/**
	 * 
	 * @param session
	 */
	public SendMailHelper(lotus.domino.Session session) {
		this(session, null);
	}

	/**
	 * 
	 * @param session
	 * @param docEmailCfg
	 */
	public SendMailHelper(lotus.domino.Session session, Document docEmailCfg) {
		try {
			// 获得邮件审批配置对象
			if (docEmailCfg == null) {
				oEmailConfiguration = new EmailConfiguration();
			} else {
				oEmailConfiguration = new EmailConfiguration(docEmailCfg);
			}

			// 初始化邮件进程
			transport = initMailSession().getTransport();

			// 连接邮件服务器
			transport.connect(oEmailConfiguration.getSMTPUserName(),
					oEmailConfiguration.getSMTPPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private javax.mail.Session initMailSession() {
		System.out.println("START INIT MAIL SESSION");
		try {
			Properties properties = new Properties();
			// get properties from mail setting
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.smtp.host", oEmailConfiguration.getSMTPHost());
			// properties.put("mail.smtp.port", "2525");
			properties.put("mail.smtp.port", oEmailConfiguration.getSMTPPort());
			// properties.put("mail.smtp.auth", "true");

			javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(
							oEmailConfiguration.getSMTPUserName(),
							oEmailConfiguration.getSMTPPassword());
				}
			};

			// Session mailSession = Session.getDefaultInstance(properties,
			// authenticator);
			mailSession = javax.mail.Session.getInstance(properties,
					authenticator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("END INIT MAIL SESSION");
		return mailSession;
	}

	public boolean sendMail(MimeMessage mimeMessage) {
		System.out.println("START SEND MAIL");

		try {
			// MimeMessage mimeMessage = createMimeMessage(getMailSession());
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		System.out.println("END SEND MAIL");
		return true;
	}

	public javax.mail.internet.MimeMessage createMimeMessage(
			Document docEmailData) {
		javax.mail.internet.MimeMessage msg = null;
		try {
			msg = new javax.mail.internet.MimeMessage(getMailSession());
			// -- Set the FROM and TO fields --
			msg.setFrom(new javax.mail.internet.InternetAddress(
					oEmailConfiguration.getMailAddress()));// get email address
															// from mail cfg

			msg.setRecipients(javax.mail.Message.RecipientType.TO,
					javax.mail.internet.InternetAddress.parse(docEmailData
							.getItemValueString("SendMailReceiver"), false));

			// -- We could include CC recipients too --
			// if (cc != null)
			// msg.setRecipients(Message.RecipientType.CC
			// ,InternetAddress.parse(cc, false));
			// -- Set the subject and body text --
			msg.setSubject(docEmailData.getItemValueString("SendMailTitle"));
			msg.setText(docEmailData.getItemValueString("SendMailBody"));

			// -- Set some other header information --
			msg.setHeader("X-Mailer", "OAEmail");
			msg.setSentDate(new Date());

		} catch (javax.mail.internet.AddressException e) {
			e.printStackTrace();
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	public javax.mail.internet.MimeMessage createMimeMessage(
			String SendMailReceiver, String SendMailTitle, String SendMailBody) {
		javax.mail.internet.MimeMessage msg = null;
		try {
			msg = new javax.mail.internet.MimeMessage(getMailSession());
			// -- Set the FROM and TO fields --
			msg.setFrom(new javax.mail.internet.InternetAddress(
					oEmailConfiguration.getMailAddress()));// get email address
															// from mail cfg

			msg.setRecipients(javax.mail.Message.RecipientType.TO,
					javax.mail.internet.InternetAddress.parse(SendMailReceiver,
							false));

			// -- We could include CC recipients too --
			// if (cc != null)
			// msg.setRecipients(Message.RecipientType.CC
			// ,InternetAddress.parse(cc, false));
			// -- Set the subject and body text --
			msg.setSubject(SendMailTitle);
			msg.setText(SendMailBody);

			// -- Set some other header information --
			msg.setHeader("X-Mailer", "OAEmail");
			msg.setSentDate(new Date());

		} catch (javax.mail.internet.AddressException e) {
			e.printStackTrace();
		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	/**
	 * @return the mailSession
	 */
	public javax.mail.Session getMailSession() {
		return mailSession;
	}

	public void recycle() {
		try {
			if (transport != null) {
				transport.close();
			}
		} catch (Exception e) {
			Helper.logError(e);
		}
	}
}
