/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.community.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Servlet filter component that create Cookie for incoming requests.
 */
@Component(service = Filter.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Filter incoming requests and create cookie",
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
		Constants.SERVICE_RANKING + "=-700"

})
public class DemoCookieFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

		String url = ((HttpServletRequest) request).getRequestURL().toString();

		// logger.info("filter start : "+url);
		if (url.contains("/myDemoAEM63App/") && !url.contains("_jcr_content")) {

			String axesCookieVal = "en";
			
			boolean isCookieAlreadySet = true;
			boolean isAxes4QSPresent = false;

			if (request.getParameter("urlParam1") != null) {
				axesCookieVal = request.getParameter("urlParam1").toString();
			}
			if(slingRequest.getCookie("myDemoCookie")==null) {
				isCookieAlreadySet = false;
			}
			

			if(!isCookieAlreadySet || isAxes4QSPresent ) {
				String cookieVal = axesCookieVal;
				Cookie axesCookie = new Cookie("myDemoCookie", cookieVal);
				axesCookie.setMaxAge(3600 * 24 * 15 * 1000); // 15 days (for example)
				axesCookie.setPath("/");
				slingResponse.addCookie(axesCookie);
			}

			// logger.info("if end");
		}

		filterChain.doFilter(request, response);
		// logger.info("filter end");
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

}