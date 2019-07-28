(function($, $document) {
    "use strict";
    var flag = true;
    $document.on("dialog-ready", function() {

        var buttonHTML = '<button is="coral-button" icon="railLeft" variant="minimal" class="cq-dialog-header-action cq-dialog-railLeft coral-Button" type="button" title="Toggle Side Panel" size="M">';
        buttonHTML += '<coral-icon class="coral-Icon coral-Icon--sizeS coral-Icon--railLeft coral3-Icon--railLeft" icon="railLeft" size="S" role="img" aria-label="rail left"></coral-icon>';
        buttonHTML += '<coral-button-label></coral-button-label>';
        buttonHTML += '</button>';

        $('.cq-Dialog coral-dialog-header.cq-dialog-header>div.cq-dialog-actions > button:nth-child(1)').after(buttonHTML);

        if (flag)
            $(document).on('click', 'button.cq-dialog-railLeft', toggleLeftRail);

        function toggleLeftRail() {
            flag = false;
            $('.editor-GlobalBar coral-actionbar-primary coral-actionbar-item button.toggle-sidepanel').click();
        }

    });
})($, $(document));