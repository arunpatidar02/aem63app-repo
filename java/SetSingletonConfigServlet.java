package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet with GET",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/admin/set-singleton-config" })
public class SetSingletonConfigServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final String PID = "com.acc.arch17.core.schedulers.SimpleScheduledTask";
	private static final String PROPERTY_NAME = "myParameter";

	@Reference
	private ConfigurationAdmin configAdmin;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServerException, IOException {
		try {
			Configuration configuration = configAdmin.getConfiguration(PID);

			resp.setHeader("Content-Type", "text/html");
			if (configuration != null) {
				resp.getWriter().print("<h2>Configurations Found</h2>");
				// get properties
				Dictionary<String, Object> properties = configuration.getProperties();
				// update properties
				/* Set properties */
		        if (properties == null) {
		            properties = new Hashtable<String, Object>();
		        }
				properties.put(PROPERTY_NAME, "new value");
				configuration.update(properties);
				resp.getWriter().print("<h4>Property Updated</h4>");
			}
			resp.getWriter().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
