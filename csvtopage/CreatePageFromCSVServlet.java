package com.acc.aem64.core.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acc.aem64.core.servlets.pojo.CreatePageModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.text.csv.Csv;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Creating page from local CSV file Servlet",
		"sling.servlet.paths=/bin/aem64app/create/csvtopage", "sling.servlet.methods=" + HttpConstants.METHOD_GET })

public class CreatePageFromCSVServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String FILE_PATH = "C:\\Desktop\\forum\\AEMPage2.csv";
	private static final char FIELD_SEPARATOR = ';';
	private static final boolean AUTO_SAVE = true;

	@Reference
	private ResourceResolverFactory resourceFactory;
	private Session session;

	Map<String, Object> paramMap = new HashMap<String, Object>();

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type", "text/html");
		try {

			ResourceResolver resourceResolver;
			paramMap.put(ResourceResolverFactory.SUBSERVICE, "aem64Service");
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			session = resourceResolver.adaptTo(Session.class);
			PageManager pm = req.getResourceResolver().adaptTo(PageManager.class);
			List<Page> pageList = new ArrayList<Page>();
			List<CreatePageModel> pageObjList = new ArrayList<CreatePageModel>();

			Iterator<String[]> csvData = readCSV();
			// Skipping first record
			if (csvData.hasNext())
				csvData.next();

			while (csvData.hasNext()) {
				String[] dataRow = csvData.next();
				CreatePageModel pageModel = new CreatePageModel(dataRow[0], dataRow[1], dataRow[2], dataRow[3]);
				pageObjList.add(pageModel);
			}
			Iterator<CreatePageModel> pages = pageObjList.iterator();

			while (pages.hasNext()) {
				CreatePageModel pageModel = pages.next();
				if (!session.nodeExists(pageModel.getPagePath()) && pageModel.getPagePath() != null) {
					try {
						Page page = pm.create(pageModel.getParentPath(), pageModel.getPageName(),
								pageModel.getTemplate(), pageModel.getTitle(), AUTO_SAVE);
						if (page != null) {
							pageList.add(page);
						}
					} catch (WCMException e) {
						logger.debug(e.getMessage());
					}

				}

			}

			Iterator<Page> pageListIterator = pageList.iterator();
			resp.getWriter().print("<h3>Following Pages have been created from CSV File " + FILE_PATH + "</h3>");
			while (pageListIterator.hasNext()) {
				resp.getWriter().print(pageListIterator.next().getPath());
				resp.getWriter().print("<br />");
			}

			session.save();
			session.logout();

		} catch (FileNotFoundException | RepositoryException | LoginException e) {
			logger.debug(e.getMessage());
		} finally {
			if (session != null) {
				session.logout();
				session = null;

			}
			resp.getWriter().close();

		}
	}

	private Iterator<String[]> readCSV() throws FileNotFoundException, IOException {
		InputStream fs = null;
		File fi = new File(FILE_PATH);
		fs = new FileInputStream(fi);
		Csv csv = new Csv();
		csv.setFieldSeparatorRead(FIELD_SEPARATOR);
		Iterator<String[]> csvData = csv.read(fs, null);
		return csvData;
	}

}
