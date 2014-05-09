package edu.byu.edge.jdbc.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import edu.byu.edge.jdbc.Exporter;
import edu.byu.edge.jdbc.JdbcGen;
import edu.byu.edge.jdbc.domain.Column;
import edu.byu.edge.jdbc.domain.ColumnComparator;
import edu.byu.edge.jdbc.domain.Table;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 12/04/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 12/04/2012
 */
public class ExporterImpl implements Exporter {

	private static final PrintWriter OUT = JdbcGen.OUT;

	private Configuration configuration;

	public ExporterImpl() {
		JdbcGen.setExporter(this);
	}

	public void setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void export(final List<Table> tables, final String baseFolder, final String pkgName, final String schema) {
		OUT.println("Exporting tables to " + baseFolder + "...");

		final File root = createFolder(null, baseFolder);
		final File base = createFolder(root, pkgName.replaceAll("\\.", "/"));
		final File domain = createFolder(base, "domain");
		final File da = createFolder(base, "da");
		final File mapper = createFolder(da, "mapper");
		final File daimpl = createFolder(da, "jdbc");

		try {
			exportBaseDao(da, daimpl, pkgName);
		} catch (final Exception e) {
			OUT.println("Error generating BaseDaoImpl. " + e.getMessage());
		}

		for (final Table t : tables) {
			try {
				exportTable(new File[]{base, domain, da, daimpl, mapper}, pkgName, t);
			} catch (final Exception e) {
				OUT.println("Error generating class for " + t.getTableName() + ". " + e.getMessage());
			}
		}

		try {
			exportSpringConfig(root, pkgName, schema, tables);
		} catch (final Exception e) {
			OUT.println("Error generating spring configuration for " + schema + ". " + e.getMessage());
		}
	}

	private void exportSpringConfig(final File path, final String pkgName, final String schema, final List<Table> tables) throws IOException {
		final Map<String, Object> map = new TreeMap<String, Object>();
		final String okSchema = cleanupSchema(schema);
		final String nameSchema = lowerFirstLetter(okSchema);
		map.put("datasource", nameSchema + "DataSource");
		map.put("jdbcName", nameSchema + "JdbcTemplate");
		map.put("package", pkgName);
		map.put("poolName", nameSchema + "Pool");
		map.put("txAdvice", nameSchema + "TxAdvice01");
		map.put("txManager", nameSchema + "TransactionManager");
		map.put("daos", new ArrayList<String[]>(Collections2.transform(tables, new TableToBean())));
		doExport(path, nameSchema + "-jdbc-context.xml", "springcontext.ftl", map);
	}

	private void exportBaseDao(final File da,  final File daImpl, final String pkgName) throws IOException {
		final Map<String, Object> map = new TreeMap<String, Object>();
		map.put("package", pkgName);
		doExport(da, "BaseDao.java", "baseDao.ftl", map);
		doExport(daImpl, "BaseDaoImpl.java", "baseDaoImpl.ftl", map);
	}

	private void exportTable(final File[] paths, final String pkgName, final Table tbl) throws IOException {
		final String className = camelCaseName(tbl.getTableName());

		OUT.println("Exporting " + tbl.getTableName() + " to " + className);

		Collections.sort(tbl.getColumns(), ColumnComparator.INSTANCE);
		final Set<String> imps = new TreeSet<String>(Collections2.filter(Collections2.transform(tbl.getColumns(), COLUMN_TO_IMPORT), IMPORT_FILTER));
		final List<String[]> props = new ArrayList<String[]>(Collections2.transform(tbl.getColumns(), COLUMN_TO_PROPERTY));

		final TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("package", pkgName);
		map.put("imports", imps);
		map.put("props", props);
		map.put("className", className);
		map.put("classRefName", lowerFirstLetter(className));
		map.put("jdbcName", lowerFirstLetter(cleanupSchema(tbl.getSchema())) + "JdbcTemplate");
		map.put("table", tbl);
		doExport(paths[1], className + ".java", "jdbcDomain.ftl", map);
		doExport(paths[4], className + "Mapper.java", "rowMapper.ftl", map);
		doExport(paths[2], className + "Dao.java", "daoInterface.ftl", map);
		doExport(paths[3], className + "DaoImpl.java", "daoImpl.ftl", map);
	}

	private void doExport(final File path, final String fileName, final String templateName, final Map<String, Object> map) throws IOException {
		final File fout = new File(path, fileName);
		final Template temp = configuration.getTemplate(templateName);
		writeTemplateToFile(fout, temp, map);
	}

