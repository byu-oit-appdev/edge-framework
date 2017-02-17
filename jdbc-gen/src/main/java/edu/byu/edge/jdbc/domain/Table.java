package edu.byu.edge.jdbc.domain;

import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 12/03/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 12/03/2012
 */
public class Table {

	protected String schema;
	protected String tableName;
	protected String autoIncrement;
	protected List<Column> columns;
	protected Index primaryKey;
	protected List<Index> indexList;

	public Table() {
	}

	public Table(final String schema, final String tableName, final String autoIncrement) {
		this.schema = schema;
		this.tableName = tableName;
		this.autoIncrement = autoIncrement;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(final String schema) {
		this.schema = schema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(final String autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(final List<Column> columns) {
		this.columns = columns;
	}

	public Index getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(final Index primaryKey) {
		this.primaryKey = primaryKey;
	}

	public List<Index> getIndexList() {
		return indexList;
	}

	public void setIndexList(final List<Index> indexList) {
		this.indexList = indexList;
		if (indexList == null || indexList.isEmpty()) {
			setPrimaryKey(null);
		} else {
			for (final Index index : indexList) {
				if ("P".equals(index.getConstraintType())) {
					setPrimaryKey(index);
				}
			}
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Table table = (Table) o;

		if (!schema.equals(table.schema)) return false;
		if (!tableName.equals(table.tableName)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = schema.hashCode();
		result = 31 * result + tableName.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Table{" +
				"schema='" + schema + '\'' +
				", tableName='" + tableName + '\'' +
				", autoIncrement='" + autoIncrement + '\'' +
				'}';
	}
}
