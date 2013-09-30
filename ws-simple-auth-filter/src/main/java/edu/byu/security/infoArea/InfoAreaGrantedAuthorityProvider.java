package edu.byu.security.infoArea;

import edu.byu.security.common.GrantedAuthorityProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/13/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/13/2013
 */
public class InfoAreaGrantedAuthorityProvider implements GrantedAuthorityProvider<InfoAreaGrantedAuthority> {

	protected final NamedParameterJdbcTemplate jdbc;
	protected final RowMapper<InfoAreaGrantedAuthority> rm;
	protected final ResultSetExtractor<List<InfoAreaGrantedAuthority>> rse;

	public InfoAreaGrantedAuthorityProvider(final NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
		rm = new InfoAreaRowMapper();
		rse = new InfoAreaExtractor(rm);
	}

	protected final String SQL_SELECT = "select PERSON_ID, INFORMATIONAL_AREA, CREDIT_INSTITUTION, to_char(EFFECTIVE_DATE, 'YYYY-MM-DD') as EFFECTIVE_DATE, " +
			"LIMITATION_TYPE, LIMITATION_VALUE, to_char(EXPIRED_DATE, 'YYYY-MM-DD') as EXPIRED_DATE, UPDATE_TYPE " +
			"from PRO.USER_AUTHORIZATION_VW " +
			"where PERSON_ID = :personId and CREDIT_INSTITUTION = 'BYU PROVO' and sysdate > EFFECTIVE_DATE and (EXPIRED_DATE is null or EXPIRED_DATE > sysdate) " +
			"order by EFFECTIVE_DATE";

	@Override
	public List<InfoAreaGrantedAuthority> getGrantedAuthorities(final String personId) {
		final HashMap<String, Object> map = new HashMap<String, Object>(2, .999999f);
		map.put("personId", personId);
		return jdbc.query(SQL_SELECT, map, rse);
	}


	private static class InfoAreaRowMapper implements RowMapper<InfoAreaGrantedAuthority> {

		private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>();

		private Date stringToDate(final String val) {
			try {
				if (val == null || "".equals(val)) return null;
				if (sdf.get() == null) sdf.set(new SimpleDateFormat("yyyy-MM-dd"));
				return sdf.get().parse(val);
			} catch (final ParseException e) {
				return null;
			} catch (final NullPointerException e) {
				return new Date();
			}
		}


		@Override
		public InfoAreaGrantedAuthority mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final InfoAreaGrantedAuthority x = new InfoAreaGrantedAuthority();
			x.setCreditInstitution(rs.getString("CREDIT_INSTITUTION"));
			x.setEffectiveDate(stringToDate(rs.getString("EFFECTIVE_DATE")));
			x.setExpiredDate(stringToDate(rs.getString("EXPIRED_DATE")));
			x.setInformationalArea(rs.getString("INFORMATIONAL_AREA"));
			x.setLimitationType(rs.getString("LIMITATION_TYPE"));
			x.setLimitationValue(rs.getString("LIMITATION_VALUE"));
			x.setPersonId(rs.getString("PERSON_ID"));
			x.setUpdateType(InfoAreaGrantedAuthority.UpdateType.fromValue(rs.getString("UPDATE_TYPE")));
			return x;
		}
	}

	private static class InfoAreaExtractor implements ResultSetExtractor<List<InfoAreaGrantedAuthority>> {

		private final RowMapper<InfoAreaGrantedAuthority> rm;

		private InfoAreaExtractor(final RowMapper<InfoAreaGrantedAuthority> rm) {
			this.rm = rm;
		}

		@Override
		public List<InfoAreaGrantedAuthority> extractData(final ResultSet rs) throws SQLException, DataAccessException {
			final List<InfoAreaGrantedAuthority> list = new LinkedList<InfoAreaGrantedAuthority>();
			int i = 0;
			while (rs.next()) list.add(rm.mapRow(rs, ++i));
			return new ArrayList<InfoAreaGrantedAuthority>(list);
		}
	}
}
