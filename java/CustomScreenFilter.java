
package com.aem.community.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Servlet filter component that redirect users on classic UI home page for incoming requests.
 */
@Component(service = Filter.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Filter incoming requests and redirect to new home page",
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
		Constants.SERVICE_RANKING + "=-701"

})
public class CustomScreenFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

		final String TOUCH_UI_HOME = "/aem/start.html";
		final String CLASSIC_UI_HOME = "/welcome";

		String targetUser = "aem63user1";

		String url = slingRequest.getPathInfo().toString();
		String currentUser = slingRequest.getRemoteUser();

		if (url.equalsIgnoreCase(TOUCH_UI_HOME) && currentUser.equals(targetUser)) {
			request.getRequestDispatcher(CLASSIC_UI_HOME).forward(request, response);
		}

		filterChain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

}