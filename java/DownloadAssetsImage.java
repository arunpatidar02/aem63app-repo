package com.aem.community.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//DAM API
import com.day.cq.dam.api.Asset;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Get Asset file Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/download/asset" })
public class DownloadAssetsImage extends org.apache.sling.api.servlets.SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Set up References
	/** Default log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	private ResourceResolverFactory resourceFactory;

	
	private Session session;
	ResourceResolver resourceResolver;
	Map<String, Object> paramMap = new HashMap<String, Object>();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServerException, IOException {

		try {
			// Invoke the adaptTo method to create a Session
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			session = resourceResolver.adaptTo(Session.class);

			String path = "/content/dam/AEM63App/cover.jpg";
			Resource rs = resourceResolver.getResource(path);
			Asset asset = rs.adaptTo(Asset.class);
			String fileName = asset.getName();
			Resource original = asset.getOriginal();
			InputStream stream = original.adaptTo(InputStream.class);
			String contentType = asset.getMetadataValue("dc:format");
			if (contentType != null) {
				response.setContentType(contentType);
			} else {
				response.setContentType("application/pdf");
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			int maxByteBufferSize = 208896;
			int length = -1;
			OutputStream outStream = response.getOutputStream();
			byte[] byteBuffer = new byte[maxByteBufferSize];
			while ((stream != null) && ((length = stream.read(byteBuffer)) != -1)) {
				outStream.write(byteBuffer, 0, length);
			}
			stream.close();
			outStream.close();

		} catch (Exception e) {
			log.info("OH NO-- AN EXCEPTION: " + e.getMessage());
		}
	}

}
