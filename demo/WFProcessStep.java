package com.aem.community.core.demo;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

@Component(service = WorkflowProcess.class, property = {
		Constants.SERVICE_DESCRIPTION + "=A dummy workflow process to check service method",
		"process.label=Dummy Workflow Process to Check Service Invoked " })
public class WFProcessStep implements WorkflowProcess {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private WorkflowService ws;
	

	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		log.info(ws.showMessage("Inside Workflow"));
		
	}
}
