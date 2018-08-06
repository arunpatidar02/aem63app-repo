(function($, $document) {
    "use strict";
    $(document).on("click", ".cq-dialog-submit", function(e) {
        var $form = $(this).closest("form.foundation-form");
        var field = $form.find("input[name='./color']");
        if (field !== typeof undefined) {
            var fieldVal = field.val();
            if (/^#([A-Fa-f0-9]{3}){1,2}$/.test(fieldVal)) {
                // console.log("value = " + fieldVal);
                field.val(hexToRgbA(fieldVal));
                // console.log("after value " + field.val());
            }
        }
    });

    function hexToRgbA(hex) {
        var c;
        c = hex.substring(1).split('');
        if (c.length == 3) {
            c = [c[0], c[0], c[1], c[1], c[2], c[2]];
        }
        c = '0x' + c.join('');
        return 'rgba(' + [(c >> 16) & 255, (c >> 8) & 255, c & 255].join(',') + ',1)';
    }
})($, $(document));