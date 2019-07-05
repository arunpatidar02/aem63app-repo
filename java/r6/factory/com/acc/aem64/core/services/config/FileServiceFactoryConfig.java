package com.acc.aem64.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "File Factory Service Configuration", description = "Factory Service Configurations")
public @interface FileServiceFactoryConfig {

	@AttributeDefinition(name = "filetype", description = "File Type", type = AttributeType.STRING)
	String file_type() default "xml";

	@AttributeDefinition(name = "size", description = "Max Size of file(KB)", type = AttributeType.LONG)
	long max_size() default 10240L;

}
