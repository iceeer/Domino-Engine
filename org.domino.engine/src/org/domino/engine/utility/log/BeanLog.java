package org.domino.engine.utility.log;

import java.util.LinkedList;

import org.domino.engine.utility.XSPHelper;

/**
 * This class provides a fixed size list to dump logging information.
 */
public class BeanLog {
	
	/** Maximum number of entries */
	public static final int MAX_SIZE = 100;
	private LinkedList<String> m_linesList = new LinkedList<String>();

	public BeanLog() {
	}

	/**
	 * Appends a new line to the log. If the log reaches {@link MAX_SIZE}, the
	 * first line is removed.
	 * 
	 * @param line
	 *            new log line
	 */
	public void println(String line) {
		m_linesList.add(line);
		if (m_linesList.size() > MAX_SIZE)
			m_linesList.removeFirst();
	}

	/**
	 * Returns the current content of the log
	 * 
	 * @return log content
	 */
	public String getContent() {
		StringBuffer sb = new StringBuffer();
		for (String currLine : m_linesList) {
			if (sb.length() > 0)
				sb.append("\n");

			sb.append(currLine);
		}
		return sb.toString();
	}

	public void setContent(String c) {
		// not supported
	}

	/**
	 * Clears the log
	 */
	public void clear() {
		m_linesList.clear();
	}

	/**
	 * The method returns the Log object instance that JSF stores in the session
	 * scope of the current user.
	 * 
	 * @return Log instance
	 */
	public static BeanLog getInstance() {
		return (BeanLog) XSPHelper.getBindingValue("#{Log}");
	}
}
