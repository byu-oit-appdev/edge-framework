package edu.byu.auth.header;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import edu.byu.edge.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This filter has no dependence on Spring per se. Spring may be used to configure this filter, however this is optional.
 * The purpose of this filter is to ensure that all requests that pass through it are from authorized hosts.
 * It's inteded use is to guarantee that a service only accepts requests from the SOA proxies or localhost.
 * Allowed format of IP addresses is in dotted decimal format (123.45.67.101) or subnets may be specified by appending a forward slash and the bit mask (123.45.67.0/24).
 *
 * The behavior is to first use the file specified by the property. If the property is not set, or the file does not exist or is empty, then the
 * system property is used. If the system property is not set or the file specified does not exist or is empty, then the addresses set to the
 * allowedIpAddresses property is used. If no addresses are specified at this point, then an exception is thrown.
 *
 * If the only allowed IP address is to be localhost, then localhost MUST be set through one of the other properties. It is insufficient to simply
 * set allowLocalhost to true. This will result in an exception. In other words, to have localhost be the only allowed host to get through this
 * filter, specify 127.0.0.1 (or equivalent netmask) through a file or the allowedIpAddresses property.
 *
 * The list of allowed IP addresses can be specified in three ways:
 * 1. Set the list of allowed IP addresses directly (allowedIpAddresses property)
 * 2. Create a file that contains the IP addresses (one per line) and set the full path to the allowedIpFileName property.
 * 3. Create a file as in #2, but set the full path as a system property. This property defaults to byu.allowed.ip.file, but this property name can be overridden by setting the name to the allowedIpFilePropertyName.
 *
 * Please note that localhost (127.0.0.1) is by default allowed. This behavior can be overridden by setting the allowLocalhost property to false (defaults to true).
 *
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/21/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/21/2013
 */
public class AllowedHostRequestFilter extends GenericFilterBean implements InitializingBean, Filter {

	private static final Logger LOG = Logger.getLogger(AllowedHostRequestFilter.class);

