[#ftl]
[#if package?? && package != ""]package ${package}.[/#if]da;

/**
*
*/
public interface BaseDao {

	public String schemaName();
	public String tableName();

}
