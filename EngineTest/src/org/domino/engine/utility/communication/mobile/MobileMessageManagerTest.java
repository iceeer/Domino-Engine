/**
 * 
 */
package org.domino.engine.utility.communication.mobile;

import java.util.Map;

import junit.framework.TestCase;

import org.domino.engine.Helper;
import org.domino.engine.foundation.NotesTestCase;
import org.domino.engine.utility.communication.mobile.MobileMessageManager;
import org.junit.Test;


/**
 * @author iceeer
 *
 */
public class MobileMessageManagerTest extends NotesTestCase{

	@Test
	public void testSendSMS_Accuracy1(){
		String phoneNumber = "13917349214"; 
		String content = "testÖÐÎÄ";
		MobileMessageManager oMobileMessageManager = new MobileMessageManager();
		assertEquals(oMobileMessageManager.sendSMS(phoneNumber, content), true);
	}
	
//	public void testReceiveSMS_Accuracy1(){
//		MobileMessageManager oMobileMessageManager = new MobileMessageManager();
//		assertEquals(oMobileMessageManager.receiveSMS(), true);
//	}
}
