package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class GetSvrIpPort
 {
	//2007测试通过
    public static void main(String[] args) {

    	String SvrIP = "";
    	int iPort = 0;
    	
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		SvrIP = RtxsvrapiObj.getServerIP();
    		iPort = RtxsvrapiObj.getServerPort();
    		System.out.println("操作成功"+ "\n"+ "服务器地址:" +SvrIP + "\n" + "服务器端口:"+iPort);

	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
