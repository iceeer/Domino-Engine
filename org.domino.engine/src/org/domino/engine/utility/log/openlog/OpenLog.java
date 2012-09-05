/**
 * 
 */
package org.domino.engine.utility.log.openlog;

import org.domino.engine.utility.log.openlog.OpenLogItem;

/**
 * @author iceeer
 * 
 */
public class OpenLog {

	/**
	 * OpenLog日记对象实例
	 */
	public static OpenLogItem soli;

	/**
	 * 
	 */
	public OpenLog() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return
	 */
	public static OpenLogItem getLog() {
		if (soli == null) {
			soli = new OpenLogItem();
		}
		return soli;
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
		soli.logEvent(msg, soli.SEVERITY_LOW, doc);
	}

	/**
	 * 记录Exception
	 * 
	 * @param e
	 *            Exception对象
	 */
	public static void logError(Exception e) {
		soli.logError(e);
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
		soli.logErrorEx(e, msg, soli.SEVERITY_HIGH, doc);
	}

}
