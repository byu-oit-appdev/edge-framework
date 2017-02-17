package edu.byu.security.identity;

import edu.byu.edge.coreIdentity.domain.CoreIdentity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by wct5 on 2/8/16.
 */
public class UserIdentity implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;

	private final CoreIdentity coreIdentity;
	private final Collection<? extends GrantedAuthority> grantedAuthorities;

	public UserIdentity(final CoreIdentity coreIdentity, final Collection<? extends GrantedAuthority> grantedAuthorities) {
		this.coreIdentity = coreIdentity;
		this.grantedAuthorities = grantedAuthorities;
	}

	public CoreIdentity getCoreIdentity() {
		return coreIdentity;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableCollection(grantedAuthorities);
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return coreIdentity.getNetId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof UserIdentity)) return false;

		UserIdentity identity = (UserIdentity) o;

		return coreIdentity != null ? coreIdentity.equals(identity.coreIdentity) : identity.coreIdentity == null;

	}

	@Override
	public int hashCode() {
		return coreIdentity != null ? coreIdentity.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "UserIdentity{" +
				"coreIdentity=" + coreIdentity +
				", grantedAuthorities=" + grantedAuthorities +
				'}';
	}
}
