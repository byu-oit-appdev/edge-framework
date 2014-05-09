package edu.byu.pro.merge.da;

import edu.byu.pro.merge.domain.PersonMerge;

import java.util.List;

/**
  *
  */
public interface PersonMergeDao {

	public List<PersonMerge> findAllMergesForSubscriber(String subscriber);
}
