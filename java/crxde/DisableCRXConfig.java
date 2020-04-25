package com.aem.community.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Disabled CRXDE Configuration", description = "Configuration to restrict user to access CRXDE")
public @interface DisableCRXConfig {

	@AttributeDefinition(name = "enabled", description = "If check, CRXDE restriction will be applied based on configurations", type = AttributeType.BOOLEAN)
	boolean isEnabled() default false;

	@AttributeDefinition(name = "Users", description = "List of Users to restrict CRXDE", type = AttributeType.STRING)
	String[] getUsers() default {};

	@AttributeDefinition(name = "Groups", description = "List of Groups to restrict CRXDE", type = AttributeType.STRING)
	String[] getGroups() default {};

	@AttributeDefinition(name = "Exclude Users", description = "List of Users to exclude to restrict CRXDE", type = AttributeType.STRING)
	String[] getExcludedUsers() default {};

}
