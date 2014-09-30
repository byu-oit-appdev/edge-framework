package edu.byu.edge.util;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
public class Base62 {

	private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
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

	public static long decode(final String value) {
		final char[] ca = value.toCharArray();
		final int x = ca.length - 1;
		long result = 0;
		for (int i = 0; i < ca.length; i++) {
			final char c = ca[x - i];
			result += ('a' <= c && c <= 'z' ? c - 87 : ('0' <= c && c <= '9' ? c - 48 : c - 29)) * BASE[i];
		}
		return result;
	}

//	private static int base62DecodeChar(final char x) {
//		if (x >= '0' && x <= '9') {
//			return x - 48;
//		}
//		if (x >= 'a' && x <= 'z') {
//			return x - 87;
//		}
//		return x - 29;
//	}

	private Base62() {}
}
