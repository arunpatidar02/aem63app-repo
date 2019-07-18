package com.aem.community.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.components.IncludeOptions;

@Component(service = Filter.class, property = {
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_COMPONENT,
		"sling.filter.resourceTypes" + "=" + "cq/experience-fragments/editor/components/experiencefragment",
		Constants.SERVICE_RANKING + ":Integer=" + 201 })
public class XFFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		IncludeOptions options = IncludeOptions.getOptions(request, true);
		// options.forceSameContext(true);
		options.getCssClassNames().add("footer");
		options.setDecorationTagName("div");

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

}