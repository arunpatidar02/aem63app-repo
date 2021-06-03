
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL) 
public class FaqModel {

/** The Constant LOGGER. */
private static final Logger LOGGER = LoggerFactory.getLogger(FaqModel.class);

/** The headline. */
@Inject
String headline;

/** The accoridonArray node */
@Inject
@Self
Resource componentResource;


/**
 * Gets the headline.
 * @return the headline
 */
public String getHeadline() {
    return headline;
}

/**
 * Gets the accoridon array.
 * @return the accoridon array
 */
public List<Map<String, String>> getFaqQuestionAnswerArray() {
    final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    try {
        Resource accoridon = componentResource.getChild("accoridonArray");
        if(accoridon!=null)
        {
            for (Resource faq : accoridon.getChildren()) {
                final Map<String, String> map = new HashMap<String, String>();
                map.put("question", faq.getValueMap().get("question",String.class));
                map.put("answer", faq.getValueMap().get("answer",String.class));
                map.put("isCitationStyling", faq.getValueMap().get("isCitationStyling",String.class));
                 
                data.add(map);
            }
        }
    } catch (final Exception e) {
        LOGGER.error("Exception occurred : ", e);
    }
    return data;
}
}
