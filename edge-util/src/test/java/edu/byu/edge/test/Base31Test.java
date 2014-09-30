package edu.byu.edge.test;

import edu.byu.edge.util.Base31;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
@RunWith(JUnit4.class)
public class Base31Test {

	private static final Logger LOG = Logger.getLogger(Base31Test.class);

	@Test
	public void testBasic() {
		Assert.assertEquals("0", Base31.encode(0));
		Assert.assertEquals("1", Base31.encode(1));
		Assert.assertEquals("2", Base31.encode(2));
		Assert.assertEquals("3", Base31.encode(3));
		Assert.assertEquals("4", Base31.encode(4));
		Assert.assertEquals("5", Base31.encode(5));
		Assert.assertEquals("6", Base31.encode(6));
		Assert.assertEquals("7", Base31.encode(7));
		Assert.assertEquals("8", Base31.encode(8));
		Assert.assertEquals("9", Base31.encode(9));
		Assert.assertEquals("A", Base31.encode(10));
		Assert.assertEquals("B", Base31.encode(11));
		Assert.assertEquals("C", Base31.encode(12));
		Assert.assertEquals("D", Base31.encode(13));
		Assert.assertEquals("E", Base31.encode(14));
		Assert.assertEquals("F", Base31.encode(15));
		Assert.assertEquals("G", Base31.encode(16));
		Assert.assertEquals("H", Base31.encode(17));
		Assert.assertEquals("J", Base31.encode(18));
		Assert.assertEquals("K", Base31.encode(19));
		Assert.assertEquals("L", Base31.encode(20));
		Assert.assertEquals("M", Base31.encode(21));
		Assert.assertEquals("N", Base31.encode(22));
		Assert.assertEquals("P", Base31.encode(23));
		Assert.assertEquals("R", Base31.encode(24));
		Assert.assertEquals("T", Base31.encode(25));
		Assert.assertEquals("U", Base31.encode(26));
		Assert.assertEquals("V", Base31.encode(27));
		Assert.assertEquals("W", Base31.encode(28));
		Assert.assertEquals("X", Base31.encode(29));
		Assert.assertEquals("Y", Base31.encode(30));
		Assert.assertEquals("10", Base31.encode(31));
	}

	@Test
//	@Ignore
	public void testSymmetry() {
//		Base31.encode(31);
		final long a = System.currentTimeMillis();
		for (int i = 0; i < 0x1b4d89f; i++) {
			Assert.assertEquals(i, Base31.decode(Base31.encode(i)));
		}
		final long b = System.currentTimeMillis();
		LOG.info("Base31 run: " + (b - a));
	}

	@Test
	@Ignore
	public void testLogs() {
		warmup();
		final long a = System.currentTimeMillis();
		final int max = 0xfffffff;
		for (int j = 0; j < 0xfff; j++) {
			for (int i = 0; i < max; i++) {
				final double log = doLog(i);
			}
		}
		final long b = System.currentTimeMillis();
		LOG.info("Math.log -> " + (b - a));

		final long c = System.currentTimeMillis();
		for (int j = 0; j < 0xfff; j++) {
			for (int i = 0; i < max; i++) {
				final double log = doLog10(i);
			}
		}
		final long d = System.currentTimeMillis();
		LOG.info("Math.log10 -> " + (d - c));
	}

	private double doLog(final int i) { return Math.log(i); }
	private double doLog10(final int i) { return Math.log10(i); }

	private void warmup() {
		for (int a = 0; a < 10; a++) {
			for (int i = 0xff; i < 0xfffff; i++) {
				doLog(i);
			}
			for (int i = 0xff; i < 0xfffff; i++) {
				doLog10(i);
			}
		}
	}
}
