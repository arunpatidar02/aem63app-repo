package com.aem.community.core.listeners;

import java.util.HashMap;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
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
public class ReplicationListener implements EventHandler{
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//dictates to the JobManager which JobConsumer will be instantiated in line 36
	//event.job.topic	= com/adobe/training/core/replicationjob
	//from http://localhost:4502/system/console/events
	private static final String TOPIC = "com/adobe/training/core/replicationjob";
	
	@Reference
	private JobManager jobManager;
	
	@Override
	public void handleEvent(final Event event) {
		ReplicationAction action = ReplicationAction.fromEvent(event);
		if(action.getType().equals(ReplicationActionType.ACTIVATE) && action.getPath()!=null) {
			// Create a properties map that contains things we want to pass through the job
			try {
				HashMap<String, Object> jobprops = new HashMap<String, Object>();
				jobprops.put("PAGE_PATH", action.getPath());
				jobManager.addJob(TOPIC, jobprops);
			//	LOGGER.info("=============Topic: '"+TOPIC+"' with payload: '"+action.getPath()+"' was added to the Job Manager");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.info("============= ERROR CREATING JOB : NO PAYLOAD WAS DEFINED");
				e.printStackTrace();
			}
		}
	}
}
