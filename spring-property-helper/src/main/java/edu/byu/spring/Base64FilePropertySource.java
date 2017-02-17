package edu.byu.spring;

import edu.byu.hash.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.PropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

/**
 * Created by wct5 on 3/23/16.
 */
public class Base64FilePropertySource extends PropertySource<Properties> {

	private static final Logger LOG = LogManager.getLogger(Base64FilePropertySource.class);

	private static final String DEFAULT_SOURCE_NAME = "base64filePropertySource";
	private static final String DEFAULT_ENV_KEY = "APPLICATION_PROPERTIES";

	private static final String DUMMY_PROPS = "H4sIAB4zp1gCA0uxTeECADaO91gEAAAA";

	private final Properties properties;

	public Base64FilePropertySource() {
		this(DEFAULT_SOURCE_NAME, DEFAULT_ENV_KEY);
	}

	public Base64FilePropertySource(final String name, final String envKey) {
		super(name, new Properties());
		this.properties = super.source;
		try {
			this.properties.load(new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(Base64.decodeToBytes(getPropValue(envKey))))));
		} catch (IOException e) {
			LOG.error("Unable to load properties.", e);
		} catch (NullPointerException e) {
			LOG.warn("Unable to load properties.", e);
		}
	}

	private String getPropValue(final String envKey) {
		if (System.getenv().containsKey(envKey)) {
			return nss(System.getenv(envKey));
		}
		if (System.getProperties().containsKey(envKey)) {
			return nss(System.getProperties().getProperty(envKey));
		}
		return DUMMY_PROPS;
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
		return s == null ? DUMMY_PROPS : s;
	}
}
