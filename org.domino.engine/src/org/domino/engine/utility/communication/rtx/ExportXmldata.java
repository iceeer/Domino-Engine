package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class ExportXmldata
 {
	//2007测试成功
    public static void main(String[] args) {
    	
    	String  strRet= "";
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		strRet = RtxsvrapiObj.exportXmldata();
    		if (strRet!= null)
    		{
    			System.out.println(strRet);
    		}
    	}
    	RtxsvrapiObj.UnInit();
    	
    }
}
