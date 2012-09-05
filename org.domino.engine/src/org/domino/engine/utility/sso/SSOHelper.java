package org.domino.engine.utility.sso;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.NotesException;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.organization.Person;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.sso.josso.JOSSOHelper;
import org.josso.gateway.ws._1_1.protocol.SSOSessionType;

public class SSOHelper {

	/**
	 * 
	 */
	public static final String MULTI_SITE_SESSION_COOKIE_NAME = "LtpaToken";

	/**
	 * 
	 */
	public static final String SINGLE_SITE_SESSION_COOKIE_NAME = "DomAuthSessId";

	/**
	 * sso base url
	 * @return
	 */
	public static String getSSOUrl(){
		return Engine.getProperty("sso.josso.url");
	}
	
	/**
	 * sso is enable
	 * 
	 * @return
	 */
	public static boolean isEnableSSO() {
		if (Engine.getProperty("sso.enable").equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param strSSOUserName
	 * @return
	 */
	public static String getDominoUserName(String strSSOUserName) {
		Person person = Person.getPersonBySSOUserName(strSSOUserName);
		if (person != null) {
			return DominoObjectHelper.getCanonicalNameString(
					Engine.getSessionAsSignerWithFullAccess(),
					person.getPersonID());
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @param srtDominoUsername
	 * @return
	 */
	public static boolean generateDominoCookie(String srtDominoUsername) {
		return generateDominoToken(srtDominoUsername);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean cleanDominoCookie(HttpServletRequest req,
			HttpServletResponse res) {
		res.setContentType("text/html");
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (Engine.isDebug()) {
					Helper.logMessage(cookie.getName());
					Helper.logMessage(cookie.getValue());
				}
				if (cookie.getName().equals(SINGLE_SITE_SESSION_COOKIE_NAME)
						| cookie.getName().equals(
								MULTI_SITE_SESSION_COOKIE_NAME)) {
					cookie.setMaxAge(0);
					cookie.setValue("");
					cookie.setPath("/");
					res.addCookie(cookie);
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * @param srtDominoUsername
	 * @return
	 */
	public static boolean generateDominoToken(String srtDominoUsername) {
		try {
			if (!DominoObjectHelper.isAnonymous(Engine.getSession())) {// 当前用户不是匿名
				String strCurrentDominoUserName = DominoObjectHelper
						.getCanonicalNameString(
								Engine.getSessionAsSignerWithFullAccess(),
								Engine.getSession().getEffectiveUserName());// 获得当前Domino
				// 用户全名
				if (srtDominoUsername.equals(strCurrentDominoUserName)) {// 当前用户就是SSO用户
					return true;
				}
			}

//			if (Application.isMultiSessionAuthentication()) {

				// generate domino token cookie
				LtpaTokenConfig oLtpaTokenConfig = new LtpaTokenConfig();
				oLtpaTokenConfig.setDominoSessionExpirtation(60 * 60 * 1000l);// 1小时
				oLtpaTokenConfig.setDominoSecret(Application.getDominoSecret());
				Date date = new Date();

				String strLtpaToken = LtpaToken.generate(
						srtDominoUsername,
						date,
						new Date(date.getTime()
								+ oLtpaTokenConfig
										.getDominoSessionExpirtation()),
						oLtpaTokenConfig.getDominoSecret()).toString();

				Helper.setCookie(XSPHelper.getResponse(), MULTI_SITE_SESSION_COOKIE_NAME,
						strLtpaToken, "", "/");

				if (Engine.isDebug()) {
					System.out.println("generate cookie LtpaToken for domino user name "
							+ srtDominoUsername);
					System.out.println("token " + strLtpaToken);
				}

				return true;
//			} else {
//				String strDomAuthSessId = getDomAuthSessId(srtDominoUsername,
//						null);
//				
//				if (Engine.isDebug()) {
//					Helper.logMessage(strDomAuthSessId);
//				}
//				Helper.setCookie(XSPHelper.getResponse(), SINGLE_SITE_SESSION_COOKIE_NAME,
//						strDomAuthSessId, "", "/");
//				if (Engine.isDebug()) {
//					System.out.println("generate cookie DomAuthSessId for domino user name "
//							+ srtDominoUsername);
//					System.out.println("token " + strDomAuthSessId);
//				}
//				return true;
//			}
		} catch (Exception e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static String getDomAuthSessId(String userName, String password) {
		return DominoObjectHelper.getDomAuthSessId(XSPHelper.buildLoginURL(),userName, password);
	}

	/**
	 * 
	 */
	public static void doSSO() {
		try {

			String strJOSSOAsserationID = XSPHelper.readParameter(
					FacesContext.getCurrentInstance(), JOSSOHelper.JOSSO_ASSERT_NAME);
			if (Helper.ValidateNotEmpty(strJOSSOAsserationID)) {
				XSPHelper.setSessionScopeValue(JOSSOHelper.JOSSO_ASSERT_NAME, strJOSSOAsserationID);
				Helper.logMessage(XSPHelper.getSessionScopeValue(JOSSOHelper.JOSSO_ASSERT_NAME));
				if (Engine.isDebug()) {
					Helper.logMessage(JOSSOHelper.JOSSO_ASSERT_NAME + ":"
							+ strJOSSOAsserationID);
				}

				SSOSessionType oSSOSessionType = JOSSOHelper
						.getSSOSession(strJOSSOAsserationID);

				if (Helper.ValidateNotNull(oSSOSessionType)) {
					String strSSOUserName = oSSOSessionType.getUsername();
					if (Helper.ValidateNotEmpty(strSSOUserName)) {
						String srtDominoUsername = SSOHelper
								.getDominoUserName(strSSOUserName);

						if (SSOHelper.generateDominoToken(srtDominoUsername)) {
							// XSPHelper
							// .getExternalContext()
							// .redirect(
							// XSPHelper
							// .redirectWithoutParm("josso_assertion_id"));
						}
					}

				} else {// not right session id

				}

				XSPHelper.getExternalContext().redirect(
						XSPHelper.redirectWithoutParm(JOSSOHelper.JOSSO_ASSERT_NAME));

			}

		} catch (Exception e) {
			Helper.logError(e);
		}
	}
}
