package com.aem.community.core.servlets.task;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;

//import com.google.gson.Gson;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "= Simple Task Servlet", "sling.servlet.paths=/bin/task/inbox/notification" })
public class SimpleCreateTaskServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// logger.info("Component Path ==> {}", request.getResource().getPath());

		TaskManager taskManager = request.getResourceResolver().adaptTo(TaskManager.class);
		try {
			Task newTask = taskManager.getTaskManagerFactory().newTask("Notification");
			newTask.setName("Demo Task");
			newTask.setContentPath("/content/AEM63App/en?showCustom=true");
			newTask.setDescription("Demo Task");
			newTask.setInstructions("Demo instruction");
			newTask.setCurrentAssignee("aem63user1");
			taskManager.createTask(newTask);

		} catch (TaskManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
