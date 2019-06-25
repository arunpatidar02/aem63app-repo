
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

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Site Simple RenderConditions Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "utils/granite/rendercondition/simple/hidesite" })
public class SiteSimpleRenderConditionsServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) req;
		boolean render = true;

		ValueMap cfg = ResourceUtil.getValueMap(req.getResource());
		String patterns[] = cfg.get("hiddenPaths", new String[] {});
		String sitePath = slingRequest.getRequestPathInfo().getSuffix();
		// String appPath = slingRequest.getRequestPathInfo().getResourcePath().replaceAll("^/mnt/override","");

		if (patterns != null && sitePath != null) {
			render = isRender(patterns, sitePath);
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
