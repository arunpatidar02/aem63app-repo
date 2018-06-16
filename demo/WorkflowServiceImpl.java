package com.aem.community.core.demo;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class WorkflowServiceImpl implements WorkflowService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	String PrefixMessage = "Service Invoked by Workflow -- ";
	
	@Override
	public String showMessage(String msg) {
		return PrefixMessage+msg;
	}

	
}

