package edu.byu.auth.header;

import com.google.common.collect.Sets;
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

/**
 * This filter has no dependence on Spring per se. Spring may be used to configure this filter, however this is optional.
 * The purpose of this filter is to ensure that all requests that pass through it are from authorized hosts.
 * It's inteded use is to guarantee that a service only accepts requests from the SOA proxies or localhost.
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

	protected Set<String> allowedIpAddresses;
	protected String allowedIpFileName;
	protected volatile String allowedIpFilePropertyName = "byu.allowed.ip.file";
	protected boolean allowLocalhost;

	public AllowedHostRequestFilter() {
		this.allowLocalhost = true;
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
	}

	protected boolean checkList() {
		if ((allowedIpAddresses == null || allowedIpAddresses.isEmpty()) && !checkFileName()) return false;
		if (allowLocalhost && !allowedIpAddresses.contains("127.0.0.1")) allowedIpAddresses.add("127.0.0.1");
		return true;
	}

	protected boolean checkFileName() {
		if ((allowedIpFileName == null || "".equals(allowedIpFileName)) && ! checkProperty()) return false;
		final File file = new File(allowedIpFileName);
		if (!file.exists() || !file.isFile() || file.length() < 7) return false;
		BufferedReader in = null;
		final List<String> list = new LinkedList<String>();
		try {
			in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null) {
				if (!line.startsWith("#")) list.add(line);
			}
			return true;
		} catch (final Throwable t) {
			return false;
		} finally {
			this.setAllowedIpAddresses(list);
			if (in != null) try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

	protected boolean checkProperty() {
		if (allowedIpFilePropertyName == null || "".equals(allowedIpFilePropertyName)) return false;
		String propVal = System.getProperty(allowedIpFilePropertyName);
		if (propVal == null || "".equals(propVal)) propVal = System.getenv(allowedIpFilePropertyName);
		if (propVal == null || "".equals(propVal)) return false;
		allowedIpFileName = propVal;
		return true;
	}

//	@Override
//	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
//		if (authentication == null) return null;
//		if (!HeaderAuthToken.class.isAssignableFrom(authentication.getClass())) return null;
//		final HeaderAuthToken hat = (HeaderAuthToken) authentication;
//		final List<String> remoteAddr = hat.getRemoteHostIpAddress();
//		final List<String> temp = new ArrayList<String>(remoteAddr.size());
//		temp.addAll(remoteAddr);
//		remoteAddr.retainAll(allowedIpAddresses);
//		if (temp.size() == 0) {
//			LOG.info("Rejecting request due to unauthorized source host. " + remoteAddr);
//			throw new IllegalArgumentException("Request does not come from an allowed host.");
//		}
//		final Map<String, String> map = hat.getAuthHeaders();
//		if (map.isEmpty()) return null;
//		final String[] idToLookup = hat.getBestId();
//		if ("".equals(idToLookup[1])) throw new IllegalArgumentException("Unable to find correct token header.");;
//		try {
//			final IdentityDetails details = (IdentityDetails) iddao.loadUserByUsername(idToLookup[1]);
//			return new HeaderAuthResult(details, idToLookup[0]);
//		} catch (final Throwable t) {
//			LOG.error(String.format("Error processing authentication: '%s' '%s'.", idToLookup[0], idToLookup[1]), t);
//			throw new IllegalArgumentException("Unable to process authentication.", t);
//		}
//	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final TreeSet<String> remoteAddresses = new TreeSet<String>();
		final String remoteAddr = request.getRemoteAddr();
		remoteAddresses.add(remoteAddr);
		if (request instanceof HttpServletRequest) {
			remoteAddresses.addAll(HeaderAuthUtils.extractRemoteIpAddress((HttpServletRequest) request));
		}
		if (Sets.intersection(remoteAddresses, allowedIpAddresses).size() > 0) {
			chain.doFilter(request, response);
		} else {
			LOG.info("Rejecting request from unauthorized remote IP address: " + remoteAddr + ".");
			throw new IllegalArgumentException("The request was not made through a trusted proxy.");
		}
	}
}
