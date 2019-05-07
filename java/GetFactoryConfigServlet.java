package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;

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
		"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.paths=" + "/bin/admin/get-factory-config" })
public class GetFactoryConfigServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final String filter = "(service.factoryPid=org.apache.sling.commons.log.LogManager.factory.config)";

	@Reference
	private ConfigurationAdmin configAdmin;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServerException, IOException {
		try {
			Configuration[] configurations = configAdmin.listConfigurations(filter);

			resp.setHeader("Content-Type", "text/html");
			String headStr = "<!DOCTYPE html> <html> <head> <title>Faeture Flags</title> <style> #feature {font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; } #feature td, #feature th {border: 1px solid #ddd; padding: 8px; } #feature tr:nth-child(even){background-color: #f2f2f2;} #feature tr:hover {background-color: #ddd;} #feature th {padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #4CAF50; color: white; } </style> </head> <body>";
			String footStr = " </body> </html>";

			String main = "<h2>Number of Configurations Found  - " + configurations.length + "</h2>";
			main += "<table id='feature'>";
			main += "<tr><th>S.No.</th><th>Pid</th></tr>";
			for (int i = 0; i < configurations.length; i++) {
				main += "<tr><td>" + (i + 1) + "</td><td>" + configurations[i].getPid() + "</td></tr>";
			}
			main += "</table>";

			resp.getWriter().print(headStr + main + footStr);
			resp.getWriter().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
