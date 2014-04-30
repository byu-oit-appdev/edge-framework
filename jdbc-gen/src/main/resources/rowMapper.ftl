[#ftl]
[#if package?? && package != ""]package ${package}.[/#if]da.mapper;

import [#if package?? && package != ""]${package}.[/#if]domain.${className};
import edu.byu.edge.jdbc.*;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
  *
  */
@Service("${classRefName}Mapper")
public class ${className}Mapper implements RowMapper<${className}> {

	public final ResultSetExtractor<${className}> EXTRACTOR;
	public final ResultSetExtractor<List<${className}>> LIST_EXTRACTOR;

	public ${className}Mapper() {
		EXTRACTOR = new ObjectResultSetExtractor<${className}>(this);
		LIST_EXTRACTOR = new ListResultSetExtractor<${className}>(this);
	}

	@Override
	public ${className} mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final ${className} r = new ${className}();

		[#list props as p]
		r.set${p[2]}(rs.${p[3]}("${p[5]}"));
		[#if p[4]?? && p[4] != ""]
		if (rs.wasNull()) r.set${p[2]}(${p[4]});
		[/#if]

		[/#list]
		return r;
	}
}
