package edu.byu.pro.email.dao;

import edu.byu.edge.jdbc.ListResultSetExtractor;
import edu.byu.edge.jdbc.ObjectResultSetExtractor;
import edu.byu.pro.email.EmailAddressDao;
import edu.byu.pro.email.ProEmailAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyatt Taylor on 1/10/17.
 */
@Repository("proEmailAddressDao")
@Transactional(readOnly = true, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW, value = "CESTransactionManager")
public class EmailAddressDaoImpl implements EmailAddressDao {

	protected final NamedParameterJdbcTemplate jdbcTemplate;
	protected final ResultSetExtractor<ProEmailAddress> objectExtractor;
	protected final ResultSetExtractor<List<ProEmailAddress>> listExtractor;

	@Autowired
	public EmailAddressDaoImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		final ProEmailAddressRowMapper mapper = new ProEmailAddressRowMapper();
		this.objectExtractor = new ObjectResultSetExtractor<ProEmailAddress>(mapper);
		this.listExtractor = new ListResultSetExtractor<ProEmailAddress>(mapper);
	}

	@Override
	public ProEmailAddress getEmailAddressRec(final String personId) {
		final Map<String, Object> params = new HashMap<String, Object>(2, .999999f);
		params.put("personId", personId);
		return jdbcTemplate.query(SQL_SELECT, params, objectExtractor);
	}

	@Override
	public String getBasicEmailAddress(final String personId) {
		final ProEmailAddress rec = getEmailAddressRec(personId);
		if (rec == null) {
			return null;
		} else if (rec.getEmailAddress() != null) {
			return rec.getEmailAddress();
		} else {
			return rec.getWorkEmailAddress();
		}
	}

	@Override
	public String getWorkEmailAddress(final String personId) {
		final ProEmailAddress rec = getEmailAddressRec(personId);
		if (rec == null) {
			return null;
		} else if (rec.getWorkEmailAddress() != null) {
			return rec.getWorkEmailAddress();
		} else {
			return rec.getEmailAddress();
		}
	}

	private static final String SQL_SELECT = "select PERSON_ID, trim(EMAIL_ADDRESS) as EMAIL_ADDRESS, trim(WORK_EMAIL_ADDRESS) as WORK_EMAIL_ADDRESS " +
			"from PRO.EMAIL_ADDRESS " +
			"where PERSON_ID = :personId";

	private static class ProEmailAddressRowMapper implements RowMapper<ProEmailAddress> {
		@Override
		public ProEmailAddress mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new ProEmailAddress(
					rs.getString("PERSON_ID"),
					rs.getString("EMAIL_ADDRESS"),
					rs.getString("WORK_EMAIL_ADDRESS")
			);
		}
	}

}
