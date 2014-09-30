package edu.byu.edge.util;

import java.util.regex.Pattern;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
public class Base31 {

	private static final String STR = "0123456789ABCDEFGHJKLMNPRTUVWXY";
	private static final char[] CHARS = STR.toCharArray();
	private static final Pattern VALID = Pattern.compile("^[" + STR + "]+$");
	private static final int NUM = CHARS.length;
	private static final long[] BASE;

	static {
		BASE = new long[13];
		BASE[0] = 1;
		for (int i = 1; i < 13; i++) {
			BASE[i] = BASE[i - 1] * NUM;
		}
	}


	public static String encode(final long raw) {
		if (raw < 0) throw new IllegalArgumentException("Number must be positive.");
		if (raw < NUM) return String.valueOf(CHARS[((int) raw)]);
		long x = raw;
		final StringBuilder sb = new StringBuilder(16);
		while (x > 0) {
			sb.append(CHARS[((int) (x % NUM))]);
			x = x / NUM;
		}
		return sb.reverse().toString();
	}

	public static long decode(final String enc) {
		return _doDecode(enc.toUpperCase());
	}

	private static long _doDecode(final String s) {
		final char[] ca = s.toCharArray();
		final int x = ca.length - 1;
		long r = 0;
		for (int i = 0; i < ca.length; i++) {
			final char k = ca[x - i];
			final int p = 'A' <= k && k <= 'H' ? k - 55 : ('0' <= k && k <= '9' ? k - 48 : ('J' <= k && k <= 'N' ? k - 56 : ('P' == k ? k - 57 : ('R' == k ? k - 58 : k - 59))));
			r += BASE[i] * p;
		}
		return r;
	}

	public static long safeDecode(final String enc) {
		if (enc == null || enc.isEmpty()) throw new IllegalArgumentException("No valid encoded number provided.");
		final String s = enc.toUpperCase();
		if (!VALID.matcher(s).matches()) throw new IllegalArgumentException("Invalid encoded number (" + s + "). Valid characters are " + STR + ".");
		return _doDecode(s);
	}

	private Base31() {}
}
