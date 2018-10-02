package com.aem.community.core.servlets;

import java.io.IOException;

import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.featureflags.Features;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Get Page Version Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/getVersion"
})
public class GetPageVersionServlet extends SlingSafeMethodsServlet {

	@Reference
	private SlingRepository repository = null;
	
	
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		String qs = "/content/AEM63App/fr/jcr:content";
		
		
				try {
					
					VersionManager vm = getJCRSession().getWorkspace().getVersionManager();
					VersionHistory vh ;
					Version version ;
					
					vh = vm.getVersionHistory(qs);
					version = vm.getBaseVersion(qs);
					resp.getWriter().write("Base Version is "+version.getPath()+"<br>");
					
					VersionIterator vi =vh.getAllVersions();
					resp.getWriter().write("Other Version are <br>");
					while(vi.hasNext()) {
						Version  v = vi.nextVersion();
						resp.getWriter().write("<br>"+vi.getPosition()+"."+v.getPath());
					}		
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resp.getWriter().write("ERROR : Not able to get Version, something is wrong --->"+e);
					logger.info("Error "+e.getMessage());
					e.printStackTrace();
				}
		
		resp.setContentType("text/html");
		resp.getWriter().close();
	}



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
}
