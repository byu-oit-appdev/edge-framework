package edu.byu.spring;

import edu.byu.hash.Base64;
import org.apache.log4j.Logger;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Created by wct5 on 3/23/16.
 */
public class Base64FilePropertySource extends PropertySource<Properties> {

	private static final Logger LOG = Logger.getLogger(Base64FilePropertySource.class);

	private static final String DEFAULT_SOURCE_NAME = "base64filePropertySource";
	private static final String DEFAULT_ENV_KEY = "APPLICATION_PROPERTIES";

	private final Properties properties;

	public Base64FilePropertySource() {
		this(DEFAULT_SOURCE_NAME, DEFAULT_ENV_KEY);
	}

	public Base64FilePropertySource(final String name, final String envKey) {
		super(name, new Properties());
		this.properties = super.source;
		try {
			this.properties.load(new StringReader(Base64.decodeToString(nss(System.getenv(envKey)))));
		} catch (IOException e) {
			LOG.error("Unable to load");
		}
	}

	@Override
	public boolean containsProperty(final String name) {
		return this.properties.containsKey(name);
	}

	@Override
	public Object getProperty(final String name) {
		return this.properties.getProperty(name);
	}

	private static String nss(final String s) {
		return s == null ? "" : s;
	}
}
