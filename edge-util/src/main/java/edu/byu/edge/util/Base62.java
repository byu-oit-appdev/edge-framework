package edu.byu.edge.util;

import java.util.regex.Pattern;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
public class Base62 {

	private static final String STR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
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
		final StringBuilder sb = new StringBuilder(11);
		if (raw < 0) throw new IllegalArgumentException("Number must be positive.");
		if (raw < NUM) return String.valueOf(CHARS[((int) raw)]);
		long x = raw;
		do {
			sb.append(CHARS[((int) (x % 62))]);
			x = x / 62;
		} while (x > 0);
		return sb.reverse().toString();
	}

	public static long decode(final String enc) {
		final char[] ca = enc.toCharArray();
		final int x = ca.length - 1;
		long result = 0;
		for (int i = 0; i < ca.length; i++) {
			final char c = ca[x - i];
			result += ('a' <= c && c <= 'z' ? c - 87 : ('0' <= c && c <= '9' ? c - 48 : c - 29)) * BASE[i];
		}
		return result;
	}

	public static long safeDecode(final String enc) {
		if (enc == null || enc.isEmpty()) throw new IllegalArgumentException("No valide encoded number provided.");
		if (!VALID.matcher(enc).matches()) throw new IllegalArgumentException("Invalid encoded number (" + enc + "). Valid characters are " + STR + ".");
		return decode(enc);
	}

	private Base62() {}
}
