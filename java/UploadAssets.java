package com.aem.community.core.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.AssetManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Upload Asset Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/upload/assets" })
public class UploadAssets extends org.apache.sling.api.servlets.SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServerException, IOException {
		response.setHeader("Content-Type", "text/html");
		response.getWriter().print("<h1>Reading image from desktop and injecting image to JCR DAM</h1>");
		try {
			AssetManager assetMgr = request.getResourceResolver().adaptTo(AssetManager.class);
			InputStream is = null;
			String fileName ="giphy.gif";
			String mimeType="image/gif";
			File fi = new File("/Users/arunpatidar/Downloads/"+fileName);

			is = new FileInputStream(fi);
			String newFile = "/content/dam/AEM63App/"+fileName;
			Session session = request.getResourceResolver().adaptTo(Session.class);
			assetMgr.createAsset(newFile, is, mimeType, true);
			session.save();
			session.logout();
			response.getWriter().print("<p>File uploaded</p>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			response.getWriter().close();
		}
	}

}
