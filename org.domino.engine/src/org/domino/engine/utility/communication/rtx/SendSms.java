package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class SendSms
 {
	//2007测试成功
    public static void main(String[] args) {

    	String receiver= "herolin";
    	String sender = "900";
    	String smsInfo = "SDK手机短信测试";
    	int autoCut = 1;
    	int noTitle = 0;
    	
    	int iRet= -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet = RtxsvrapiObj.sendSms(sender, receiver, smsInfo,autoCut, noTitle);
    		if (iRet == 0)
    		{
    			System.out.println("发送成功");
    			
    		}
    		else 
    		{
    			System.out.println("发送失败");
    		}

    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
