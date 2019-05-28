package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Iterator;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=servlet to relate assets",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/relate-assets" })
public class RelatedAssetsServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServerException, IOException {
		response.setHeader("Content-Type", "text/html");
		try {
			AssetManager assetMgr = request.getResourceResolver().adaptTo(AssetManager.class);
			String parentAsset = "/content/dam/AEM64App/asset.jpg";
			String childAsset = "/content/dam/arch17/asset.jpg";
			if (assetMgr.assetExists(parentAsset) && assetMgr.assetExists(childAsset)) {
				Asset asset = assetMgr.getAsset(parentAsset);
				asset.addRelation("derived", childAsset);
			}
			response.getWriter().print("<p>Relation added</p>");
			Iterator<? extends Asset> it = assetMgr.getAsset(parentAsset).listRelated("derived");
			response.getWriter().print("<p>Relation from derived</p>");
			while (it.hasNext()) {
				response.getWriter().print(it.next().getPath() + "<br>");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		} finally {
			response.getWriter().close();
		}

	}
}
