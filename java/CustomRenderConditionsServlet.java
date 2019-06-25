
package com.acc.aem64.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;

/**
 * Servlet that use to render or not render fields based on sites(content paths) and/or app paths
 * 
 * granite:rendercondition node properties 
 * - sling:resourceType (String)
 * 		utils/granite/rendercondition/simple/sites-apps 
 * - hiddenSitePaths (String[])
 * 		content path regex for which field will not rendered 
 * - hiddenAppPaths	(String[])
 * 		apps path regex for which field will not rendered in dialog 
 * - and (Boolean)
 * 		true to not rendered field based on both App and Content path regex,false otherwise.
 * 
 */

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Custom Path RenderConditions Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "utils/granite/rendercondition/simple/sites-apps" })
public class CustomRenderConditionsServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) req;
		boolean render = true;
		ValueMap cfg = ResourceUtil.getValueMap(req.getResource());

		// Reading renderer properties
		String sitePatterns[] = cfg.get("hiddenSitePaths", new String[] {});
		String appPatterns[] = cfg.get("hiddenAppPaths", new String[] {});
		Boolean andCondition = cfg.get("and", Boolean.class) != null ? cfg.get("and", Boolean.class) : false;

		String sitePath = slingRequest.getRequestPathInfo().getSuffix();
		String appPath = slingRequest.getRequestPathInfo().getResourcePath().replaceAll("^/mnt/override", "");

		if ((sitePatterns != null && sitePath != null) || (appPatterns != null && appPath != null)) {
			if (sitePatterns != null && appPatterns != null)
				if (andCondition)
					render = isRender(sitePatterns, sitePath) || isRender(appPatterns, appPath);
				else
					render = isRender(sitePatterns, sitePath) && isRender(appPatterns, appPath);
			else if (sitePatterns != null)
				render = isRender(sitePatterns, sitePath);
			else if (appPatterns != null)
				render = isRender(appPatterns, appPath);
		}

		req.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(render));
	}

	private boolean isRender(String[] patterns, String path) {
		for (int i = 0; i < patterns.length; i++) {
			if (path.matches(patterns[i])) {
				return false;
			}
		}
		return true;
	}
}
