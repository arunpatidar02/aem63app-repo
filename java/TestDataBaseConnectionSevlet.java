package com.aem.community.core.servlets;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.community.core.utils.DSConnectionWA;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Testing Database Connection", "sling.servlet.paths=/bin/aem63app/testDbCon",
		 "sling.servlet.methods=" + HttpConstants.METHOD_GET })

public class TestDataBaseConnectionSevlet extends SlingSafeMethodsServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private DSConnectionWA db; 
	
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "text/html");
		resp.getWriter().print("<h1>Getting DB Connection from com.aem.community.core.utils.DSCoonectionWA</h1>");
		try {
			Connection con = db.getConnection();
			resp.getWriter().print("<h2>Coonection is open ? : "+!con.isClosed() +"</h2>");
			db.closeConnection(con);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.getWriter().print("<h1>Error in com.aem.community.core.utils.DSCoonectionWA</h1>");
			logger.info("Exeption : {}",e);	
		}
		resp.getWriter().close();
	}
}
