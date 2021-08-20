package com.allianz.onemarketing.dechap.core.censhare.services;
import java.io.IOException;
import java.util.Iterator;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.taskmanagement.Filter;
import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "= Simple Inbox Servlet", "sling.servlet.paths=/bin/user/inbox" })
public class GetInbox extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		
		try {
			
			TaskManager taskManager = request.getResourceResolver().adaptTo(TaskManager.class);
			Iterator<Task> ti = taskManager.getTasks(new Filter());
			response.getWriter().write("<h2>Task Items</h2>");
			String str = "<table border='1'><tr><td>S.No</td><td>Task Id</td><td>Created by</td><td>Assignee</td><td>Task Description</td></tr>";
			while(ti.hasNext()) {
				int c = 1;
				str += "<tr>";
				str += "<td>"+ c++ +"</td>";
				Task task = ti.next();
				
				str+="<td>"+task.getId()+"</td>";
				str+="<td>"+task.getCreatedBy()+"</td>";
				str+="<td>"+task.getCurrentAssignee()+"</td>";
				str+="<td>"+task.getDescription()+"</td>";
				
				str += "</tr>";
			}
			str += "</table>";
			
			response.getWriter().write(str);
			
			WorkflowSession graniteWorkflowSession = request.getResourceResolver().adaptTo(WorkflowSession.class);		    
			WorkItem[] workItems = graniteWorkflowSession.getActiveWorkItems();
						
			response.getWriter().write("<h2>Workflow Items("+ workItems.length+")</h2>");
		    str = "<table border='1'><tr><td>S.No</td><td>workflow Id</td><td>Initaited by</td><td>Assignee</td><td>Type</td><td>Due Date</td></tr>";
			for(int i=0;i<workItems.length;i++) {
				int c = 1;
				str += "<tr>";
				str += "<td>"+ c++ +"</td>";
				
				str+="<td>"+workItems[i].getId()+"</td>";
				str+="<td>"+workItems[i].getWorkflow().getInitiator()+"</td>";
				str+="<td>"+workItems[i].getCurrentAssignee()+"</td>";
				str+="<td>"+workItems[i].getItemType()+"</td>";
				str+="<td>"+workItems[i].getDueTime()+"</td>"; //dueTime property
				str += "</tr>";
			}
			str += "</table>";
			
			response.getWriter().write(str);
			request.getResourceResolver().adaptTo(Session.class).save();
						
		} catch (Exception e) {
		  response.getWriter().write(e.getMessage());
		}
		response.setContentType("text/html");
		response.getWriter().close();
	}

}
