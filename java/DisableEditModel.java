package com.aem.community.core.models;

import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.commons.WCMUtils;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DisableEditModel {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@SlingObject
	private SlingHttpServletRequest request;
	
	private final String GROUP ="my-approver";

	@PostConstruct
	protected void init() {
		try {
			boolean decoration=false;
			User currentUser = request.getResourceResolver().adaptTo(User.class);
			
			if(currentUser.isAdmin())
				return;
			
			Iterator<Group> currentUserGroups = currentUser.memberOf();

			while (currentUserGroups.hasNext()) {
				Group grp = (Group) currentUserGroups.next();
				if(grp.getID().equals(GROUP)) {
					decoration =true;
					return;
				}
			}

			ComponentContext cc = WCMUtils.getComponentContext(request);
			cc.setDecorate(decoration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(e.getMessage());
		}
	}

}
