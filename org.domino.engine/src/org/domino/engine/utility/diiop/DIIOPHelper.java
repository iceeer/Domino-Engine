/**
 * 
 */
package org.domino.engine.utility.diiop;

import lotus.domino.NotesError;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

import org.domino.engine.Engine;
import org.domino.engine.utility.DominoObjectHelper;

/**
 * @author admin
 * 
 */
public class DIIOPHelper {
	public static String getStrHostName() {
		return Engine.getProperty("host.name");
	}

	public static String getStrDIIOPPort() {
		return Engine.getProperty("host.diiop.port");
	}

	public static String getStrUser() {
		return Engine.getProperty("host.user");
	}

	public static String getStrPwd() {
		return Engine.getProperty("host.pwd");
	}

	/**
	 * 获得Domino进程
	 * 
	 * @param strHostName
	 * @param strDIIOPPort
	 * @param strUser
	 * @param strPwd
	 * @return Domino进程
	 */
	public static Session createSession(String strUser, String strPwd) {
		return createSession(getStrHostName(), getStrDIIOPPort(), strUser,
				strPwd);
	}

	/**
	 * 获得Domino进程
	 * 
	 * @return
	 */
	public static Session createSession() {
		return createSession(getStrHostName(), getStrDIIOPPort(), getStrUser(),
				getStrPwd());
	}

	/**
	 * 获得Domino进程
	 * 
	 * @return
	 */
	public static Session createSessionAsSigner() {
		return createSession(getStrHostName(), getStrDIIOPPort(), getStrUser(),
				getStrPwd());
	}

	/**
	 * 获得Domino进程
	 * 
	 * @return
	 */
	public static Session createSessionAsSignerWithFullAccess() {
		return createSession(getStrHostName(), getStrDIIOPPort(), getStrUser(),
				getStrPwd());
	}

	/**
	 * 根据用户名、密码通过DIIOP创建Domino进程
	 * 
	 * @param strHostName
	 * @param strDIIOPPort
	 * @param strUser
	 * @param strPwd
	 * @return Domino进程
	 */
	public static Session createSession(String strHostName,
			String strDIIOPPort, String strUser, String strPwd) {
		try {
			return NotesFactory.createSession(strHostName + ":" + strDIIOPPort,
					strUser, strPwd);
		} catch (NotesException e) {
			String dominoErrorText = e.text;
			int dominoErrorID = e.id;

			switch (dominoErrorID) {
			case NotesError.NOTES_ERR_INVALID_USERNAME_PASSWD:
				// Invalid password.
				System.out.println("This password, " + strPwd
						+ ", is not valid");
				break;
			case NotesError.NOTES_ERR_INVALID_USERNAME:
				// Invalid username.
				System.out.println("This user, " + strUser + ", is not valid");
				break;
			case NotesError.NOTES_ERR_SERVER_ACCESS_DENIED:
				// Access denied.
				System.out
						.println("This user, "
								+ strUser
								+ ", is not authorized to open DIIOP connections with the Domino server.  Check your DIIOP configuration.  NotesException - "
								+ dominoErrorID + " " + dominoErrorText);
				break;
			case NotesError.NOTES_ERR_GETIOR_FAILED:
				// Could not get IOR from Domino Server.
				System.out
						.println("Unable to open a DIIOP connection with "
								+ strHostName
								+ ":"
								+ strDIIOPPort
								+ ".  Make sure the DIIOP and HTTP tasks are running on the Domino server, and that ports are open.  NotesException - "
								+ dominoErrorID + " " + dominoErrorText + ".");
				break;
			default:
				// Unexpected error. Show detailed message.
				System.out.println("NotesException - " + dominoErrorID + " "
						+ dominoErrorText);
				e.printStackTrace(System.out);
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	/**
	 * 根据ltpaToken通过DIIOP创建Domino进程
	 * 
	 * @param strHostName
	 * @param strDIIOPPort
	 * @param strUser
	 * @param strPwd
	 * @return Domino进程
	 */
	public static Session createSession(String strHostName,
			String strDIIOPPort, String ltpaTokenString) {
		try {
			return NotesFactory.createSession(strHostName + ":" + strDIIOPPort,
					ltpaTokenString);
		} catch (NotesException e) {
			String dominoErrorText = e.text;
			int dominoErrorID = e.id;

			switch (dominoErrorID) {
			case NotesError.NOTES_ERR_SSOTOKEN_EXP:
				// Single Sign-on Token has expired.
				System.out.println("NotesException - " + dominoErrorID + " "
						+ dominoErrorText);
				break;
			case NotesError.NOTES_ERR_SERVER_ACCESS_DENIED:
				// Access denied.
				System.out
						.println("This user is not authorized to open DIIOP connections with the Domino server.  Check your DIIOP configuration.  NotesException - "
								+ dominoErrorID + " " + dominoErrorText);
				break;
			case NotesError.NOTES_ERR_GETIOR_FAILED:
				// Could not get IOR from Domino Server.
				System.out
						.println("Unable to open a DIIOP connection with "
								+ strHostName
								+ ":"
								+ strDIIOPPort
								+ ".  Make sure the DIIOP and HTTP tasks are running on the Domino server, and that ports are open.  NotesException - "
								+ dominoErrorID + " " + dominoErrorText + ".");
				break;
			default:
				// Unexpected error. Show detailed message.
				System.out.println("NotesException - " + dominoErrorID + " "
						+ dominoErrorText);
				e.printStackTrace(System.out);

			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}
}
