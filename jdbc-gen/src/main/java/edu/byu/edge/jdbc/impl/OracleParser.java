package edu.byu.edge.jdbc.impl;

import edu.byu.edge.jdbc.JdbcGen;
import edu.byu.edge.jdbc.ListResultSetExtractor;
import edu.byu.edge.jdbc.Parser;
import edu.byu.edge.jdbc.domain.Column;
import edu.byu.edge.jdbc.domain.Index;
import edu.byu.edge.jdbc.domain.IndexColumn;
import edu.byu.edge.jdbc.domain.Schema;
import edu.byu.edge.jdbc.domain.Sequence;
import edu.byu.edge.jdbc.domain.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 12/03/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 12/03/2012
 */
public class OracleParser implements Parser {

	public static final String DRIVER_CLASS_NAME = "oracle.jdbc.OracleDriver";

	private final NamedParameterJdbcOperations jdbc;

	private final RowMapper<Table> tableRowMapper;
	private final RowMapper<Column> columnRowMapper;
	private final RowMapper<Index> indexRowMapper;
	private final RowMapper<IndexColumn> indexColumnRowMapper;
	private final RowMapper<Sequence> sequenceRowMapper;

	private final ListResultSetExtractor<Table> tableListExtractor;
	private final ListResultSetExtractor<Column> columnListExtractor;
	private final ListResultSetExtractor<Index> indexListExtractor;
	private final ListResultSetExtractor<IndexColumn> indexColumnListExtractor;
	private final ListResultSetExtractor<Sequence> sequenceListExtractor;

	public OracleParser(final NamedParameterJdbcOperations jdbc) {
		this.jdbc = jdbc;

		this.tableRowMapper = new TableRowMapper();
		this.columnRowMapper = new ColumnRowMapper();
		this.indexRowMapper = new IndexRowMapper();
		this.indexColumnRowMapper = new IndexColumnRowMapper();
		this.sequenceRowMapper = new SequenceRowMapper();

		tableListExtractor = new ListResultSetExtractor<>(tableRowMapper);
		columnListExtractor = new ListResultSetExtractor<>(columnRowMapper);
		indexListExtractor = new ListResultSetExtractor<>(indexRowMapper);
		indexColumnListExtractor = new ListResultSetExtractor<>(indexColumnRowMapper);
		sequenceListExtractor = new ListResultSetExtractor<>(sequenceRowMapper);

		JdbcGen.setParser(this);
	}

	@Override
	public Schema getTables(final String schema, final String tableNames) {
		final Map<String, Object> paramMap = new HashMap<String, Object>(3, .999999f);
		paramMap.put("schema", schema);
		if (tableNames == null || "*".equals(tableNames) || "".equals(tableNames)) {
			return new Schema(schema, jdbc.query(SQL_TABLE_NAMES, paramMap, tableListExtractor), getSequencesForSchema(schema));
		} else {
			final String[] sa = tableNames.split(",");
			for (int i = 0; i < sa.length; i++) {
				if (sa[i] == null) sa[i] = "";
				sa[i] = sa[i].trim().toUpperCase();
			}
			paramMap.put("tables", Arrays.asList(sa));
			return new Schema(schema, jdbc.query(SQL_TABLE_NAMES_FILTERED, paramMap, tableListExtractor), getSequencesForSchema(schema));
		}
	}

	@Override
	public List<Column> getColumnsForTable(final Table t) {
		final Map<String, Object> map = new HashMap<String, Object>(3, .999999f);
		map.put("schema", t.getSchema());
		map.put("table", t.getTableName());
		return jdbc.query(SQL_COLS, map, columnListExtractor);
	}

	@Override
	public List<Table> getColumnsForTables(final List<Table> tables) {
		for (final Table t : tables) {
			t.setColumns(getColumnsForTable(t));
			getIndexesForTable(t);
		}
		return tables;
	}

	@Override
	public Table getIndexesForTable(final Table table) {
		final Map<String, Object> map = new HashMap<>(3, .999999f);
		map.put("owner", table.getSchema());
		map.put("tableName", table.getTableName());
		final List<Index> indexList = jdbc.query(SQL_IDX_NAMES, map, indexListExtractor);
		for (final Index index : indexList) {
			map.put("indexName", index.getIndexName());
			index.setColumnList(jdbc.query(SQL_IDX_COLS, map, indexColumnListExtractor));
		}
		table.setIndexList(indexList);
		return table;
	}

