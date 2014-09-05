package edu.byu.edge.jdbc.util;

/**
 * Utility methods to facilitate transforming to and from booleans.
 *
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/05/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/05/2014
 */
public final class BooleanHelper {

	/**
	 * Converts a boolean to a String representation.
	 * @param b A value.
	 * @return "Y" if true, "N" if false
	 */
	public static String booleanToString(final boolean b) {
		return b ? "Y" : "N";
	}

	/**
	 * Converts a boolean to a String representation.
	 * @param b A value.
	 * @param valueIfNull Value to return if b is null.
	 * @return "Y" if true, "N" if false, "" if null
	 */
	public static String booleanToString(final Boolean b, final String valueIfNull) {
		return b == null ? valueIfNull : (b ? "Y" : "N");
	}

	/**
	 * Accepts a String with various values, like "1", "0", "T", "true", "Y", "No", etc. If null or empty, returns false;
	 * @param s String with a boolean-like value.
	 * @return true or false
	 */
	public static boolean stringToBoolean(final String s) {
		if (s == null || s.isEmpty()) return false;
		final char c = s.charAt(0);
		return '1' == c || 'T' == c || 't' == c || 'Y' == c || 'y' == c;
	}

	/**
	 * Accepts a String with various values, like "1", "0", "T", "true", "Y", "No", etc. If null or empty, returns valueIfNull;
	 * @param s String with a boolean-like value.
	 * @param valueIfNull Value to return if s is null or empty
	 * @return true, false, or null
	 */
	public static Boolean stringToBoolean(final String s, final Boolean valueIfNull) {
		if (s == null || s.isEmpty()) return valueIfNull;
		return stringToBoolean(s);
	}

	private BooleanHelper() {
	}
}
