package edu.byu.pro.merge.da.jdbc;

import edu.byu.pro.merge.da.PersonQueueDao;
import edu.byu.pro.merge.da.mapper.PersonQueueMapper;
import edu.byu.pro.merge.domain.PersonQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
  *
  */
@Repository("personQueueDao")
public class PersonQueueDaoImpl extends BaseDaoImpl implements PersonQueueDao {

	protected final NamedParameterJdbcTemplate jdbc;
	protected final RowMapper<PersonQueue> mapper;
	protected final ResultSetExtractor<PersonQueue> objectExtractor;
	protected final ResultSetExtractor<List<PersonQueue>> listExtractor;

	@Autowired
	public PersonQueueDaoImpl(
			@Qualifier("<jdbcTemplateName>") NamedParameterJdbcTemplate jdbc,
			@Qualifier("personQueueMapper") PersonQueueMapper mapper
	) {
		this.jdbc = jdbc;
		this.mapper = mapper;
		this.objectExtractor = mapper.EXTRACTOR;
		this.listExtractor = mapper.LIST_EXTRACTOR;
	}


}
