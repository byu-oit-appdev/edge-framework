package edu.byu.auth.header;

import edu.byu.security.userdetails.IdentityDetails;
import edu.byu.security.userdetails.IdentityDetailsDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/07/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/07/2013
 */
public class HeaderAuthenticationFilter extends AbstractAuthenticationProcessingFilter implements InitializingBean {

	private static final Logger LOG = Logger.getLogger(HeaderAuthenticationFilter.class);

	private static final String DEFAULT_FILTER_URL = "/";

	protected IdentityDetailsDao iddao;

	public HeaderAuthenticationFilter() {
		this(DEFAULT_FILTER_URL);
	}

	public HeaderAuthenticationFilter(final String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	public void setIddao(final IdentityDetailsDao iddao) {
		this.iddao = iddao;
	}

	@Override
	public void afterPropertiesSet() {
		if (iddao == null) throw new IllegalStateException("An IdentityDetailsDao is required for this filter.");
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		final Map<String, String> map = HeaderAuthUtils.extractAuthHeaders(request);
		if (map == null || map.isEmpty()) throw new AuthenticationCredentialsNotFoundException("Acceptable authentication headers missing from request.");
		final String[] bestId = HeaderAuthUtils.findBestId(map);
		if ("".equals(bestId[1])) throw new AuthenticationCredentialsNotFoundException("No valid credential was found in the credential headers.");
		try {
			final IdentityDetails identityDetails = iddao.loadIdentityDetailsByUsername(bestId[1]);
			final HeaderAuthResult result = new HeaderAuthResult(identityDetails, bestId[0], true);
//			result.setAuthenticated(true);
			return result;
		} catch (final Throwable t) {
			throw new AuthenticationServiceException("Unable to process authentication.", t);
		}
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		super.doFilter(req, res, chain);
//		chain.doFilter(req, res);
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request,
			final HttpServletResponse response,
			final FilterChain chain,
			final Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

	/**
	 * Determines if the given httpServletRequest matches the filterProcessesUrl.
	 * @param httpServletRequest HttpServletRequest to match against the filterProcessesUrl.
	 * @return boolean true if the request path startes with the filterProcessesUrl, false otherwise.
	 */
	private boolean matchesFilterProcessesUrl(final HttpServletRequest httpServletRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("matchesFilterProcessesURL: " + getRequestPath(httpServletRequest).startsWith(getFilterProcessesUrl()));
		}
		return getRequestPath(httpServletRequest).startsWith(getFilterProcessesUrl());
	}

	/**
	 * Constructs the request path from the given httpServletRequest.
	 * @param httpServletRequest HttpServletRequest to build the request path from.
	 * @return request path which includes the servletPath and pathInfo fro the httpServletRequest.
	 */
	private String getRequestPath(final HttpServletRequest httpServletRequest) {
		StringBuilder path = new StringBuilder();
		String servletPath = httpServletRequest.getServletPath();
		String pathInfo = httpServletRequest.getPathInfo();

		if (servletPath != null && servletPath.length() > 0) {
			path.append(servletPath);
		}

		if (pathInfo != null && pathInfo.length() > 0) {
			path.append(pathInfo);
		}

		if (path.length() == 0) {
			path.append("/");
		}
		return path.toString();
	}

	private boolean supportsAuthentication(final HttpServletRequest httpServletRequest) {
		final Map<String, String> map = HeaderAuthUtils.extractAuthHeaders(httpServletRequest);
		return map != null && !map.isEmpty();
	}

	@Override
	protected boolean requiresAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
//		final boolean a = matchesFilterProcessesUrl(request);
		final boolean b = supportsAuthentication(request);
//		if (LOG.isDebugEnabled()) LOG.debug("RequiresAuthentication: matches(" + a + ") && supports(" + b + ")");
		if (LOG.isDebugEnabled()) LOG.debug("RequiresAuthentication: supports(" + b + ")");
//		return a && b;
		return b;
	}

	@Override
	protected boolean getAllowSessionCreation() {
		return false;
	}
}
