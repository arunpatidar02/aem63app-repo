package com.aem.community.core.services.impl;

import java.util.Arrays;
import java.util.Iterator;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.community.core.services.DisableCRXConfig;
import com.aem.community.core.services.DisableCRXService;

@Component(service = DisableCRXService.class, immediate = true)
@Designate(ocd = DisableCRXConfig.class)
public class DisableCRXServiceImpl implements DisableCRXService {

	@Reference
	private SlingRepository repository;

	private Session session;

	// private final Logger logger = LoggerFactory.getLogger(getClass());

	// local variables to hold OSGi config values
	private boolean enabled;
	private String[] restrictedUsers;
	private String[] restrictedGroups;
	private String[] excludedUsers;

	@Activate
	@Modified
	protected void activate(final DisableCRXConfig crxConfiguration) {
		this.enabled = crxConfiguration.isEnabled();
		this.restrictedUsers = crxConfiguration.getUsers();
		this.restrictedGroups = crxConfiguration.getGroups();
		this.excludedUsers = crxConfiguration.getExcludedUsers();
	}

	@Deactivate
	protected void deactivated() {
		// logger.debug("Service deactivated");
	}

	@Override
	public boolean isServiceEnabled() {
		return enabled;
	}

	@Override
	public boolean isRestrictedUser(String currentUserId) {
		boolean isRestrcited = false;
		if (excludedUsers != null && Arrays.asList(excludedUsers).contains(currentUserId))
			isRestrcited = false;
		else if (restrictedUsers != null && Arrays.asList(restrictedUsers).contains(currentUserId))
			isRestrcited = true;
		else if (restrictedGroups != null && restrictedGroups.length > 0) {
			if (!restrictedGroups[0].isEmpty() && isUserMemberOfRestrictedGroup(currentUserId, restrictedGroups))
				isRestrcited = true;
		}
		return isRestrcited;
	}

	private boolean isUserMemberOfRestrictedGroup(String currentUserId, String[] restrictedGroups) {
		boolean foundUser = false;
		try {
			session = repository.loginService("readService", null);
			UserManager uM;
			JackrabbitSession jcrSession = (JackrabbitSession) session;
			uM = jcrSession.getUserManager();
			for (int i = 0; i < restrictedGroups.length; i++) {
				// checking if user already belongs to one of previous group
				if (!foundUser) {
					Group group = (Group) uM.getAuthorizable(restrictedGroups[i].trim());
					Iterator<Authorizable> itr = group.getMembers();
					while (itr.hasNext()) {
						Object obj = itr.next();
						if (obj instanceof User) {
							User user = (User) obj;
							String uid = user.getID();
							if (uid.equalsIgnoreCase(currentUserId)) {
								foundUser = true;
								break;
							}
						}
					}
				}
			}

		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.logout();
				session = null;
			}
		}
		return foundUser;
	}

}
