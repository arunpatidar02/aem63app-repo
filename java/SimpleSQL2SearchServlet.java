package com.aem.community.core.servlets;

import java.io.IOException;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Simple SQL 2 Serach Servlet", "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/page/simple-sql2-search" })
public class SimpleSQL2SearchServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repository = null;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;

	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		Session session = null;
			try {
				String q = "SELECT * FROM [cq:Page] AS s WHERE s.[jcr:content/sling:resourceType] = 'AEM63App/components/structure/page' AND ISDESCENDANTNODE('/content/AEM63App/en')";
				session = repository.loginService("readService",null);
				final Query query=session.getWorkspace().getQueryManager().createQuery(q,Query.JCR_SQL2);
				final NodeIterator result=query.execute().getNodes();
				logger.info("Result node {}" , result.getSize());
				response.getWriter().print("Result nodes found  : "+result.getSize());

			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				logger.info("Exception {}", e);
				response.getWriter().print(e.getMessage());
			}finally {
				if(session != null)
				session.logout();
				response.getWriter().close();
		} 
		response.setContentType("text/plain");
		
	}

}
