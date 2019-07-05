package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.acc.aem64.core.services.FileService;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Demo Servlet to access factory configs",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/facttest" })
public class FactConfigTestServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	
	@Reference(target="(file.type=xml)")
	FileService fs1;
	
	@Reference(target="(file.type=pdf)")
	FileService fs2;
	
	
	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServerException, IOException {
		try {
			resp.setContentType("text/html");
			resp.getWriter().write("<br>"+fs1.getFileData());
			resp.getWriter().write("<br>"+fs2.getFileData());
			resp.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
