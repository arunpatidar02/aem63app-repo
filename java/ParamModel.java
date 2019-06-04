package com.acc.aem64.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

/*
 * Sightly code
	<div data-sly-use.myObj="${'com.acc.aem64.core.models.ParamModel' @text='Some text'}">
		${myObj.reversed}
	</div>
 * 
 */

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ParamModel {

	@RequestAttribute(name = "text")
	private String text;

	private String reversed;

	@PostConstruct
	protected void init() {
		reversed = new StringBuilder(text).reverse().toString();
	}

	public String getReversed() {
		return reversed;
	}
}
