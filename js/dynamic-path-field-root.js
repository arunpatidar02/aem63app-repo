var root = "&root="
var filter = '&filter='
var pickerAttr = 'pickersrc'
var $pickerEle = $('foundation-autocomplete.mypath-picker')
var picker = $pickerEle.attr(pickerAttr).split(root);
var filterPath = picker[1].split(filter);
var changedPath = filterPath[0];
changedPath = encodeURIComponent("/content/mypath"); // dynamic path

path = picker[0] + root + changedPath + filter + filterPath[1];
console.log(path);
$pickerEle.attr(pickerAttr, path)
