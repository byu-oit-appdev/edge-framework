package edu.byu.edge.jdbc.util;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 12/13/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 12/13/2012
 */
public abstract class DefaultResultSet<T> implements ResultSet {

	protected final T[] myRows;
	protected volatile int whichRow = 0;
	protected final int lastRow;
	protected final boolean hasRows;
	protected final int afterLast;

	protected DefaultResultSet(final T[] myRows) {
		this.myRows = myRows;
		lastRow = myRows != null ? myRows.length : 0;
		hasRows = myRows != null && myRows.length > 0;
		afterLast = lastRow + 1;
	}

	protected T getCurRow() {
		return myRows[whichRow - 1];
	}

	@Override
	public boolean wasNull() throws SQLException {
		return hasRows && getCurRow() == null;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return hasRows && whichRow < 1;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return hasRows && whichRow > myRows.length;
	}

	@Override
	public boolean isFirst() throws SQLException {
		return hasRows && whichRow == 1;
	}

	@Override
	public boolean isLast() throws SQLException {
		return hasRows && whichRow == lastRow;
	}

	@Override
	public void beforeFirst() throws SQLException {
		whichRow = 0;
	}

	@Override
	public void afterLast() throws SQLException {
		whichRow = afterLast;
	}

	@Override
	public boolean first() throws SQLException {
		whichRow = 1;
		return hasRows;
	}

	@Override
	public boolean last() throws SQLException {
		whichRow = lastRow;
		return hasRows;
	}

	@Override
	public boolean next() throws SQLException {
		whichRow++;
		return hasRows && whichRow <= lastRow;
	}

	@Override
	public boolean previous() throws SQLException {
		whichRow--;
		return hasRows && whichRow > 0;
	}

	@Override
	public int getRow() throws SQLException {
		return whichRow;
	}

	@Override
	public boolean absolute(final int row) throws SQLException {
		whichRow = row;
		if (whichRow > afterLast) whichRow = afterLast;
		return hasRows && row < afterLast && row > 0;
	}

