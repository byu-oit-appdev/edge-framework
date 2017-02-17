package edu.byu.edge.jdbc.domain;

import java.util.List;

/**
 * Created by wct5 on 5/6/16.
 */
public class Index {
	protected String indexName;
	protected String indexType;
	protected String tableOwner;
	protected String tableName;
	protected String constraintType;
	protected String uniqueness;
	protected List<IndexColumn> columnList;

	public Index() {
	}

	public Index(final String indexName, final String indexType, final String tableOwner, final String tableName, final String constraintType, final String uniqueness) {
		this.indexName = indexName;
		this.indexType = indexType;
		this.tableOwner = tableOwner;
		this.tableName = tableName;
		this.constraintType = constraintType;
		this.uniqueness = uniqueness;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(final String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(final String indexType) {
		this.indexType = indexType;
	}

	public String getTableOwner() {
		return tableOwner;
	}

	public void setTableOwner(final String tableOwner) {
		this.tableOwner = tableOwner;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getConstraintType() {
		return constraintType;
	}

	public void setConstraintType(final String constraintType) {
		this.constraintType = constraintType;
	}

	public String getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(final String uniqueness) {
		this.uniqueness = uniqueness;
	}

	public List<IndexColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(final List<IndexColumn> columnList) {
		this.columnList = columnList;
	}

	@Override
	public String toString() {
		return "Index{" +
				"indexName='" + indexName + '\'' +
				", indexType='" + indexType + '\'' +
				", tableOwner='" + tableOwner + '\'' +
				", tableName='" + tableName + '\'' +
				", constraintType='" + constraintType + '\'' +
				'}';
	}
}
