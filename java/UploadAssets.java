package com.aem.community.core.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Upload Asset Servlet",
    "sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/upload/asset"})
public class UploadAssets extends org.apache.sling.api.servlets.SlingAllMethodsServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
    response.setHeader("Content-Type", "text/html");
    PrintWriter pw = response.getWriter();
    pw.write("<h1>Reading image from desktop and injecting image to JCR DAM</h1>");
    try {
      ResourceResolver r = request.getResourceResolver();
      AssetManager assetMgr = r.adaptTo(AssetManager.class);
      InputStream is = null;
      String fileName = "assest-metadata.PNG";
      String mimeType = "image/png";
      File fi = new File("/Users/arunpatidar/Downloads/" + fileName);

      is = new FileInputStream(fi);
      String newFile = "/content/dam/we-retail/" + fileName;
      Asset asset = assetMgr.createAsset(newFile, is, mimeType, true);
      saveCustomMetadataInfo(pw, asset);
      r.commit();
      pw.write("<p>File uploaded</p>");
    }
    catch (Exception e) {
      pw.write(e.getMessage());
    }
  }

  void saveCustomMetadataInfo(PrintWriter pw, final Asset asset) {
    Resource assetResource = asset.adaptTo(Resource.class);
    String customPropName = "prop1";
    String customPropValue = "prop1Val";

    if (assetResource != null) {
      assetResource = assetResource.getChild(JcrConstants.JCR_CONTENT + "/metadata");
    }

    if (assetResource != null) {
      ValueMap assetProperties = assetResource.adaptTo(ModifiableValueMap.class);

      if (assetProperties != null) {
        assetProperties.put(customPropName, customPropValue);
        pw.write("<p>Metadta is set</p>");
      }
    }
  }

}
