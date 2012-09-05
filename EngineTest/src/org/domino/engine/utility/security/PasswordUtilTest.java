/**
 * 
 */
package org.domino.engine.utility.security;

import junit.framework.Assert;
import junit.framework.TestCase;
import lotus.domino.NotesException;

import org.domino.engine.Engine;
import org.domino.engine.utility.security.PasswordUtil;
import org.junit.* ;
import static org.junit.Assert.* ;

/**
 * @author iceeer
 * 
 */
public class PasswordUtilTest extends TestCase {
	
	@Test
	public void testComparePassword_Accuracy1() throws NotesException {
		Assert.assertTrue(PasswordUtil.comparePassword(Engine.getSession(),
				"admin", "12345678"));

	}

//	@Test
//	public void testVerifyPassword_Accuracy1() throws NotesException {
//		String strPassword = "12345678";
//		String strHASH = PasswordUtil.hashPassword(this.getSession(),
//				strPassword);
//		Assert.assertTrue(PasswordUtil.verifyPassword(this.getSession(),
//				strPassword, strHASH));
//
//	}

//	@Test
//	public void testVerifyPassword_Accuracy2() throws NotesException {
//		String strPassword = "12345678";
//		String strHASH = "(GrsSyVfb8UkAQZlgU/7x)";
//		Assert.assertTrue(PasswordUtil.verifyPassword(this.getSession(),
//				strPassword, strHASH));
//
//	}
	
	@Test
	public void testHashPassword_Failure() throws NotesException {
		String strPassword = "12345678";
		String strHASH1 = PasswordUtil.hashPassword(Engine.getSession(),
				strPassword);
		String strHASH2 = PasswordUtil.hashPassword(Engine.getSession(),
						strPassword);
		Assert.assertFalse(strHASH1.equals(strHASH2));
	}
	
	@Test
	public void testUserHasThisPassword_Accuracy1() throws NotesException {
		String strPassword = "12345678";
		Assert.assertTrue((PasswordUtil.getUserHasThisPassword(Engine.getSession(), strPassword).size() > 0));

	}
	
	@Test
	public void testUserHasThisPassword_Accuracy2() throws NotesException {
		String strPassword = "";
		Assert.assertTrue(PasswordUtil.getUserHasThisPassword(Engine.getSession(), strPassword).size() == 0);
	}

}
