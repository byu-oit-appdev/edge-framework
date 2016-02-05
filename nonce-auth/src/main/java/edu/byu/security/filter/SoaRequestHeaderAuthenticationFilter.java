package edu.byu.security.filter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wct5 on 1/20/16.
 */
public class SoaRequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	private static final Logger LOG = Logger.getLogger(SoaRequestHeaderAuthenticationFilter.class);

	private static final List<String> VALID_HEADERS = Arrays.asList("SM_USER", "ACTOR_ID", "PRINCIPLE_ID");

	public SoaRequestHeaderAuthenticationFilter() {
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(final HttpServletRequest request) {
		final List<String> list = Lists.newArrayList(
				Collections2.filter(
						Lists.transform(VALID_HEADERS, new HdrNameToVal(request)
						), VALID_STRING_PRED
				)
		);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(final HttpServletRequest request) {
		return getPreAuthenticatedPrincipal(request);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
	}

	private static final Predicate<String> VALID_STRING_PRED = new Predicate<String>() {
		@Override
		public boolean apply(final String input) {
			return input != null && !"".equals(input);
		}
	};

	private static class HdrNameToVal implements Function<String, String> {

		private final HttpServletRequest request;

		private HdrNameToVal(final HttpServletRequest request) {
			this.request = request;
		}

		@Override
		public String apply(final String input) {
			return request.getHeader(input);
		}
	}

}