	public static final Pattern IP_PATTERN = Pattern.compile("^" +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\." +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\." +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\." +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"$");

	public static final Pattern NET_MASK_PATTERN = Pattern.compile("^" +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\." +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\." +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\." +
			"(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])" +
			"\\/(3[0-2]|[1-2][0-9]|[1-9])" +
			"$");

	/*
	This can be static since it only helps facilitate the creation of bitmasks. In fact, by making it static, all instances in the same classloader
	can now benefit from.
	 */
	private static Map<String, String> IP_CONVERSION_CACHE = Collections.synchronizedMap(new TreeMap<String, String>());

	private Set<String> allowedIpAddresses;
	private String allowedIpFileName;
	private volatile String allowedIpFilePropertyName = "byu.allowed.ip.file";
	private boolean allowLocalhost;

	private Set<String> realAllowedAddresses;
	private Set<String> alreadyAuthorizedAddresses;
	private final Function<String, String[]> ipToBitmask = new IpToBitsFunction();

	public AllowedHostRequestFilter() {
		this.allowLocalhost = true;
		this.realAllowedAddresses = new TreeSet<String>();
		this.alreadyAuthorizedAddresses = Collections.synchronizedSet(new TreeSet<String>());
	}

	public void setAllowedIpAddresses(final Collection<String> allowedIpAddresses) {
		this.allowedIpAddresses = new TreeSet<String>();
		this.allowedIpAddresses.addAll(allowedIpAddresses);
	}

	public void setAllowedIpFileName(final String allowedIpFileName) {
		this.allowedIpFileName = allowedIpFileName;
	}

	public void setAllowedIpFilePropertyName(final String allowedIpFilePropertyName) {
		this.allowedIpFilePropertyName = allowedIpFilePropertyName;
	}

	public void setAllowLocalhost(final boolean allowLocalhost) {
		this.allowLocalhost = allowLocalhost;
	}

	@Override
	public void afterPropertiesSet() {
		if (!checkList()) throw new IllegalArgumentException("Specify either a file name or a list of allowed IP addresses or a System property that points to a file of allowed IP addresses.");
		if (allowLocalhost) addLocalhost();
	}

	protected final boolean checkList() {
		if (allowedIpFileName != null && !"".equals(allowedIpFileName) && parseLoadedIntoReal(tryFileLoad(allowedIpFileName)))
			return true;
		final String propVal = getPropertyValue();
		if (!"".equals(propVal) && parseLoadedIntoReal(tryFileLoad(propVal)))
			return true;
		return parseLoadedIntoReal(allowedIpAddresses) && !realAllowedAddresses.isEmpty();
	}

	protected final Set<String> tryFileLoad(final String fileName) {
		final Set<String> result = new HashSet<String>(8, .999999f);
		final File fin = new File(fileName);
		if (fin.exists() && fin.isFile()) {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(fin));
				String line;
				while ((line = in.readLine()) != null) {
					if (!line.startsWith("#")) result.add(line);
				}
			} catch (final Throwable t) {
				LOG.error("Error reading IP address file " + fileName + ".", t);
			} finally {
				if (in != null) try {
					in.close();
				} catch (IOException ignore) {
				}
			}
		}
		return result;
	}

	protected final String getPropertyValue() {
		if (allowedIpFilePropertyName == null || "".equals(allowedIpFilePropertyName)) return "";
		String propVal = System.getProperty(allowedIpFilePropertyName);
		if (propVal != null && !"".equals(propVal)) return propVal;
		propVal = System.getenv(allowedIpFilePropertyName);
		if (propVal != null && !"".equals(propVal)) return propVal;
		return "";
	}

	protected final boolean parseLoadedIntoReal(final Set<String> loaded) {
		for (final String s : loaded) {
			if (NET_MASK_PATTERN.matcher(s).matches()) {
				final String[] vals = s.split("\\/");
				final int bits = Integer.parseInt(vals[1]);
				final String ipbits = parseIpToBits(vals[0]);
				realAllowedAddresses.add(ipbits.substring(0, bits));
			} else if (IP_PATTERN.matcher(s).matches()) {
				realAllowedAddresses.add(parseIpToBits(s));
			} else {
				LOG.info("Invalid IP address information: " + s);
			}
		}
		return !realAllowedAddresses.isEmpty();
	}

	protected final void addLocalhost() {
		realAllowedAddresses.add(parseIpToBits("127.0.0.1").substring(0, 8));
	}

	protected static String parseIpToBits(final String ipAddr) {
		if (IP_CONVERSION_CACHE.containsKey(ipAddr)) return IP_CONVERSION_CACHE.get(ipAddr);
		if (NET_MASK_PATTERN.matcher(ipAddr).matches()) {
			final String[] vals = ipAddr.split("\\/");
			final String bits = parseIpToBits(vals[0]).substring(0, Integer.parseInt(vals[1]));
			IP_CONVERSION_CACHE.put(ipAddr, bits);
			return bits;
		} else if (IP_PATTERN.matcher(ipAddr).matches()) {
			final String[] strings = ipAddr.split("\\.");
			final String a = StringUtils.padLeft(Long.toBinaryString(Integer.parseInt(strings[0])), '0', 8);
			final String b = StringUtils.padLeft(Long.toBinaryString(Integer.parseInt(strings[1])), '0', 8);
			final String c = StringUtils.padLeft(Long.toBinaryString(Integer.parseInt(strings[2])), '0', 8);
			final String d = StringUtils.padLeft(Long.toBinaryString(Integer.parseInt(strings[3])), '0', 8);
			final String bits = a + b + c + d;
			IP_CONVERSION_CACHE.put(ipAddr, bits);
			return bits;
		}
		return ipAddr;
	}

	protected final boolean isAddressAllowed(final Set<String> addresses) {
		if (addresses == null || addresses.isEmpty()) return false;
		if (!Sets.intersection(addresses, alreadyAuthorizedAddresses).isEmpty()) return true;
		final Collection<String[]> masks = Collections2.transform(addresses, ipToBitmask);
		for (final String[] m : masks) {
			for (final String a : realAllowedAddresses) {
				if (m[1].startsWith(a)) {
					alreadyAuthorizedAddresses.add(m[0]);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final TreeSet<String> remoteAddresses = new TreeSet<String>();
		final String remoteAddr = request.getRemoteAddr();
		remoteAddresses.add(remoteAddr);
		if (request instanceof HttpServletRequest) {
			remoteAddresses.addAll(HeaderAuthUtils.extractRemoteIpAddress((HttpServletRequest) request));
		}

		if (isAddressAllowed(remoteAddresses)) {
			chain.doFilter(request, response);
		} else {
			LOG.info("Rejecting request from unauthorized remote IP address: " + remoteAddr + ".");
			throw new IllegalArgumentException("The request was not made through a trusted proxy.");
		}
	}

	private static class IpToBitsFunction implements Function<String, String[]> {
		@Override
		public String[] apply(final String input) {
			return new String[]{input, parseIpToBits(input)};
		}
	}
}
