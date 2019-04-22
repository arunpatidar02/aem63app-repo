(function ($, $document) {
    "use strict";


    $(document).on("click", ".cq-dialog-submit", function (e) {
        //$(window).adaptTo("foundation-ui").alert("Close", "Dialog closed, selector [.cq-dialog-submit]");
        var regexSelector = $('[data-selector="coral3-regex-dailog"]');
        var error=false;
      //  var $regexItems=$(regexSelector);
        
        $(".regex-error").remove();
        
        $(regexSelector).each(function( i ) {
          var $this = $(this);
          var regex = $this.data("regex");
          var regexText = $this.data("regextext");
          var value = $this.val();
       //   console.log($this.prop('checked') +" - "+ $this.prop("tagName"));
          
          if($this.hasClass('coral3-NumberInput')){
             value = $this.children( "input" ).attr("aria-valuenow");
            // console.log(value);
          }
          else if($this.hasClass('coral-PathBrowser')){
              value = $this.children( "span" ).children( "input" ).val();
          }
          else if($this.hasClass('coral3-Select')){
            //  console.log($this.children( "select" ).children("option:selected").val());
              value = $this.children( 'input[handle="input"]' ).val();
          }
          else if($this.hasClass("coral-Checkbox")){ 
              if($this.prop('checked'))
                value = "true";
              else
                value = "";
          }
          else if($this.hasClass('coral-RadioGroup')){
            //  console.log($this.children( "coral-radio" ).prop("checked"));
              if($this.children( "coral-radio" ).prop('checked'))
                value = "true";
              else
                value = "";
          }
          else if($this.hasClass('coral-Form-fieldset')){
            //  console.log($this.children( "coral-radio" ).prop("checked"));
              if($this.children( "coral-checkbox" ).prop('checked'))
                value = "true";
              else
                value = "";
          }
          
         
         
          if(regexText == "" || regexText == null){
              regexText = Granite.I18n.get("Invalid input");
          }else{
              regexText = Granite.I18n.get(regexText);
          }
        
          if((regex !== "" || regex !== null)){
            //  console.log($this);
            //  console.log(value);
              if ((!(new RegExp(regex).test(value)) && regex != "required") || (regex === "required" && value.length==0)) {
              //    console.log(value);
                  error=true;
                 var fieldErrorEl = $("<span class='coral-Form-fielderror coral-Icon coral-Icon--alert coral-Icon--sizeS regex-error' data-init='quicktip' data-quicktip-type='error' />");
                 $this.attr("aria-invalid", "true").toggleClass("is-invalid", true);
                 if($this.hasClass('coral3-NumberInput')){
                     $this.children( "input" ).addClass("is-invalid");
                 }
                  
                 //RTE
                 if($this.parent().hasClass("richtext-container")){
                  fieldErrorEl.attr("data-quicktip-arrow", "right").attr("data-quicktip-content", regexText).insertAfter($this.parent());
                   $this.parent().siblings(".coral-Form-fieldinfo").addClass("hide");
                 }
                 else{
                     fieldErrorEl.attr("data-quicktip-arrow", "right").attr("data-quicktip-content", regexText).insertAfter($this);
                     $this.siblings(".coral-Form-fieldinfo").addClass("hide");
                 }
                  
                 
                }
              /*  else if(regex === "required" && value.length==0){
                error=true;
                 var fieldErrorEl = $("<span class='coral-Form-fielderror coral-Icon coral-Icon--alert coral-Icon--sizeS regex-error' data-init='quicktip' data-quicktip-type='error' />");
                 fieldErrorEl.attr("data-quicktip-arrow", "right").attr("data-quicktip-content", regexText).insertAfter($this);
                 $this.attr("aria-invalid", "true").toggleClass("is-invalid", true);
                 $this.siblings(".coral-Form-fieldinfo").addClass("hide");
               //  console.log(value);
                } */
                else{
                    $this.attr("aria-invalid", "false")
                    $this.removeAttr("aria-invalid").removeClass("is-invalid");
                    if($this.hasClass('coral3-NumberInput')){$this.children( "input" ).removeClass("is-invalid");}
                    if($this.parent().hasClass("richtext-container")){
                       $this.parent().siblings(".coral-Form-fieldinfo").removeClass("hide");
                    }else{
                       $this.siblings(".coral-Form-fieldinfo").removeClass("hide");
                    }
                }
          }
          
        });
        
            if(error)
            return false;
            
        
    });

})($, $(document));


