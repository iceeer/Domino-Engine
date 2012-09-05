package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class SetUserSimpleInfo
 {
	//2006测试成功
    public static void main(String[] args) {

    	String userName = "herolin";
    	String ChsName="林xx";
    	String email="sdksupport@tencent.com";
    	String gender="0";
    	String mobile="13510143536";
    	String phone="0755-83765566";
    	String pwd="123";
        
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet =RtxsvrapiObj.SetUserSimpleInfo(userName,ChsName,email,gender,mobile,phone,pwd);
    		if (iRet==0)
    		{
    			System.out.println("设置简单资料成功");
    		}
    		else 
    		{
    			System.out.println("设置简单资料失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
