package edu.byu.pro.merge.da.mapper;

import edu.byu.pro.merge.domain.PersonQueue;
import edu.byu.edge.jdbc.*;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
  *
  */
@Service("personQueueMapper")
public class PersonQueueMapper implements RowMapper<PersonQueue> {

	public final ResultSetExtractor<PersonQueue> EXTRACTOR;
	public final ResultSetExtractor<List<PersonQueue>> LIST_EXTRACTOR;

	public PersonQueueMapper() {
		EXTRACTOR = new ObjectResultSetExtractor<PersonQueue>(this);
		LIST_EXTRACTOR = new ListResultSetExtractor<PersonQueue>(this);
	}

	@Override
	public PersonQueue mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final PersonQueue r = new PersonQueue();

		r.setPersonId(rs.getString("PERSON_ID"));
		if (rs.wasNull()) r.setPersonId("");

		r.setSubscriber(rs.getString("SUBSCRIBER"));
		if (rs.wasNull()) r.setSubscriber("");

		r.setDateTimeUpdated(rs.getTimestamp("DATE_TIME_UPDATED"));
		if (rs.wasNull()) r.setDateTimeUpdated(null);

		r.setUpdatedById(rs.getString("UPDATED_BY_ID"));

		r.setReturnCode(rs.getInt("RETURN_CODE"));
		if (rs.wasNull()) r.setReturnCode(null);

		r.setStatusCode(rs.getInt("STATUS_CODE"));
		if (rs.wasNull()) r.setStatusCode(null);

		r.setNetId(rs.getString("NET_ID"));
		if (rs.wasNull()) r.setNetId("");

		return r;
	}
}
