/**
 * Extension to the standard dropdown/select component. It enabled hidding/unhidding of other components based on the
 * selection made in the dropdown/select.
 *
 * How to use:
 *
 * - add the class cq-dialog-dropdown-showhide to the dropdown/select element
 * - add the data attribute cq-dialog-dropdown-showhide-target to the dropdown/select element, value should be the
 *   selector, usually a specific class name, to find all possible target elements that can be shown/hidden.
 * - add the target class to each target component that can be shown/hidden
 * - add the class hidden to each target component to make them initially hidden
 * - add the attribute showhidetargetvalue to each target component, the value should equal the value of the select
 *   option that will unhide this element.
 */
(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideHandler($(".cq-dialog-dropdown-showhide", e.target));
    });

    $(document).on("selected", ".cq-dialog-dropdown-showhide", function (e) {
        showHideHandler($(this));
    });

    function showHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-select")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("select");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("cqDialogDropdownShowhideTarget");
        var $target = $(target);

        if (target) {
            var value;
            if (component.value) {
                value = component.value;
            } else {
                value = component.getValue();
            }

            // make sure all unselected target elements are hidden.
            $target.not(".hide").addClass("hide");

            // unhide the target element that contains the selected value as data-showhidetargetvalue attribute
            $target.filter("[data-showhidetargetvalue='" + value + "']").removeClass("hide"); // deprecated
        }
    }

})(document, Granite.$);
