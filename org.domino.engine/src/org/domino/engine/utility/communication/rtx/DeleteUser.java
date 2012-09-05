package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class DeleteUser
 {
	//2007测试成功
    public static void main(String[] args) {

    	String userName = "herolin";
    	int iRet = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet =RtxsvrapiObj.deleteUser(userName);
    		if (iRet==0)
    		{
    			System.out.println("删除成功");
    		}
    		else 
    		{
    			System.out.println("删除失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
