package edu.byu.pro.merge.da.mapper;

import edu.byu.pro.merge.domain.PersonMerge;
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
@Service("personMergeMapper")
public class PersonMergeMapper implements RowMapper<PersonMerge> {

	public final ResultSetExtractor<PersonMerge> EXTRACTOR;
	public final ResultSetExtractor<List<PersonMerge>> LIST_EXTRACTOR;

	public PersonMergeMapper() {
		EXTRACTOR = new ObjectResultSetExtractor<PersonMerge>(this);
		LIST_EXTRACTOR = new ListResultSetExtractor<PersonMerge>(this);
	}

	@Override
	public PersonMerge mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final PersonMerge r = new PersonMerge();

		r.setSubscriber(rs.getString("SUBSCRIBER"));

		r.setOldPersonId(rs.getString("OLD_PERSON_ID"));

		r.setNewPersonId(rs.getString("NEW_PERSON_ID"));

		r.setDateTimeUpdated(rs.getTimestamp("DATE_TIME_UPDATED"));

		r.setUpdatedById(rs.getString("UPDATED_BY_ID"));

		r.setSourceInstitution(rs.getString("SOURCE_INSTITUTION"));
		if (rs.wasNull()) r.setSourceInstitution("");

		r.setSourceApplication(rs.getString("SOURCE_APPLICATION"));
		if (rs.wasNull()) r.setSourceApplication("");

		r.setSourceFunction(rs.getString("SOURCE_FUNCTION"));
		if (rs.wasNull()) r.setSourceFunction("");

		r.setOldByuId(rs.getString("OLD_BYU_ID"));
		if (rs.wasNull()) r.setOldByuId("");

		r.setOldNetId(rs.getString("OLD_NET_ID"));
		if (rs.wasNull()) r.setOldNetId("");

		r.setOldSortName(rs.getString("OLD_SORT_NAME"));
		if (rs.wasNull()) r.setOldSortName("");

		r.setOldRestOfName(rs.getString("OLD_REST_OF_NAME"));
		if (rs.wasNull()) r.setOldRestOfName("");

		r.setOldSurname(rs.getString("OLD_SURNAME"));
		if (rs.wasNull()) r.setOldSurname("");

		r.setOldPreferredFirstName(rs.getString("OLD_PREFERRED_FIRST_NAME"));
		if (rs.wasNull()) r.setOldPreferredFirstName("");

		r.setNewByuId(rs.getString("NEW_BYU_ID"));
		if (rs.wasNull()) r.setNewByuId("");

		r.setNewNetId(rs.getString("NEW_NET_ID"));
		if (rs.wasNull()) r.setNewNetId("");

		r.setNewSortName(rs.getString("NEW_SORT_NAME"));
		if (rs.wasNull()) r.setNewSortName("");

		r.setNewRestOfName(rs.getString("NEW_REST_OF_NAME"));
		if (rs.wasNull()) r.setNewRestOfName("");

		r.setNewSurname(rs.getString("NEW_SURNAME"));
		if (rs.wasNull()) r.setNewSurname("");

		r.setNewPreferredFirstName(rs.getString("NEW_PREFERRED_FIRST_NAME"));
		if (rs.wasNull()) r.setNewPreferredFirstName("");


		return r;
	}
}
