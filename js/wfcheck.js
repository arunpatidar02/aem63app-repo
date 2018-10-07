(function(document, $) {
    var eventRegFlag = true;

    $(document).on('click', ".cq-siteadmin-admin-actions-create-activator", function() {
        if (eventRegFlag) {
            $(".cq-siteadmin-admin-createworkflow").click(function(event) {

                eventRegFlag = false;
                var wfFound = false;
                var arrWF = [];
                var page = $('coral-columnview-item[selected]');

                page.each(function() {


                    var page = $(this).attr("data-foundation-picker-collection-item-text");
                    path = "/resource-status/editor" + page + ".1.json";

                    $.ajax({
                        url: path,
                        async: false,
                        success: function(data) {
                            //console.log( data );
                            var length = Object.keys(data).length;
                            //console.log(data[0]);
                            if (length > 0) {

                                for (i = 0; i < length; i++) {
                                    if (data[i].statusType === "workflow") {
                                        var item = {}
                                        item["page"] = page;
                                        item["wfName"] = data[i].shortMessage;
                                        if(typeof data[i].workItemId == "undefined"){
                                            item["workItemId"] = "/";
                                        }else{
                                            item["workItemId"] = data[i].workItemId;
                                        }
                                        arrWF.push(item);

                                    }
                                }

                            }
                        }
                    });
                })

                if (arrWF.length > 0) {
                    wfFound = true;

                    var wfDetails = "<ul>";
                    for (j = 0; j < arrWF.length; j++) {
                        wfDetails = wfDetails + "<li>";
                        wfDetails = wfDetails + arrWF[j].page + " --- " + arrWF[j].wfName + '&nbsp;<a href="' + window.location.origin + '/crx/de/index.jsp#' + arrWF[j].workItemId + '" target="_blank">( CRXDE )</a>';
                        wfDetails = wfDetails + "</li>";
                    }
                    wfDetails = wfDetails + "</ul>";
                    var dialog = new Coral.Dialog().set({
                        id: 'myDialog',
                        header: {
                            innerHTML: '<coral-alert-header>Workflow cannot be started</coral-alert-header>'
                        },
                        content: {
                            innerHTML: '<coral-alert-content>Workflow cannot be initiated as the page has an active workflow already running. <br> Please complete the below workflow before starting a new one</coral-alert-content><br>' + wfDetails
                        },
                        footer: {
                            innerHTML: '<button is="coral-button" variant="primary" coral-close id="myAlert">Ok</button>'
                        },
                        variant: "error"
                    });

                    document.body.appendChild(dialog);
                    dialog.show();
                }

                if (wfFound){
                    event.stopImmediatePropagation();
                }

            });
        }
    });
})(document, Granite.$);
