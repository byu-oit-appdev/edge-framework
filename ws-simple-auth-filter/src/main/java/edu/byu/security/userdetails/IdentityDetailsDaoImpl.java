package edu.byu.security.userdetails;

import edu.byu.security.common.GrantedAuthorityComparator;
import edu.byu.security.common.GrantedAuthorityProvider;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/10/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/10/2013
 */
public class IdentityDetailsDaoImpl implements IdentityDetailsDao {

	private static final Logger LOG = Logger.getLogger(IdentityDetailsDaoImpl.class);

	protected static final IdentityDetailsResultSetExtractor RSE = new IdentityDetailsResultSetExtractor();

	protected final NamedParameterJdbcOperations jdbc;
	protected List<GrantedAuthorityProvider> gaps;

	protected final String SQL_PERSON = "select p.PERSON_ID, p.NET_ID, p.BYU_ID, p.PREFERRED_FIRST_NAME, p.SURNAME, p.SURNAME_POSITION, p.REST_OF_NAME, e.EMAIL_ADDRESS from PERSON p left join EMAIL_ADDRESS e on p.PERSON_ID = e.PERSON_ID where :id in (p.PERSON_ID, p.NET_ID, p.BYU_ID)";

	public IdentityDetailsDaoImpl(final NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	public void setGaps(final Collection<GrantedAuthorityProvider> gaps) {
		if (gaps == null) this.gaps = new ArrayList<GrantedAuthorityProvider>(0);
		else this.gaps = new ArrayList<GrantedAuthorityProvider>(gaps);
	}

	@Override
	public UserDetails loadUserDetails(final Authentication token) throws UsernameNotFoundException {
		LOG.debug("Token is " + token);
		return getDetails((String) token.getPrincipal());
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return getDetails(username);
	}

	@Override
	public IdentityDetails loadIdentityDetailsByUsername(final String username) {
		return (IdentityDetails) loadUserByUsername(username);
	}

	private IdentityDetails getDetails(final String id) {
		final HashMap<String, Object> map = new HashMap<String, Object>(2, .999999f);
		map.put("id", id);
		final IdentityDetails result = jdbc.query(SQL_PERSON, map, RSE);
		if (result == null) {
			throw new UsernameNotFoundException("The username provided (" + id + ") is not found.");
		}
		final List<GrantedAuthority> list = new LinkedList<GrantedAuthority>();
		for (final GrantedAuthorityProvider g : gaps) {
			list.addAll(g.getGrantedAuthorities(result.getPersonId()));
		}
		final GrantedAuthorityComparator comparator = new GrantedAuthorityComparator();
		final TreeSet<GrantedAuthority> set = new TreeSet<GrantedAuthority>(comparator);
		set.addAll(list);
		result.setAuthorities(set);
		return result;
	}

	private static class IdentityDetailsResultSetExtractor implements ResultSetExtractor<IdentityDetails> {

		@Override
		public IdentityDetails extractData(final ResultSet rs) throws SQLException, DataAccessException {
			if (rs.next()) return mapRow(rs, 0);
			else return null;
		}

		public IdentityDetails mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final IdentityDetails identityDetails = new IdentityDetails();
			identityDetails.setPersonId(rs.getString("PERSON_ID"));
			identityDetails.setByuId(rs.getString("BYU_ID"));
			identityDetails.setNetId(rs.getString("NET_ID"));
			identityDetails.setEmailAddress(rs.getString("EMAIL_ADDRESS"));
			identityDetails.setFirstName(rs.getString("PREFERRED_FIRST_NAME"));
			identityDetails.setSurname(rs.getString("SURNAME"));
			identityDetails.setSurnamePosition(rs.getString("SURNAME_POSITION"));
			return identityDetails;
		}
	}
}
