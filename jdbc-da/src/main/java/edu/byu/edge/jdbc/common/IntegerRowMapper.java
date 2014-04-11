package edu.byu.edge.jdbc.common;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class IntegerRowMapper implements RowMapper<Integer> {

	public static final IntegerRowMapper MAPPER = new IntegerRowMapper();

	private IntegerRowMapper() {
	}

	@Override
	public Integer mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return rs.getInt(1);
	}
}
