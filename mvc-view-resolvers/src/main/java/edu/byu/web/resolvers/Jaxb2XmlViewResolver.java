package edu.byu.web.resolvers;

import org.springframework.oxm.Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.Locale;

/**
 * Created by Wyatt Taylor on 1/30/17.
 */
public class Jaxb2XmlViewResolver implements ViewResolver {

	private final Marshaller marshaller;

	public Jaxb2XmlViewResolver(final Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	@Override
	public View resolveViewName(final String viewName, final Locale locale) throws Exception {
		return new MarshallingView(marshaller);
	}
}
