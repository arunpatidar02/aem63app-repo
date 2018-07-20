package com.aem64.core.utils;

import java.util.HashMap;
import java.util.Map;
import javax.jcr.Session;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;

/*
 * 10-03-2018 : Not useful, nothing is working , Need to create service
 * 22-04-2018 : Created service - working after creating services and using other services as @Reference
 * 
 */

@Component
public class HandleSessionsImpl implements HandleSession {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Reference
	private SlingRepository repository = null;

	@Reference
	private ResourceResolverFactory resourceFactory;

	ResourceResolver resourceResolver = null;

	// Get Session inside Sling Servlet
	public Session getServeltSession(SlingHttpServletRequest req) {
		Session session = null;
		try {
			session = req.getResourceResolver().adaptTo(Session.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getServeltSession : Unable to Login");
		}
		return session;
	}

	// Get Session from current Page
	public Session getSessionFromPage(Page currentPage) {
		Session session = null;
		try {
			resourceResolver = currentPage.getContentResource().getResourceResolver();
			session = resourceResolver.adaptTo(Session.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getSessionFromPage : Unable to Login");
		}
		return session;
	}

	public Session getSubserviceSession() {
		Session session = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
		try {
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			// logger.debug("User id inside {}", resourceResolver.getUserID());
			session = resourceResolver.adaptTo(Session.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getSubserviceSession : Unable to Login : " + e);
		}
		return session;
	}

	// Get Session for JCR repository operation
	public Session getJCRSession() {
		Session session = null;
		try {
			session = repository.loginService("readService", null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.debug("getJCRSession : Unable to Login : " + e1);
		}
		return session;
	}

	// Close Session
	public void closeSession(Session session) {
		if (session != null) {
			session.logout();
			session = null;

		}
	}

}
