package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class GetChildDepts
 {
	//2007测试通过
    public static void main(String[] args) {

    	String pdeptId= "20";
    	String depts[] = null;
    	int iRet = -1;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		depts = RtxsvrapiObj.getChildDepts(pdeptId);
    		if (depts != null)
    		{
    			for (  int i = 0 ; i< depts.length;i ++)
    			{
    				System.out.println(depts[i]);
    			}
    		}
    		else 
    		{
    			System.out.println("该部门没有子部门");
    			
    		}
    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
