package edu.byu.auth.header;

import edu.byu.security.userdetails.IdentityDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/10/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/10/2013
 */
public class HeaderAuthResult extends AbstractAuthenticationToken implements Authentication {

	private static final long serialVersionUID = 1L;

	private IdentityDetails id;
	private String headerUsed;

	public HeaderAuthResult(final IdentityDetails id, final String headerUsed, final boolean authenticated) {
		super(id.getAuthorities());
		this.id = id;
		this.headerUsed = headerUsed;
		super.setAuthenticated(authenticated);
	}

	@Override
	public Object getCredentials() {
		return headerUsed;
	}

	@Override
	public Object getPrincipal() {
		return id;
	}
}
