package org.anvard.karaf.greeter;

import java.util.ArrayList;
import java.util.List;

import org.anvard.karaf.greeter.api.Greeter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GreeterManagerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreeterManager.class);

    /**
     * Figure out what plugins are available
     */
    public List<String> getLanguages() {
        BundleContext context = 
                FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        
        // Passing null as the filter argument returns all services
        // implementing the interface
        ServiceReference[] refs = null;
        try {
            refs = context.getServiceReferences(Greeter.class.getName(), null);
            if (null != refs) {
            	List<String> langs = new ArrayList<>();
            	for (ServiceReference ref: refs) {
            		langs.add(ref.getProperty("language").toString());
            	}
            	return langs;
            }
        } catch (InvalidSyntaxException e) {
            LOGGER.error("Invalid query syntax", e);
        }
    	return new ArrayList<>();
    }
    
    /**
     * Select a specific plugin and use it
     */
    public String greet(String language) {
        BundleContext context = 
                FrameworkUtil.getBundle(this.getClass()).getBundleContext();
         
        String filter = "(language=" + language + ")";
        
        // Get ServiceReference(s) from OSGi framework, filtered for the specified value      
        ServiceReference[] refs = null;
        try {
            refs = context.getServiceReferences(Greeter.class.getName(), filter);
            if (null != refs) {
            	Greeter greeter = (Greeter)context.getService(refs[0]);
            	return greeter.greet();
            }
        } catch (InvalidSyntaxException e) {
            LOGGER.error("Invalid query syntax", e);
        }
        LOGGER.warn("No plugins found, making the default greeting");
        return "Hello from the greeter manager!";
	}

}
