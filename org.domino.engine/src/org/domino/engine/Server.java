/**
 * 
 */
package org.domino.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author iceeer
 *
 */
public class Server {

	
	public static void showServerInfo(String server,int port){
		try{
			
			Socket sc=new Socket(server,port);
			StringBuffer sb=new StringBuffer();
			sb.append("HEAD / HTTP/1.1\n");
			sb.append("Accept: */* \n");
			sb.append("Host: "+server+"\n");
			sb.append("Connection: Keep-Alive \n\n");
			sc.getOutputStream().write(sb.toString().getBytes());
			BufferedReader in=new BufferedReader(new InputStreamReader(sc.getInputStream()));
			String userInput;
			String format = "%1$-25s%2$-54s\n";
			while ((userInput = in.readLine()) != null) {
				if(userInput.startsWith("Server:")){
					if(userInput.length()>55) userInput=userInput.substring(0,54);
					break;
				}
			}
			sc.getOutputStream().close();
			in.close();
			sc.close();
		}catch(Exception ex){
			System.out.println("Err:"+server+","+ex.getMessage());
		}
	}
	
	/**
	 * 服务器是否处于调试状态
	 * @return
	 */
	public static boolean isDebug(){
		//TODO 实现
		return false;
	}
}
