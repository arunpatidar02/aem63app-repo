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
		cfInput = cfInput + "/jcr:content/renditions/original/jcr:content";
		try {
			Node cdNode = getResource().getResourceResolver().getResource(cfInput).adaptTo(Node.class);
			if (cdNode != null) {
				if (cdNode.hasProperty("jcr:data") && cdNode.hasProperty("jcr:mimeType")) {
					if (cdNode.getProperty("jcr:mimeType").getString().equals("text/html")) {
						InputStream in = cdNode.getProperty("jcr:data").getBinary().getStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						StringBuilder out = new StringBuilder();
						String line;
						while ((line = reader.readLine()) != null) {
							out.append(line);
						}
						reader.close();
						content = out.toString();
					} else {
						log.info("Content Fragment asset file is not selected");
					}
				} else {
					log.info("no data or mime type property set");
				}
			} else {
				log.info("Content Fragment master content doesn't exist");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getContent() {
		return content;
	}

}