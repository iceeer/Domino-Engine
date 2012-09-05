package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class GetDeptUsers
 {
	//2007测试通过
    public static void main(String[] args) {

    	String deptId= "1";
    	String users[] = null;
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		users = RtxsvrapiObj.getDeptUsers(deptId);
    		if (users != null)
    		{
    			for (  int i = 0 ; i< users.length;i ++)
    			{
    				System.out.println(users[i]);
    			}
    		}
    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
