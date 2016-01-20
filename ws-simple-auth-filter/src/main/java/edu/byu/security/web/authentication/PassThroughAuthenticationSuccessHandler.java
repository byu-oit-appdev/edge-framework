package edu.byu.security.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The PassThroughAuthenticationSuccessHandler is an AuthenticationSuccessHandler that allows the request to continue to the destination with out interruption.
 * <p/>
 * When dealing with certain types of authentication it is necessary to allow the authentication to take place on each request, allowing the
 * request to continue to it's destination when successfully authenticated. For example web service calls are stateless and authenticate on each
 * request.
 *
 * @author Andrew Largey
 * @since 1.2.0.0
 */
public final class PassThroughAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	/**
	 * Name of the request attribute set by this handler to facilitate pass through.
	 */
	public static final String AUTHENTICATION_PASS_THROUGH = "byu.oit.authentication.pass.through";

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
		request.setAttribute(AUTHENTICATION_PASS_THROUGH, Boolean.TRUE);
	}
}
