package com.aem.community.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

/*
 *  Use = http://localhost:4502/bin/utils/read-file.html?file=/content/AEM63App/Example_XML_fd854f8f-733e-45e3-9d95-c1198319f463.xml
 * 
 */


@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Read File Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/utils/read-file",
		"sling.servlet.extensions=" + "html" })
public class ReadFileServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		Object qs = req.getParameter("file");
		if (qs != null) {
			Node node = req.getResourceResolver().getResource(qs.toString()+"/jcr:content").adaptTo(Node.class);
			if (node != null) {
				try {
					InputStream in = node.getProperty("jcr:data").getBinary().getStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			        StringBuilder out = new StringBuilder();
			        String line;
			        while ((line = reader.readLine()) != null) {
			            out.append(line);
			        }
			        reader.close();
					resp.getWriter().write("File DATA ==> " + out.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resp.getWriter().write("ERROR : Not able to read, something is wrong");
					e.printStackTrace();
				}
			}else {
				resp.getWriter().write("File Not Found!");
			}
		}
		else {
			resp.getWriter().write("Please provide file path in page quesry string parameter e.g. ?file=/content/mysite/mypage");
		}
		resp.setContentType("text/plain");
	}
}
