package com.aem.community.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.featureflags.Feature;
import org.apache.sling.featureflags.Features;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Get Features Servlet", "sling.servlet.paths=" + "/bin/page/get-flags" })

public class GetFeaturesServlet extends SlingSafeMethodsServlet {

	@Reference
	private Features features;

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		Feature[] allFeature = features.getFeatures();
		response.setHeader("Content-Type", "text/html");
		String headStr = "<!DOCTYPE html> <html> <head> <title>Faeture Flags</title> <style> #feature {font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; } #feature td, #feature th {border: 1px solid #ddd; padding: 8px; } #feature tr:nth-child(even){background-color: #f2f2f2;} #feature tr:hover {background-color: #ddd;} #feature th {padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #4CAF50; color: white; } </style> </head> <body>";
		String footStr = " </body> </html>";

		String main = "<h2>Number of Features Found  - " + allFeature.length + "</h2>";
		main += "<table id='feature'>";
		main += "<tr><th>S.No.</th><th>Feature Name</th><th>Description</th><th>Enabled ?</th><th>Class</th></tr>";
		for (int i = 0; i < allFeature.length; i++) {
			main += "<tr><td>" + (i + 1) + "</td><td>" + allFeature[i].getName() + "</td><td>"
					+ allFeature[i].getDescription() + "</td><td>" + features.isEnabled(allFeature[i].getName()) + "</td><td>" + allFeature[i].getClass()
					+ "</td></tr>";
		}
		main += "</table>";

		response.getWriter().print(headStr + main + footStr);
		response.getWriter().close();

	}

}
