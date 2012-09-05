package org.domino.engine.foundation;

import java.util.Properties;
import java.util.Vector;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * 
 */

/**
 * @author iceeer
 * 
 */
public class DataFactory {

	/**
	 * 是否调试
	 */
	private static boolean debug = false;

	/**
	 * 
	 * @param strDataName
	 * @return
	 */
	public static String getDataValueByName(String strDataName) {
		return getDataValueByName(strDataName, "");
	}

	/**
	 * 
	 * @param session
	 * @param strDataName
	 * @param strDefaultDataValue
	 * @return
	 */
	public static String getDataValueByName(String strDataName,
			String strDefaultDataValue) {
		String strDataValue = "";
		try {
			String strDBName = "";
			String strViewName = "";

			// 获得试图名
			strViewName = Helper
					.getProperty("data.dictionary.lookup.view.name");

			Document docData = DominoObjectHelper.getDocumentByKey(
					Application.getSettingDB(), strViewName, strDataName);
			if (docData != null) {
				strDataValue = docData.getItemValueString("DataValue");
			} else {
				Helper.logError("数据项[" + strDataName + "]不存在于设置数据库");
				strDataValue = strDefaultDataValue;
			}

		} catch (Exception e) {
			Helper.logError(e);
		}

		if (isDebug()) {
			Helper.logMessage("get the property [" + strDataName + "] value ["
					+ strDataValue + "]from data dictionary ");

		}
		return strDataValue;
	}

	/**
	 * @param debug
	 *            the debug to set
	 */
	public static void setDebug(boolean debug) {
		DataFactory.debug = debug;
	}

	/**
	 * @return the debug
	 */
	public static boolean isDebug() {
		return debug;
	}
}
