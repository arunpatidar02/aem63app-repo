package com.aem.community.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Component Dropdown option fetching through Servlet",
		"sling.servlet.resourceTypes=/apps/aem63lab/dailog/dropdown/json",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class DatasourceJson extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	
	protected final String OPTIONS_PROPERTY = "options";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			ResourceResolver resolver = request.getResourceResolver();
			// set fallback
			request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
		
			Resource datasource = request.getResource().getChild("datasource");
			ValueMap dsProperties = ResourceUtil.getValueMap(datasource);
			String genericListPath = dsProperties.get(OPTIONS_PROPERTY, String.class);
			
			
			if(genericListPath != null) {
				Node cfNode = request.getResource().getResourceResolver().getResource(genericListPath+"/jcr:content").adaptTo(Node.class);
				InputStream in = cfNode.getProperty("jcr:data").getBinary().getStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder out = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					out.append(line);
				}
				reader.close();
				
				JSONArray jsonArray = new JSONArray(out.toString()); 
				ValueMap vm = null;
				List<Resource> optionResourceList = new ArrayList<Resource>();

				for (int i = 0; i < jsonArray.length(); i++) {
				    JSONObject json = jsonArray.getJSONObject(i);
				    String Text = "";
					String Value = "";
					vm = new ValueMapDecorator(new HashMap<String, Object>());
					Text = json.getString("text");
					Value = json.getString("value");

					vm.put("value", Value);
					vm.put("text", Text);
					optionResourceList
					.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));

				}
						DataSource ds = new SimpleDataSource(optionResourceList.iterator());
					request.setAttribute(DataSource.class.getName(), ds);

				} else {
					logger.info("JSON file is not found ");
				}
		}
			
				catch(Exception e)
	{
		logger.info("Error in Get Drop Down Values", e);
	}
}

}
