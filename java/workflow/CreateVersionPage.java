package com.aem.community.core.workflows;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
@Component(service = WorkflowProcess.class, property = {
		Constants.SERVICE_DESCRIPTION + "=A workflow process to Create Page Version.",
		"process.label=Create Version Process" })
public class CreateVersionPage implements WorkflowProcess {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	ResourceResolverFactory resourceResolverFactory;
	ResourceResolver resourceResolver = null;
	SlingRepository repository;

	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		WorkflowData workflowData = item.getWorkflowData();
			String path = workflowData.getPayload().toString();
			resourceResolver = getResourceResolver(session.getSession());
			Page page = resourceResolver.getResource(path).adaptTo(Page.class);
			PageManager pm = resourceResolver.adaptTo(PageManager.class);
			try {
				if (page != null) {
					pm.createRevision(page, "Test", "Test-comment");
				}
			} catch (WCMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	

	private ResourceResolver getResourceResolver(Session session) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
		try {
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceResolver;
	}

}
