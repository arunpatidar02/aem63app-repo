package com.aem.community.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

//import com.google.gson.Gson;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Hello World component Dropdown option fetch Servlet",
		// "sling.servlet.paths=/apps/AEM63App/components/content/helloworld/confirmDD",
		"sling.servlet.resourceTypes=/apps/AEM63App/components/content/helloworld/confirmDD",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class HelloWorldCompDDServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			// set fallback
			request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
			ResourceResolver resolver = request.getResourceResolver();
			// Create an ArrayList to hold data
			List<Resource> fakeResourceList = new ArrayList<Resource>();
			ValueMap vm = null;
			// Add 5 values to drop down!
			for (int i = 0; i < 5; i++) {
				// allocate memory to the Map instance
				vm = new ValueMapDecorator(new HashMap<String, Object>());
				// Specify the value and text values
				String Value = "value" + i;
				String Text = "text" + i;

				// populate the map
				vm.put("value", Value);
				vm.put("text", Text);
				/*if(i==2) {
					vm.put("selected", true);
				}*/

				fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
			}

			// Create a DataSource that is used to populate the drop-down control
			DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
			request.setAttribute(DataSource.class.getName(), ds);
		} catch (Exception e) {
			logger.info("Error in Get Drop Down Values", e);
		}
	}

}
