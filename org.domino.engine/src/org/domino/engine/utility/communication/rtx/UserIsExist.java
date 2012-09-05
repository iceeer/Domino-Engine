package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class UserIsExist
 {
	//2006测试通过
    public static void main(String[] args) {

    	String username= "r5ryerye";
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet = RtxsvrapiObj.userIsExist(username);
    		if (iRet == 0 )
    		{
    			System.out.println("用户存在");
    		}
    		else 
    		{
    			System.out.println("用户不存在");
    		}

    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
