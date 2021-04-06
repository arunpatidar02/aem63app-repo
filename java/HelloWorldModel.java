package com.aemlab.aem65.core.models;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.IncludeOptions;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class HelloWorldModel {

	@ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = "No resourceType")
	protected String resourceType;

	@ValueMapValue(name = "cq:styleIds", injectionStrategy = InjectionStrategy.OPTIONAL)
	protected String[] styleIds;

	@OSGiService
	private SlingSettingsService settings;
	@OSGiService
	private SlingRepository repository;
	@SlingObject
	private Resource currentResource;
	@SlingObject
	private ResourceResolver resourceResolver;
	@SlingObject
	private SlingHttpServletRequest request;

	private String message;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	protected void init() {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		String currentPagePath = Optional.ofNullable(pageManager).map(pm -> pm.getContainingPage(currentResource))
				.map(Page::getPath).orElse("");

		IncludeOptions options = IncludeOptions.getOptions(request, true);
		logger.error("classes applied - {}", String.join(",", options.getCssClassNames()));
		options.getCssClassNames().add("new-class");

		message = "Hello World!\n" + "Resource type is: " + resourceType + "\n" + "Current page is:  " + currentPagePath
				+ "\n" + "This is instance: " + settings.getSlingId() + "\n" + "This is defualt style: "
				+ getDesignPropertyValue(currentResource, "cq:styleDefaultClasses", "") + "\n"
				+ "This is applied style: " + getStyleNode(currentResource, styleIds) + "\n";
	}

	public String getMessage() {
		return message;
	}

	/**
	 * Method reads design dialog property and sets it.
	 *
	 * @param resource     - resource
	 * @param propertyName - propertyName
	 * @return <b>String</b> - property value or <b>null</b>
	 */
	public String getDesignPropertyValue(Resource resource, String propertyName, String defaultValue) {
		ResourceResolver resourceResolver = resource.getResourceResolver();
		ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
		if (policyManager != null) {
			ContentPolicy contentPolicy = policyManager.getPolicy(resource);
			if (contentPolicy != null) {
				return contentPolicy.getProperties().get(propertyName, defaultValue);
			}
		}
		return defaultValue;
	}

	
	public String getStyleNode(Resource resource, String[] styleIds) {
		String styleValue = "";
		ResourceResolver resourceResolver = resource.getResourceResolver();
		ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
		if (policyManager != null) {
			ContentPolicy contentPolicy = policyManager.getPolicy(resource);
			if (contentPolicy != null) {

				Resource policyResource = resource.getResourceResolver().getResource(contentPolicy.getPath());
				Resource styleResource = policyResource.getChild("cq:styleGroups");
				if (styleResource != null) {
					for (String s : styleIds) {
						styleValue = styleValue + getStyleValue(resourceResolver, s, styleResource.getPath()) + " ";
					}
				}
			}
		}
		return styleValue;
	}

	public String getStyleValue(ResourceResolver resourceResolver, String styleId, String path) {
		String sClass = null;
		String q = "SELECT * FROM [nt:unstructured] AS n WHERE n.[cq:styleId] = '" + styleId
				+ "' AND ISDESCENDANTNODE('" + path + "')";
		try {

			final Query query = resourceResolver.adaptTo(Session.class).getWorkspace().getQueryManager().createQuery(q,
					Query.JCR_SQL2);
			final NodeIterator result = query.execute().getNodes();
			logger.error("size : {}", result.getSize());
			while (result.hasNext()) {
				sClass = result.nextNode().getProperty("cq:styleClasses").getString();
			}

		} catch (RepositoryException e) {
			logger.error(e.getMessage());
		}

		return sClass;
	}

}
