package edu.byu.security;

import edu.byu.edge.coreIdentity.client.CoreIdentityClient;
import edu.byu.edge.coreIdentity.domain.CoreIdentity;
import edu.byu.security.grantedAuthority.GrantedAuthorityLoader;
import edu.byu.security.identity.UserIdentity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Wyatt Taylor on 8/24/16.
 */
public class ServiceBasedAuthenticationUserDetailsService<T extends CasAssertionAuthenticationToken> implements AuthenticationUserDetailsService<T>, InitializingBean {

	protected CoreIdentityClient identityClient;
	protected List<GrantedAuthorityLoader> grantedAuthoritiesLoaders;

	public ServiceBasedAuthenticationUserDetailsService() {
	}

	public void setIdentityClient(final CoreIdentityClient identityClient) {
		this.identityClient = identityClient;
	}

	public void setGrantedAuthoritiesLoaders(final List<GrantedAuthorityLoader> grantedAuthoritiesLoaders) {
		this.grantedAuthoritiesLoaders = grantedAuthoritiesLoaders;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(identityClient, "An identity client is required.");
		if (grantedAuthoritiesLoaders == null) {
			grantedAuthoritiesLoaders = new ArrayList<>(1);
		}
	}

	@Override
	public UserDetails loadUserDetails(final T token) throws UsernameNotFoundException {
		final CoreIdentity id = identityClient.getCoreIdentityByNetId(token.getName());
		final Set<GrantedAuthority> set = new HashSet<>(64, .999999f);
		for (final GrantedAuthorityLoader loader : grantedAuthoritiesLoaders) {
			final Collection<? extends GrantedAuthority> c = loader.loadGrantedAuthoritiesForIdentity(id);
			if (c != null) {
				set.addAll(c);
			}
		}
		return new UserIdentity(id, set);
	}
}
