package com.aem.community.core.filters;
import java.io.IOException;

import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
@Component(service = TransformerFactory.class, name = "Custom Link Transformer", immediate = true,
property = {
		"pipeline.type=append-version",
		"pipeline.mode=global"
})

public class CustomLinkChecker implements Transformer, TransformerFactory{
    
      
    private ContentHandler contentHandler;
    Logger log = LoggerFactory.getLogger(CustomLinkChecker.class);
    public CustomLinkChecker()
    {
  //      log.info("customlinkchecker");
    }
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        contentHandler.characters(ch, start, length);
        
    }
    public void endDocument() throws SAXException {
        contentHandler.endDocument();
        
    }
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        contentHandler.endElement(uri, localName, qName);
        
    }
    public void endPrefixMapping(String prefix) throws SAXException {
        contentHandler.endPrefixMapping(prefix);
        
    }
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        contentHandler.ignorableWhitespace(ch, start, length);
        
    }
    public void processingInstruction(String target, String data)
            throws SAXException {
    contentHandler.processingInstruction(target, data);
        
    }
    public void setDocumentLocator(Locator locator) {
        contentHandler.setDocumentLocator(locator);
        
    }
    public void skippedEntity(String name) throws SAXException {
        contentHandler.skippedEntity(name);
        
    }
    public void startDocument() throws SAXException {
    //    log.info("starting of document");
        contentHandler.startDocument();
        
    }
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
     //   log.info("starting of element");
        final AttributesImpl attributes = new AttributesImpl(atts);
     //   log.info("Processing element: " + attributes.getValue("href"));
        final String href = attributes.getValue("href");
        if (href != null && href.startsWith("/content/AEM63App")) {
        //    log.info("href--" + href);
            for (int i = 0; i < attributes.getLength(); i++) {
          //      log.info("attribute" + attributes.getQName(i));
                if ("href".equalsIgnoreCase(attributes.getQName(i))) {
               //     log.info("hrefvalue" + attributes.getValue("href"));
                    attributes.setValue(i, replaceHref(attributes.getValue("href")));
                }
            }
            
        }
        contentHandler.startElement(uri, localName, qName, attributes);
      
        
    }
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        contentHandler.startPrefixMapping(prefix, uri);
        
    }
    public Transformer createTransformer() {
        // TODO Auto-generated method stub
        return new CustomLinkChecker();
    }
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    public void init(ProcessingContext arg0,
            ProcessingComponentConfiguration arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }
    public void setContentHandler(ContentHandler arg0) {
        this.contentHandler = arg0;
        
    }
    
   public String replaceHref(String href)
   {
       String newHref = "";
       if(href != null)
       {
      //  newHref= href.replaceAll("/content/","/");
    	   newHref= href;
       }
       return newHref;
   }
    
}
