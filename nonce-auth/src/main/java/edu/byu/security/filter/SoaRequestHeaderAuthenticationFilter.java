package edu.byu.security.filter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by wct5 on 1/20/16.
 */
public class SoaRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	private static final Logger LOG = Logger.getLogger(SoaRequestHeaderAuthenticationFilter.class);

	private static final List<String> VALID_HEADERS = Arrays.asList("SM_USER", "ACTOR_ID", "PRINCIPLE_ID");
	public static final Set<String> RAW_HEADERS = Collections.unmodifiableSet(new HashSet<String>(VALID_HEADERS));

	public SoaRequestHeaderAuthenticationFilter() {
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(final HttpServletRequest request) {
		final String[] arr = determineValidHeader(request);
		return arr == null ? null : arr[1];
	}

	@Override
	protected Object getPreAuthenticatedCredentials(final HttpServletRequest request) {
		final String[] arr = determineValidHeader(request);
		return arr == null ? null : arr[0];
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

	private static String[] determineValidHeader(final HttpServletRequest request) {
		final List<String[]> list = newArrayList(filter(Lists.transform(VALID_HEADERS, new HdrNameToVal(request)), VALID_STRING_PRED));
		return list.isEmpty() ? null : list.get(0);
	}

	private static final Predicate<String[]> VALID_STRING_PRED = new Predicate<String[]>() {
		@Override
		public boolean apply(final String[] input) {
			return input != null && !"".equals(input[1]);
		}
	};

	private static class HdrNameToVal implements Function<String, String[]> {

		private final HttpServletRequest request;

		private HdrNameToVal(final HttpServletRequest request) {
			this.request = request;
		}

		@Override
		public String[] apply(final String input) {
			return new String[]{input, request.getHeader(input)};
		}
	}

}
