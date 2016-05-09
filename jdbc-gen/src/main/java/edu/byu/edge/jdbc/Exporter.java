package edu.byu.edge.jdbc;

import edu.byu.edge.jdbc.domain.Schema;
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
	 * @param schema the schema
	 * @param baseFolder the base output folder
	 * @param pkgName the java package name
	 * @param schemaName the database schema
	 */
	public void export(Schema schema, String baseFolder, String pkgName, String schemaName);
}
