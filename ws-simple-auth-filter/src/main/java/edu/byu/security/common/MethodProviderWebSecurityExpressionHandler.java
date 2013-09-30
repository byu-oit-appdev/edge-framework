package edu.byu.security.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/05/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/05/2013
 */
public class MethodProviderWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler implements InitializingBean {

	private static final Logger LOG = Logger.getLogger(MethodProviderWebSecurityExpressionHandler.class);

	private List<SecurityExpressionMethodProvider> methodProviders;

	private Map<String, Object> providedMethods = new LinkedHashMap<String, Object>(16, .999999f);

	public void setMethodProviders(final List<SecurityExpressionMethodProvider> methodProviders) {
		LOG.debug("MethodProvider: setMethodProviders");
		this.methodProviders = methodProviders;
		for (final SecurityExpressionMethodProvider mp : methodProviders) {
			providedMethods.putAll(mp.getMethods());
		}
	}

	@Override
	protected StandardEvaluationContext createEvaluationContextInternal(final Authentication authentication, final FilterInvocation invocation) {
		LOG.debug("MethodProvider: createEvaluationContextInternal");
		final StandardEvaluationContext ctx = super.createEvaluationContextInternal(authentication, invocation);
		ctx.setVariable("filterInvocation", invocation);
		ctx.setVariables(providedMethods);
		return ctx;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.debug("MethodProvider: afterPropertiesSet");
	}
}
