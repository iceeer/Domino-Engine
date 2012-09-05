package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class DeleteDept
 {
	//2007测试成功
    public static void main(String[] args) {

    	String deptId= "1";
    	String type = "1";
    	
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet = RtxsvrapiObj.deleteDept(deptId,type);
    		
    		if (iRet == 0)
    		{
    			System.out.println("删除部门成功");
    			
    		}
    		else 
    		{
    			System.out.println("删除部门失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