	private void writeTemplateToFile(final File fout, final Template temp, final Map<String, Object> map) throws IOException {
		fout.createNewFile();
		final FileWriter out = new FileWriter(fout);
		try {
			temp.process(map, out);
		} catch (final TemplateException e) {
			OUT.println("Error processing template. " + e.getMessage());
		}
		try {
			out.close();
		} catch (final Throwable ignore) {
		}
	}

	private static String determineColumnType(final Column c) {
		final String rawType = c.getDataType().toLowerCase();
		if (!TYPE_MAPPING.containsKey(rawType)) {
			OUT.println("Unknown data type: " + rawType + ". Defaulting to String.");
			return "java.lang.String";
		}
		final boolean nullable = c.getNullable();
		final String[] type = TYPE_MAPPING.get(rawType);

		if (type == TLONG || type == TDOUBLE || type == TBOOL) {
			return nullable ? NULL_MAP.get(type[0]) : type[0];
		}
		if (type.length == 1) {
			return type[0];
		}
		if (c.getNumScale() > 0) {
			return type[0];
		}
		if (c.getNumPrec() > 9) {
			return nullable ? NULL_MAP.get(type[2]) : type[2];
		}
		return nullable ? NULL_MAP.get(type[1]) : type[1];
	}

	private static String camelCaseName(final String base) {
		if (base == null || "".equals(base)) return "";
		final String[] parts = base.split(DB_IDENT_SEP);
		final StringBuilder sb = new StringBuilder(base.length());
		for (final String p : parts) {
			sb.append(p.substring(0, 1).toUpperCase());
			if (p.length() > 1)
				sb.append(p.substring(1).toLowerCase());
		}
		return sb.toString();
	}

	private static File createFolder(final File base, final String fldr) {
		final File dir = base != null ? new File(base, fldr) : new File(fldr);
		if (dir.exists() && ! dir.isDirectory()) throw new IllegalArgumentException(dir + " is not a valid folder");
		if (! dir.exists() && ! dir.mkdirs()) throw new IllegalArgumentException("Unable to create base folder: " + dir);
		return dir;
	}

	private static String cleanupSchema(final String base) {
		return lowerFirstLetter(camelCaseName(base));
	}

