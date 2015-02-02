package edu.byu.edge.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Contains utilities to manipulate strings.
 *
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 10/10/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 10/10/2013
 */
public class StringUtils {

	private StringUtils() {}

	/**
	 * Pads a string with a given character to a specific length. If base is already at lease length long, it is immediately returned.
	 *
	 * @param base The string to pad.
	 * @param padWith The character to pad with.
	 * @param length The length to pad to.
	 * @return The padded string. Null if base is null.
	 */
	public static String padLeft(final String base, final char padWith, final int length) {
		if (base == null) return base;
		if (base.length() >= length) return base;
		if (length < 8 || length - base.length() < 2) {
			return padLeft(padWith + base, padWith, length);
		}
		final char[] c = new char[length];
		Arrays.fill(c, 0, length - base.length(), padWith);
		System.arraycopy(base.toCharArray(), 0, c, length - base.length(), base.length());
		return new String(c);
	}

	/**
	 * Pads a string with a given character to a specific length. If base is already at lease length long, it is immediately returned.
	 *
	 * @param base The string to pad.
	 * @param padWith The character to pad with.
	 * @param length The length to pad to.
	 * @return The padded string. Null if base is null.
	 */
	public static String padRight(final String base, final char padWith, final int length) {
		if (base == null) return base;
		if (base.length() >= length) return base;
		if (length < 8 || length - base.length() < 2) {
			return padRight(base + padWith, padWith, length);
		}
		final char[] c = Arrays.copyOf(base.toCharArray(), length);
		Arrays.fill(c, base.length(), length, padWith);
		return new String(c);
	}

	/**
	 * Returns the empty string if the parameter is null.
	 *
	 * @param s Possibly null string.
	 * @return A not null string.
	 */
	public static String nullSafeString(final String s) {
		return s == null ? "" : s;
	}

	private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_WITH_MILLIS = new DateFormatThreadLocal("yyyy-MM-dd-HH-mm-ss-S");

	private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_NO_MILLIS = new DateFormatThreadLocal("yyyy-MM-dd-HH-mm-ss");

	private static SimpleDateFormat getDateFormatForThread(final boolean includeMillis) {
		return includeMillis ? DATE_FORMAT_WITH_MILLIS.get() : DATE_FORMAT_NO_MILLIS.get();
	}

	/**
	 * Returns the current time as a formatted String (yyyy-MM-dd-HH-mm-ss)
	 *
	 * @return The date string.
	 */
	public static String getNowAsFormattedDate(final boolean includeMillis) {
		return getDateFormatForThread(includeMillis).format(new Date());
	}
	/**
	 * Returns the current time as a formatted String (yyyy-MM-dd-HH-mm-ss)
	 *
	 * @return The date string.
	 */
	public static String getNowAsFormattedDate(final boolean includeMillis, final int suffix, final int padded) {
		return getDateFormatForThread(includeMillis).format(new Date()) + '-' + padLeft(String.valueOf(suffix), '0', padded);
	}

	private static class DateFormatThreadLocal extends ThreadLocal<SimpleDateFormat> {
		private final String format;

		private DateFormatThreadLocal(final String format) {
			this.format = format;
		}

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(format);
		}
	}
}
