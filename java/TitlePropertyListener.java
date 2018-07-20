package com.aem.community.core.listeners;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate=true,service=TitlePropertyListener.class)
public class TitlePropertyListener implements EventListener {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Reference
	private ResourceResolverFactory resourceFactory;
	private Session session;
	private ObservationManager observationManager;
	
	@Activate
	protected void active() {
		logger.debug("*************inside active");
		ResourceResolver resourceResolver=null;	
		session=null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, "readService");
		
		
	/*	ResourceResolver resolver = resolverFactory.getAdministrativeResourceResolver(null); 
	 * //  Session sessObj = resolver.adaptTo(Session.class); 
	 * session = repository.loginAdministrative(null); 
	 * log.info("sessObj is live?"+session.isLive()); 
	 * obsMgnr = session.getWorkspace().getObservationManager();
	*/	
		try {
			resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
			session = resourceResolver.adaptTo(Session.class);
			observationManager = session.getWorkspace().getObservationManager();
			observationManager.addEventListener(this, Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED, "/content/AEM63App/fr/jcreventpage", true, null, new String[]{"cq:PageContent","nt:unstructured"} , true);
			logger.debug("*************Session is live {}",session.isLive());
			logger.debug("*************added JCR event listener");
		} catch (LoginException | RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("Catch {}",e.getMessage());
		}
		
	}
	
	@Deactivate
	protected void deactivate() {
		if(observationManager!=null) {
			try {
				observationManager.removeEventListener(this);
				logger.debug("*************removed JCR event listener");
			} catch (RepositoryException re) {
				// TODO Auto-generated catch block
				logger.debug("*************error removing the JCR event listener ", re);
				re.printStackTrace();
			}
			finally {
				if (session != null) {
					session.logout();
					session = null;
				}
			}
		}
	}
	
	public void onEvent(EventIterator it) {
		logger.debug("size of event {}",it.getSize());
		while(it.hasNext()) {
			Event event = it.nextEvent();
			logger.debug("event {}",event.getType());
			try {
				Property changedProperty = session.getProperty(event.getPath());
				if(changedProperty.getName().equalsIgnoreCase("jcr:title") && !changedProperty.getString().endsWith("!")) {
					changedProperty.setValue(changedProperty.getValue()+"!");
					logger.debug("*************Property updated: {}", event.getPath());
					session.save();
				}
			} catch (PathNotFoundException e) {
				// TODO Auto-generated catch block
				logger.debug("Catch2 {}",e.getMessage());
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug("Catch3 {}",e.getMessage());
			}
			
		}
	}
	
}
