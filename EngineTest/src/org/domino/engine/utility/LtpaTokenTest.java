/**
 * 
 */
package org.domino.engine.utility;

import static org.junit.Assert.*;

import java.util.Date;

import junit.framework.Assert;

import org.domino.engine.utility.sso.LtpaToken;
import org.domino.engine.utility.sso.LtpaTokenConfig;
import org.junit.Test;

/**
 * @author iceeer
 * 
 */
public class LtpaTokenTest {

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.sso.LtpaToken#generate(java.lang.String, java.util.Date, java.util.Date, java.lang.String)}
	 * .
	 */
	@Test
	public void testGenerate() {
		LtpaTokenConfig oLtpaTokenConfig = new LtpaTokenConfig();
		// oLtpaTokenConfig.setCookieName("");
		oLtpaTokenConfig.setDominoSessionExpirtation(60 * 60 * 1000l);
		oLtpaTokenConfig.setDominoSecret("QZFaEPZwd2cUUlfI7ewqXhQ2T/8=");
		Date date = new Date(2111,1,1,1,0,0);

		String strLtpaToken = LtpaToken.generate(
				"CN=admin/O=org",
				date,
				new Date(date.getTime()
						+ oLtpaTokenConfig.getDominoSessionExpirtation()),
				oLtpaTokenConfig.getDominoSecret()).toString();
		System.out.println(strLtpaToken);
		Assert.assertEquals(
				"AAECA0VGRjI4QUExMEVGRjI4QjgyMENOPWFkbWluL089b3JnSl9zJUQIkOiZVVFkk9f+Fm7FeP4=",
				strLtpaToken);
	}
	
	/**
	 * 
	 * Test method for
	 * {@link org.domino.engine.utility.sso.LtpaToken#generate(java.lang.String, java.util.Date, java.util.Date, java.lang.String)}
	 * .
	 */
	@Test
	public void testGenerate2() {
		LtpaTokenConfig oLtpaTokenConfig = new LtpaTokenConfig();
		// oLtpaTokenConfig.setCookieName("");
		oLtpaTokenConfig.setDominoSessionExpirtation(60 * 60 * 1000l);//1Сʱ
		oLtpaTokenConfig.setDominoSecret("QZFaEPZwd2cUUlfI7ewqXhQ2T/8=");
		Date date = new Date();

		String strLtpaToken = LtpaToken.generate(
				"CN=admin/O=org",
				date,
				new Date(date.getTime()
						+ oLtpaTokenConfig.getDominoSessionExpirtation()),
				oLtpaTokenConfig.getDominoSecret()).toString();
		System.out.println(strLtpaToken);
		Assert.assertTrue(!strLtpaToken.equals(""));
	}

}
