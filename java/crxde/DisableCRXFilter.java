
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
import org.osgi.service.component.annotations.Reference;

import com.aem.community.core.services.DisableCRXService;

/**
 * Simple Servlet filter component that redirect users on Touch UI home page if
 * access CRXDE for incoming requests.
 */
@Component(service = Filter.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Filter incoming CRXDE requests and redirect to new home page",
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
		Constants.SERVICE_RANKING + "=-701"

})
public class DisableCRXFilter implements Filter {

	@Reference
	private DisableCRXService disableCRXService;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		if (disableCRXService.isServiceEnabled()) {
			final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
			final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

			String currentUser = slingRequest.getRemoteUser();

			final String CRXDE_URL = "/crx/de/index.jsp";
			final String TOUCH_UI_HOME_SCRIPT = "/apps/AEM63App/clientlibs/crxredirect.js";
			final String SCRIPT_URL = "/libs/cq/i18n/dict";
			
			String refererURL = slingRequest.getHeader("referer");
			String url = slingRequest.getPathInfo();

			if (url.contains(SCRIPT_URL) && refererURL != null && refererURL.contains(CRXDE_URL)) {
				if (disableCRXService.isRestrictedUser(currentUser))
					slingResponse.sendRedirect(TOUCH_UI_HOME_SCRIPT);
			}
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