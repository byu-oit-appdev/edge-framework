package edu.byu.edge.jdbc;

import edu.byu.edge.jdbc.domain.Table;

import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 12/04/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 12/04/2012
 */
public interface Exporter {

	/**
	 *
	 * @param tables the tables
	 * @param baseFolder the base output folder
	 * @param pkgName the java package name
	 * @param schema the database schema
	 */
	public void export(List<Table> tables, String baseFolder, String pkgName, String schema);
}
