package edu.byu.web.resolvers;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

/**
 * Created by Wyatt Taylor on 1/30/17.
 */
public class JsonViewResolver implements ViewResolver {

	@Override
	public View resolveViewName(final String viewName, final Locale locale) throws Exception {
		final MappingJackson2JsonView view = new MappingJackson2JsonView();
//		view.setPrettyPrint(false);
		return view;
	}
}
