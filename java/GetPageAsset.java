package com.aem.community.core.servlets;

import java.io.IOException;
import java.util.Map;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Page Get Asset Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/get-asset"})
public class GetPageAsset extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	protected final String REPLICATION_PROPERTY = "cq:lastReplicationAction";

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		Object qs = req.getParameter("page");
		if (qs != null) {
			Resource rs = req.getResourceResolver().getResource(qs.toString());
			if(rs!=null) {
			Page page = rs.adaptTo(Page.class);
			if (page != null){
				try {
					resp.getWriter().write("<p>Page has followings assets references : </p>");	
					Resource resource = req.getResourceResolver().getResource(qs.toString()+"/"+JcrConstants.JCR_CONTENT);  
	                 Node node = resource.adaptTo(Node.class);  
	                 AssetReferenceSearch assetReference = new AssetReferenceSearch(node,"/content/dam",req.getResourceResolver());  
	                for (Map.Entry<String, Asset> assetMap : assetReference.search().entrySet()) {  
	                 //    String val = assetMap.getKey();  
	                     Asset asset = assetMap.getValue();  
	                     Resource resource2 = req.getResourceResolver().getResource(asset.getPath()+"/"+JcrConstants.JCR_CONTENT);
	                     Node assetsNode = resource2.adaptTo(Node.class);
	                     String replicationStatus ="NOT REPLICATED YET";
	                     if(assetsNode.hasProperty(REPLICATION_PROPERTY)) {
	                    	 replicationStatus = assetsNode.getProperty(REPLICATION_PROPERTY).getString();
	                     }
	                     resp.getWriter().write("<br>"); 
	                     resp.getWriter().write("Assets "+asset.getPath() + " --- Replication Status "+replicationStatus);  
	                      
	                }  
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resp.getWriter().write("ERROR : Not able to get Tags, something is wrong");
					e.printStackTrace();
				}
			} else {
				resp.getWriter().write("Either Page doesn't have any Tag or Page not found!");
			}
		} 
			else {
				resp.getWriter().write("Page not found!");
			}
		}else {
			resp.getWriter().write(
					"Please provide Page path in page quesry string parameter e.g. ?page=/content/mysite/mypage");
		}
		resp.setContentType("text/html");
	}
}
