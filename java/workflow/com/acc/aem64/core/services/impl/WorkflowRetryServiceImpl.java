package com.acc.aem64.core.services.impl;

import java.io.IOException;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acc.aem64.core.services.WorkflowRetryService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;

@Component
public class WorkflowRetryServiceImpl implements WorkflowRetryService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void retryWorkflow(WorkflowSession wfSession, WorkItem workflowItem,
			Map<String, String> param) {
		try {
			if (wfSession != null) {
				advance(wfSession, workflowItem,  param);
			} else {
				logger.warn("[retryWorkflow] : Workflow session is null");
			}
		} catch (WorkflowException | IOException e) {
			e.printStackTrace();
			logger.info("Exception " + e.getMessage());
		}
	}

	private void advance(WorkflowSession wfSession, WorkItem workflowItem, Map<String, String> param)
			throws WorkflowException, IOException {
		if ("FailureItem".equals(workflowItem.getItemSubType())) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				workflowItem.getMetaDataMap().put(entry.getKey(), entry.getValue());
			}
			retryStep(wfSession, workflowItem);
		}
	}

	public static void retryStep(WorkflowSession wfSession, WorkItem item) throws WorkflowException, IOException {
		if (item != null) {
			Route loopRoute = new LoopbackRoute(item);
			wfSession.complete(item, loopRoute);
		}
	}

}