package edu.byu.security.grantedAuthority;

import edu.byu.edge.coreIdentity.domain.CoreIdentity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Implementors should be able to load granted authorities from any typical ID from the CoreIdentity, such as person_id, byu_id, or net_id.
 *
 * Created by Wyatt Taylor on 8/24/16.
 */
public interface GrantedAuthorityLoader {
	/**
	 *
	 * @param identity the identity
	 * @return A collection of granted authorities.
	 */
	public Collection<? extends GrantedAuthority> loadGrantedAuthoritiesForIdentity(CoreIdentity identity);
}
