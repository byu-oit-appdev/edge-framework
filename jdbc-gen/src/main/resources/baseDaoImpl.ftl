[#ftl]
[#if package?? && package != ""]package ${package}.[/#if]da.jdbc;

import [#if package?? && package != ""] ${package}.[/#if]da.BaseDao;

import java.util.Map;
import java.util.TreeMap;

/**
*
*/
public abstract class BaseDaoImpl implements BaseDao {

	protected BaseDaoImpl() {
	}

	protected Map<String, Object> paramMap() {
		return new TreeMap<String, Object>();
	}

}
