package com.aem.community.core.listeners;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	@Component(immediate=true)
	public class SampleJCRUGD implements EventListener{ 
		private final Logger logger = LoggerFactory.getLogger(getClass());
		
		@Reference
		private SlingRepository repository;
		
		private Session session;
		private ObservationManager observationManager;
		
		protected void activate(ComponentContext context) throws Exception {
			session = repository.loginService("readService",null);
			observationManager = session.getWorkspace().getObservationManager();			
			observationManager.addEventListener(this, Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED, "/content/usergenerated/asi/jcr", true, null, 
					new String[]{"sling:Folder","nt:unstructured"} , true);
			
			//logger.info("*************added JCR event listener");
		}
		protected void deactivate(ComponentContext componentContext) {
			try {
				if (observationManager != null) {
					observationManager.removeEventListener(this);
					//logger.info("*************removed JCR event listener");
				}
			}
			catch (RepositoryException re) {
				//logger.error("*************error removing the JCR event listener ", re);
			}
			finally {
				if (session != null) {
					session.logout();
					session = null;
				}
			}
		}
		public void onEvent(EventIterator it) {
			while (it.hasNext()) {
				Event event = it.nextEvent();
				try {
						logger.info("*************Path updated: {}", event.getPath());
						session.save();
					
				}
				catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}		
		}
	}
