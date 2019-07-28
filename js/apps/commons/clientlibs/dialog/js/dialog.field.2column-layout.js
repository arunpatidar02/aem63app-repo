(function($, $document) {
    "use strict";
    $document.on("dialog-ready", function() {
        var items = $('[data-rowresume="true"]');
        $(items).each(function(i) {
            $(this).closest('.coral-Form-fieldwrapper').addClass("coral-Form-fieldwrapper--rowresume");
        });
    });
})($, $(document));