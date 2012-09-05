/**
 * 
 */
package org.domino.engine.utility.sso.josso;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationRequestType;
import org.josso.gateway.ws._1_1.protocol.AssertIdentityWithSimpleAuthenticationResponseType;
import org.josso.gateway.ws._1_1.protocol.NoSuchSessionErrorType;
import org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionRequestType;
import org.josso.gateway.ws._1_1.protocol.ResolveAuthenticationAssertionResponseType;
import org.josso.gateway.ws._1_1.protocol.SSOIdentityProviderErrorType;
import org.josso.gateway.ws._1_1.protocol.SSOSessionType;
import org.josso.gateway.ws._1_1.protocol.SessionRequestType;
import org.josso.gateway.ws._1_1.protocol.SessionResponseType;
import org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProvider;
import org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderWS;
import org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOIdentityProviderWSLocator;
import org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManager;
import org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerWS;
import org.josso.gateway.ws._1_1.wsdl.soapbinding.SSOSessionManagerWSLocator;

/**
 * @author admin
 * 
 */
public class JOSSOHelper {
	
	public static final String JOSSO_ASSERT_NAME = "josso_assertion_id";
	
	public static SSOSessionType getSSOSession(String strJOSSOAsserationID) {
		try {
			if (Helper.ValidateNotEmpty(strJOSSOAsserationID)) {
				if (Engine.isDebug()) {
					Helper.logMessage(JOSSO_ASSERT_NAME + ":"
							+ strJOSSOAsserationID);
				}

				// Get the session id by assert id
				SSOIdentityProviderWS oSSOIdentityProviderWS = new SSOIdentityProviderWSLocator();
				SSOIdentityProvider oSSOIdentityProvider = oSSOIdentityProviderWS
						.getSSOIdentityProvider();
				ResolveAuthenticationAssertionRequestType oResolveAuthenticationAssertionRequestType = new ResolveAuthenticationAssertionRequestType(
						strJOSSOAsserationID);
				ResolveAuthenticationAssertionResponseType oResolveAuthenticationAssertionResponseType = oSSOIdentityProvider
						.resolveAuthenticationAssertion(oResolveAuthenticationAssertionRequestType);
				String strSSOSessionID = oResolveAuthenticationAssertionResponseType
						.getSsoSessionId();
				if (Engine.isDebug()) {
					Helper.logMessage("josso session id " + strSSOSessionID);
				}

				// get the session by session id
				SSOSessionManagerWS oSSOSessionManagerWS = new SSOSessionManagerWSLocator();
				SSOSessionManager oSSOSessionManager = oSSOSessionManagerWS
						.getSSOSessionManager();
				SessionRequestType oSessionRequestType = new SessionRequestType(
						strSSOSessionID);
				SessionResponseType oSessionResponseType = oSSOSessionManager
						.getSession(oSessionRequestType);
				return oSessionResponseType.getSSOSession();

			}
		}catch(SSOIdentityProviderErrorType ie){
			Helper.logError("SSO验证错误");
		}catch (NoSuchSessionErrorType ne) {
			Helper.logError("SSO Session不存在");
		} catch (Exception e) {
			Helper.logError(e);
		}
		return null;
	}

	public static String getJOSSOAsserationID(java.lang.String securityDomain,
			java.lang.String username, java.lang.String password) {

		try {
			SSOIdentityProviderWS oSSOIdentityProviderWS = new SSOIdentityProviderWSLocator();
			SSOIdentityProvider oSSOIdentityProvider = oSSOIdentityProviderWS
					.getSSOIdentityProvider();
			AssertIdentityWithSimpleAuthenticationRequestType oAssertIdentityWithSimpleAuthenticationRequestType = new AssertIdentityWithSimpleAuthenticationRequestType(
					securityDomain, username, password);
			AssertIdentityWithSimpleAuthenticationResponseType oAssertIdentityWithSimpleAuthenticationResponseType = oSSOIdentityProvider
					.assertIdentityWithSimpleAuthentication(oAssertIdentityWithSimpleAuthenticationRequestType);
			return oAssertIdentityWithSimpleAuthenticationResponseType
					.getAssertionId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
