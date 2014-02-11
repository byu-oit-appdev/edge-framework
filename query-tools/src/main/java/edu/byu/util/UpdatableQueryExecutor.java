package edu.byu.util;

import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 02/10/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 02/10/2014
 */
public interface UpdatableQueryExecutor<P> {

	/**
	 * This is the method that performs the actual query. This should be a modification-type query (insert, update, delete).
	 *
	 * Note that this method returns an int, which is common practice for modifying queries. This indicates the number of rows or records modified.
	 *
	 * @param list the parameter list.
	 * @return The number of rows/records modified.
	 */
	public int doQuery(List<P> list);
}
