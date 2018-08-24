package com.aem.community.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/*
 * Rename old-page to new-page at /content/we-retail/us/en/men
 */

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Page Rename Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/move-page" })
public class MovePageServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		Page page = req.getResourceResolver().getResource("/content/we-retail/us/en/men/old-page").adaptTo(Page.class);
		PageManager pm = req.getResourceResolver().adaptTo(PageManager.class);
		try {
			resp.getWriter().write(page.getTitle());
			resp.getWriter().write(pm.toString());
			pm.move(page, "/content/we-retail/us/en/men/new-page", null, false, true, null);
			resp.getWriter().write("Page moved Suucesfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resp.getWriter().write("ERROR : Not able to move Page , something is wrong" + e.getMessage());
			e.printStackTrace(resp.getWriter());
		}

		resp.setContentType("text/plain");
	}
}
