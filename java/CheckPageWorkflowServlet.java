package com.aem.community.core.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.workflow.exec.Workflow;
import com.day.cq.workflow.status.WorkflowStatus;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Check Workflow Status Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/checkWfStatus"
 })
public class CheckPageWorkflowServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		final String page = "/content/AEM63App/fr/testJS";
		Resource rs = req.getResourceResolver().getResource(page);
		WorkflowStatus ws = rs.adaptTo(WorkflowStatus.class);
		boolean status = ws.isInRunningWorkflow(true);
		resp.setContentType("text/html");
		resp.getWriter().write("Workflow Found : "+status);
		if(status) {
			resp.getWriter().write("<h3>Workflows are </h3>");
			List<Workflow> wfList =ws.getWorkflows(true);
			Iterator<Workflow> it = wfList.iterator();
			while(it.hasNext()) {
				resp.getWriter().write("<br>Workflow Instance Id : "+it.next().getId());
			}
		}
		resp.getWriter().close();

	}
}
