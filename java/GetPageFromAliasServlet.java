package com.aem.community.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Page Get Child Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/get-alias" })
public class GetPageFromAliasServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		Object qs = req.getParameter("page");
		if (qs != null) {
			Resource rs = req.getResourceResolver().resolve(qs.toString());
			if (rs != null) {
				Page page = rs.adaptTo(Page.class);
				if (page != null) {
					resp.getWriter().write("Page Title is " + page.getPageTitle());
				} else {
					resp.getWriter().write("No Page Found ");
				}
			}
		}
		resp.setContentType("text/plain");
	}
}
