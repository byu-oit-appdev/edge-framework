package edu.byu.edge.jdbc.domain;

/**
 * Created by wct5 on 5/6/16.
 */
public class IndexColumn implements Comparable<IndexColumn> {
	protected String columnName;
	protected int columnPosition;
	protected String columnExpression;

	public IndexColumn() {
	}

	public IndexColumn(final String columnName, final int columnPosition, final String columnExpression) {
		this.columnName = columnName;
		this.columnPosition = columnPosition;
		this.columnExpression = columnExpression;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public int getColumnPosition() {
		return columnPosition;
	}

	public void setColumnPosition(final int columnPosition) {
		this.columnPosition = columnPosition;
	}

	public String getColumnExpression() {
		return columnExpression;
	}

	public void setColumnExpression(final String columnExpression) {
		this.columnExpression = columnExpression;
	}

	@Override
	public String toString() {
		return "IndexColumn{" +
				"columnName='" + columnName + '\'' +
				", columnPosition='" + columnPosition + '\'' +
				'}';
	}

	@Override
	public int compareTo(final IndexColumn o) {
		return this.getColumnPosition() - o.getColumnPosition();
	}
}
