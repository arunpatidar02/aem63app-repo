
# 'hcms' Selector to get Page JSON Response

This Selector is similar to 'model' selector but with extension
  - Allow to rename properties
  - filter results(exclude properties based on config)
  - include reference response e.g. experience fragments


# Uses
  URL - http://host:port/resourcepath.hcms.json
  Examples : 
  - [http://localhost:4504/content/we-retail/language-masters/en/men.hcms.json](http://localhost:4504/content/we-retail/language-masters/en/men.hcms.json)
  
  - [http://localhost:4504/content/experience-fragments/demoxf/demoxf.hcms.json](http://localhost:4504/content/experience-fragments/demoxf/demoxf.hcms.json)
 
 with `tidy` selector
  - [http://localhost:4504/content/experience-fragments/demoxf/demoxf.hcms.tidy.json](http://localhost:4504/content/experience-fragments/demoxf/demoxf.hcms.tidy.json)
  
## OSGI Config 
  - exclude properties : list of properties to be excluded from json response
 - include reference : list of properties which specify the reference of another resource.
-  rename properties : list of property to rename if response , e.g.
  originalname=newname
-  limit : to restrict lookup if there is infinite loop due to reference inclusion or number of childnodes more then expected. 
  
  
![ Config in OSGI Console](https://github.com/arunpatidar02/aem63app-repo/blob/master/java/page/json/osgi-json-config.PNG)
  
  
## JSON Output
JSON output contain array of node json objects. Each object have the `name`, `properties` and `childnodes` items.
> The json object would not have properties or childnodes if they are empty, Thats means if node doesn't contains any property then there will be no property variable in json representation of the object and if there is no child node of a node then childnodes will be not there in the response)

json representation of osgi configuration object
```js

{
  "name": "jcr:content",
  "properties": {
    "cq:tags": [],
    "jcr:title": "demoXF",
    "cq:xfVariantType": "web",
    "type": "weretail/components/structure/xfpage",
    "cq:template": "/conf/we-retail/settings/wcm/templates/experience-fragment-web-variation",
    "cq:xfMasterVariation": true
  },
  "childnodes": [
    {
      "name": "root",
      "properties": {
        "type": "wcm/foundation/components/responsivegrid"
      },
      "childnodes": [
        {
          "name": "product_grid",
          "properties": {
            "tagsMatch": "any",
            "pages": [
              "/content/we-retail/language-masters/en/products/men/shirts/eton-short-sleeve-shirt",
              "/content/we-retail/language-masters/en/products/men/pants/trail-model-pants",
              "/content/we-retail/language-masters/en/products/men/shorts/pipeline-board-shorts",
              "/content/we-retail/language-masters/en/products/men/shirts/amsterdam-short-sleeve-travel-shirt",
              "/content/we-retail/language-masters/en/products/men/shorts/buffalo-plaid-shorts",
              "/content/we-retail/language-masters/en/products/men/coats/portland-hooded-jacket"
            ],
            "feedEnabled": true,
            "displayAs": "products",
            "listFrom": "static",
            "limit": "6",
            "orderBy": "jcr:title",
            "type": "weretail/components/content/productgrid",
            "pageMax": "0"
          }
        },
        {
          "name": "image",
          "properties": {
            "isDecorative": "false",
            "altValueFromDAM": "true",
            "titleValueFromDAM": "true",
            "fileReference": "/content/dam/core-components-examples/library/sample-assets/mini.jpg",
            "displayPopupTitle": "true",
            "type": "weretail/components/content/image"
          }
        }
      ]
    }
  ]
}     
```


# POM Gson dependency
````xml
<dependency>
	<groupId>com.google.code.gson</groupId>
	<artifactId>gson</artifactId>
	<version>2.8.5</version>
</dependency>
````
