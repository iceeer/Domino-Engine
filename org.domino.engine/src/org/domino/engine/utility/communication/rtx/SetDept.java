package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class SetDept
 {
	//2007 测试成功
    public static void main(String[] args) {

    	String deptId= "1";
    	String DetpInfo = "修改SDK测试部门为TestDept";
    	String DeptName = "SDK测试部门";
    	String ParentDeptId = "0";
    	
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iRet = RtxsvrapiObj.setDept(deptId,DetpInfo,DeptName,ParentDeptId);
    		
    		if (iRet == 0)
    		{
    			System.out.println("修改部门资料成功");
    			
    		}
    		else 
    		{
    			System.out.println("修改部门资料失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
