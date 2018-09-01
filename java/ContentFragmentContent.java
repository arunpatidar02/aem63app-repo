package com.aem.community.core.components;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.jcr.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

public class ContentFragmentContent extends WCMUsePojo {
	private static final Logger log = LoggerFactory.getLogger(ContentFragmentContent.class);
	private String content = "";

	@Override
	public void activate() {
		String cfInput = get("cfInput", String.class);
		String variation = "";
		try {
			variation = get("variation", String.class);
			if (variation.equals("master")) {
				variation = "original";
			}
		} catch (Exception e) {
			variation = "original";
		}
		cfInput = cfInput + "/jcr:content/renditions/" + variation + "/jcr:content";
		try {
			Node cfNode = getResource().getResourceResolver().getResource(cfInput).adaptTo(Node.class);
			if (cfNode != null) {
				if (cfNode.hasProperty("jcr:data") && cfNode.hasProperty("jcr:mimeType")) {
					if (cfNode.getProperty("jcr:mimeType").getString().equals("text/html")) {
						InputStream in = cfNode.getProperty("jcr:data").getBinary().getStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						StringBuilder out = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							out.append(line);
						}
						reader.close();
						content = out.toString();
					} else {
						log.info("Content Fragment is not selected");
					}
				} else {
					log.info("Either jcr:data or mime type property set");
				}
			} else {
				log.info("Content Fragment doesn't exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getContent() {
		return content;
	}

}