package com.acc.aem64.core.servlets.pojo;

public class CreatePageModel {

	String parentPath;
	String pageName;
	String template;
	String title;
	String pagePath;

	public CreatePageModel(String parentPath, String pageName, String template, String title) {
		this.parentPath = parentPath;
		this.pageName = pageName;
		this.template = template;
		this.title = title;
	}

	public String getParentPath() {
		return parentPath;
	}

	public String getPageName() {
		return pageName;
	}

	public String getTemplate() {
		return template;
	}

	public String getTitle() {
		return title;
	}
	
	public String getPagePath() {
		if(parentPath!=null && pageName!=null) {
			return parentPath+"/"+pageName;
		}
		return null;
	}
	

}