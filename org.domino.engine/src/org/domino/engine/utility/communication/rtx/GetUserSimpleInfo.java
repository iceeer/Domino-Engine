package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class GetUserSimpleInfo
 {
	//3.61测试通过
    public static void main(String[] args) {

    	String[][] info = null;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		info =RtxsvrapiObj.GetUserSimpleInfo("mike");
    		if (info!=null)
    		{
    			for (int i = 0 ; i < info.length; i ++)
				{
					for (int j = 0 ; j < 2 ; j++)
					{
				
						System.out.print(info[i][j]);
						System.out.print("\t");
					}
			
					System.out.println();
				}
    		}
    		else 
    		{
    			System.out.println("获取简单资料失败");
    		}

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}