package com.acc.aem64.core.listeners;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;

@Component(service=EventHandler.class,
immediate = true,
property = {
        Constants.SERVICE_DESCRIPTION + "=Demo to listen event.job.topic on page Activation ",
        EventConstants.EVENT_TOPIC + "=com/day/cq/replication"
})
public class ReplicationListenerDemo implements EventHandler{
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handleEvent(final Event event) {
		ReplicationAction action = ReplicationAction.fromEvent(event);
		if(action.getType().equals(ReplicationActionType.ACTIVATE) && action.getPath()!=null) {
			try {
				LOGGER.info("Payload {} is Activated", action.getPath());
			} catch (Exception e) {
				LOGGER.info("============= ERROR CREATING JOB : NO PAYLOAD WAS DEFINED");
				e.printStackTrace();
			}
		}
	}
}