package edu.byu.auth.header;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/24/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/24/2013
 */
public class BasicAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException {
//		SecurityContextHolder.getContext().setAuthentication(authentication);
		request.setAttribute("isAuthenticated", true);
	}
}
