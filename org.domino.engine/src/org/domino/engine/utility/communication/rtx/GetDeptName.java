package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class GetDeptName
 {
	//2007测试通过,添加成功
    public static void main(String[] args) {

    	String DeptName = null;
    	
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		DeptName = RtxsvrapiObj.GetDeptName("2");
    		
    		if (DeptName == null)
    		{
    			System.out.println("获取失败");
    			
    		}
    		else 
    		{
    			System.out.println("部门名称:"+DeptName);
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}