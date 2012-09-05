package org.domino.engine;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.domino.engine.Helper;
import org.domino.engine.foundation.DataFactory;
import org.domino.engine.foundation.PropertyLoader;
import org.domino.engine.library.EnginelibActivator;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.diiop.DIIOPHelper;

/**
 * 
 */

/**
 * @author iceeer
 * 
 * @20111029 兼容代理运行
 * 
 */
public class Engine {

	/**
	 * 获得进程
	 * 
	 * @return 进程对象
	 */
	public static Session getSession() {

		Session session = null;
		if (isTest()) {
			session = DIIOPHelper.createSession();
		} else if (isAgent()) {

		} else {
			session = XSPHelper.getCurrentSession();
		}

		return session;
	}

	/**
	 * @return the sessionAsSigner
	 */
	public static Session getSessionAsSigner() {
		Session sessionAsSigner = null;
		if (isTest()) {
			sessionAsSigner = DIIOPHelper.createSessionAsSigner();
		} else {
			sessionAsSigner = XSPHelper.getSessionAsSigner();
		}

		return sessionAsSigner;
	}

	/**
	 * @return the sessionAsSignerWithFullAccess
	 */
	public static Session getSessionAsSignerWithFullAccess() {
		Session sessionAsSignerWithFullAccess = null;
		if (isTest()) {
			sessionAsSignerWithFullAccess = DIIOPHelper
					.createSessionAsSignerWithFullAccess();
		} else {
			sessionAsSignerWithFullAccess = XSPHelper
					.getSessionAsSignerWithFullAccess();
		}

		return sessionAsSignerWithFullAccess;
	}

	/**
	 * 获得属性值
	 * 
	 * @param strDataName
	 *            属性名
	 * @return
	 */
	public static String getProperty(String strPropertyName) {
		return getProperty(strPropertyName, "");
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
		if (isTest()) {
			strValue = Helper.getProperty(
					EngineConstants.TEST_PROPERTIES_CLASS_NAME,
					strPropertyName, strDefaultPropertyValue);
		} else {
			// 从数据字典获得属性值
			strValue = DataFactory.getDataValueByName(strPropertyName,
					strDefaultPropertyValue);

			if (!Helper.ValidateNotEmpty(strValue)) {// 属性值为空
				// 从属性文件获得属性值
				strValue = Helper.getProperty(
						EngineConstants.APPLICATION_PROPERTIES_CLASS_NAME,
						strPropertyName, strDefaultPropertyValue);
			}
		}

		return strValue;
	}

	/**
	 * 设置当前文件夹
	 * 
	 * @param parmCurrentFolder
	 */
	public static void setCurrentFolder(String parmCurrentFolder) {
		currentFolder = parmCurrentFolder;
	}

	/**
	 * 获得当前文件夹
	 * 
	 * @return
	 */
	public static String getCurrentFolder() {
		if (currentFolder != null) {
			return currentFolder;
		} else {
			// return XSPHelper.getCurrentFolder();
			return DominoObjectHelper.getCurrentFolderPath(Engine.getSession());
		}
	}

	/**
	 * 设置资源路径
	 * 
	 * @param baseResourcePath
	 *            the baseResourcePath to set
	 */
	public static void setBaseResourcePath(String strBaseResourcePath) {
		baseResourcePath = strBaseResourcePath;
	}

	/**
	 * 获得资源路径
	 * 
	 * @return the baseResourcePath
	 */
	public static String getBaseResourcePath() {
		if (baseResourcePath != null) {
			return baseResourcePath;
		} else {
			return DominoObjectHelper.getResourceBaseURL(Engine.getSession());
		}
	}

	/**
	 * 设置应用为调试状态
	 * 
	 * @param debug
	 */
	public static void setDebug(boolean blDebug) {
		debug = blDebug;
	}

	/**
	 * 设置应用调试状态
	 * 
	 */
	public static void setDebug() {
		debug = !debug;
	}

	/**
	 * 是否开启调试
	 * 
	 * @return the debug 调试状态
	 */
	public static boolean isDebug() {
		if (isTest()) {
			return debug;
		}

		boolean blDebug = false;

		// 应用配置是否开启调试
		if (Helper.getProperty("debug.enable").equals("true")) {
			return true;
		}
		// 服务器是否开启调试
		if (Server.isDebug() == true) {
			blDebug = true;
		} else {
			// 引擎应用是否开启调试ApplicationScope
			if (debug == true) {
				blDebug = true;
			} else {
				if (!isAgent()) {// 判断是否运行在代理中
					// 进程是否开启调试SessionScope
					if (XSPHelper.getSessionScopeValue("debug.enable").equals(
							"true")) {
						blDebug = true;
					} else {
						// 请求是否开启调试RequestScope
						if (XSPHelper.getRequestScopeValue("debug.enable")
								.equals("true")) {
							blDebug = true;
						} else {
							// 页面是否开启调试ViewScope
							if (XSPHelper.getViewScopeValue("debug.enable")
									.equals("true")) {
								blDebug = true;
							}
						}
					}
				}

			}
		}
		return blDebug;
	}

	/**
	 * 获得版本号
	 * 
	 * @return
	 */
	public static String getLibVersion() {
		if (isTest()) {
			return "org.domino.engine.test";
		} else if (isAgent()) {
			return "org.domino.engine.agent";
		}
		try {
			String s = AccessController
					.doPrivileged(new PrivilegedAction<String>() {
						public String run() {
							Object o = EnginelibActivator.instance.getBundle()
									.getHeaders().get("Bundle-Version");
							if (o != null) {
								return o.toString()
										+ " - "
										+ EnginelibActivator.instance
												.getBundle().getLastModified();
							}
							return null;
						}
					});
			if (s != null) {
				return s;
			}
		} catch (Exception ex) {
			System.out.println("Can Not Get Engine Library Version");
		}
		return "";
	}

	/**
	 * 设置测试状态
	 * 
	 * @param test
	 */
	public static void setTest(boolean test) {
		Engine.test = test;
	}

	/**
	 * 获得是否测试状态
	 * 
	 * @return
	 */
	public static boolean isTest() {
		return test;
	}

	/**
	 * 设置代理运行时的进程
	 * 
	 * @param session
	 */
	public static void setAgent(Session session) {
		agent = true;
		Engine.session = session;
	}

	/**
	 * 是是否代理运行
	 * 
	 * @return
	 */
	public static boolean isAgent() {
		return agent;
	}

	/**
	 * 当前文件夹
	 */
	private static String currentFolder = null;

	/**
	 * 资源路径
	 */
	private static String baseResourcePath = null;

	/**
	 * 调试状态
	 */
	private static boolean debug = false;

	/**
	 * 测试状态
	 */
	private static boolean test = false;

	/**
	 * 代理运行
	 */
	private static boolean agent = false;

	/**
	 * 进程
	 */
	private static Session session = null;

}
