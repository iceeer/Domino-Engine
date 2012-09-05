/**
 * 
 */
package org.domino.engine.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.apache.commons.lang3.StringUtils;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.organization.Person;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.sso.LtpaToken;
import org.domino.engine.utility.sso.LtpaTokenConfig;
import org.domino.engine.utility.sso.SSOHelper;
import org.domino.engine.utility.sso.josso.JOSSOHelper;

import javax.xml.rpc.ParameterMode;
import org.josso.gateway.ws._1_1.protocol.SSOSessionType;

/**
 * @author admin
 * 
 */
public class XSPCommonHeader {

	/**
	 * 
	 */
	public XSPCommonHeader() {
	}

	public void load() {
		if (Engine.isDebug()) {
			Helper.logMessage("load XSPCommonHeader");
		}

		try {
			
			//处理SSO登录
			if (SSOHelper.isEnableSSO()) {
				SSOHelper.doSSO();
			}
			
			//是否允许匿名登录
			if(Engine.getProperty("anonymous.allow").equals("false")){
				if (DominoObjectHelper.isAnonymous(Engine.getSession())) {// 当前用户是匿名
					XSPHelper.redirect(XSPHelper.buildLoginURL(XSPHelper.getFullURL()));
				}
			}
		} catch (Exception e) {
			Helper.logError(e);
		}
	}

}
