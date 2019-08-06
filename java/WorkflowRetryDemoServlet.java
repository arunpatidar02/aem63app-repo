package com.acc.aem64.core.servlets.workflow;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.acc.aem64.core.services.WorkflowRetryService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;


@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Workflow Retry demo Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/workflow/retry" })
public class WorkflowRetryDemoServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;

	@Reference
	private WorkflowRetryService wrs;
	
	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServerException, IOException {
		
			resp.setHeader("Content-Type", "text/html");
			resp.getWriter().write("Get call from servlet - testPostdemo<br>");
			
			String workflowItemPath = "/var/workflow/instances/server0/2019-07-28/request_copy_2/workItems/node1_var_workflow_instances_server0_2019-07-28_request_copy_2";
			Map<String, String> params = new HashMap<String, String>();
			params.put("comment", "comment - retrying");
			WorkflowSession wfSession = (WorkflowSession)req.getResourceResolver().adaptTo(WorkflowSession.class);
			WorkItem workflowItem;
			try {
				workflowItem = wfSession.getWorkItem(workflowItemPath);
				// method 1
				wrs.retryWorkflow(wfSession, workflowItem, params);
				
				// method 2
				// wrs.retryWorkflow(workflowItemPath, params);
			} catch (WorkflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			resp.getWriter().write("Done<br>");
			resp.getWriter().close();
			
		}
}
	  
	
