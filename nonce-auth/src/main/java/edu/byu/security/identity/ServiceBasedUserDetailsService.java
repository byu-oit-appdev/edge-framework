package edu.byu.security.identity;

import edu.byu.edge.coreIdentity.client.CoreIdentityClient;
import edu.byu.edge.coreIdentity.client.MemberOfClient;
import edu.byu.edge.coreIdentity.domain.CoreIdentity;
import edu.byu.security.filter.SoaRequestHeaderAuthenticationFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wct5 on 2/5/16.
 */
public class ServiceBasedUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService, InitializingBean {

	private static final Logger LOG = Logger.getLogger(ServiceBasedUserDetailsService.class);

	private List<String> groupsToCheck;
	private CoreIdentityClient identityClient;
	private MemberOfClient memberOfClient;

	public ServiceBasedUserDetailsService() {
	}

	public void setGroupsToCheck(final List<String> groupsToCheck) {
		this.groupsToCheck = groupsToCheck;
	}

	public void setIdentityClient(final CoreIdentityClient identityClient) {
		this.identityClient = identityClient;
	}

	public void setMemberOfClient(final MemberOfClient memberOfClient) {
		this.memberOfClient = memberOfClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {
		return resolveIdentity(username, "SM_USER");
	}

	@Override
	public UserDetails loadUserDetails(final Authentication token) throws UsernameNotFoundException {
		if (token == null) return null;
		if (PreAuthenticatedAuthenticationToken.class.isAssignableFrom(token.getClass())) {
			//this means that we're using the SoaRequestHeaderAuthenticationFilter
			//and also the credential tells us what header name and principal and cred are strings
			return resolveIdentity((String) token.getPrincipal(), (String) token.getCredentials());
		}
		LOG.error("Unable to process authentication token - " + token);
		return null;
	}

	private UserIdentity resolveIdentity(final String id, final String type) {
		final CoreIdentity coreIdentity = SoaRequestHeaderAuthenticationFilter.RAW_HEADERS.contains(type) ? getCoreIdentityFromHeader(id) : getCoreIdentityFromOauth();
		if (coreIdentity == null) throw new UsernameNotFoundException("Unable to retrieve details for '" + id + "'.");
		final List<GrantedAuthority> grantedAuthorities = new LinkedList<GrantedAuthority>();
		for (final String groupid : groupsToCheck) {
			try {
				if (memberOfClient.isPersonMemberOfGroup(coreIdentity.getPersonId(), groupid)) {
					grantedAuthorities.add(new CoreGroGrantedAuthority("GRO_" + groupid));
				}
			} catch (Exception e) {
				LOG.error("Error determining GRO membership: " + coreIdentity.getNetId() + ":" + groupid, e);
				throw new AuthenticationServiceException("Error determining GRO membership: " + coreIdentity.getNetId() + ":" + groupid, e);
			}
		}
		return new UserIdentity(coreIdentity, grantedAuthorities);
	}

	private CoreIdentity getCoreIdentityFromOauth() {
		return null;
	}

	private CoreIdentity getCoreIdentityFromHeader(final String id) {
		if (NET_ID.matcher(id).matches()) {
			return identityClient.getCoreIdentityByNetId(id);
		} else {
			return getCoreIdentityByNumeric(id);
		}
	}

	private CoreIdentity getCoreIdentityByNumeric(final String id) {
		if (PERSON_ID.matcher(id).matches()) {
			final CoreIdentity ci = identityClient.getCoreIdentityByPersonId(id);
			if (ci != null) {
				return ci;
			} else {
				return identityClient.getCoreIdentityByByuId(id);
			}
		} else if (BYU_ID.matcher(id).matches()) {
			return identityClient.getCoreIdentityByByuId(id);
		} else {
			throw new BadCredentialsException("Unable to determine id type for '" + id + "'.");
		}
	}

	private static final Pattern NET_ID = Pattern.compile("^[a-z]+?[a-z0-9]*$");
	private static final Pattern PERSON_ID = Pattern.compile("^[0-9]{3}2[0-9]{4}2$");
	private static final Pattern BYU_ID = Pattern.compile("^[0-9]{2}\\-?[0-9]{3}\\-?[0-9]{4}$");

}