	@Override
	public boolean relative(final int rows) throws SQLException {
		final int x = whichRow += rows;
		if (whichRow < 0) whichRow = 0;
		if (whichRow > afterLast) whichRow = afterLast;
		return hasRows && x < afterLast && x > 0;
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public String getString(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public boolean getBoolean(final int columnIndex) throws SQLException {
		return false;
	}

	@Override
	public byte getByte(final int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public short getShort(final int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public int getInt(final int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public long getLong(final int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public float getFloat(final int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public double getDouble(final int columnIndex) throws SQLException {
		return 0;
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
		return null;
	}

	@Override
	public byte[] getBytes(final int columnIndex) throws SQLException {
		return new byte[0];
	}

	@Override
	public Date getDate(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getAsciiStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public String getString(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public boolean getBoolean(final String columnLabel) throws SQLException {
		return false;
	}

	@Override
	public byte getByte(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public short getShort(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public int getInt(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public long getLong(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public float getFloat(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public double getDouble(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
		return null;
	}

	@Override
	public byte[] getBytes(final String columnLabel) throws SQLException {
		return new byte[0];
	}

	@Override
	public Date getDate(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public InputStream getAsciiStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public InputStream getBinaryStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
	}

	@Override
	public String getCursorName() throws SQLException {
		return null;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return null;
	}

	@Override
	public Object getObject(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Object getObject(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public int findColumn(final String columnLabel) throws SQLException {
		return 0;
	}

	@Override
	public Reader getCharacterStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Reader getCharacterStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return 0;
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
	}

	@Override
	public int getFetchSize() throws SQLException {
		return 0;
	}

	@Override
	public int getType() throws SQLException {
		return 0;
	}

	@Override
	public int getConcurrency() throws SQLException {
		return 0;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return false;
	}

	@Override
	public void updateNull(final int columnIndex) throws SQLException {
	}

	@Override
	public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
	}

	@Override
	public void updateByte(final int columnIndex, final byte x) throws SQLException {
	}

	@Override
	public void updateShort(final int columnIndex, final short x) throws SQLException {
	}

	@Override
	public void updateInt(final int columnIndex, final int x) throws SQLException {
	}

	@Override
	public void updateLong(final int columnIndex, final long x) throws SQLException {
	}

	@Override
	public void updateFloat(final int columnIndex, final float x) throws SQLException {
	}

	@Override
	public void updateDouble(final int columnIndex, final double x) throws SQLException {
	}

	@Override
	public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
	}

	@Override
	public void updateString(final int columnIndex, final String x) throws SQLException {
	}

	@Override
	public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
	}

	@Override
	public void updateDate(final int columnIndex, final Date x) throws SQLException {
	}

	@Override
	public void updateTime(final int columnIndex, final Time x) throws SQLException {
	}

	@Override
	public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
	}

	@Override
	public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
	}

	@Override
	public void updateObject(final int columnIndex, final Object x) throws SQLException {
	}

	@Override
	public void updateNull(final String columnLabel) throws SQLException {
	}

	@Override
	public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
	}

	@Override
	public void updateByte(final String columnLabel, final byte x) throws SQLException {
	}

	@Override
	public void updateShort(final String columnLabel, final short x) throws SQLException {
	}

	@Override
	public void updateInt(final String columnLabel, final int x) throws SQLException {
	}

	@Override
	public void updateLong(final String columnLabel, final long x) throws SQLException {
	}

	@Override
	public void updateFloat(final String columnLabel, final float x) throws SQLException {
	}

	@Override
	public void updateDouble(final String columnLabel, final double x) throws SQLException {
	}

	@Override
	public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
	}

	@Override
	public void updateString(final String columnLabel, final String x) throws SQLException {
	}

	@Override
	public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
	}

	@Override
	public void updateDate(final String columnLabel, final Date x) throws SQLException {
	}

	@Override
	public void updateTime(final String columnLabel, final Time x) throws SQLException {
	}

	@Override
	public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
	}

	@Override
	public void updateBinaryStream(final String columnLabel,
			final InputStream x,
			final int length) throws SQLException {
	}

	@Override
	public void updateCharacterStream(final String columnLabel,
			final Reader reader,
			final int length) throws SQLException {
	}

	@Override
	public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
	}

	@Override
	public void updateObject(final String columnLabel, final Object x) throws SQLException {
	}

	@Override
	public void insertRow() throws SQLException {
	}

	@Override
	public void updateRow() throws SQLException {
	}

	@Override
	public void deleteRow() throws SQLException {
	}

	@Override
	public void refreshRow() throws SQLException {
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
	}

	@Override
	public void moveToInsertRow() throws SQLException {
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
	}

	@Override
	public Statement getStatement() throws SQLException {
		return null;
	}

	@Override
	public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
		return null;
	}

	@Override
	public Ref getRef(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Blob getBlob(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Clob getClob(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Array getArray(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
		return null;
	}

	@Override
	public Ref getRef(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Blob getBlob(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Clob getClob(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Array getArray(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
		return null;
	}

	@Override
	public URL getURL(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public URL getURL(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public void updateRef(final int columnIndex, final Ref x) throws SQLException {
	}

	@Override
	public void updateRef(final String columnLabel, final Ref x) throws SQLException {
	}

	@Override
	public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
	}

	@Override
	public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
	}

	@Override
	public void updateClob(final int columnIndex, final Clob x) throws SQLException {
	}

	@Override
	public void updateClob(final String columnLabel, final Clob x) throws SQLException {
	}

	@Override
	public void updateArray(final int columnIndex, final Array x) throws SQLException {
	}

	@Override
	public void updateArray(final String columnLabel, final Array x) throws SQLException {
	}

	@Override
	public RowId getRowId(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public RowId getRowId(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
	}

	@Override
	public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
	}

	@Override
	public int getHoldability() throws SQLException {
		return 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public void updateNString(final int columnIndex, final String nString) throws SQLException {
	}

	@Override
	public void updateNString(final String columnLabel, final String nString) throws SQLException {
	}

	@Override
	public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
	}

	@Override
	public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
	}

	@Override
	public NClob getNClob(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public NClob getNClob(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public SQLXML getSQLXML(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public SQLXML getSQLXML(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
	}

	@Override
	public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
	}

	@Override
	public String getNString(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public String getNString(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public Reader getNCharacterStream(final int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Reader getNCharacterStream(final String columnLabel) throws SQLException {
		return null;
	}

	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
	}

	@Override
	public void updateNCharacterStream(final String columnLabel,
			final Reader reader,
			final long length) throws SQLException {
	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
	}

	@Override
	public void updateAsciiStream(final String columnLabel,
			final InputStream x,
			final long length) throws SQLException {
	}

	@Override
	public void updateBinaryStream(final String columnLabel,
			final InputStream x,
			final long length) throws SQLException {
	}

	@Override
	public void updateCharacterStream(final String columnLabel,
			final Reader reader,
			final long length) throws SQLException {
	}

	@Override
	public void updateBlob(final int columnIndex,
			final InputStream inputStream,
			final long length) throws SQLException {
	}

	@Override
	public void updateBlob(final String columnLabel,
			final InputStream inputStream,
			final long length) throws SQLException {
	}

	@Override
	public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
	}

	@Override
	public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
	}

	@Override
	public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
	}

	@Override
	public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
	}

	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
	}

	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
	}

	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
	}

	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
	}

	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
	}

	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
	}

	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
	}

	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
	}

	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
	}

	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
	}

	@Override
	public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
	}

	@Override
	public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
	}

	@Override
	public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
	}

	@Override
	public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return false;
	}
}