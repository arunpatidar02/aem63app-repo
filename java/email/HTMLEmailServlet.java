package com.acc.aem64.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.servlet.Servlet;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

/*
 * ------ POM Dependencies for email --------
 * 
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-email</artifactId>
	<version>1.5</version>
	<scope>provided</scope>
</dependency>
			
<dependency>
	<groupId>commons-lang</groupId>
	<artifactId>commons-lang</artifactId>
	<version>2.6</version>
	<scope>provided</scope>
</dependency>
 * 
 * -------------- Email Template and mail output ---------
 * https://github.com/arunpatidar02/aem63app-repo/tree/master/java/email
 * 
 */

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Template Email Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/email/templates/html" })
public class HTMLEmailServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final String EMAIL_TEMPLATE = "/apps/AEM64App/workflow/email/html5-template.txt";

	@Reference
	private MessageGatewayService messageGatewayService;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServerException, IOException {
		try {
			resp.setContentType("text/html");
			PrintWriter pw = resp.getWriter();
			Node templateNode = req.getResourceResolver().getResource(EMAIL_TEMPLATE).adaptTo(Node.class);
			final Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("title", "Demo Email");
			parameters.put("name", "arunpatidar02");
			parameters.put("id", "0001");
			parameters.put("host.prefix", "http://localhost");
			parameters.put("faqpath", "/content/AEM63App/faq.html");
			final MailTemplate mailTemplate = MailTemplate.create(EMAIL_TEMPLATE, templateNode.getSession());
			HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(parameters), HtmlEmail.class);
			email.setSubject("AEM - Demo Email for Templated email");
			email.addTo("noreply.aem@gmail.com");
			MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
			messageGateway.send(email);
			pw.write("email sent");
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
			resp.getWriter().close();
		}
	}
}
