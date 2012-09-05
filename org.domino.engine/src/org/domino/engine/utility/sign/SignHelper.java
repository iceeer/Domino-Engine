/**
 * 
 */
package org.domino.engine.utility.sign;

import java.io.File;

import org.domino.engine.EngineConstants;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;

/**
 * @author iceeer
 *
 */
public class SignHelper {
	
	/**
	 * 签名图片文件夹
	 */
	public static final String SIGN_IMAGE_FOLDER = "signimg";
	
	/**
	 * 
	 * @return
	 */
	public static String getSignImagePath() {
		return DominoObjectHelper.getEnvironmentString("Directory") + "\\domino\\html\\"
				+ EngineConstants.DIRECTORY_NAME_FOR_DOMINO_TMP_USE + "\\" + SIGN_IMAGE_FOLDER + "\\";
	}

	/**
	 * 签名某人的签名图片文件路径
	 * @return
	 */
	public static String getSignImagePath(String strUserName) {
		String strFileName = getSignImagePath() + getSignImageFileName(strUserName);
		File file = new File(strFileName);
		if (file.exists()) {
			return strFileName;
		} else {
			return "";
		}
	}
	
	/**
	 * 签名某人的签名图片文件URL地址
	 * @param strUserName
	 * @return
	 */
	public static String getSignImageURL(String strUserName) {
		return "/" + EngineConstants.DIRECTORY_NAME_FOR_DOMINO_TMP_USE + "/" + SIGN_IMAGE_FOLDER + "/" + getSignImageFileName(strUserName);
	}
	
	/**
	 * 签名某人的签名图片文件名
	 * @param strUserName
	 * @return
	 */
	public static String getSignImageFileName(String strUserName) {
		String strFileName = strUserName + ".png";
		
		return strFileName;
	}
}