	public List<Sequence> getSequencesForSchema(final String schema) {
		final Map<String, Object> map = new HashMap<>(2, .999999f);
		map.put("owner", schema);
		return jdbc.query(SQL_SEQUENCES, map, sequenceListExtractor);
	}

	@Override
	public String getSchemaNameMessage() {
		return "Please enter schema. By default, in Oracle, schema names are in all upper-case letters.";
	}

	private static final String SQL_IDX_COLS = "select I.COLUMN_NAME, I.COLUMN_POSITION " +
			"from ALL_IND_COLUMNS I " +
			"where I.TABLE_OWNER = :owner " +
			"and I.TABLE_NAME = :tableName " +
			"and I.INDEX_NAME = :indexName ";

	private static final String SQL_IDX_NAMES = "select i.INDEX_NAME, i.INDEX_TYPE, i.TABLE_OWNER, i.TABLE_NAME, c.CONSTRAINT_TYPE, i.UNIQUENESS " +
			"from ALL_INDEXES i " +
			"left join ALL_CONSTRAINTS c on (i.TABLE_OWNER = c.OWNER or i.OWNER = c.OWNER) and i.TABLE_NAME = c.TABLE_NAME and i.INDEX_NAME = c.INDEX_NAME and c.CONSTRAINT_TYPE in ('P', 'U') " +
			"where i.TABLE_OWNER = :owner " +
			"and i.TABLE_NAME = :tableName";

	private static final String SQL_COLS = "select COLUMN_NAME, COLUMN_ID, NULLABLE, DATA_TYPE, CHAR_COL_DECL_LENGTH, DATA_PRECISION, DATA_SCALE " +
			"from ALL_TAB_COLUMNS " +
			"where owner = :schema and TABLE_NAME = :table";

	private static final String SQL_TABLE_NAMES_FILTERED = "select OWNER, TABLE_NAME " +
			"from ALL_TABLES " +
			"where OWNER = :schema and upper(TABLE_NAME) in (:tables) " +
			"union " +
			"select OWNER, VIEW_NAME " +
			"from ALL_VIEWS " +
			"where OWNER = :schema and upper(VIEW_NAME) in (:tables) ";

	private static final String SQL_TABLE_NAMES = "select OWNER, TABLE_NAME " +
			"from ALL_TABLES " +
			"where OWNER = :schema " +
			"union " +
			"select OWNER, VIEW_NAME " +
			"from ALL_VIEWS " +
			"where OWNER = :schema ";

	private static final String SQL_SEQUENCES = "select SEQUENCE_OWNER, SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, INCREMENT_BY, CYCLE_FLAG, ORDER_FLAG, CACHE_SIZE, LAST_NUMBER " +
			"from DBA_SEQUENCES " +
			"where SEQUENCE_OWNER = :owner";

	private static final class TableRowMapper implements RowMapper<Table> {
		@Override
		public Table mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new Table(
					rs.getString("OWNER"),
					rs.getString("TABLE_NAME"),
					""
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
					getInt(rs, "COLUMN_ID"),
					getBool(rs, "NULLABLE"),
					rs.getString("DATA_TYPE"),
					getLong(rs, "CHAR_COL_DECL_LENGTH"),
					getLong(rs, "DATA_PRECISION"),
					getLong(rs, "DATA_SCALE"),
					rs.getString("DATA_TYPE"),
					""
			);
		}
	}

	private static final class IndexRowMapper implements RowMapper<Index> {

		@Override
		public Index mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new Index(
					rs.getString("INDEX_NAME"),
					rs.getString("INDEX_TYPE"),
					rs.getString("TABLE_OWNER"),
					rs.getString("TABLE_NAME"),
					rs.getString("CONSTRAINT_TYPE"),
					rs.getString("UNIQUENESS")
			);
		}
	}

	private static final class IndexColumnRowMapper implements RowMapper<IndexColumn> {

		@Override
		public IndexColumn mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new IndexColumn(rs.getString("COLUMN_NAME"), rs.getInt("COLUMN_POSITION"));
		}
	}

	private static final class SequenceRowMapper implements RowMapper<Sequence> {
		@Override
		public Sequence mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new Sequence(
					rs.getString("SEQUENCE_OWNER"),
					rs.getString("SEQUENCE_NAME"),
					rs.getBigDecimal("MIN_VALUE"),
					rs.getBigDecimal("MAX_VALUE"),
					rs.getInt("INCREMENT_BY"),
					rs.getString("CYCLE_FLAG"),
					rs.getString("ORDER_FLAG"),
					rs.getInt("CACHE_SIZE"),
					rs.getBigDecimal("LAST_NUMBER")
			);
		}
	}
}
