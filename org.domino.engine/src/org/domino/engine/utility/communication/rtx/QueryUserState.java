package org.domino.engine.utility.communication.rtx;

import org.domino.engine.utility.communication.rtx.RTXSvrApi;   
public class QueryUserState
 {
	//2007测试成功
    public static void main(String[] args) {

    	String userName = "dfdf";
    	int iState = -1;
    	RTXSvrApi  RtxsvrapiObj = new RTXSvrApi();        		
    	if( RtxsvrapiObj.Init())
    	{   
    		iState =RtxsvrapiObj.QueryUserState(userName);
    		
    		switch   (iState)   
    		  {   
    		          case   0:   
    		        	  	System.out.println("用户: " + userName + "状态为: " + "离线");
    		                break;   
    		          case   1:   
    		        	  	System.out.println("用户: " + userName + "状态为: " + "在线");
    		        	  	break;   
    		          case   2:     
    		        	  	System.out.println("用户: " + userName + "状态为: " + "离开");
    		                break;   
    		          case   -984:     
  		        	  		System.out.println("用户: " + userName + "不存在");
  		        	  		break;  
    		          default:   
    		        	  	System.out.println("访问RTX服务器出错!");
    		                break;   
    		  }
	    }	
    	RtxsvrapiObj.UnInit();
    	
    }
}
