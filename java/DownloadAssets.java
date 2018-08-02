package com.aem.community.core.servlets;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;

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
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
//QueryBuilder APIs
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Get Asset Zip Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/myDownloadServlet" })
public class DownloadAssets extends org.apache.sling.api.servlets.SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Set up References
	/** Default log. */
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	private ResourceResolverFactory resourceFactory;

	@Reference
	private QueryBuilder builder;

	private Session session;
	ResourceResolver resourceResolver;
	Map<String, Object> paramMap = new HashMap<String, Object>();

	@Override
	// The GET Method uses the AEM QueryBuilder API to retrieve DAM Assets, places
	// them in a ZIP and returns it
	// in the HTTP output stream
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServerException, IOException {

		try {
			// Invoke the adaptTo method to create a Session
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			session = resourceResolver.adaptTo(Session.class);

			// create query description as hash map (simplest way, same as form post)
			Map<String, String> map = new HashMap<String, String>();

			// set QueryBuilder search criteria
			map.put("type", "dam:Asset");
			map.put("path", "/content/dam/AEM63App");
			// map.put("property.value", "image/png");

			builder = resourceResolver.adaptTo(QueryBuilder.class);

			// INvoke the Search query
			Query query = builder.createQuery(PredicateGroup.create(map), session);

			SearchResult sr = query.getResult();

			// write out to the AEM Log file
			log.info("Search Results: " + sr.getTotalMatches());

			// Create a MAP to store results
			Map<String, InputStream> dataMap = new HashMap<String, InputStream>();

			// iterating over the results
			for (Hit hit : sr.getHits()) {

				// Convert the HIT to an asset - each asset will be placed into a ZIP for
				// downloading
				String path = hit.getPath();
				Resource rs = resourceResolver.getResource(path);
				Asset asset = rs.adaptTo(Asset.class);

				// We have the File Name and the inputstream
				InputStream data = asset.getOriginal().getStream();
				String name = asset.getName();

				// Add to map
				dataMap.put(name, data); // key is fileName and value is inputStream - this will all be placed in ZIP
											// file
			}

			// ZIP up the AEM DAM Assets
			byte[] zip = zipFiles(dataMap);

			//
			// Sends the response back to the user / browser. The
			// content for zip file type is "application/zip". We
			// also set the content disposition as attachment for
			// the browser to show a dialog that will let user
			// choose what action will he do to the sent content.
			//

			ServletOutputStream sos = response.getOutputStream();

			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment;filename=dam.zip");

			// Write bytes to tmp file.
			sos.write(zip);
			sos.flush();
			log.info("The ZIP is sent");
		} catch (Exception e) {
			log.info("OH NO-- AN EXCEPTION: " + e.getMessage());
		}
	}

	/**
	 * Create the ZIP with AEM DAM Assets.
	 */
	private byte[] zipFiles(Map data) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		byte bytes[] = new byte[2048];
		Iterator<Map.Entry<String, InputStream>> entries = data.entrySet().iterator();

		while (entries.hasNext()) {
			Map.Entry<String, InputStream> entry = entries.next();

			String fileName = (String) entry.getKey();
			InputStream is1 = (InputStream) entry.getValue();

			BufferedInputStream bis = new BufferedInputStream(is1);

			// populate the next entry of the ZIP with the AEM DAM asset
			zos.putNextEntry(new ZipEntry(fileName));

			int bytesRead;
			while ((bytesRead = bis.read(bytes)) != -1) {
				zos.write(bytes, 0, bytesRead);

			}
			zos.closeEntry();
			bis.close();
			is1.close();

		}

		zos.flush();
		baos.flush();
		zos.close();
		baos.close();

		return baos.toByteArray();
	}

}
