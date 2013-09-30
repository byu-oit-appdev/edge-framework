package edu.byu.auth.header;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

import java.util.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/21/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/21/2013
 */
public class HeaderAuthToken extends AbstractAuthenticationToken implements Authentication, CredentialsContainer {

	private static final long serialVersionUID = 1L;

	protected final Map<String, String> authHeaders;
	protected final String[] bestId;

	public HeaderAuthToken(final Map<String, String> authHeaders) {
		super(null);
		this.authHeaders = Collections.unmodifiableMap(new HashMap<String, String>(authHeaders));
		this.bestId = HeaderAuthUtils.findBestId(this.authHeaders);
	}

	public Map<String, String> getAuthHeaders() {
		return authHeaders;
	}

	public String[] getBestId() {
		return bestId;
	}

	@Override
	public Object getCredentials() {
		return bestId[0];
	}

	@Override
	public Object getPrincipal() {
		return bestId[1];
	}

	@Override
	public String toString() {
		return "HeaderAuthToken{" +
				"authHeaders=" + authHeaders.toString() +
				", bestId=" + Arrays.toString(bestId) +
				'}';
	}
}
