
package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

		ValueMap cfg = ResourceUtil.getValueMap(req.getResource());
		boolean show = true;
		String sites[] = cfg.get("hidden", new String[] {});

		if (sites != null) {
			String site = req.getPathInfo().replaceAll("^.*_cq_dialog\\.html\\/content/", "").replaceAll("\\/.*", "");
			List<String> appList = Arrays.asList(sites);
			if (appList.contains(site)) {
				show = false;
			}
		}

		req.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(show));
	}
}
