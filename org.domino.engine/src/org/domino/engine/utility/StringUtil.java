package org.domino.engine.utility;

public class StringUtil {

	/**
	 * The method compares two strings. It is supported that one of the strings
	 * or both are null.
	 * 
	 * @param str1 string 1
	 * @param str2 string 2
	 * @return <code>true</code>, if the strings are equal
	 */
	public static boolean isEqual(String str1, String str2) {
		if (str1==null) {
			return str2==null;
		}
		else {
			return str1.equals(str2);
		}
	}
}
