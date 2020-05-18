package com.aem.community.core.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Simple Get Group Servlet", "sling.servlet.paths=/bin/user/create", })
public class CreateUserServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	ResourceResolver resourceResolver;
	@Reference
	private ResourceResolverFactory resourceFactory;

	@Reference
	private SlingRepository repository = null;

	Map<String, Object> paramMap = new HashMap<String, Object>();

	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		try {
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "cugService");
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			UserManager userManager = resourceResolver.adaptTo(UserManager.class);
			if (userManager == null) {
				logger.error("userManager == null!");
				return;
			}
			String login = "cug-user-1";
			String password = "cug-user-1";
			User user = userManager.createUser(login, password);

			Authorizable authUser = userManager.getAuthorizable(user.getID());
			Group group = (Group) userManager.getAuthorizable("my-approver");
			group.addMember(authUser);

			logger.info("User id is {}", user.getID());
			resourceResolver.close();

			Session session = repository.loginService("cugService", null);

			// check user permissions to Group
			AccessControlManager accCtrlMgr = session.getAccessControlManager();
			Privilege pri[] = accCtrlMgr.getPrivileges("/content/AEM63App");
			logger.info("ACL - {}", Arrays.asList(pri));

			session.logout();

		} catch (Exception e) {
			response.getWriter().println("Something is wrong...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		response.getWriter().close();
	}

}
