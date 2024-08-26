(function(document, $) {
  "use strict";

  const VALIDATION_PROPERTY = 'data-cmp-tabs-v1-dialog-max-items';

  $(document).on("click", ".cq-dialog-submit", function(e) {
    e.preventDefault();
    if (validateTabs()) {
      $(e.target).closest('form.foundation-form').submit();
    }
  });

  function validateTabs() {
    const $maxItemObj = document.querySelector(`input[${VALIDATION_PROPERTY}]`);
    if (!$maxItemObj) return true;

    const maxItem = parseInt($maxItemObj.getAttribute(VALIDATION_PROPERTY), 10);
    const $multifieldObj = $maxItemObj.closest('coral-panelstack').querySelector('coral-multifield');
    const multifieldItemCount = $multifieldObj.querySelectorAll('coral-multifield-item').length;

    if (multifieldItemCount > maxItem) {
      showDialog(maxItem);
      return false;
    }

    return true;
  }

  function prepareDialog(maxItem) {
    const dialog = new Coral.Dialog().set({
      id: "maxTabErrorDialog",
      variant: "error",
      header: { innerHTML: "Max Tab Exceeded" },
      content: { innerHTML: `Max Tab Exceeded, Only ${maxItem} are allowed` },
      footer: { innerHTML: "<button is=\"coral-button\" variant=\"primary\" coral-close=\"\">Ok</button>" }
    });
    document.body.appendChild(dialog);
    return dialog;
  }

  function showDialog(maxItem) {
    let dialog = document.querySelector('#maxTabErrorDialog');
    if (!dialog) {
      dialog = prepareDialog(maxItem);
    }
    dialog.show();
  }

})(document, Granite.$);
