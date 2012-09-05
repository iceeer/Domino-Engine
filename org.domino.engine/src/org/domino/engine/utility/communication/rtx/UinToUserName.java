package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class UinToUserName
 {
	//2006测试成功
    public static void main(String[] args) {

    	String uin = "1006";
    	String userName = null;
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		userName = RtxsvrapiObj.UinToUserName(uin);
    		if (userName != null)
    		{
    			System.out.println("UIN: " + uin + "\n" + "用户名："+userName);
    		}
    		

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
