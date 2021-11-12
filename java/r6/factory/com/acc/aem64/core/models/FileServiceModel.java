package com.acc.aem64.core.models;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.community.aemlab.core.services.FileService;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FileServiceModel {

	@OSGiService(filter = "(file.type=xml)")
	private FileService fs1;

	private String fileData1 = "";
	private String fileData2 = "";

	@PostConstruct
	protected void init() {
		BundleContext bundleContext = FrameworkUtil.getBundle(FileServiceModel.class).getBundleContext();
		Collection<ServiceReference<FileService>> serviceList = null;
		try {
			serviceList = bundleContext.getServiceReferences(FileService.class, getFilter("pdf", "10"));
			Iterator<ServiceReference<FileService>> it = serviceList.iterator();
			if (it.hasNext()) {
				FileService fs2 = bundleContext.getService(it.next());
				if (fs2 != null) {
					fileData2 = fs2.getFileData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getMessage() {
		return fileData1 + fileData2;
	}

	private String getFilter(String type, String maxSize) {
		return "(&(max.size=" + maxSize + ")(file.type=" + type + "))";
	}

}