	private static String lowerFirstLetter(final String s) {
		if (s == null || "".equals(s)) return "";
		if (s.length() == 1) return s.toLowerCase();
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	private static final Predicate<String> IMPORT_FILTER = new ImportFilter();
	private static final Function<Column, String> COLUMN_TO_IMPORT = new ColumnToImport();
	private static final Function<Column, String[]> COLUMN_TO_PROPERTY = new ColumnToProperty();

	private static final String DB_IDENT_SEP = "[_\\.\\/&\\s]+";

	private static final String[] STRING = {"java.lang.String"};
	private static final String[] TBOOL = {"boolean"};
	private static final String[] TINT = {"int"};
	private static final String[] TLONG = {"long"};
	private static final String[] TDOUBLE = {"double"};
	private static final String[] BYTEARR = {"byte[]"};
	private static final String[] DATE = {"java.util.Date"};
	private static final String[] TBIGDEC = {"java.math.BigDecimal"};
	private static final String[] TNUMBER = {"java.math.BigDecimal", "int", "long"};

	private static final Map<String, String[]> TYPE_MAPPING = createTypeMapping();
	private static final Map<String, String> NULL_MAP = createNullMap();
	private static final Map<String, String[]> IMPORT_MAP = createImportMap();
	private static final Map<String, String> RESULT_SET_MAP = createResultSetMap();
	private static final Map<String, String> SET_NULL_MAP = createSetNullValueMap();

	private static Map<String, String[]> createTypeMapping() {
		final Map<String, String[]> map = new TreeMap<String, String[]>();

		map.put("bigint", TLONG);
		map.put("binary", BYTEARR);
		map.put("bit", TBOOL);
		map.put("blob", BYTEARR);
		map.put("boolean", TBOOL);
		map.put("char", STRING);
		map.put("date", DATE);
		map.put("datetime", DATE);
		map.put("decimal", TBIGDEC);
		map.put("double", TDOUBLE);
		map.put("enum", STRING);
		map.put("float", TDOUBLE);
		map.put("int", TNUMBER);
		map.put("interval day", STRING);
		map.put("interval year", STRING);
		map.put("long raw", BYTEARR);
		map.put("longblob", BYTEARR);
		map.put("longtext", STRING);
		map.put("mediumblob", BYTEARR);
		map.put("mediumint", TINT);
		map.put("mediumtext", STRING);
		map.put("number", TNUMBER);
		map.put("numeric", TNUMBER);
		map.put("raw", BYTEARR);
		map.put("set", STRING);
		map.put("smallint", TINT);
		map.put("text", STRING);
		map.put("time", STRING);
		map.put("timestamp", DATE);
		map.put("timestamp(6)", DATE);
		map.put("tinyblob", BYTEARR);
		map.put("tinyint", TINT);
		map.put("tinytext", STRING);
		map.put("undefined", STRING);
		map.put("varbinary", BYTEARR);
		map.put("varchar", STRING);
		map.put("varchar2", STRING);

		return Collections.unmodifiableMap(map);
	}

	private static Map<String, String> createNullMap() {
		final Map<String, String> map = new TreeMap<String, String>();

		map.put("int", "java.lang.Integer");
		map.put("long", "java.lang.Long");
		map.put("boolean", "java.lang.Boolean");
		map.put("double", "java.lang.Double");

		return Collections.unmodifiableMap(map);
	}

	private static Map<String, String[]> createImportMap() {
		final Map<String, String[]> map = new TreeMap<String, String[]>();

		map.put("int", new String[]{null, "int"});
		map.put("long", new String[]{null, "long"});
		map.put("boolean", new String[]{null, "boolean"});
		map.put("double", new String[]{null, "double"});
		map.put("java.lang.Integer", new String[]{null, "Integer"});
		map.put("java.lang.Long", new String[]{null, "Long"});
		map.put("java.lang.Boolean", new String[]{null, "Boolean"});
		map.put("java.lang.Double", new String[]{null, "Double"});
		map.put("java.lang.String", new String[]{null, "String"});
		map.put("java.util.Date", new String[]{"java.util.Date", "Date"});
		map.put("java.math.BigDecimal", new String[]{"java.math.BigDecimal", "BigDecimal"});

		return Collections.unmodifiableMap(map);
	}

	private static Map<String, String> createResultSetMap() {
		final Map<String, String> map = new TreeMap<String, String>();

		map.put("int", "getInt");
		map.put("long", "getLong");
		map.put("boolean", "getBoolean");
		map.put("double", "getDouble");
		map.put("Integer", "getInt");
		map.put("Long", "getLong");
		map.put("Boolean", "getBoolean");
		map.put("Double", "getDouble");
		map.put("String", "getString");
		map.put("Date", "getTimestamp");
		map.put("BigDecimal", "getBigDecimal");
		map.put("byte[]", "getBytes");

		return Collections.unmodifiableMap(map);
	}

	private static Map<String, String> createSetNullValueMap() {
		final Map<String, String> map = new TreeMap<String, String>();

		map.put("Integer", "null");
		map.put("Long", "null");
		map.put("Boolean", "null");
		map.put("Double", "null");
		map.put("String", "\"\"");
		map.put("Date", "null");
		map.put("BigDecimal", "null");
		map.put("byte[]", "new byte[0]");

		return Collections.unmodifiableMap(map);
	}

	private static class TableToBean implements Function<Table, String[]> {
		@Override
		public String[] apply(final Table input) {
			final String s = camelCaseName(input.getTableName());
			return new String[]{lowerFirstLetter(s), s};
		}
	}

	private static class ColumnToImport implements Function<Column, String> {
		@Override
		public String apply(final Column input) {
			return IMPORT_MAP.get(determineColumnType(input))[0];
		}
	}

	private static class ImportFilter implements Predicate<String> {
		@Override
		public boolean apply(final String input) {
			return input != null;
		}
	}

	private static class ColumnToProperty implements Function<Column, String[]> {
		@Override
		public String[] apply(final Column c) {
			final String colType = determineColumnType(c);
			OUT.println("\t" + colType + " <- " + c.getDataType() + " " + c.getColType());
			final String cn = camelCaseName(c.getColumnName());
			final String[] arr = new String[6];
			arr[0] = IMPORT_MAP.get(colType)[1];
			arr[1] = lowerFirstLetter(cn);
			arr[2] = cn;
			arr[3] = RESULT_SET_MAP.get(arr[0]);
			arr[4] = c.getNullable() && SET_NULL_MAP.containsKey(arr[0]) ? SET_NULL_MAP.get(arr[0]) : null;
			arr[5] = c.getColumnName();
			return arr;
		}
	}
}
