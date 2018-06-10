package com.aem.community.core.utils.feature;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.featureflags.ExecutionContext;
import org.apache.sling.featureflags.Feature;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Feature.class, immediate = true)
public class AuthorFeature implements Feature {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Reference
	private ResourceResolverFactory resourceFactory;

	@Override
	public String getDescription() {
		return "mysite-author description2";
	}

	@Override
	public String getName() {
		return "mysite-author2";
	}

	@Override
	public boolean isEnabled(ExecutionContext ec) {
		
		// if user is admin, return true to available feature for admin
		if (ec.getResourceResolver().getUserID().equalsIgnoreCase("admin"))
			return true;

		boolean isAuthor = false;
		boolean foundAuthor = false;
		try {
			// featureNodePath contains group names against which feature evaluate and
			// return true or false
			String featureNodePath = "/apps/AEM63App/tools/features/my-author2";
			Node itemNode = ec.getResourceResolver().getResource(featureNodePath).adaptTo(Node.class);
			if (itemNode.hasProperty("group")) {
				// getting all the groups specify for this feature
				Value[] vm = itemNode.getProperty("group").getValues();

				String currentUser = ec.getResourceResolver().getUserID();
				/*
				 * getting session to access /home/ node for checking current user against
				 * groups avoid getting session from current user session, e.g. current user may
				 * not have access to /home/ folder in this example
				 * 
				 */
				Session session = getSubserviceSession();

				UserManager uM;
				JackrabbitSession jcrSession = (JackrabbitSession) session;
				uM = jcrSession.getUserManager();

				for (int i = 0; i < vm.length; i++) {
					// checking if user already belongs to one of previous group a
					if (!foundAuthor) {
						Group group = (Group) uM.getAuthorizable(vm[i].getString().trim());

						Iterator<Authorizable> itr = group.getMembers();
						while (itr.hasNext()) {
							Object obj = itr.next();
							if (obj instanceof User) {
								User user = (User) obj;
								String uid = user.getID();
								if (uid.equalsIgnoreCase(currentUser)) {
									isAuthor = true;
									foundAuthor = true;
									break;
								}
							}
						}
					}
				}

				closeSession(session);

			}

		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isAuthor;
	}

	// Getting Session from system user
	public Session getSubserviceSession() {
		Session session = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
		try {
			ResourceResolver resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			session = resourceResolver.adaptTo(Session.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("getSubserviceSession : Unable to Login : " + e);
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
