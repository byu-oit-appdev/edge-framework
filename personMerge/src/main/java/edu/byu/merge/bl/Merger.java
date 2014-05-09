package edu.byu.merge.bl;

import edu.byu.pro.merge.domain.PersonMerge;
import org.springframework.dao.DataAccessException;

import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/06/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/06/2014
 */
public interface Merger {
	/**
	 * Executes the specified person merge.
	 *
	 * IMPORTANT: This should be the transactional boundary in most cases.
	 * If a merge should fail on a specific table, the data should remain intact for research purposes.
	 * A merge should not be partially done as this might put the data in an inconsistant state.
	 *
	 * @param personMerge The merge to execute.
	 * @return A map containing table names with the number of rows modified in that table.
	 * @throws DataAccessException
	 */
	public Map<String, Integer> doMerge(PersonMerge personMerge) throws DataAccessException;

}
