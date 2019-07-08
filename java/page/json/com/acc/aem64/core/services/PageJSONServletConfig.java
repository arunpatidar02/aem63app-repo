package com.acc.aem64.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
@ObjectClassDefinition(name = "Page JSON Servlet Service Configuration", description = "Service Configuration to modify/customise JSON response")
public @interface PageJSONServletConfig {
	
	
	@AttributeDefinition(
	        name = "exclude properties",
	        description = "List of properties to exclude from JSON response",
	        type = AttributeType.STRING
	    )
	String[] exclude_properties() default {"jcr:created","jcr:primaryType","jcr:createdBy","cq:lastModified","cq:lastModifiedBy","jcr:lastModifiedBy","jcr:lastModified"};
	
	@AttributeDefinition(
	        name = "include reference",
	        description = "name of properties which have other resources reference and need to include json response in page e.g. experience frangments(XF)",
	        type = AttributeType.STRING
	    )
	String[] refrence_properties() default {"fragmentPath"};
	
	@AttributeDefinition(
	        name = "rename properties",
	        description = "Rename properties in JSON response, separate orginal and new name of the property by '=', e.g. sling:resourceType=type",
	        type = AttributeType.STRING
	    )
	String[] rename_properties() default {"sling:resourceType=type"};
	
	@AttributeDefinition(
	        name = "limit",
	        description = "Node traversing limit , -1 for no limit",
	        type = AttributeType.LONG
	    )
	long limit() default 10000L;
}

