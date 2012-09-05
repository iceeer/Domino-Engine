package org.domino.engine;

import java.io.File;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import lotus.domino.Session;
import lotus.domino.View;

import org.domino.engine.foundation.DataFactory;
import org.domino.engine.foundation.PropertyLoader;
import org.domino.engine.library.EnginelibActivator;
import org.domino.engine.utility.log.openlog.OpenLogItem;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.Version;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class Helper {

	/**
	 * 是否调试
	 */
	private static boolean debug = false;

	/**
	 * 年月日中文日期格式刷
	 */
	public static SimpleDateFormat sdfChineseDate = new SimpleDateFormat(
			"yyyy年MM月dd日");

	/**
	 * 日期时间中文格式刷
	 */
	public static SimpleDateFormat sdfChineseDateTime = new SimpleDateFormat(
			"yyyy年MM月dd日HH:mm:ss");

	/**
	 * 获得当前的日期（2011年5月26日）
	 * 
	 * @return
	 */
	public static String getCurrentChineseDateString() {
		return sdfChineseDate.format(new Date());
	}

	/**
	 * 获得URL参数值
	 * 
	 * @param query
	 * @param key
	 * @return
	 */
	public static String getQueryValue(String query, String key) {
		return getQueryMap(query).get(key);
	}

	/**
	 * 获得URL参数列表
	 * 
	 * @param query
	 * @return
	 */
	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");

		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			if (param.contains("=")) {
				String[] arr = param.split("=");
				if (arr.length >= 2) {
					String name = arr[0];
					String value = arr[1];
					map.put(name, value);
				}
			}
		}

		return map;
	}

	/**
	 * 获得当前日期的简单格式,格式：20100906
	 * 
	 * @return 日期字符窜
	 */
	public static String GetSimpleDate() {
		Date dateNow = new Date();
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		return dateformatYYYYMMDD.format(dateNow);
	}

	/**
	 * 获得当前日期时间的简单格式,格式：20100906122023
	 * 
	 * @return 日期时间字符窜
	 */
	public static String GetSimpleDateAndTime() {
		Date dateNow = new Date();
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat(
				"yyyyMMddHHMMSS");
		return dateformatYYYYMMDD.format(dateNow);
	}

	/**
	 * 获得JAVA临时路径
	 * 
	 * @return 临时路径字符串
	 */
	public static String GetTempDirectory() {
		String tempDir = "";
		try {
			// This is the property name for accessing OS temporary directory or
			// folder.
			String property = "java.io.tmpdir";

			// Get the temporary directory
			String strDominoJavaTempDirectory = System.getProperty(property);

			tempDir = strDominoJavaTempDirectory
					+ EngineConstants.DIRECTORY_NAME_FOR_DOMINO_TMP_USE + "\\";
			File f = new File(tempDir);
			if (!f.exists()) {
				f.mkdir();
				Helper.logMessage("Directory Created:" + tempDir);
			}

			tempDir += Helper.GetSimpleDate() + "\\";
			f = new File(tempDir);
			if (!f.exists()) {
				f.mkdir();
				Helper.logMessage("Directory Created:" + tempDir);
			}
		} catch (Exception e) {
			Helper.logError(e);
		}
		return tempDir;
	}

	/**
	 * 记录信息至日记库
	 * 
	 * @param msg
	 *            信息
	 */
	public static void logMessage(Object msg) {
		System.out.println(msg);
	}

	/**
	 * 记录信息至日记库
	 * 
	 * @param msg
	 *            信息
	 * @param doc
	 *            关联文档对象
	 */
	public static void logMessage(String msg, lotus.domino.Document doc) {
		System.out.println(msg);
	}

	/**
	 * 记录Exception
	 * 
	 * @param e
	 *            Exception对象
	 */
	public static void logError(Exception e) {
		e.printStackTrace();
	}

	/**
	 * 记录错误
	 * 
	 * @param strMessage
	 */
	public static void logError(Object strMessage) {
		System.out.println(strMessage);
	}

	/**
	 * 记录Exception
	 * 
	 * @param e
	 *            Exception对象
	 * @param msg
	 *            信息
	 * @param doc
	 *            关联文档对象
	 */
	public static void logError(Exception e, String msg) {
		e.printStackTrace();
		System.out.println(msg);
	}

	/**
	 * 记录Exception
	 * 
	 * @param e
	 *            Exception对象
	 * @param msg
	 *            信息
	 * @param doc
	 *            关联文档对象
	 */
	public static void logError(Exception e, String msg,
			lotus.domino.Document doc) {
		logError(e, msg);
	}

	/**
	 * 判断对象是否为null，为null则返回false
	 * 
	 * @param o
	 *            对象
	 * @return
	 */
	public static boolean ValidateNotNull(Object o) {
		return (o != null);
	}

	/**
	 * 判断字符是否为空，为空或null则返回false
	 * 
	 * @param o
	 *            对象
	 * @return
	 */
	public static boolean ValidateNotEmpty(String o) {
		if (o == null) {
			return false;
		}
		if (o.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * get the URL("/folder/file.nsf") by file path("folder\file.nsf")
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getURLByFilePath(String filePath) {
		if (ValidateNotEmpty(filePath)) {
			return "/" + filePath.replace("\\", "/");
		} else {
			return "";
		}
	}

	/**
	 * 构造静态标识
	 * 
	 * @param doc
	 * @return
	 * @throws NotesException
	 */
	public static String buildUniqueID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	/**
	 * 获得类路径
	 * 
	 * @return
	 */
	public String getCurrentClassPath() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		return classLoader.getResource("").toString();
	}

	/**
	 * 获得Java版本
	 * 
	 * @return
	 */
	public String getJavaVersion() {
		String version = System.getProperty("java.version");
		return version;
	}

	/**
	 * Find any cookie value from the browser.
	 * 
	 * @param cookieName
	 *            Name of the cookie.
	 * @return The value of the cookie.
	 */
	private static String getCookieValue(HttpServletRequest request,
			String cookieName) {

		Cookie[] cookies;

		try {
			cookies = request.getCookies();
			if (cookies != null) {

				for (int iCookieCounter = 0; iCookieCounter < cookies.length; iCookieCounter++) {

					if (cookies[iCookieCounter].getName().toLowerCase().equals(
							cookieName.toLowerCase())) {
						return cookies[iCookieCounter].getValue();
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Stores a new Cookie in the HttpServletResponse.
	 * 
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param ltpaTokenCookieDomain
	 * @param strPath
	 * @return
	 */
	public static boolean setCookie(HttpServletResponse response,
			String cookieName, String cookieValue, String cookieDomain,
			String strPath) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setDomain(cookieDomain);
		cookie.setPath(strPath);
		response.addCookie(cookie);

		return true;
	}

	/**
	 * 是否月末
	 * 
	 * @param dt
	 * @return
	 */
	public static boolean isMonthEnd(Date dt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 获得属性值，如果不存在则取传入的默认值
	 * 
	 * @param strDataName
	 *            属性名
	 * @param strDefaultDataValue
	 *            默认值
	 * @return
	 */
	public static String getProperty(String strPropertyName) {
		return Helper.getProperty(
				EngineConstants.APPLICATION_PROPERTIES_CLASS_NAME,
				strPropertyName, "");
	}

	/**
	 * 获得属性值，如果不存在则取传入的默认值
	 * 
	 * @param strDataName
	 *            属性名
	 * @param strDefaultDataValue
	 *            默认值
	 * @return
	 */
	public static String getProperty(String strPropertyName,
			String strDefaultPropertyValue) {
		String strValue = "";
		// 从属性文件获得属性值
		strValue = Helper.getProperty(
				EngineConstants.APPLICATION_PROPERTIES_CLASS_NAME,
				strPropertyName, strDefaultPropertyValue);

		return strValue;
	}

	/**
	 * 获得属性值，如果不存在则取传入的默认值
	 * 
	 * @param strClassName
	 *            属性文件名（类名）
	 * @param strPropertyName
	 *            属性名
	 * @param strDefaultPropertyValue
	 *            默认值
	 * @return
	 */
	public static String getProperty(String strClassName,
			String strPropertyName, String strDefaultPropertyValue) {
		String strPropertyValue = "";

		try {
			// if (prop == null) {
			// 获得属性文件
			Properties prop = PropertyLoader.loadProperties(strClassName);
			// }

			// 获得属性值
			strPropertyValue = prop.getProperty(strPropertyName,
					strDefaultPropertyValue);

		} catch (Exception e) {
			Helper.logError(e);
			// 获得属性失败时取默认值
			strPropertyValue = strDefaultPropertyValue;
		}
		if (isDebug()) {
			Helper.logMessage("get the property [" + strPropertyName
					+ "] value [" + strPropertyValue + "]from file "
					+ strClassName);

		}
		return strPropertyValue;
	}

	/**
	 * @param debug
	 *            the debug to set
	 */
	public static void setDebug(boolean debug) {
		Helper.debug = debug;
	}

	/**
	 * @return the debug
	 */
	public static boolean isDebug() {
		return debug;
	}

	public static String trims(String Str) {
		return trims(Str, "");
	}

	public static String trims(String Str, String Flag) {
		if (Str == null || Str.equals("")) {
			return Str;
		} else {
			Str = "" + Str;
			if (Flag == "l" || Flag == "L")/* trim left side only */
			{
				String RegularExp = "^[　 ]+";
				return Str.replaceAll(RegularExp, "");
			} else if (Flag == "r" || Flag == "R")/* trim right side only */
			{
				String RegularExp = "[　 ]+$";
				return Str.replaceAll(RegularExp, "");
			} else/* defautly, trim both left and right side */
			{
				String RegularExp = "^[　 ]+|[　 ]+$";
				return Str.replaceAll(RegularExp, "");
			}
		}
	}
}
