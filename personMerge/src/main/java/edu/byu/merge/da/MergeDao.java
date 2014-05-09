package edu.byu.merge.da;

import edu.byu.pro.merge.domain.PersonMerge;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/09/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/09/2014
 */
public interface MergeDao {

	/**
	 * Utility method (non-transactional) that informs the caller of the schema name.
	 * This is to allow for AOP/proxies/etc to be used and still expose useful information to the caller.
	 *
	 * @return The name of the scheama.
	 */
	public String schemaName();

	/**
	 * Utility method (non-transactional) that informs the caller of the table name.
	 * This is to allow for AOP/proxies/etc to be used and still expose useful information to the caller.
	 *
	 * @return The name of the table.
	 */
	public String tableName();

	/**
	 * Performs the specified person merge on a particular table.
	 * This should update all necessary columns in that table.
	 * Implementations may create multiple methods that are called by this method to facilitate code readability.
	 *
	 * For example: {@code public int mergePerson(PersonMerge pm) {
	 *     int num = 0;
	 *     num += this.doMergeColumnA(pm);
	 *     num += this.doMergeColumnB(pm);
	 *     etc...
	 *     return num;
	 * }
	 * }
	 *
	 * @param personMerge The merge to execute.
	 * @return The number of rows modified by the merge.
	 */
	public int mergePerson(PersonMerge personMerge);

}
