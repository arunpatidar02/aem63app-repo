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
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

/**
 * Servlet that use to populate Touch UI dropdown options dynamically from json file or servlet
 * 
 * datasource node properties 
 * - options (String) - path of json file or Servlet, which returns json examples : 
 		/apps/commons/utils/json/dropdown/color.json
  		/bin/utils/json/dropdown/style 
 * - sling:resourceType (String)
  	utils/granite/components/select/datasource/json 
 * - repoSource (Boolean) - optional property to specify file in repository or servlet json source
 * true(Default) : to read JSON file stored in repository 
 * false : to get JSON from another Servlet
 * 
 * 
 * 
 * ------ POM Dependencies http and Gson -------- 
 * <dependency>
 * 	<groupId>com.google.code.gson</groupId> 
 * 	<artifactId>gson</artifactId>
 *  <version>2.8.5</version>
 * </dependency>
 * 
 * <dependency>
 *  <groupId>org.apache.httpcomponents</groupId>
 *  <artifactId>httpclient</artifactId>
 *  <version>4.5.5</version> 
 * </dependency>
 * 
 * 
 */

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Populating Coral Dropdown using JSON Data return by Servlet/Resource",
		"sling.servlet.resourceTypes=utils/granite/components/select/datasource/json",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class PopulateTouchUIDropdownFromJsonResource extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			ResourceResolver resolver = request.getResourceResolver();
			// set fallback
			request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());

			Resource datasource = request.getResource().getChild("datasource");
			ValueMap datasourceVM = ResourceUtil.getValueMap(datasource);
			String optionJosn = datasourceVM.get("options", String.class);
			Boolean repoSource = datasourceVM.get("repoSource", Boolean.class) != null
					? datasourceVM.get("repoSource", Boolean.class)
					: true;

			String jsonStr = null;
			if (optionJosn != null) {
				if (repoSource) {
					Resource jsonResource = resolver.getResource(optionJosn + "/jcr:content");
					jsonStr = getJsonFromFile(jsonResource);

				} else {
					String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
							+ optionJosn;
					jsonStr = getJsonFromServlet(url);
				}
				Gson gson = new Gson();
				TypeToken<List<Option>> token = new TypeToken<List<Option>>() {
				};

				List<Option> optionList = gson.fromJson(jsonStr, token.getType());
				List<Resource> optionResourceList = new ArrayList<Resource>();

				Iterator<Option> oi = optionList.iterator();
				while (oi.hasNext()) {
					Option opt = oi.next();
					ValueMap vm = getOptionValueMap(opt);
					optionResourceList
							.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
				}

				DataSource ds = new SimpleDataSource(optionResourceList.iterator());
				request.setAttribute(DataSource.class.getName(), ds);
			} else {
				logger.info("options property is missing in datasource node ");
			}
		} catch (IOException io) {
			logger.info("Error fetching JSON data ");
			io.printStackTrace();
		} catch (Exception e) {
			logger.info("Error in Getting Drop Down Values ");
			e.printStackTrace();
		}
	}

	private ValueMap getOptionValueMap(Option opt) {
		ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());

		vm.put("value", opt.getValue());
		vm.put("text", opt.getText());
		if (opt.isSelected()) {
			vm.put("selected", true);
		}
		if (opt.isDisabled()) {
			vm.put("disabled", true);
		}
		return vm;
	}

	private String getJsonFromServlet(String url)
			throws RepositoryException, ValueFormatException, PathNotFoundException, IOException {
		String json = null;
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials("admin", "admin"));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		try {

			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				json = EntityUtils.toString(response.getEntity());
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return json;
	}

	private String getJsonFromFile(Resource jsonResource)
			throws RepositoryException, ValueFormatException, PathNotFoundException, IOException {
		String json = null;
		if (!ResourceUtil.isNonExistingResource(jsonResource)) {
			Node cfNode = jsonResource.adaptTo(Node.class);
			InputStream in = cfNode.getProperty("jcr:data").getBinary().getStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			json = sb.toString();
			reader.close();

		}
		return json;
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