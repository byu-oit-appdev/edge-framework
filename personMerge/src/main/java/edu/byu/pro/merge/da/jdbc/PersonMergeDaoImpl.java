package edu.byu.pro.merge.da.jdbc;

import edu.byu.pro.merge.da.PersonMergeDao;
import edu.byu.pro.merge.da.mapper.PersonMergeMapper;
import edu.byu.pro.merge.domain.PersonMerge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
  *
  */
@Repository("personMergeDao")
public class PersonMergeDaoImpl extends BaseDaoImpl implements PersonMergeDao {

	protected final NamedParameterJdbcTemplate jdbc;
	protected final RowMapper<PersonMerge> mapper;
	protected final ResultSetExtractor<PersonMerge> objectExtractor;
	protected final ResultSetExtractor<List<PersonMerge>> listExtractor;

	@Autowired
	public PersonMergeDaoImpl(
			@Qualifier("<jdbcTemplateName>") NamedParameterJdbcTemplate jdbc,
			@Qualifier("personMergeMapper") PersonMergeMapper mapper
	) {
		this.jdbc = jdbc;
		this.mapper = mapper;
		this.objectExtractor = mapper.EXTRACTOR;
		this.listExtractor = mapper.LIST_EXTRACTOR;
	}

	@Override
	public List<PersonMerge> findAllMergesForSubscriber(final String subscriber) {
		final Map<String, Object> map = paramMap();
		map.put("subscriber", subscriber);
		return jdbc.query(SQL_SELECT, map, listExtractor);
	}

	private static final String SQL_SELECT = "select pm.*, " +
			"old.BYU_ID as OLD_BYU_ID, old.NET_ID as OLD_NET_ID, old.SORT_NAME as OLD_SORT_NAME, old.REST_OF_NAME as OLD_REST_OF_NAME, " +
			"old.SURNAME as OLD_SURNAME, old.PREFERRED_FIRST_NAME as OLD_PREFERRED_FIRST_NAME, " +
			"new.BYU_ID as NEW_BYU_ID, new.NET_ID as NEW_NET_ID, new.SORT_NAME as NEW_SORT_NAME, new.REST_OF_NAME as NEW_REST_OF_NAME, " +
			"new.SURNAME as NEW_SURNAME, new.PREFERRED_FIRST_NAME as NEW_PREFERRED_FIRST_NAME " +
			"from PRO.PERSON_MERGE pm " +
			"left join PRO.PERSON old on pm.OLD_PERSON_ID = old.PERSON_ID " +
			"left join PRO.PERSON new on pm.NEW_PERSON_ID = new.PERSON_ID " +
			"where pm.SUBSCRIBER = :subscriber " +
			"order by pm.DATE_TIME_UPDATED";

}
