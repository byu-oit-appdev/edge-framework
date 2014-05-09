package edu.byu.merge.bl.impl;

import edu.byu.merge.bl.Merger;
import edu.byu.merge.da.MergeDao;
import edu.byu.pro.merge.domain.PersonMerge;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/06/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/06/2014
 */
public class DefaultDaoMerger implements Merger {

	private static final Logger LOG = Logger.getLogger(DefaultDaoMerger.class);

	private List<MergeDao> mergeDaoList;

	@Override
	public Map<String, Integer> doMerge(final PersonMerge pm) throws DataAccessException {
		final Map<String, Integer> map = new TreeMap<String, Integer>();
		for (final MergeDao dao : mergeDaoList) {
			try {
				map.put(dao.tableName(), dao.mergePerson(pm));
			} catch (final Exception e) {
				LOG.error("Error executing merge " + dao.tableName() + " : " + pm.getOldPersonId() + " -> " + pm.getNewPersonId(), e);
				map.put(dao.tableName(), -1);
			}
		}
		if (map.values().contains(-1)) throw new DataIntegrityViolationException("Error executing merge.");
		return map;
	}

	public void setMergeDaoList(final List<MergeDao> mergeDaoList) {
		this.mergeDaoList = mergeDaoList;
	}

}
