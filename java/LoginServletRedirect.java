package com.aem.community.core.servlets;
import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
 
 
@Component(service=Servlet.class,
property={
Constants.SERVICE_DESCRIPTION+"= Header Servlet",
"sling.servlet.methods=" + HttpConstants.METHOD_GET,
"sling.servlet.paths=/bin/LoginServlet",
//"sling.servlet.resourceTypes="+"travellingdiva/components/structure/login",
"sling.servlet.extensions="+"txt"
})
public class LoginServletRedirect extends SlingAllMethodsServlet{
 
/**
*
*/
private static final long serialVersionUID = 1L;
 
 
@Override
protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse res) throws IOException
{

res.sendRedirect("/content/AEM63App/en.html");

}
 
 
}
