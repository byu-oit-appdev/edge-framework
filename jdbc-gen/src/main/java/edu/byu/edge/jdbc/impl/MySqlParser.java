package edu.byu.edge.jdbc.impl;

import edu.byu.edge.jdbc.JdbcGen;
import edu.byu.edge.jdbc.Parser;
import edu.byu.edge.jdbc.domain.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 11/30/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 11/30/2012
 */
public class MySqlParser implements Parser {

	public static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	private final NamedParameterJdbcOperations jdbc;
	private final TableRowMapper tableRowMapper;
	private final ColumnRowMapper columnRowMapper;

	public MySqlParser(final NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;
		this.tableRowMapper = new TableRowMapper();
		this.columnRowMapper = new ColumnRowMapper();
		JdbcGen.setParser(this);
	}

	@Override
	public List<Table> getTables(final String schema, final String tableNames) {
		final TreeMap<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("schema", schema);
		if (tableNames == null || "*".equals(tableNames) || "".equals(tableNames)) {
			return jdbc.query(
					"select TABLE_SCHEMA, TABLE_NAME, AUTO_INCREMENT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = :schema " +
							"union " +
							"select TABLE_SCHEMA, TABLE_NAME, null from INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = :schema ",
					paramMap, tableRowMapper);
		} else {
			final String[] sa = tableNames.split(",");
			for (int i = 0; i < sa.length; i++) {
				if (sa[i] == null) sa[i] = "";
				sa[i] = sa[i].trim().toUpperCase();
			}
			paramMap.put("tables", Arrays.asList(sa));
			return jdbc.query(
					"select TABLE_SCHEMA, TABLE_NAME, AUTO_INCREMENT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = :schema and upper(TABLE_NAME) in (:tables) " +
							"union " +
							"select TABLE_SCHEMA, TABLE_NAME, null from INFORMATION_SCHEMA.VIEWS where TABLE_SCHEMA = :schema and upper(TABLE_NAME) in (:tables) ",
					paramMap, tableRowMapper);
		}
	}

	@Override
	public List<Column> getColumnsForTable(final Table t) {
		final TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("schema", t.getSchema());
		map.put("table", t.getTableName());
		return jdbc.query("select * from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA = :schema and TABLE_NAME = :table order by ORDINAL_POSITION", map, columnRowMapper);
	}

	@Override
	public List<Table> getColumnsForTables(final List<Table> tables) {
		for (final Table t : tables) {
			t.setColumns(getColumnsForTable(t));
		}
		return tables;
	}

	@Override
	public String getSchemaNameMessage() {
		return "Please enter schema name. Please note that in MySQL, the schema name IS case-sensitive.";
	}

	private static final class TableRowMapper implements RowMapper<Table> {
		@Override
		public Table mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new Table(
					rs.getString("TABLE_SCHEMA"),
					rs.getString("TABLE_NAME"),
					rs.getString("AUTO_INCREMENT")
			);
		}
	}

	private static final class ColumnRowMapper implements RowMapper<Column> {

		private static final Pattern NUM = Pattern.compile("^\\d+$");
		private static final Pattern SCI_NUM = Pattern.compile("^\\d{1}(\\.\\d+E\\-?\\d+)?");
		private static final Pattern BOOL = Pattern.compile("^[Yy1Tt]{1}.*$");

		private static int getInt(final ResultSet rs, final String colName) throws SQLException {
			final String s = rs.getString(colName);
			if (s == null || !(NUM.matcher(s).matches() || SCI_NUM.matcher(s).matches())) return 0;
			return rs.getInt(colName);
		}

		private static long getLong(final ResultSet rs, final String colName) throws SQLException {
			final String s = rs.getString(colName);
			if (s == null || !(NUM.matcher(s).matches() || SCI_NUM.matcher(s).matches())) return 0L;
			return rs.getLong(colName);
		}

		private static boolean getBool(final ResultSet rs, final String colName) throws SQLException {
			return BOOL.matcher(rs.getString(colName)).matches();
		}

		@Override
		public Column mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new Column(
					rs.getString("COLUMN_NAME"),
					getInt(rs, "ORDINAL_POSITION"),
					getBool(rs, "IS_NULLABLE"),
					rs.getString("DATA_TYPE"),
					getLong(rs, "CHARACTER_MAXIMUM_LENGTH"),
					getLong(rs, "NUMERIC_PRECISION"),
					getLong(rs, "NUMERIC_SCALE"),
					rs.getString("COLUMN_TYPE"),
					rs.getString("COLUMN_KEY")
			);
		}
	}
}
