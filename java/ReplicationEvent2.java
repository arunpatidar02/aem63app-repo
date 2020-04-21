package com.aem.community.core.listeners;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;

@Component(service = EventHandler.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Demo to listen event on page Activation ",
		EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
		EventConstants.EVENT_FILTER + "=(paths=/content/AEM63App/*)" })
public class ReplicationEvent2 implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ReplicationEvent2.class);

	@Override
	public void handleEvent(Event event) {
		LOG.info("Event is called with paths Replication Test 2......");
	}

}
