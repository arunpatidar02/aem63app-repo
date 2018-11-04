(function($, $document) {
    "use strict";

    $document.on("foundation-contentloaded", function(e) {
        
        var article = document.querySelector('#articleRootId');
        var defaultSelect = document.querySelector('#defaultArticleId');
        
        if(article != null && defaultSelect != null ){
           var $article = $(article);
            var articleVal = $article.find('coral-tag').val();
            // on dialog load populate dropdown
            if(articleVal !== ""){
              updateChange(defaultSelect, articleVal);
            }
           
           
            //on path field change populate dropdown
            
            $article.off('change').on('change',function(event) {
               var articleValChange = $article.find('coral-tag').val();
               if(articleValChange !=="" || articleValChange !== articleVal){
            
              //  Remove existing items from dropdown
                defaultSelect.items.clear();
                
                // Populate new items in dropdown
                updateChange(defaultSelect, articleValChange);
               }
            }); 
            
        }
        
        function updateChange(defaultSelect, page) {
            var url = "/bin/page/get-child?page="+page+"&ck=" + Math.random();
            var varHiddenSel = $('input[name="./selectionHidden"]');
            var isSelected=false;
            $.get(url, function(data, status) {
                for (var i in data) {
                    isSelected = false;
                    if(varHiddenSel.val()===data[i].value){
                        isSelected=true;
                    }
                    defaultSelect.items.add({
                        value: data[i].value,
                        content:{ innerHTML :data[i].text},
                        selected:isSelected,
                        disabled:false
                    });
                }
            });
        }
    }); 
    
    //setting hidden variable value to select preselct dropdown option
    $(document).on("click", ".cq-dialog-submit", function(e) {
        var varHiddenSel = $('input[name="./selectionHidden"]');
        var varSel = $('input[name="./defaultArticle"]');
        if (typeof varHiddenSel !== 'undefined' && typeof varSel !== 'undefined') {
            varHiddenSel.val(varSel.val());
        }
    }); 

    
})($, $(document));
