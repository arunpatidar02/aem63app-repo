package com.aem.community.core.components;

import java.util.ArrayList;
import java.util.List;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.LibraryType;

/*
 * Sightly Code
 * 
<!-- CSS -->
<sly data-sly-use.jsObj="com.aem.community.core.components.JSModel" data-sly-list="${jsObj.cssFiles}">
    <link rel="stylesheet" href="${item}" type="text/css" async>
</sly>

<!-- JS -->
<sly data-sly-use.jsObj="com.aem.community.core.components.JSModel" data-sly-list="${jsObj.jsFiles}">
 	<script async type="text/javascript" src="${item}"></script>
</sly>


 * 
 */


public class JSModel extends WCMUsePojo {
	public List<String> jsFiles = new ArrayList<String>();
	public List<String> cssFiles = new ArrayList<String>();;

	@Override
	public void activate() throws Exception {

		HtmlLibraryManager clientlibmanager = getSlingScriptHelper().getService(HtmlLibraryManager.class);
		if (clientlibmanager != null) {
			String[] categoryArray = { "AEM63Lab.page.async" };
			java.util.Collection<ClientLibrary> libs = clientlibmanager.getLibraries(categoryArray, LibraryType.JS,
					false, false);
			for (ClientLibrary lib : libs) {
				String libPath = lib.getIncludePath(LibraryType.JS);
				libPath = libPath.replaceFirst("^/apps/", "/etc.clientlibs/");
				jsFiles.add(libPath);
			}
			
			libs = clientlibmanager.getLibraries(categoryArray, LibraryType.CSS,
					false, false);
			for (ClientLibrary lib : libs) {
				String libPath = lib.getIncludePath(LibraryType.CSS);
				libPath = libPath.replaceFirst("^/apps/", "/etc.clientlibs/");
				cssFiles.add(libPath);
			}
		}
	}
	
	public List<String> getJsFiles() {
		return jsFiles;
	}
	
	public List<String> getCSSFiles() {
		return cssFiles;
	}

}

