package edu.byu.edge.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
public class Base31 {

	private static final char[] CHARS = "0123456789ABCDEFGHJKLMNPRTUVWXY".toCharArray();
//	private static final long[] BOUNDS;


	private static final int NUM = CHARS.length;
	private static final long[] BASE;

	private static final Map<Character, Integer> REV;

	static {
		REV = new HashMap<Character, Integer>(32, .999999f);
		for (int i = 0; i < NUM; i++) {
			REV.put(CHARS[i], i);
		}
		BASE = new long[13];
		BASE[0] = 1;
		for (int i = 1; i < 13; i++) {
			BASE[i] = BASE[i - 1] * NUM;
		}
//		BOUNDS = new long[1 + (int) (Math.log(Long.MAX_VALUE) / Math.log(NUM))];
//		for (int i = 0; i < BOUNDS.length - 1; i++) {
//			BOUNDS[i] = (long) Math.pow(NUM, i);
//		}
//		BOUNDS[12] = Long.MAX_VALUE;
	}


	public static String encode(final long raw) {
		if (raw < 0) throw new IllegalArgumentException("Number must be positive.");
		if (raw < NUM) return String.valueOf(CHARS[((int) raw)]);
//		final int a = Arrays.binarySearch(BOUNDS, raw);
//		final int b = a < 0 ? -a : a + 1;
//		final char[] c = new char[b];
//		long x = raw;
//		for (int i = 0; i < b; i++) {
//			c[i] = CHARS[((int) (x % NUM))];
//			x = x / NUM;
//		}
//		return new String(c);
		long x = raw;
		final StringBuilder sb = new StringBuilder(16);
		while (x > 0) {
			sb.append(CHARS[((int) (x % NUM))]);
			x = x / NUM;
		}
		return sb.reverse().toString();
	}

	public static long decode(final String enc) {
		final char[] ca = enc.toUpperCase().toCharArray();
		final int x = ca.length - 1;
		long r = 0;
		for (int i = 0; i < ca.length; i++) {
			final char k = ca[x - i];
			if (!REV.containsKey(k)) throw new IllegalArgumentException("Unknown character (" + k + ") at position " + i + ".");
			r += BASE[i] * REV.get(k);
//			final int p = Arrays.binarySearch(CHARS, ca[i]);
//			if (p < 0) throw new IllegalArgumentException("Unknown character (" + ca[i] + ") at position " + i + ".");
//			r += Math.pow(NUM, i) * p;
		}
		return r;
	}

	private Base31() {}
}
