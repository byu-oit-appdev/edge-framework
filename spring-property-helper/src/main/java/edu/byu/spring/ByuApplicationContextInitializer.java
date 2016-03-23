package edu.byu.spring;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by wct5 on 3/23/16.
 */
public class ByuApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private String sourceName;
	private String envKey;

	public ByuApplicationContextInitializer() {
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(final String sourceName) {
		this.sourceName = sourceName;
	}

	public String getEnvKey() {
		return envKey;
	}

	public void setEnvKey(final String envKey) {
		this.envKey = envKey;
	}

	@Override
	public void initialize(final ConfigurableApplicationContext applicationContext) {
		final Base64FilePropertySource source = createPropertySource();
		applicationContext.getEnvironment().getPropertySources().addFirst(source);
	}

	private Base64FilePropertySource createPropertySource() {
		if (vs(sourceName) && vs(envKey)) {
			return new Base64FilePropertySource(sourceName, envKey);
		} else {
			return new Base64FilePropertySource();
		}
	}

	private static boolean vs(final String s) {
		return s != null && !"".equals(s);
	}
}
