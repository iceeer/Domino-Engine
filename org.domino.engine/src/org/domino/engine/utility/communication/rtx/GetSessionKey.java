package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class GetSessionKey
 {
	//2007测试通过,可以正常获取
    public static void main(String[] args) {

    	String username= "mike";
    	String szKey= "";
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		szKey = RtxsvrapiObj.getSessionKey(username);
    		System.out.println("SessionKey:" + szKey);

    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
