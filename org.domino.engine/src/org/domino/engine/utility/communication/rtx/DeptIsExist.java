package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class DeptIsExist
 {
	//2007测试未通过,有bug,不管部门ID存在与否，都返回存在
    public static void main(String[] args) {

    	String deptId= "1";
    	
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet = RtxsvrapiObj.deptIsExist(deptId);
    		
    		if (iRet == -5)
    		{
    			System.out.println("部门存在");
    			
    		}
    		else if (iRet == 0)
    		{
    			System.out.println("部门不存在");
    		}
    		

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
