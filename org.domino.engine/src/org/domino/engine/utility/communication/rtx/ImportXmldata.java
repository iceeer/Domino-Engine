package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class ImportXmldata
 {
	//2007测试通过，把导出来的数据导回去
    public static void main(String[] args) {
    	
    	String  strRet= "";
    	int iRet = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		strRet = RtxsvrapiObj.exportXmldata();
    		System.out.println(strRet);
    		
    		if (strRet!= null)
    		{
    			iRet = RtxsvrapiObj.importXmldata(strRet);
    			if (iRet==0)
    			{
    				System.out.println("导入成功");
    			}
    			else
    			{
    				System.out.println("导入失败");
    			}
    		}
    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
