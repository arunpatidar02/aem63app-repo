package com.aem.community.core.components;

import java.util.Dictionary;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.community.core.ReadOsgiRepoConf;

/**
 * AEM com.adobe.granite.confmgr.Conf example of getting a configuration by
 * using adaptTo and reading various settings and defining default values.
 */
@Component(service = ReadOsgiRepoConf.class, immediate = true)
public class ReadOsgiRepoConfImpl implements ReadOsgiRepoConf {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final String pid = "org.apache.sling.commons.log.LogManager.factory.config.5451fd43-b723-4265-8ea8-5856a58b32ee";

	private int propsSize;

	@Reference
	private ConfigurationAdmin configAdmin;

	@Activate
	protected void activate() throws Exception {

	}

	public String getOsgiData() {
		String msg = "Log level details not available, Please check " + pid + " configMGR";

		Configuration conf = null;
		propsSize = 0;
		try {
			conf = configAdmin.getConfiguration(pid);
			Dictionary<String, Object> props = conf.getProperties();
			propsSize = props.size();

			/*
			 * Enumeration<String> e = props.keys(); 
			 * while (e.hasMoreElements()) { 
			 * String k = e.nextElement(); log.info(k + ": " + props.get(k)); 
			 * }
			 */

			if (propsSize > 0) {
				msg = "Log Lvel is : ";
				msg += (String) props.get("org.apache.sling.commons.log.level");
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return msg;
	}
	
	public String getOsgiDataConfigAuthor() {
		String msg = "Log level details not available, Please check com.adobe.granite.csrf.impl.CSRFFilter configMGR";

		Configuration conf = null;
		propsSize = 0;
		try {
			conf = configAdmin.getConfiguration("com.adobe.granite.csrf.impl.CSRFFilter");
			Dictionary<String, Object> props = conf.getProperties();
			propsSize = props.size();

			/*
			 * Enumeration<String> e = props.keys(); 
			 * while (e.hasMoreElements()) { 
			 * String k = e.nextElement(); log.info(k + ": " + props.get(k)); 
			 * }
			 */

			if (propsSize > 0) {
				msg = "Log Lvel is : ";
				msg += (String) props.get("filter.excluded.paths");
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return msg;
	}

}
