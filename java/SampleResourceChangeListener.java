import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResourceChangeListener.class, immediate = true)
@Designate(ocd = SampleResourceChangeListener.Config.class)
public class SampleResourceChangeListener implements ResourceChangeListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(SampleResourceChangeListener.class);

  @Override
  public void onChange(List<ResourceChange> list) {
    LOGGER.debug("On add/change of jcr:lastModified, listener activated with config via OCD");
  }

  @Activate
  protected void activate(SampleResourceChangeListener.Config configValues) {
    LOGGER.debug("Config values={}", ArrayUtils.toString(configValues.resource_paths()));
  }

  @ObjectClassDefinition(name = "SampleResourceChangeListener", description = "Resource change Listener Registration properties")
  public @interface Config {
    @AttributeDefinition(name = "Paths", description = "ResourceChangeListener Paths property")
    String[] resource_paths() default { "/content/aemlab/dam/import", "/content/aemlab/import"};

    @AttributeDefinition(name = "Changes", description = "ResourceChangeListener Changes property")
    String[] resource_change_types() default { "CHANGED"};

    @AttributeDefinition(name = "Properties", description = "ResourceChangeListener PropertyNamesHint property")
    String[] resource_property_names_hint() default { "jcr:lastModified"};
  }
}
