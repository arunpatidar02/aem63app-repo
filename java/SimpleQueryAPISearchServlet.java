package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
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

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Simple Query Builder API Serach Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/simple-qb-search" })
public class SimpleQueryAPISearchServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repository = null;

	@Reference
	private QueryBuilder queryBuilder;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;

	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		Session session = null;
		try {
			/*
			 * Query
			 * 
			 * type=cq:Page path=/content/we-retail 
			 * group.1_property=jcr:content/jcr:created
			 * group.1_property.operation= exists
			 * group.2_property=jcr:content/cq:lastModified 
			 * group.2_property.operation=exists 
			 * group.p.or=true 
			 * p.limit=-1
			 * 
			 * 
			 * 
			 */
			session = repository.loginService("readService", null);

			Map<String, String> filteredQueryParamMap = new HashMap<>();

			filteredQueryParamMap.put("type", "cq:Page");
			filteredQueryParamMap.put("path", "/content/we-retail");
			filteredQueryParamMap.put("group.1_property", "jcr:content/@jcr:created");
			filteredQueryParamMap.put("group.1_property.operation", "exists");
			filteredQueryParamMap.put("group.2_property", "jcr:content/@cq:lastModified");
			filteredQueryParamMap.put("group.2_property.operation", "exists");
			filteredQueryParamMap.put("group.p.or", "true");
			filteredQueryParamMap.put("p.limit", "-1");
			
			
			// Map 2
			Map<String, String[]> filteredQueryParamMap2 = new HashMap<>();

			filteredQueryParamMap2.put("type", new String[]{"cq:Page"});
			filteredQueryParamMap2.put("path", new String[]{"/content/we-retail"});
			filteredQueryParamMap2.put("group.1_property", new String[]{"jcr:content/@jcr:created"});
			filteredQueryParamMap2.put("group.1_property.operation", new String[]{"exists"});
			filteredQueryParamMap2.put("group.2_property", new String[]{"jcr:content/@cq:lastModified"});
			filteredQueryParamMap2.put("group.2_property.operation", new String[]{"exists"});
			filteredQueryParamMap2.put("group.p.or", new String[]{"true"});
			filteredQueryParamMap2.put("p.limit", new String[]{"-1"});

			Query query = queryBuilder.createQuery(PredicateGroup.create(filteredQueryParamMap2), session);
			SearchResult result = query.getResult();
			logger.info("Result node {}", result.getTotalMatches());
			response.getWriter().print("Result nodes found  : " + query.getResult().getHits().size());
			/*result.getHits().forEach((r) -> {
				try {
					response.getWriter().print("nodes  : " + r.getPath());
				} catch (IOException | RepositoryException e) {
					e.printStackTrace();
				}
			});
			response.getWriter().print("----- Result nodes found  : " + query.getResult().getHits().size());*/

		} catch (RepositoryException e) {
			logger.info("Exception {}", e);
			response.getWriter().print(e.getMessage());
		} finally {
			if (session != null)
				session.logout();
			response.getWriter().close();
		}
		response.setContentType("text/html");

	}

}
