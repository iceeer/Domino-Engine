package org.domino.engine.utility;


import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageBundle {

	private static ResourceBundle msgBundle = null;

	
	public MessageBundle(String msgResource) {
		msgBundle = ResourceBundle.getBundle(msgResource, Locale.CHINA);
	}

	
	public static String getMessage(String _key) {
		String message = null;
		try {
			message = new String(msgBundle.getString(_key)
					.getBytes("ISO8859_1"), "gb2312");
		} catch (MissingResourceException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return message;
	}
	
	public static void main(String[] args) {
		

		 MessageBundle msg=new MessageBundle("jdbc");
		 String driver   = msg.getMessage("jdbc.driverClassName");
	     String url      = msg.getMessage("jdbc.url");
	     String username =msg.getMessage("jdbc.username");
	     String password =msg.getMessage("jdbc.password");

	     System.out.println(driver);
	     System.out.println(url);
	     System.out.println(username);
	     System.out.println(password);
		
		
	}

}

