$(window).load(function () {
    var BUTTON1 = ".cq-siteadmin-admin-actions-button1-activator",
        BUTTON2 = ".cq-siteadmin-admin-actions-button2-activator",
        ALLOWED_PATH_PREFIX = "/content/mysites",
        PATH_ATTRIBUTE = "data-granite-collection-item-id",
        doOnSelectionChange = function (whichSelector) {
            $(BUTTON1).parent().hide();
            $(BUTTON2).parent().hide();
            var selected = $(whichSelector).filter(function (index, page) {
                var path = page.getAttribute(PATH_ATTRIBUTE);
                return path.startsWith(ALLOWED_PATH_PREFIX) && page.selected;
            });
            if (selected.length === 1) {
                $(BUTTON1).parent().show();
                $(BUTTON2).parent().show();
            }
        };
    $(document).on('foundation-selections-change', function (e) {

        switch (e.target.tagName) {
            case "CORAL-MASONRY":
                doOnSelectionChange("coral-masonry-item");
                break;
            case "CORAL-COLUMNVIEW":
                doOnSelectionChange("coral-columnview-item");
                break;
            case "TABLE":
                doOnSelectionChange("tr.foundation-collection-item");
                break;
            default:
                return;
        }
    });
});
