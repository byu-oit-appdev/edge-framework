package edu.byu.edge.jdbc;

import edu.byu.edge.jdbc.domain.*;

import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 11/30/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 11/30/2012
 */
public interface Parser {
	/**
	 *
	 * @return result
	 */
	public String getSchemaNameMessage();

	/**
	 *
	 * @param schema the schema to use
	 * @param tableSet a comma-delimited list of table names to include. * or empty string means all
	 * @return result
	 */
	public List<Table> getTables(String schema, String tableSet);

	/**
	 *
	 * @param t the table
	 * @return the columns
	 */
	public List<Column> getColumnsForTable(Table t);

	/**
	 *
	 * @param tables the tables
	 * @return same tables
	 */
	public List<Table> getColumnsForTables(List<Table> tables);
}
