[#ftl]
[#if package?? && package != ""]package ${package}.[/#if]da;

import [#if package?? && package != ""]${package}.[/#if]domain.${className};

/**
  *
  */
public interface ${className}Dao extends BaseDao {

}
