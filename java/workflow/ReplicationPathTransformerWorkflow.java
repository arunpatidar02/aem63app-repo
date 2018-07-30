package com.aem.community.core.workflows;

import javax.jcr.Session;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.replication.Agent;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationPathTransformer;

@Component(service = ReplicationPathTransformer.class, immediate = true)
public class ReplicationPathTransformerWorkflow implements ReplicationPathTransformer {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public String transform(Session session, String replicationPath, ReplicationAction replicationAction, Agent agent) {
		/*
		 * transform the replicationPath
		 */
		// for the sake of simplicity, and as this is an example return the
		// replicationPath as is.
		log.info("Inside transform");
		return replicationPath;
	}

	@Override
	public boolean accepts(Session session, ReplicationAction replicationAction, Agent agent) {
		/*
		 * Check if the agent is a dispatcher agent if it is the agent you are
		 * targeting, return true
		 */
		// transform all urls
		return true;
	}
}
