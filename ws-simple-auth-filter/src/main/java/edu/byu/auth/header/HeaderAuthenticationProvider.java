package edu.byu.auth.header;

import edu.byu.security.userdetails.IdentityDetailsDao;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/12/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/12/2013
 */
public class HeaderAuthenticationProvider implements AuthenticationProvider {

	protected IdentityDetailsDao identityDetailsDao;

	public void setIdentityDetailsDao(final IdentityDetailsDao identityDetailsDao) {
		this.identityDetailsDao = identityDetailsDao;
	}

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) return null;
		final HeaderAuthToken hat = (HeaderAuthToken) authentication;
		return new HeaderAuthResult(identityDetailsDao.loadIdentityDetailsByUsername((String) hat.getPrincipal()), (String) hat.getCredentials(), true);
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return HeaderAuthToken.class.isAssignableFrom(authentication);
	}
}
