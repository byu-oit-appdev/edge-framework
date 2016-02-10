package edu.byu.security.identity;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Wyatt Taylor on 2/8/16.
 */
public class CoreGroGrantedAuthority implements GrantedAuthority {

	private final String groRole;

	public CoreGroGrantedAuthority(final String groRole) {
		this.groRole = groRole == null ? "" : groRole;
	}

	public String getGroRole() {
		return groRole;
	}

	@Override
	public String getAuthority() {
		return groRole;
	}

	@Override
	public String toString() {
		return "CoreGrantedAuthority{" +
				"groRole='" + groRole + '\'' +
				'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CoreGroGrantedAuthority authority = (CoreGroGrantedAuthority) o;

		return groRole.equals(authority.groRole);
	}

	@Override
	public int hashCode() {
		return groRole.hashCode();
	}
}
