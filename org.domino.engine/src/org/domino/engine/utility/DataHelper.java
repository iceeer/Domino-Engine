/**
 * 
 */
package org.domino.engine.utility;

/**
 * @author iceeer
 * 
 */
public class DataHelper {

	// ==============================================================================
	// Data conversion
	// ==============================================================================

	public static String asString(Object v, String defaultValue) {
		if (v != null) {
			return v.toString();
		}
		return defaultValue;
	}

	public static String asString(Object v) {
		return asString(v, null);
	}

	public static int asInteger(Object v, int defaultValue) {
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).intValue();
			}
			if (v instanceof String) {
				return Integer.valueOf((String) v);
			}
			if (v instanceof Boolean) {
				return ((Boolean) v) ? 1 : 0;
			}
		}
		return defaultValue;
	}

	public static int asInteger(Object v) {
		return asInteger(v, 0);
	}

	public static long asLong(Object v, long defaultValue) {
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).longValue();
			}
			if (v instanceof String) {
				return Long.valueOf((String) v);
			}
			if (v instanceof Boolean) {
				return ((Boolean) v) ? 1L : 0L;
			}
		}
		return defaultValue;
	}

	public static long asLong(Object v) {
		return asLong(v, 0);
	}

	public static double asDouble(Object v, double defaultValue) {
		if (v != null) {
			if (v instanceof Number) {
				return ((Number) v).doubleValue();
			}
			if (v instanceof String) {
				return Double.valueOf((String) v);
			}
			if (v instanceof Boolean) {
				return ((Boolean) v) ? 1.0 : 0.0;
			}
		}
		return defaultValue;
	}

	public static double asDouble(Object v) {
		return asDouble(v, 0);
	}

	public static boolean asBoolean(Object v, boolean defaultValue) {
		if (v != null) {
			if (v instanceof Boolean) {
				return ((Boolean) v).booleanValue();
			}
			if (v instanceof String) {
				return Boolean.valueOf((Boolean) v);
			}
			if (v instanceof Number) {
				return ((Number) v).intValue() != 0;
			}
			return true;
		}
		return defaultValue;
	}

	public static boolean asBoolean(Object v) {
		return asBoolean(v, false);
	}

}
