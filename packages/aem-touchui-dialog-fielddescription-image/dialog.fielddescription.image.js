(function ($, $document) {
    "use strict";
    
    $(document).on("dialog-ready", function () {
        var rendition = "/jcr:content/renditions/cq5dam.thumbnail.140.100.png";
        const CUSTOM_FIELD_IMG_DESCRIPTION_CLASS='coral-field--description_img';
        const CUSTOM_TOOLTIP_CLASS='coral-tooltip--img';
        const CUSTOM_TOOLTIP_CONTENT_CLASS='coral-tooltip-content--img';
        const FIELD_SELECTOR = '.cq-dialog-content .coral-Form-fieldwrapper [data-fielddescriptionimage]:not(.'+CUSTOM_FIELD_IMG_DESCRIPTION_CLASS+')';
        const MULTIFIELD_ADD_SELECTOR = '.cq-dialog-content .coral-Form-fieldwrapper coral-multifield  [coral-multifield-add]';
        const IMAGE_DESCRIPTION_DATA_ATTR = 'fielddescriptionimage';
        const TOOLTIP_ELEMENT = 'coral-tooltip-content';
        const CORAL_TOOLTIP_SELECTOR = 'coral-tooltip';
        const RTE_SELECTOR = 'cq-RichText-editable';
        const IMG_ELEMENT = 'img';
        const IMG_SRC_ATTR = 'src';
        
        processImageDescription();
        
        $(document).on('click', MULTIFIELD_ADD_SELECTOR, function() { 
            processImageDescription();
        });

        function processImageDescription(){
            var Fields = $(FIELD_SELECTOR);
            Fields.each(function(){
                var fieldDescriptionImage = $(this).data(IMAGE_DESCRIPTION_DATA_ATTR);
                if(fieldDescriptionImage !=null && fieldDescriptionImage!=='undefined'){
                    var coralTooltip = $(this).hasClass(RTE_SELECTOR)? $(this).parent().siblings(CORAL_TOOLTIP_SELECTOR) : $(this).siblings(CORAL_TOOLTIP_SELECTOR);
                    if(coralTooltip!=null && coralTooltip!=='undefined'){
                        var coralTooltipContent = coralTooltip.find(TOOLTIP_ELEMENT);
                        var imgNode = document.createElement(IMG_ELEMENT);
                        imgNode.setAttribute(IMG_SRC_ATTR,fieldDescriptionImage+rendition);
                        coralTooltipContent.append(imgNode).addClass(CUSTOM_TOOLTIP_CONTENT_CLASS);
                        coralTooltip.addClass(CUSTOM_TOOLTIP_CLASS);
                        $(this).addClass(CUSTOM_FIELD_IMG_DESCRIPTION_CLASS);
                    }
                }
            })
        }
    });
    
})($, $(document));
