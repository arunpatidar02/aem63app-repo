package com.aem.community.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet to get Session ",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/get/session/subservice"
})

public class SeesionInServletWithSubservice extends SlingSafeMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	ResourceResolver resourceResolver;
	@Reference
	private ResourceResolverFactory resourceFactory;
	
	Map<String,Object> paramMap = new HashMap<String,Object>();
	
	//paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
	
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException,IOException{
		response.setContentType("text/html");
		try {
			// Get the resource (a node in the JCR) using ResourceResolver from the request
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			Session session = resourceResolver.adaptTo(Session.class);
			if(session.isLive()) {
				response.getWriter().println("Session User is "+session.getUserID());
				session.logout();
			}
			

			
		} catch (Exception e) {
			response.getWriter().println("Can't get session from subservice");
			logger.error(e.getMessage());
		}
		response.getWriter().close();
	}
	
}
