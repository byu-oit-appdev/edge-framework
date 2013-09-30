package edu.byu.auth.header;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/21/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/21/2013
 */
public class HeaderAuthUtils {

	/** A RegEx pattern for net-ids */
	public static final Pattern NET_ID_PATTERN = Pattern.compile("^[a-z]{1}[a-z0-9]*?$");

	/** A list of header names allowed by this authentication mechanism */
	public static final List<AuthHeaders> HEADER_NAMES;
	static {
		final List<AuthHeaders> list = Arrays.asList(AuthHeaders.values());
		Collections.sort(list, new Comparator<AuthHeaders>() {
			@Override
			public int compare(final AuthHeaders o1, final AuthHeaders o2) {
				return o1.getOrder() - o2.getOrder();
			}
		}
		);
		HEADER_NAMES = Collections.unmodifiableList(list);
	}

	/**
	 * Identifies the best header/id to use as the operator/actor/principal. This determines what identity is placed in the security context.
	 *
	 * Returns a String array of two empty strings if no usable header is found.
	 *
	 * @param map a map of headers
	 * @return the best header (name and value)
	 */
	public static String[] findBestId(final Map<String, String> map) {
		for (final AuthHeaders headerName : HEADER_NAMES) {
			if (map.containsKey(headerName.getHeaderName())) return new String[]{headerName.getHeaderName(), map.get(headerName.getHeaderName())};
		}
		return new String[]{"", ""};
	}

	/**
	 * Extracts headers from request that are relevant to this authentication mechanism. This method will not return a null value.
	 *
	 * @param req the request
	 * @return a map containing the request headers where the key is the header name and the value is the header value
	 */
	public static Map<String, String> extractAuthHeaders(final HttpServletRequest req) {
		final Map<String, String> result = new HashMap<String, String>(HEADER_NAMES.size() + 1, .999999f);
		for (final AuthHeaders ah : HEADER_NAMES) {
			final String hn = ah.getHeaderName();
			final String hv = req.getHeader(hn);
			if (hv != null && !"".equals(hv)) result.put(hn, hv);
		}
		return result;
	}

	public static List<String> extractRemoteIpAddress(final HttpServletRequest req) {
		final List<String> list = new LinkedList<String>();
		list.add(req.getRemoteAddr());
		final Enumeration en = req.getHeaders("X-Forwarded-For");
		while (en.hasMoreElements()) {
			String s = (String) en.nextElement();
			if (s != null && !"".equals(s)) list.add(s);
		}
		return Collections.unmodifiableList(new ArrayList<String>(list));
	}

	private HeaderAuthUtils() {
	}

}
