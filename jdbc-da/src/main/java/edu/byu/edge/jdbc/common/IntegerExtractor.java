package edu.byu.edge.jdbc.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class IntegerExtractor implements ResultSetExtractor<Integer> {

	public static final IntegerExtractor EXTRACTOR = new IntegerExtractor();

	private final RowMapper<Integer> mapper = IntegerRowMapper.MAPPER;

	private IntegerExtractor() {
	}

	@Override
	public Integer extractData(final ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next())
			return mapper.mapRow(rs, 1);
		else
			return null;
	}
}
