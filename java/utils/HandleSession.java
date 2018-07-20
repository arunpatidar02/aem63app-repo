package com.aem64.core.utils;

import javax.jcr.Session;
import org.apache.sling.api.SlingHttpServletRequest;
import com.day.cq.wcm.api.Page;

public interface HandleSession {
	public Session getServeltSession(SlingHttpServletRequest req);
	public Session getSessionFromPage(Page currentPage);
	public Session getSubserviceSession();
	public Session getJCRSession();
	public void closeSession(Session session);
}
