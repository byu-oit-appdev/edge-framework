package edu.byu.util;

import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 02/10/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 02/10/2014
 */
public interface SelectableQueryExecutor<P, R> {

	/**
	 * This is the actual method that does the query.
	 *
	 * Note that the results may not match one-to-one with the supplied parameters.
	 *
	 * @param list The parameter list.
	 * @return The result of the query.
	 */
	public List<R> doQuery(List<P> list);
}
