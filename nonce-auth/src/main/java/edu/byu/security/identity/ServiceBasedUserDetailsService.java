package edu.byu.security.identity;

import edu.byu.edge.coreIdentity.client.CoreIdentityClient;
import edu.byu.edge.coreIdentity.client.MemberOfClient;
import edu.byu.edge.coreIdentity.domain.CoreIdentity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wct5 on 2/5/16.
 */
public class ServiceBasedUserDetailsService implements UserDetailsService, InitializingBean {

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
		final CoreIdentity coreIdentity = identityClient.getCoreIdentityByNetId(username);
		if (coreIdentity == null) throw new UsernameNotFoundException("Unable to retrieve details for '" + username + "'.");
		final List<GrantedAuthority> grantedAuthorities = new LinkedList<GrantedAuthority>();
		for (final String groupid : groupsToCheck) {
			try {
				if (memberOfClient.isPersonMemberOfGroup(coreIdentity.getPersonId(), groupid)) {
					grantedAuthorities.add(new CoreGroGrantedAuthority("GRO_" + groupid));
				}
			} catch (Exception e) {
				LOG.error("Error determining GRO membership: " + coreIdentity.getNetId() + ":" + groupid, e);
			}
		}
		return new UserIdentity(coreIdentity, grantedAuthorities);
	}
}
