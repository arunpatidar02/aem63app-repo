(function($, $document) {
    "use strict";

    $document.on("dialog-ready", function() {
        var cfinput = $('input[name="./cfdata"]');

        if (typeof cfinput !== undefined) {

            $('foundation-autocomplete[name="./cfdata"]>div>div>input').blur(function(event) {
                updateChange();
            });

            function updateChange() {
                var cfFile = $('input[name="./cfdata"]').val();
                var url = cfFile + ".cfm.info.json?ck=" + Math.random();

                $.get(url, function(data, status) {
                    var domSelect = "";
                    for (var i in data.variations) {
                        domSelect = domSelect + '<coral-select-item value="' + data.variations[i].name + '">' + data.variations[i].title + '</coral-select-item>';
                    }

                    $('coral-select[name="./variation"]>coral-select-item').remove();
                    $('coral-select[name="./variation"]').append(domSelect);

                });
            }
        }

        // on load 

        var cfFile = $('coral-taglist[name="./cfdata"]>coral-tag').val();
        var url = cfFile + ".cfm.info.json?ck=" + Math.random();
        if (cfFile !== "" || cfFile !== null || cfFile !== "undefined") {
            $.get(url, function(data, status) {
                var domSelect = "";
                if (typeof data.variations !== undefined){
                    for (var i in data.variations) {
                        domSelect = domSelect + '<coral-select-item value="' + data.variations[i].name + '">' + data.variations[i].title + '</coral-select-item>';
                    }
                    $('coral-select[name="./variation"]').append(domSelect);
                }
            });
        }

        setTimeout(updateHiddenVal, 100);

        // on select val
        function updateHiddenVal() {
            $('coral-select[name="./variation"]>coral-select-item').each(function(index) {
                if ($(this).val() === $('input[name="./selectVariation"]').val()) {
                    $(this).attr("selected", "selected");
                    return false;
                }
            });
        }

    });

    $(document).on("click", ".cq-dialog-submit", function(e) {
        var varHiddenSel = $('input[name="./selectVariation"]');
        var varSel = $('input[name="./variation"]');

        if (typeof varHiddenSel !== undefined || typeof varSel !== undefined) {
            varHiddenSel.val(varSel.val());
            console.log(varHiddenSel.val());
        }

    });


})($, $(document));
