/*
 * Filename: AthensVoter
 * Created: Oct 14, 2008
 */
package edu.byu.security.vote;

import edu.byu.security.infoArea.InfoAreaExpressionMethodProvider;
import edu.byu.security.infoArea.InfoAreaGrantedAuthority;
import edu.byu.security.infoArea.InfoAreaGrantedAuthorityParseException;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * The InfoAreaVoter is an ExtendedVoter that operates on InfoAreaGrantedAuthority authorizations.
 *
 * @author Tyler Southwick
 */
public final class InfoAreaVoter implements ExtendedVoter {

	/**
	 * Log4J logger used by this class.
	 * <p/>
	 * The logger name is the class name.
	 */
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean supports(final ConfigAttribute configAttribute) {
		return InfoAreaGrantedAuthority.isAthensGrantedAuthority(configAttribute.getAttribute());
	}

	/**
	 * This voter supports all types of classes.
	 *
	 * @param klass Class to determine if this voter supports.
	 * @return always <code>true</code>
	 */
	public boolean supports(final Class klass) {
		return true;
	}

	@Override
	public int vote(final Authentication authentication, final Object object, final Collection configAttributes) {
		int result = AccessDecisionVoter.ACCESS_ABSTAIN;
		final Collection<ConfigAttribute> calist = configAttributes;
		for (ConfigAttribute configAttribute : calist) {
			if (this.supports(configAttribute)) {
				result = AccessDecisionVoter.ACCESS_DENIED;
				try {
					if (InfoAreaExpressionMethodProvider.hasInfoArea(authentication, configAttribute.getAttribute())) {
						return AccessDecisionVoter.ACCESS_GRANTED;
					}
				} catch (InfoAreaGrantedAuthorityParseException e) {
					logger.error("Failed to parse the given ConfigAttribute.", e);
				}
			}
		}
		return result;
	}

	@Override
	public boolean isAuthorized(final String authorityString) {
		try {
			return InfoAreaExpressionMethodProvider.hasInfoArea(authorityString);
		} catch (InfoAreaGrantedAuthorityParseException e) {
			logger.error("Failed to parse the given ConfigAttribute.", e);
		}
		return false;
	}

	@Override
	public void checkAuthorized(final String authorityString) {
		if (isAuthorized(authorityString)) {
			return;
		}

		throw new AccessDeniedException("Not authorized");
	}
}
