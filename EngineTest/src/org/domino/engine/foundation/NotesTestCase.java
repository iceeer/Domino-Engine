/**
 * 
 */
package org.domino.engine.foundation;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.swing.SwingUtilities;

import org.domino.engine.Engine;
import org.domino.engine.EngineConstants;
import org.domino.engine.Helper;

import junit.framework.TestCase;
import lotus.domino.NotesThread;

/**
 * @author iceeer
 *
 */
public abstract class NotesTestCase extends TestCase {
	@Override
	public void setUp() {
		Engine.setTest(true);
		Engine.setDebug(getProperty("debug.enable").equals("true"));
	}

	@Override
	public void tearDown() {
		
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
		String strPropertyValue = "";

		try {
			// if (prop == null) {
			// 获得属性文件
			Properties prop = PropertyLoader
					.loadProperties(EngineConstants.TEST_PROPERTIES_CLASS_NAME);
			// }

			// 获得属性值
			strPropertyValue = prop.getProperty(strPropertyName,
					strDefaultPropertyValue);

		} catch (Exception e) {
			Helper.logError(e);
			// 获得属性失败时取默认值
			strPropertyValue = strDefaultPropertyValue;
		}

		return strPropertyValue;
	}
}
