/**
 * 
 */
package org.domino.engine.utility.sso.josso;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.domino.engine.Helper;
import org.josso.gateway.ws._1_1.protocol.SSOSessionType;

/**
 * @author admin
 * 
 */
public class JOSSOHelperTest extends TestCase {
	public void testgetSSOSession_Accuracy1() {
		String strJOSSOAsserationID = JOSSOHelper.getJOSSOAsserationID("josso",
				"root", "gtn");// TODO Change the static
		SSOSessionType oSSOSessionType = JOSSOHelper
				.getSSOSession(strJOSSOAsserationID);
		Assert.assertNotNull(oSSOSessionType);
	}
}
