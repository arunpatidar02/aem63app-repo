package com.myproj.core.models;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FaqModelTest {

  @Rule
  public final AemContext context = new AemContext();

  FaqModel modelObject;

  static final String COMPONENT_JSON = "/com/myproj/core/models/faq.json";
  static final String COMPONENT_PATH = "/content/myproj/testpage/"
      + "/jcr:content/root/parsys/faq";

  @Before
  public void setup() throws Exception {
    context.addModelsForClasses(FaqModel.class);
    context.load()
           .json(COMPONENT_JSON, COMPONENT_PATH);

    modelObject = context.resourceResolver()
                         .getResource(COMPONENT_PATH)
                         .adaptTo(FaqModel.class);
  }

  /**
   * Test cta label.
   */
  @Test
  public void testCtaLabel() {
    Assert.assertEquals("Faq headline is not valid", "Headline1", modelObject.getHeadline());
  }

  /**
   * Test cta target.
   */
  @Test
  public void testCtaTarget() {

    final List<Map<String, String>> accordionArray = modelObject.getFaqQuestionAnswerArray();
    if (null != accordionArray && accordionArray.size() > 0) {
      for (final Map<String, String> key : accordionArray) {
        Assert.assertEquals("Faq Question is not valid", "Question", key.get("question"));
        Assert.assertEquals("Faq Answer is not valid", "Answer", key.get("answer"));
        Assert.assertEquals("true", key.get("isCitationStyling"));
      }
    }
    else {
      Assert.assertFalse("Faq Question answer is not valid", true);
    }

  }
}
