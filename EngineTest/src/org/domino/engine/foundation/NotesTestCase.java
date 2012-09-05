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
	 * �������ֵ
	 * 
	 * @param strDataName
	 *            ������
	 * @return
	 */
	public static String getProperty(String strPropertyName) {
		return getProperty(strPropertyName, "");
	}

	/**
	 * �������ֵ�������������ȡ�����Ĭ��ֵ
	 * 
	 * @param strDataName
	 *            ������
	 * @param strDefaultDataValue
	 *            Ĭ��ֵ
	 * @return
	 */
	public static String getProperty(String strPropertyName,
			String strDefaultPropertyValue) {
		String strPropertyValue = "";

		try {
			// if (prop == null) {
			// ��������ļ�
			Properties prop = PropertyLoader
					.loadProperties(EngineConstants.TEST_PROPERTIES_CLASS_NAME);
			// }

			// �������ֵ
			strPropertyValue = prop.getProperty(strPropertyName,
					strDefaultPropertyValue);

		} catch (Exception e) {
			Helper.logError(e);
			// �������ʧ��ʱȡĬ��ֵ
			strPropertyValue = strDefaultPropertyValue;
		}

		return strPropertyValue;
	}
}
