// https://levelup.gitconnected.com/aem-extend-core-component-models-using-resource-type-association-and-delegation-b8855ed281e2

package com.acc.aem64.core.models;
import com.adobe.cq.wcm.core.components.models.Image;
import lombok.experimental.Delegate;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = Image.class, // Adapts to the CC model interface
        resourceType = "demo/components/content/image", // Maps to OUR component, not the CC component
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL // No properties? No problem!
)
public class CustomImage implements Image { // We will be honoring the contract between the HTL and the model implementation

    @Self // Indicates that we are resolving the current resource
    @Via(type = ResourceSuperType.class) // Resolve not as this model, but as the model of our supertype (ie: CC Image)
    @Delegate(excludes = DelegationExclusion.class) // Delegate all our methods to the CC Image except those defined below
    private Image delegate;

    @ValueMapValue
    private boolean useOriginal; // This is a new property that we are introducing

    @ValueMapValue
    protected String fileReference; // This is the CC Image property that point to the image asset location in the DAM

    @Override
    public String getSrc() {
        // If useOriginal is checked, then serve the raw asset in full resolution, otherwise,
        // delegate this method to the CC Image
        return useOriginal ? fileReference : delegate.getSrc();
    }

    private interface DelegationExclusion { // Here we define the methods we want to override
        String getSrc(); // Override the method which determines the source of the asset
    }
}
