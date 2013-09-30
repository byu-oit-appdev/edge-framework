package edu.byu.security.vote;

import edu.byu.security.gro.GroExpressionMethodProvider;
import edu.byu.security.gro.GroGrantedAuthority;
import edu.byu.security.gro.GroGrantedAuthorityParseException;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 08/20/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 08/20/2013
 */
public class GroVoter implements ExtendedVoter {

	private static final Logger LOG = Logger.getLogger(GroVoter.class);

	@Override
	public boolean isAuthorized(final String permission) {
		return false;
	}

	@Override
	public void checkAuthorized(final String permission) {
	}

	@Override
	public boolean supports(final ConfigAttribute attribute) {
		return attribute != null && GroGrantedAuthority.isGroGrantedAuthority(attribute.getAttribute());
	}

	@Override
	public boolean supports(final Class clazz) {
		return true;
	}

	@Override
	public int vote(final Authentication authentication, final Object object, final Collection attributesCollection) {
		if (attributesCollection == null || attributesCollection.isEmpty()) return AccessDecisionVoter.ACCESS_ABSTAIN;
		int result = AccessDecisionVoter.ACCESS_ABSTAIN;
		final List<? extends GrantedAuthority> list = GroExpressionMethodProvider.extractGroGrantedAuthoritiesFromAuthentication(authentication);
		final List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>(attributesCollection);
		for (final ConfigAttribute a : attributes) {
			if (this.supports(a)) {
				result = AccessDecisionVoter.ACCESS_DENIED;
				try {
					if (GroExpressionMethodProvider.isMemberOfGroup(a.getAttribute(), list)) return AccessDecisionVoter.ACCESS_GRANTED;
				} catch (final GroGrantedAuthorityParseException e) {
					LOG.error("Failed to parse the given ConfigAttribute: " + a, e);
				}
			}
		}
		return result;
	}
}
