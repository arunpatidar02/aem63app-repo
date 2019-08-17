package com.aem.community.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;


@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Multifield Coral2 to Coral3 Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/utils/multifield/coral2to3" })
public class MultifieldConvertCoral2to3Servlet extends SlingSafeMethodsServlet {

	@Reference
	private SlingRepository repository = null;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_PATH = "/content/AEM63App";
	private static final String MULTIFIELD_COMPONENT = "AEM63App/components/content/touchmulti";
	private static final String MULTIFIELD_PROPERTY = "iItems";

	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		Session session = null;
		try {
			
			String q = "SELECT * FROM [nt:unstructured] AS s WHERE s.[sling:resourceType] = '"+MULTIFIELD_COMPONENT+"'  AND ISDESCENDANTNODE('"+CONTENT_PATH+"')";
			session = repository.loginService("readService", null);
			final Query query = session.getWorkspace().getQueryManager().createQuery(q, Query.JCR_SQL2);
			final NodeIterator result = query.execute().getNodes();
			logger.info("Result node {}", result.getSize());
			response.getWriter().print("Result nodes found  : " + result.getSize());
			while(result.hasNext()) {
				Node node = (Node)result.next();
				if(node.hasProperty(MULTIFIELD_PROPERTY) && !node.hasNode(MULTIFIELD_PROPERTY)) {
					Property property = node.getProperty(MULTIFIELD_PROPERTY);
					Value[] values = property.getValues();
					Gson gson = new Gson();
					Node Childnode = node.addNode(MULTIFIELD_PROPERTY);
					int c=0;
					for (Value v : values) {		
						MultifieldConvertItems item= gson.fromJson(v.getString(), MultifieldConvertItems.class);
						Node itemNode = Childnode.addNode("item"+c++);
						itemNode.setProperty("page", item.getPage());
						itemNode.setProperty("path", item.getPath());
						itemNode.setProperty("aboutrich", item.getAboutrich());
					}
				}
			}
			session.save();

		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			logger.info("Exception {}", e);
			response.getWriter().print(e.getMessage());
		} finally {
			if (session != null)
				session.logout();
			response.getWriter().close();
		}
		response.setContentType("text/plain");

	}
	
	public class MultifieldConvertItems {
		private String page;
		private String path;
		private String aboutrich;

		public String getPage() {
			return page;
		}

		public String getPath() {
			return path;
		}

		public String getAboutrich() {
			return aboutrich;
		}

	}

}
