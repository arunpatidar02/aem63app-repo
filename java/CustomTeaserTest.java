
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import com.adobe.cq.wcm.core.components.models.Teaser;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class CustomTeaserTest {
	
private final String TEASER1 = DemoConstants.TEST_CONTENT_ROOT+"/teaser";
private final String TEASER2 = DemoConstants.TEST_CONTENT_ROOT+"/teaser2";
private final AemContext context = new AemContext();

	@BeforeEach
	void setUp() throws Exception {
		context.addModelsForClasses(CustomTeaser.class);
		context.load().json(CustomTeaserTest.class.getResourceAsStream("CustomTeaserTest.json"),TEASER1);
		context.load().json(CustomTeaserTest.class.getResourceAsStream("CustomTeaserTest2.json"),TEASER2);
	}

	@Test
	void testCustomTeaser() {
		context.currentResource(TEASER1);
		CustomTeaser customTeaser1 = context.request().adaptTo(CustomTeaser.class);
		assertNotNull(customTeaser1);
		assertNotNull(context.currentResource(TEASER1+"/"+Teaser.NN_ACTIONS));
		assertNotNull(customTeaser1.getImgAlign());
	}
	
}