(function ($, $document) {
    "use strict";


    $(document).on("click", ".cq-dialog-submit", function (e) {
        //$(window).adaptTo("foundation-ui").alert("Close", "Dialog closed, selector [.cq-dialog-submit]");
        var regexSelector = ".coral3-regex-dailog";
        var errorMsg="";
      //  var $regexItems=$(regexSelector);
        
        $(regexSelector).each(function( i ) {
          var $this = $(this);
          var regex = $this.data("regex");
          var regexText = $this.data("regextext");
          var value = $this.val();
          var pos="";
          
          if($this.hasClass('coral-NumberInput')){
             value = $this.children( "input" ).attr("aria-valuetext");
            // console.log(value);
          }
          else if($this.hasClass('coral-PathBrowser')){
              value = $this.children( "span" ).children( "input" ).val();
          }
          else if($this.hasClass('coral-Select')){
              console.log($this.children( "select" ).children("option:selected").val());
              value = $this.children( "select" ).children("option:selected").val();
          }
          else if($this.hasClass('coral-RadioGroup') && $this.attr('aria-invalid')=="false"){
              value = "true";
          }
        
          if(regex !== "" || regex !== null){
              if (!(new RegExp(regex).test(value)) && regex != "required") {
                 //TextField
                 var $eleLabel=$this.siblings('label.coral-Form-fieldlabel');
                 var eleLabel=null;
                 if(typeof $eleLabel !== undefined){
                    if ($eleLabel.parents('coral-multifield-item').length) {
                            //console.log('Your clicked element is having div#hello as parent');
                            pos = "["+parseInt($eleLabel.parents('coral-multifield-item').index()+1)+"]";
                    }
                    
                     
                    if($eleLabel.length == 1){
                         eleLabel=$eleLabel.text();
                     }
                      //RTE Textarea
                     else if($eleLabel.length == 0 && $this.siblings("input.coral-RichText-isRichTextFlag").length==1 && $this.siblings("input.coral-RichText-isRichTextFlag").val()=="true"){
                         eleLabel=$this.closest(".richtext-container").siblings('label.coral-Form-fieldlabel').text();
                     }
                }
                
                 if(eleLabel!==null ){
                     errorMsg =errorMsg+ eleLabel+pos+" : "+regexText+"<br>";
                 }else{
                     errorMsg =errorMsg+regexText+"<br>";
                 }
                 
              }
              
              else if(regex === "required" && value.length==0){
                  var $eleLabel=$this.siblings('label.coral-Form-fieldlabel');
                 var eleLabel=null;
                 if(typeof $eleLabel !== undefined){
                     if ($eleLabel.parents('coral-multifield-item').length) {
                            //console.log('Your clicked element is having div#hello as parent');
                            pos = "["+parseInt($eleLabel.parents('coral-multifield-item').index()+1)+"]";
                    }
                     if($eleLabel.length == 1){
                         eleLabel=$eleLabel.text();
                     }
                      //RTE Textarea
                     else if($eleLabel.length == 0 && $this.siblings("input.coral-RichText-isRichTextFlag").length==1 && $this.siblings("input.coral-RichText-isRichTextFlag").val()=="true"){
                         eleLabel=$this.closest(".richtext-container").siblings('label.coral-Form-fieldlabel').text();
                     }
                     
                }
                
                 if(eleLabel!==null ){
                     errorMsg =errorMsg+ eleLabel+pos+" : "+regexText+"<br>";
                 }else{
                     errorMsg =errorMsg+regexText+"<br>";
                 }
              }
          }
          
        });
        
        if(errorMsg!==""){
            console.log(errorMsg);
            
            var dialog = new Coral.Dialog().set({
                id: 'regexDialog',
                header: {
                            innerHTML: '<coral-alert-header>Dialog cannot be Submitted</coral-alert-header>'
                        },
                content: {
                            innerHTML: '<coral-alert-content><b>Following Fields are invalid :</b><br></coral-alert-content><br>' + errorMsg
                        },
                footer: {
                            innerHTML: '<button is="coral-button" variant="primary" coral-close id="regexDialogOk">Ok</button>'
                        },
                variant: "error"
               
                });

            document.body.appendChild(dialog);
            dialog.show();
            return false;
        }
        
    });

})($, $(document));


