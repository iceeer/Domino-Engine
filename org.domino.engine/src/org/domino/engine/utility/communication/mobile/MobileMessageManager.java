package org.domino.engine.utility.communication.mobile;

import org.domino.engine.utility.communication.mobile.empp.MobileTool;

/**
 * 
 * @author iceeer
 *
 */
public class MobileMessageManager {//TODO 聚合度太大
	MobileTool tool = new MobileTool();
	
	
	public boolean sendSMS(String phoneNumber, String content){
		tool.sendShortMessage(phoneNumber, content);
		return true;
	}
	
	public boolean receiveSMS(){
		tool.receiveShortMessage();
		return true;
	}
}
