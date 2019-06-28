package com.acc.aem64.core.servlets.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Get Coral Dropdown options from json ",
		"sling.servlet.resourceTypes=utils/granite/components/select/datasource/json",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class PopulateTouchUIDropdownFromJSON extends SlingSafeMethodsServlet {

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
			String optionJosn = ResourceUtil.getValueMap(datasource).get(OPTIONS_PROPERTY, String.class);

			if (optionJosn != null) {
				Resource jsonResource = resolver.getResource(optionJosn + "/jcr:content");
				if (!ResourceUtil.isNonExistingResource(jsonResource)) {
					Node cfNode = jsonResource.adaptTo(Node.class);
					InputStream in = cfNode.getProperty("jcr:data").getBinary().getStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder out = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						out.append(line);
					}
					reader.close();

					Gson gson = new Gson();
					TypeToken<List<Option>> token = new TypeToken<List<Option>>() {};
					List<Option> optionList = gson.fromJson(out.toString(), token.getType());
					List<Resource> optionResourceList = new ArrayList<Resource>();

					Iterator<Option> oi = optionList.iterator();
					while (oi.hasNext()) {
						Option opt = oi.next();
						ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());

						vm.put("value", opt.getValue());
						vm.put("text", opt.getText());
						if (opt.isSelected()) {
							vm.put("selected", true);
						}
						if (opt.isDisabled()) {
							vm.put("disabled", true);
						}
						optionResourceList
								.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
					}

					DataSource ds = new SimpleDataSource(optionResourceList.iterator());
					request.setAttribute(DataSource.class.getName(), ds);
				}

				else {
					logger.info("JSON file is not found! ");
				}
			} else {
				logger.info(OPTIONS_PROPERTY + " property is missing in datasource node ");
			}
		} catch (IOException io) {
			logger.info("Error fetching JSON data ");
			io.printStackTrace();
		} catch (Exception e) {
			logger.info("Error in Getting Drop Down Values ");
			e.printStackTrace();
		}
	}

	private class Option {
		String text;
		String value;
		boolean selected;
		boolean disabled;

		public String getText() {
			return text;
		}

		public String getValue() {
			return value;
		}

		public boolean isSelected() {
			return selected;
		}

		public boolean isDisabled() {
			return disabled;
		}
	}

}