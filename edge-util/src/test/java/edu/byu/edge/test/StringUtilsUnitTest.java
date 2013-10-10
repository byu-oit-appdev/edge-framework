package edu.byu.edge.test;

import edu.byu.edge.util.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 10/10/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 10/10/2013
 */
@RunWith(JUnit4.class)
public class StringUtilsUnitTest {
	private static final Logger LOG = Logger.getLogger(StringUtilsUnitTest.class);

	@Test
	public void testPadLeft1() {
		Assert.assertEquals("", StringUtils.padLeft("", '0', 0));
	}

	@Test
	public void testPadLeft2() {
		Assert.assertEquals(null, StringUtils.padLeft(null, '0', 0));
	}

	@Test
	public void testPadLeft3() {
		Assert.assertEquals("01", StringUtils.padLeft("1", '0', 2));
	}

	@Test
	public void testPadLeft4() {
		Assert.assertEquals("Hello", StringUtils.padLeft("Hello", '0', 3));
	}

	@Test
	public void testPadRight1() {
		Assert.assertEquals("", StringUtils.padRight("", '0', 0));
	}

	@Test
	public void testPadRight2() {
		Assert.assertEquals(null, StringUtils.padRight(null, '0', 0));
	}

	@Test
	public void testPadRight3() {
		Assert.assertEquals("10", StringUtils.padRight("1", '0', 2));
	}

	@Test
	public void testPadRight4() {
		Assert.assertEquals("Hello", StringUtils.padRight("Hello", '0', 3));
	}

	@Test
	public void testNullSafeString() {
		Assert.assertEquals("", StringUtils.nullSafeString(null));
		Assert.assertEquals("", StringUtils.nullSafeString(""));
		Assert.assertEquals("a", StringUtils.nullSafeString("a"));
	}
}
