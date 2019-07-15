
## Touch UI RTE Plugins
- HTML DOM Navigation and HTML element selector inside RTE Editor
- Custom Style(Style Picker) Plugins
- Color Picker Plugin

## Packages contain:
- Plugin code
- Sample Test Component

## Plugins
 ### HTML DOM Elements Navigator extension:
- Shows selected/current HTML element's DOM structure in RTE
- Allows selecting an exact HTML element in RTE to apply settings using RTE plugins.
- RTE Navigation will only be shown for the RTE which has **showBreadcrumb** property with **_true_**.

Although navigation can be enable for all the RTEs by changing below line in /apps/commons/rte/rte-breadcrumb/js/rte-breadcrumb.js file by replacing below code with new code:

```` js
$('.coral-RichText[data-showbreadcrumb="true"]').each(function() {
                $(this).parent().find(".rte-sourceEditor").after(breadcrumbItem);
});
````

***Code to show for All RTE***
```` js
$('.coral-RichText').each(function() {
                $(this).parent().find(".rte-sourceEditor").after(breadcrumbItem);
});
````


### Style Picker Plugins
 It apply a class attribute to selected HTML elements unlike OOTB style plugin create span tag. The exact HTML element can be selected from DOM navigator e.g. ul, ol, hr, p, div etc. These elements can be selected by DOM Element Navigator.
 
After installing the package you can find the Style Picker Plugins files at:

**Plugins JS and CSS at**
/apps/commons/rte/plugins/clientlibs/js/style-picker.js
/apps/commons/rte/plugins/clientlibs/css/style-picker.css

**Plugins popover dialogs**
/apps/commons/rte/plugins/popovers/style-picker


**Plugin Popover Dialog - dropdown option datasource and JSON at**
/apps/commons/rte/plugins/popovers/style-picker/datasource
/apps/commons/rte/plugins/popovers/style-picker/options.json

JSON file should have an array of elements in below format:

If want to show as Heading/Category
    {"text": "Background Color","value": "BTC","heading": "true"}

If Option
    {"text": "White","value": "white"}

**Example** : /apps/commons/rte/plugins/popovers/style-picker/options.json

````json
[
    {"text": "Background Color","value": "BTC","heading": "true"},
    {"text": "White","value": "white"},
    {"text": "Black","value": "black"},
    {"text": "Green","value": "green"},
    {"text": "Orange","value": "orange"},
    {"text": "Light Grey","value": "lightgrey"},
    {"text": "List Style","value": "LS","heading": "true"},
    {"text": "Check","value": "list-checked"},
    {"text": "Cross","value": "list-crossed"},
    {"text": "Link Style","value": "LS","heading": "true"},
    {"text": "Primary","value": "btn-primary"},
    {"text": "Secondary","value": "btn-secondary"}
]
````


### Color Picker Plugin
The Color Picker Plugin as same as ColorPicker Plugin from http://experience-aem.blogspot.com but it is a consolidated version to works with 6.3 to 6.5

After installing the package you can find the ColorPicker Plugins files at :

**Plugins JS and CSS at**
/apps/commons/rte/plugins/clientlibs/js/color-picker.js
/apps/commons/rte/plugins/clientlibs/css/color-picker.css


**Plugins popover dialogs**
/apps/commons/rte/plugins/popovers/color-picker


## Sample Components

Sample Test Component package contains a sample component (/apps/mfHTL63/components/content/simple-rte)

This component can be referred to as configure ColorPicker and StylePicker Plugin configurations.

## More Details
To find detail explaination please read https://aemlab.blogspot.com/2019/07/aem-rte-custom-plugins-1.html
