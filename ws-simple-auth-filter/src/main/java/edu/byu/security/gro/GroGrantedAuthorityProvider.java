package edu.byu.security.gro;

import edu.byu.security.common.GrantedAuthorityProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/14/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/14/2013
 */
public class GroGrantedAuthorityProvider implements GrantedAuthorityProvider<GroGrantedAuthority> {

	protected final NamedParameterJdbcTemplate jdbc;
	protected final RowMapper<GroGrantedAuthority> rm;
	protected final ResultSetExtractor<List<GroGrantedAuthority>> rse;

	public GroGrantedAuthorityProvider(final NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
		rm = new GroGrantedAuthorityRowMapper();
		rse = new GroGrantedAuthorityExtractor(rm);
	}

	protected final String SQL_SELECT = "select GROUP_ID from PERSON_GROUP where PERSON_ID = :personId";

	@Override
	public List<GroGrantedAuthority> getGrantedAuthorities(final String personId) {
		final HashMap<String, Object> map = new HashMap<String, Object>(2, .999999f);
		map.put("personId", personId);
		return jdbc.query(SQL_SELECT, map, rse);
	}

	private static class GroGrantedAuthorityRowMapper implements RowMapper<GroGrantedAuthority> {
		@Override
		public GroGrantedAuthority mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final GroGrantedAuthority x = new GroGrantedAuthority();
			x.setGroupName(rs.getString("GROUP_ID"));
			return x;
		}
	}

	private static class GroGrantedAuthorityExtractor implements ResultSetExtractor<List<GroGrantedAuthority>> {

		private final RowMapper<GroGrantedAuthority> rm;

		private GroGrantedAuthorityExtractor(final RowMapper<GroGrantedAuthority> rm) {
			this.rm = rm;
		}

		@Override
		public List<GroGrantedAuthority> extractData(final ResultSet rs) throws SQLException, DataAccessException {
			final List<GroGrantedAuthority> list = new LinkedList<GroGrantedAuthority>();
			int i = 0;
			while (rs.next()) list.add(rm.mapRow(rs, ++i));
			return new ArrayList<GroGrantedAuthority>(list);
		}
	}
}
