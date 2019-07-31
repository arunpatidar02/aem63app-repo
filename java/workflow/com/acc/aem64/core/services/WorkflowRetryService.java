package com.acc.aem64.core.services;

import java.util.Map;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;

public interface WorkflowRetryService {
	public void retryWorkflow(WorkflowSession wfSession, WorkItem workflowItem, Map<String, String> param);
}