package edu.byu.edge.jdbc.domain;

/**
 * Created by wct5 on 5/6/16.
 */
public class IndexColumn implements Comparable<IndexColumn> {
	protected String columnName;
	protected int columnPosition;

	public IndexColumn() {
	}

	public IndexColumn(final String columnName, final int columnPosition) {
		this.columnName = columnName;
		this.columnPosition = columnPosition;
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
