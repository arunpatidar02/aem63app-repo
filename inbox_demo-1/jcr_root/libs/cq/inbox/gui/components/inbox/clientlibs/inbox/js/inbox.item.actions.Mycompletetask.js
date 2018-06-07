(function(window, $, URITemplate) {
    "use strict";

    var ns = ".cq-inbox-list";
    var ui = $(window).adaptTo("foundation-ui");
    var dialogPath = "/mnt/overlay/cq/inbox/content/inbox/dialogs/myTaskcompletedialog";
  //  alert("Load");

    $(window).adaptTo("foundation-registry").register("foundation.collection.action.action", {
        name: "cq.inbox.action.MycompleteTask",
        handler: function(name, el, config, collection, selections) {
            var submitAction = "/libs/granite/taskmanager/updatetask"; // $(selections).data("submitAction");
            var itemId = $(selections).data("foundationCollectionItemId");
            var taskActions = $(selections).data("taskActions");
             //   alert("submit");
            if (submitAction && itemId) {
                CQ.Inbox.UI.commons.loadDialog(dialogPath).done(function () {
                //    console.log("Test");
                    completeTask(submitAction, itemId, taskActions);
                }).fail(function (error) {
                    ui.alert(Granite.I18n.get("Error"), error, "error");
                });
            }
        }
    });

    function completeTask(submitAction, itemId, taskActions) {
        var form = $("#updatetaskform");
        var dialog = $("#taskCompletionDialog");

        var select = dialog.find("coral-select").get(0);
        if (select && select.items) {
            select.items.clear();
        }
        var commentField = $("[name=myComment]", dialog);
        if (commentField) {
            commentField.val("");
        }

        if (taskActions && taskActions.length > 0) {
            select.show();
            for(var actionIndex=0; actionIndex<taskActions.length; actionIndex++) {
                var taskAction = taskActions[actionIndex];

                select.items.add({
                    content: {
                        innerHTML: taskAction.actionName
                    },
                    value: taskAction.actionId
                });
            }
        } else {
            select.hide();
        }

        dialog.get(0).show();
        dialog.find(".task-complete-dialog-submit")
            .off('click' + ns)
            .on('click' + ns, function() {
                var that = this;

                if (!checkTaskAction()) {
                    // this should somehow indicate that the
                    // selected action is invalid.
                    return;
                }

                ui.wait();
                this.disabled = true;

                submitForm(form, true, submitAction, itemId)
                    .done(function() {
                        // var api = $(".foundation-collection").adaptTo("foundation-collection");
                        // api.reload();
                        // ui.clearWait();
                        window.location.reload(true);

                    })
                    .fail(function (xhr) {
                        form.trigger("foundation-form-submit-callback", [xhr]);
                        dialog.get(0).hide();
                        ui.clearWait();
                        that.disabled = false;
                    });
            });

    };

    function checkTaskAction() {
        var actionSelect = $("#taskCompletionDialog").find("coral-select");
        if ( actionSelect.length > 0 ) {
            // only check the actions if the action select is not hidden
            if (!actionSelect[0].hidden) {
                if (!actionSelect.val()) {
                    return false;
                }
            }
        }
        return true;
    };

    function submitForm($form, completeTask, postUrl, taskId) {
        var taskModel = $form.serializeAsJSON();

        // now fix the assignee -> formfield is named 'asignee' to match the persisted task,
        // however taskmanager expects ownerId
        taskModel["ownerId"] = taskModel["assignee"];
        delete taskModel["assignee"];

        taskModel["title"] = taskModel["name"];

        if (completeTask) {
            var $actionSelect = $("#taskCompletionDialog").find("coral-select");
            if ( $actionSelect.length > 0) {
                var coralActionSelect = $actionSelect[0];
                if (!coralActionSelect.hidden) {
                    taskModel["action"] = coralActionSelect.value;
                }
            }
            taskModel["comment"] = $("[name=myComment]", "#taskCompletionDialog").val();

            taskModel["status"] = "COMPLETE";
            return saveTask(postUrl, taskId, taskModel);
        } else {
            return saveTask(postUrl, taskId, taskModel);
        }
    };

    function saveTask(postUrl, taskId, data) {
        var url = Granite.HTTP.externalize(postUrl);

        url += "?taskId=" + encodeURIComponent(taskId);
        if (data["action"]) {
            url += (url.indexOf("?") > 0 ? "&" : "?") + "selectedAction=" + encodeURIComponent(data["action"]);
            delete data["action"];
        }
        // ensure sling treats this as utf-8
        url += "&_charset_=utf-8";

        var jsonTask = createJSONTask(data);

        return $.ajax({
            type: "POST",
            url: url,
            contentType: "application/json",
            processData: false,
            data: JSON.stringify(jsonTask)
        });
    };

    function createJSONTask(formDataAsJSON) {
        var result = {};

        setTaskProperty("description", result, formDataAsJSON);
        setTaskProperty("instructions", result, formDataAsJSON);
        setTaskProperty("name", result, formDataAsJSON);
        setTaskProperty("ownerId", result, formDataAsJSON);
        setTaskProperty("status", result, formDataAsJSON);
        setTaskProperty("value", result, formDataAsJSON);
        setTaskProperty("contentPath", result, formDataAsJSON);

        result.properties = {};
        for (var property in formDataAsJSON) {
            if (formDataAsJSON.hasOwnProperty(property) && !property.startsWith(":")) {
                result.properties[property] = formDataAsJSON[property];
            }
        }

        return result;
    };

    function setTaskProperty(propertyName, result, original) {
        if (original.hasOwnProperty(propertyName) && original[propertyName]) {
            result[propertyName] = original[propertyName];
            delete original[propertyName];
        }
    };

    function getUrlVar(key){
        var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);
        return result && decodeURIComponent(result[1]) || "";
    };

    /**
     * jQuery plugin that serializes form data as a JSON object.
     * @return {Object} JSON representation of the form data
     */
    $.fn.serializeAsJSON = function() {

        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };


})(window, Granite.$, Granite.URITemplate);

