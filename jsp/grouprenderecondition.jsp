<%--
  ==============================================================================
  has property render condition
   A condition that takes a resource path and property name and makes the
   rendering decision based on whether that resource has the property.
  /**
   * The groups to evaluate.
   */
    - groups (String[])
  ==============================================================================
--%>
<%@include file="/libs/granite/ui/global.jsp"%>
<%@page session="false"
	import="com.adobe.granite.ui.components.Config,
            com.adobe.granite.ui.components.rendercondition.RenderCondition,
            com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition,
			java.util.HashSet,
			java.util.Iterator,
			java.util.Set,
			org.apache.jackrabbit.api.security.user.Group,
			org.apache.jackrabbit.api.security.user.User,
			com.google.common.collect.Sets,
			org.slf4j.Logger,
            org.slf4j.LoggerFactory,
            org.apache.sling.api.resource.ResourceResolver,
			org.apache.sling.api.resource.Resource"%>
<%
	Logger logger = LoggerFactory.getLogger(this.getClass());
	ResourceResolver resolver = resourceResolver;
	boolean render = false;
	try {
		// if user is admin, return true to available feature for admin
		if (resourceResolver.getUserID().equalsIgnoreCase("admin")){
		    render = true;
		}
		else{
		    Config cfg = new Config(resource);
			String groups[] = cfg.get("groups", new String[] {});
			Set<String> groupList = new HashSet<String>();
			for (String x : groups)
				groupList.add(x);

			User currentUser = resourceResolver.adaptTo(User.class);

			Iterator<Group> currentUserGroups = currentUser.memberOf();
			Set<String> userGroupSet = new HashSet<String>();

			while (currentUserGroups.hasNext()) {
				Group grp = (Group) currentUserGroups.next();
				userGroupSet.add(grp.getID());
			}

			Set<String> allowedGroup = Sets.intersection(groupList, userGroupSet);

			if (!allowedGroup.isEmpty())
				render = true;
		}
		

		/* Display or hide the widget */
		request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(render));
		
	} catch (Exception e) {
		logger.error("Error in custom renderer " + e.getMessage());
	}
%>
