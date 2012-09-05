/**
 * 
 */
package org.domino.engine.utility.log;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.XSPHelper;

/**
 * @author admin
 * 
 */
public class ScopeMessage {

	/**
	 * 
	 */
	public static final int iDefault = 0;

	/**
	 * 
	 */
	public static final int iError = 1;

	/**
	 * 
	 */
	public static final int iWarn = 2;

	/**
	 * 
	 */
	public static final int iInfo = 3;

	/**
	 * 
	 * @param iType
	 * @param strMessageTitle
	 * @param strMessageContent
	 * @param strReturnURL
	 */
	public static void logMessage(int iType, String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		XSPHelper.setSessionScopeValue("msg-type", iType);
		XSPHelper.setSessionScopeValue("msg-title", strMessageTitle);
		XSPHelper.setSessionScopeValue("msg-content", strMessageContent);
		XSPHelper.setSessionScopeValue("msg-rurl", strReturnURL);
	}

	public static void logError(String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		logMessage(iError, strMessageTitle, strMessageContent, strReturnURL);
	}

	public static void logWran(String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		logMessage(iWarn, strMessageTitle, strMessageContent, strReturnURL);
	}

	public static void logInfo(String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		logMessage(iInfo, strMessageTitle, strMessageContent, strReturnURL);
	}

	public static int getMessageType() {
		try {
			return (Integer) XSPHelper.getSessionScopeValue("msg-type");
		} catch (Exception e) {
			Helper.logError(e);
			return iDefault;
		}
	}

	/**
	 * 生成信息页HTML
	 * 
	 * @return
	 */
	public static String buildDisplayHTML() {
		String strHTML = "<html>";

		String strContent = XSPHelper.getSessionScopeValueString("msg-content");

		if (Helper.ValidateNotEmpty(strContent)) {
			int iType = getMessageType();
			String strTitle = XSPHelper.getSessionScopeValueString("msg-title");
			String strURL = XSPHelper.getSessionScopeValueString("msg-rurl");

			// title
			if (Helper.ValidateNotEmpty(strTitle)) {
				strHTML += "<head><title>" + strTitle + "</title></head>";
			} else {
				if (iType == iError) {
					strHTML += "<head><title>错误</title></head>";
				} else if (iType == iWarn) {
					strHTML += "<head><title>警告</title></head>";
				} else {
					strHTML += "<head><title>提示</title></head>";
				}
			}

			// icon
			if (iType == iError) {

			} else if (iType == iWarn) {

			} else {

			}

			strHTML += strContent;

			if (Helper.ValidateNotEmpty(strURL)) {
				strHTML += "<br /><a href='" + strURL + "'>返回" + "</a>";
			}

		} else {
			strHTML = "log is not exist";
		}

		strHTML += "</html>";

		return strHTML;
	}

	public static void printMessage(int iType, String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		logMessage(iType, strMessageTitle, strMessageContent, strReturnURL);
		XSPHelper.getXSPContext().redirectToPage(
				Engine.getProperty("message.page.name"));
	}

	public static void printError(String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		printMessage(iError, strMessageTitle, strMessageContent, strReturnURL);
	}

	public static void printWran(String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		printMessage(iWarn, strMessageTitle, strMessageContent, strReturnURL);
	}

	public static void printInfo(String strMessageTitle,
			String strMessageContent, String strReturnURL) {
		printMessage(iInfo, strMessageTitle, strMessageContent, strReturnURL);
	}
}
