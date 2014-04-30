package edu.byu.edge.jdbc;

import edu.byu.edge.jdbc.domain.Column;
import edu.byu.edge.jdbc.domain.Table;
import edu.byu.edge.jdbc.impl.MySqlParser;
import edu.byu.edge.jdbc.impl.OracleParser;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.io.*;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 11/30/2012
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 11/30/2012
 */
public class JdbcGen {

	private static final Logger LOG = Logger.getLogger(JdbcGen.class);

	private static final Pattern NUMBERS = Pattern.compile("^\\d+$");

	public static volatile Console CONSOLE;
	public static volatile PrintWriter OUT;
	private static volatile String[] data;
	private static volatile int pos = 0;

	private static volatile Parser p;
	private static volatile Exporter e;

	public static void main(String[] args) throws IOException {
		data = checkForInputFile(args);
		OUT.println("At any time, enter 'Q' to quit.");
		OUT.println();
		OUT.println("Select database provider:");
		OUT.println("1. Oracle");
		OUT.println("2. MySQL");
		OUT.println();

		determineParser();

		if (p == null) return;
		OUT.println(p.getSchemaNameMessage());
		final String schema = getInput();
		OUT.println("Please enter a comma-separated list of table names to output. A '*' or an empty line means all.");
		final String tableSet = getInput();
		final List<Table> tables = p.getTables(schema, tableSet);
		if (tables == null || tables.isEmpty()) {
			OUT.println("No tables found.");
			return;
		}
		p.getColumnsForTables(tables);
		OUT.println();
		OUT.println("Please enter output directory.");
		final String path = getInput();
		OUT.println("Please enter base java package name.");
		final String pkgraw = getInput();
		final String pkg = pkgraw.replaceAll("[\\\\\\/]", ".");
		if (pkg.equals("")) {
			OUT.println("You must enter a package. The default package is not allowed.");
			return;
		}
		e.export(tables, path, pkg, schema);
		OUT.println();
	}

	private static String[] checkForInputFile(final String[] args) {
		if (args.length == 1) {
			final File input = new File(args[0]);
			if (input.exists() && input.isFile()) {
				final List<String> tmp = new LinkedList<String>();
				try {
					final BufferedReader in = new BufferedReader(new FileReader(input));
					String line;
					while ((line = in.readLine()) != null) {
						tmp.add(line);
					}
					OUT = new PrintWriter(System.out, true);
					CONSOLE = null;
					return tmp.toArray(new String[tmp.size()]);
				} catch (final Throwable ignore) {
				}
			}
		}
		CONSOLE = System.console();
		OUT = CONSOLE.writer();
		return null;
	}

	private static String getInput() {
		return getInput(false);
	}

	private static String getInput(final boolean mask) {
		if (data != null) return data[pos++];
		if (mask) return new String(CONSOLE.readPassword());
		else return CONSOLE.readLine();
	}

	private static String[] getParams() {
		OUT.println("Please enter JDBC URL:");
		final String url = getInput();
		OUT.println("Please enter username.");
		final String user = getInput();
		OUT.println("Please enter password.");
		final String password1 = getInput(true);
		OUT.println("Please verify password.");
		final String password2 = getInput(true);
		if (!password1.equals(password2)) {
			OUT.println("Passwords do not match.");
			System.exit(-1);
		}
		return new String[] {url, user, password1};
	}

	private static void determineParser() {
		final String s = getInput();
		if ("Q".equals(s)) {
		} else if (NUMBERS.matcher(s).matches()) {
			createParser(Integer.parseInt(s));
		} else {
			OUT.println("Invalid input.");
		}
	}

	private static void createParser(final int option) {
		final String[] p = getParams();
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.registerShutdownHook();
		final TreeMap<String, Object> map = new TreeMap<String, Object>();
		ctx.getEnvironment().getPropertySources().addFirst(new MapPropertySource("MY_PROPS", map));
		map.put("jdbcUrl", p[0]);
		map.put("user", p[1]);
		map.put("password", p[2]);
		switch (option) {
			case 1:
				map.put("driverName", OracleParser.DRIVER_CLASS_NAME);
				ctx.setConfigLocation("classpath:oracle-context.xml");
				break;
			case 2:
				map.put("driverName", MySqlParser.DRIVER_CLASS_NAME);
				ctx.setConfigLocation("classpath:mysql-context.xml");
				break;
			default:
				OUT.println("Invalid input.");
				return;
		}
		ctx.refresh();
	}

	public static void setParser(final Parser theParser) {
		p = theParser;
	}

	public static void setExporter(final Exporter theExporter) {
		e = theExporter;
	}
}
