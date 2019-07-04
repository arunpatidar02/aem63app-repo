package com.acc.arch17.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrUtil;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Copy Node Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/copynode" })

public class CopyNodeServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private Session adminSession;

	@Reference
	org.apache.sling.jcr.api.SlingRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(CopyNodeServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		try {
			adminSession = repository.loginService("readService", null);
			Node oldNode = adminSession.getNode("/apps/AEM64App/components/forum/granite-test-component-4/cq:dialog/content/items/column/items/textfield");
			Node parentNode =  adminSession.getNode("/apps/AEM64App/components/forum/dy-tmplte/compA/cq:dialog/content/items/column/items");
			
            JcrUtil.copy(oldNode, parentNode, null);
            adminSession.save();
       
		} catch (RepositoryException e) {
			logger.error("unable to register session", e);

		} finally {
			if (adminSession != null) {
				adminSession.logout();
			}
		}

	}

}