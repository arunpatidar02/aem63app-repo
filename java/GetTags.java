package com.aem64.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Page Get Tags Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/tags/get-tags" })
public class GetTags extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		try {
			TagManager tagManager = req.getResourceResolver().adaptTo(TagManager.class);

			Tag tagObj = tagManager.resolve("/content/cq:tags/we-retail/apparel/hat");
			Tag tagObj2 = tagManager.resolve("/etc/tags/we-retail/apparel/hat");

			resp.getWriter().write("<p>Hat Tag details(Content) " + tagObj.getTitle() + "</p>");
			resp.getWriter().write("<p>Hat Tag details(etc) " + tagObj2.getTitle() + "</p>");

		} catch (Exception e) {
			resp.getWriter().write("<p>" + e.getMessage() + "</p>");
		}

		resp.getWriter().close();

		resp.setContentType("text/html");
	}
}
