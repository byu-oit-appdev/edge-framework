package edu.byu.edge.test;

import edu.byu.auth.header.AllowedHostRequestFilter;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 10/10/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 10/10/2013
 */
@RunWith(JUnit4.class)
public class AllowedHostRequestFilterUnitTest {

	private static final Logger LOG = Logger.getLogger(AllowedHostRequestFilterUnitTest.class);

	private static final List<String> ADDRESSES_TO_ALLOW = Arrays.asList("10.5.36.2", "10.8.0.0/16");
	private static final List<String> ALLOWED_ADDRESSES_TEST = Arrays.asList("127.0.0.1", "10.5.36.2", "10.8.0.5", "10.8.100.150");
	private static final List<String> DENIED_ADDRESSES_TEST = Arrays.asList("128.0.0.2", "10.4.36.2", "10.7.255.255", "10.9.0.0", "128.0.0.0", "126.255.255.255");
	private static final List<String[]> IP_ADDRESS_AND_BITS = Arrays.asList(
			new String[]{"127.0.0.1/8", "01111111"},
			new String[]{"127.0.0.1", "01111111000000000000000000000001"},
			new String[]{"1.2.3.4", "00000001000000100000001100000100"},
			new String[]{"255.128.62.1", "11111111100000000011111000000001"},
			new String[]{"135.16.48.172/24", "100001110001000000110000"},
			new String[]{"192.168.25.1/27", "110000001010100000011001000"},
			new String[]{"172.96.45.227", "10101100011000000010110111100011"},
			new String[]{"0.0.0.0", "00000000000000000000000000000000"},
			new String[]{"255.255.255.255", "11111111111111111111111111111111"},
			new String[]{"255.0.255.0", "11111111000000001111111100000000"}
	);

	@Test
	public void testIpToBits() {
		for (final String[] arr : IP_ADDRESS_AND_BITS) {
			if (LOG.isDebugEnabled()) LOG.debug(String.format("Testing: %s -> %s.", arr[0], arr[1]));
			assertEquals(arr[0] + " should become " + arr[1], arr[1], MyFilter.doParseIpToBits(arr[0]));
		}
	}

	@Test
	public void testAllowOrNot() {
		final MyFilter x = new MyFilter();
		x.setAllowedIpAddresses(ADDRESSES_TO_ALLOW);
		x.setAllowLocalhost(true);
		x.afterPropertiesSet();

		for (final String s : ALLOWED_ADDRESSES_TEST) {
			if (LOG.isDebugEnabled()) LOG.debug(String.format("Testing: %s allowed.", s));
			final TreeSet<String> set = new TreeSet<String>();
			set.add(s);
			assertTrue(s + " should be allowed.", x.doIsAddressAllowed(set));
		}

		for (final String s : DENIED_ADDRESSES_TEST) {
			if (LOG.isDebugEnabled()) LOG.debug(String.format("Testing: %s denied.", s));
			final TreeSet<String> set = new TreeSet<String>();
			set.add(s);
			assertFalse(s + " should not be allowed.", x.doIsAddressAllowed(set));
		}

	}

	private static class MyFilter extends AllowedHostRequestFilter {
		public static String doParseIpToBits(final String ipAddr) { return parseIpToBits(ipAddr); }
		public boolean doIsAddressAllowed(final Set<String> addresses) { return isAddressAllowed(addresses); }
	}
}
