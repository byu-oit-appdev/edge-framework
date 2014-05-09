package edu.byu.pro.merge.da.jdbc;

import java.util.Map;
import java.util.TreeMap;

/**
*
*/
public abstract class BaseDaoImpl {

	protected BaseDaoImpl() {
	}

	protected Map<String, Object> paramMap() {
		return new TreeMap<String, Object>();
	}

}
