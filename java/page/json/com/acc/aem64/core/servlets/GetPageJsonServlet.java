package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acc.aem64.core.services.PageJSONServletService;
import com.acc.aem64.core.servlets.pojo.JSONNode;
import com.day.cq.wcm.api.NameConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=JSON renderer Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.resourceTypes=" + "sling/servlet/default",
		"sling.servlet.selectors=" + "hcms", "sling.servlet.extensions=" + "json" })
public class GetPageJsonServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String PN_REDIRECT_TARGET = "cq:redirectTarget";
	private long limit = -1L;
	JSONNode pageTree = null;
	private long counter = 0L;
	List<String> excludePropertiesList = null;
	List<String> referecePropertiesList = null;
	List<String> renamePropertiesList = null;
	boolean tidy = false;

	@Reference
	private PageJSONServletService pageJsonServletService;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServerException, IOException {
		try {
			resp.setCharacterEncoding("UTF-8");
			resp.setHeader("Content-Type", "application/json");
			excludePropertiesList = covertArrayToList(pageJsonServletService.getExcludedProperties());
			referecePropertiesList = covertArrayToList(pageJsonServletService.getReferenceProperties());
			renamePropertiesList = covertArrayToList(pageJsonServletService.getRenameProperties());
			counter = 0L;
			limit = pageJsonServletService.getLimit();

			Resource currentResource = req.getResource();
			tidy = hasSelector(req, "tidy");

			if (resourceNotExists(currentResource)) {
				throw new ResourceNotFoundException("No data to render.");
			}

			currentResource = getCurrentResource(currentResource);
			pageTree = new JSONNode(currentResource.getName(), getFilteredValueMap(currentResource.getValueMap()));
			collectChild(pageTree, currentResource, req, null);
			resp.setContentType("application/json");
			resp.getWriter().write(getJsonString(pageTree));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resp.getWriter().close();
		}
	}

	private Resource getCurrentResource(Resource currentResource) {
		Resource jcrResource = currentResource.getChild(NameConstants.NN_CONTENT);
		if (jcrResource.getValueMap().containsKey(PN_REDIRECT_TARGET)) {
			String newResource = jcrResource.getValueMap().get(PN_REDIRECT_TARGET, String.class);
			if (!newResource.equals(currentResource.getPath())
					&& !resourceNotExists(currentResource.getResourceResolver().getResource(newResource))) {
				jcrResource = currentResource.getResourceResolver().getResource(newResource);
			}
		}
		return jcrResource;
	}

	private boolean resourceNotExists(Resource resource) {
		return ResourceUtil.isNonExistingResource(resource)
				|| ResourceUtil.isNonExistingResource(resource.getChild(NameConstants.NN_CONTENT));
	}

	private void collectChild(JSONNode pageTree, Resource resource, SlingHttpServletRequest req, String prefix) {
		try {
			if (counter <= limit || limit < 0) {
				Iterator<Resource> children = resource.listChildren();
				while (children.hasNext()) {
					Resource res = (Resource) children.next();
					String nodeName = res.getName();
					if (prefix != null) {
						nodeName = prefix + nodeName;
					}
					JSONNode item = pageTree.addChild(nodeName, getFilteredValueMap(res.getValueMap()));

					Iterator<Resource> it = resource.listChildren();
					if (it.hasNext()) {
						collectChild(item, res, req, null);
					}
					if (!referecePropertiesList.isEmpty()) {
						Iterator<String> ri = referecePropertiesList.iterator();
						while (ri.hasNext()) {
							String refProp = ri.next();
							Object refrenece = res.getValueMap().get(refProp);
							if (refrenece != null) {
								Resource ref = req.getResourceResolver().getResource(refrenece.toString());
								if (!ResourceUtil.isNonExistingResource(ref)) {
									collectChild(item, ref, req, "ref_" + refProp + "_");
								}
							}
						}
					}
				}
				counter = counter + 1L;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, Object> getFilteredValueMap(ValueMap vm) {
		HashMap<String, Object> propMap = new HashMap<String, Object>();
		try {
			for (Entry<String, Object> e : vm.entrySet()) {
				String key = e.getKey();
				if (!excludePropertiesList.contains(key)) {
					Object value = e.getValue();
					key = getKeyNewName(renamePropertiesList, key);

					if ((value instanceof InputStream)) {
						propMap.put(key, 0);
					} else if ((value instanceof Calendar)) {
						propMap.put(key, format((Calendar) value));
					} else if ((value instanceof Boolean)) {
						propMap.put(key, ((Boolean) value).booleanValue());
					} else if ((value instanceof Long)) {
						propMap.put(key, ((Long) value).longValue());
					} else if ((value instanceof Integer)) {
						propMap.put(key, ((Integer) value).intValue());
					} else if ((value instanceof Double)) {
						propMap.put(key, ((Double) value).doubleValue());
					} else if (value.getClass().isArray()) {
						propMap.put(key, value);
					} else {
						propMap.put(key, value.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propMap;
	}

	private String getKeyNewName(List<String> renamePropertiesList, String key) {
		if (!renamePropertiesList.isEmpty()) {
			Iterator<String> ri = renamePropertiesList.iterator();
			while (ri.hasNext()) {
				String[] renameProp = ri.next().split("=");
				if (renameProp.length >= 2 && renameProp[0].equals(key)) {
					key = renameProp[1];
					break;
				}
			}

		}
		return key;
	}

	private String getJsonString(JSONNode pageTree) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String json = gson.toJson(pageTree).replaceAll(",\"childnodes\":\\[\\]", "");
		if (tidy) {
			gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			JsonElement jsonElement = new JsonParser().parse(json);
			json = gson.toJson(jsonElement);
		}

		return json;
	}

	private List<String> removeDuplicates(List<String> props) {
		List<String> newList = new ArrayList<String>();
		for (String ele : props) {
			if (!newList.contains(ele)) {
				newList.add(ele.trim());
			}
		}
		return newList;
	}

	private List<String> covertArrayToList(String[] props) {
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(props);
		list = removeDuplicates(list);
		return list;
	}

	public static String format(Calendar date) {
		Locale DATE_FORMAT_LOCALE = Locale.US;
		DateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", DATE_FORMAT_LOCALE);
		formatter.setTimeZone(date.getTimeZone());
		return formatter.format(date.getTime());
	}

	protected boolean hasSelector(SlingHttpServletRequest req, String selectorToCheck) {
		for (String selector : req.getRequestPathInfo().getSelectors()) {
			if (selectorToCheck.equals(selector)) {
				return true;
			}
		}
		return false;
	}

}
