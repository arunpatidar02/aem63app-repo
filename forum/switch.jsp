<%--
  ADOBE CONFIDENTIAL

  Copyright 2015 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="org.apache.commons.lang.StringUtils,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Tag" %><%--###
Switch
======

.. granite:servercomponent:: /libs/granite/ui/components/coral/foundation/form/switch
   :supertype: /libs/granite/ui/components/coral/foundation/form/field

   A switch component to represent boolean concept (the "On"/"Off" toggle switch).
   
   It extends :granite:servercomponent:`Field </libs/granite/ui/components/coral/foundation/form/field>` component.

   It has the following content structure:

   .. gnd:gnd::

      [granite:FormSwitch] > granite:FormField
      
      /**
       * The name that identifies the field when submitting the form.
       */
      - name (String)
      
      /**
       * ``true`` to generate the `SlingPostServlet @Delete <http://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html#delete>`_ hidden input based on the name.
       */
      - deleteHint (Boolean) = true
      
      /**
       * The value of the field.
       */
      - value (String) = 'true'
      
      /**
       * The submit value of the field when it is unchecked.
       */
      - uncheckedValue (String) = 'false'

      /**
       * Indicates if the field is in disabled state.
       */
      - disabled (Boolean)
      
      /**
       * Indicates if the field is mandatory to be filled.
       */
      - required (Boolean)
      
      /**
       * The name of the validator to be applied. E.g. ``foundation.jcr.name``.
       * See :doc:`validation </jcr_root/libs/granite/ui/components/coral/foundation/clientlibs/foundation/js/validation/index>` in Granite UI.
       */
      - validation (String) multiple
      
      /**
       * Indicates if the switch is checked when the form values don't have a match by ``name`` property.
       *
       * .. warning:: When setting ``defaultChecked`` = ``true``, you have to set the value of ``uncheckedValue`` so that the form values will be always populated.
       *    Otherwise Sling Post Servlet will remove the property from the form values, which makes the switch to be always checked.
       *
       * e.g. Given a switch with ``name`` = ``name1`` 
       *
       * ===========================  ================  ===========  =========
       * Form Values has ``named1``?  Its value match?  ``checked``  Checked?
       * ===========================  ================  ===========  =========
       * ``true``                     ``true``          n/a          ``true``
       * ``true``                     ``false``         n/a          ``false``
       * ``false``                    n/a               ``true``     ``true``
       * ``false``                    n/a               ``false``    ``false``
       * ===========================  ================  ===========  =========
       */
      - checked (Boolean)

      /**
       * If ``false``, the checked status is based on matching the form values by ``name`` and ``value`` properties.
       * Otherwise, the form values are not matched, and the checked status is based on ``checked`` property specified.
       */
      - ignoreData (Boolean)
      
      /**
       * The text for "On".
       */
      - onText (String) = 'On' i18n
      
      /**
       * The text for "Off".
       */
      - offText (String) = 'Off' i18n
      
      /**
       * The class for the wrapper element.
       */
      - wrapperClass (String)
###--%><%

    if (!cmp.getRenderCondition(resource, false).check()) {
        return;
    }

    Config cfg = cmp.getConfig();
    
    String name = cfg.get("name", String.class);
    String value = cfg.get("value", "true");
    String uncheckedValue = cfg.get("uncheckedValue", "false");
    boolean disabled = cfg.get("disabled", false);
    String fieldLabel = cfg.get("fieldLabel", String.class);
    String fieldDesc = cfg.get("fieldDescription", String.class);
    boolean required = cfg.get("required", false);
    
    boolean checked = cmp.getValue().isSelected(value, cfg.get("checked", false));
    
    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();
    cmp.populateCommonAttrs(attrs);

    attrs.add("name", name);
    attrs.add("value", value);
    attrs.addDisabled(disabled);
    attrs.addBoolean("required", required);
    attrs.addChecked(checked);
    
    String validation = StringUtils.join(cfg.get("validation", new String[0]), " ");
    attrs.add("data-foundation-validation", validation);
    attrs.add("data-validation", validation); // Compatibility
    
    String offText = outVar(xssAPI, i18n,cfg.get("offText", "Off"));
    String onText = outVar(xssAPI, i18n, cfg.get("onText", "On"));

    String readOnlyValue;
    if (checked) {
        readOnlyValue = i18n.get("On");
    } else {
        readOnlyValue = i18n.get("Off");
    }
    
    AttrBuilder deleteAttrs = new AttrBuilder(request, xssAPI);
    deleteAttrs.addClass("foundation-field-related");
    deleteAttrs.add("type", "hidden");
    deleteAttrs.addDisabled(disabled);
    
    AttrBuilder defaultValueAttrs = new AttrBuilder(request, xssAPI);
    defaultValueAttrs.addClass("foundation-field-related");
    defaultValueAttrs.add("type", "hidden");
    defaultValueAttrs.addDisabled(disabled);
    defaultValueAttrs.add("value", uncheckedValue);
    
    AttrBuilder defaultValueWhenMissingAttrs = new AttrBuilder(request, xssAPI);
    defaultValueWhenMissingAttrs.addClass("foundation-field-related");
    defaultValueWhenMissingAttrs.add("type", "hidden");
    defaultValueWhenMissingAttrs.addDisabled(disabled);
    defaultValueWhenMissingAttrs.add("value", true);
    
    if (!StringUtils.isBlank(name)) {
        deleteAttrs.add("name", name + "@Delete");
        defaultValueAttrs.add("name", name + "@DefaultValue");
        defaultValueWhenMissingAttrs.add("name", name + "@UseDefaultWhenMissing");
    }

    if (cfg.get("renderReadOnly", false)) {
        attrs.addClass("coral-Form-field");
        
        AttrBuilder roWrapperAttrs = new AttrBuilder(request, xssAPI);
        roWrapperAttrs.addClass("foundation-field-readonly coral-Form-fieldwrapper");
        roWrapperAttrs.addClass(cfg.get("wrapperClass", String.class));
        
        AttrBuilder editWrapperAttrs = new AttrBuilder(request, xssAPI);
        editWrapperAttrs.addClass("foundation-field-edit coral-Form-fieldwrapper");
        editWrapperAttrs.addClass(cfg.get("wrapperClass", String.class));
        
        %><div class="foundation-field-editable">
            <div <%= roWrapperAttrs %>><%
                if (fieldLabel != null) {
                    %><label class="coral-Form-fieldlabel"><%= outVar(xssAPI, i18n, fieldLabel) %></label><%
                }
                
                %><div class="coral-Form-field"><%= xssAPI.encodeForHTML(readOnlyValue) %></div>
            </div>
            <div <%= editWrapperAttrs %>><%
                if (fieldLabel != null) {
                    %><label class="coral-Form-fieldlabel"><%= outVar(xssAPI, i18n, fieldLabel) %><%= required ? " *" : "" %></label><%
                }
                
	            %><coral-switch <%= attrs.build() %>></coral-switch><%
                
	            if (cfg.get("deleteHint", true)) {
                    %><input <%= deleteAttrs.build() %>><%
                }
                
	            if (uncheckedValue != null) {
                    %><input <%= defaultValueAttrs.build() %>><%
                    %><input <%= defaultValueWhenMissingAttrs.build() %>><%
                }
                
                if (fieldDesc != null) {
                    %><coral-icon class="coral-Form-fieldinfo" icon="infoCircle"></coral-icon>
                    <coral-tooltip target="_prev" placement="left">
                        <coral-tooltip-content><%= outVar(xssAPI, i18n, fieldDesc) %></coral-tooltip-content>
                    </coral-tooltip><%
                }
            %></div>
        </div><%
    } else {
        boolean renderWrapper = false;
        
        if (cmp.getOptions().rootField()) {
            attrs.addClass("coral-Form-field");
            
            renderWrapper = fieldLabel != null || fieldDesc != null;
        }
        
        if (renderWrapper) {
            AttrBuilder wrapperAttrs = new AttrBuilder(request, xssAPI);
            wrapperAttrs.addClass("coral-Form-fieldwrapper");
            wrapperAttrs.addClass(cfg.get("wrapperClass", String.class));
            
            %><div <%= wrapperAttrs %>><%

            if (fieldLabel != null) {
                %><label class="coral-Form-fieldlabel"><%= outVar(xssAPI, i18n, fieldLabel) %><%= required ? " *" : "" %></label><%
            }
        }
        %>
        <span class="coral3-Switch"><%= offText %></span>
	<coral-switch <%= attrs.build() %>></coral-switch>
	<span class="coral3-Switch"><%= onText %></span>
	<%
        
        if (cfg.get("deleteHint", true)) {
            %><input <%= deleteAttrs.build() %>><%
        }
        
        if (uncheckedValue != null) {
            %><input <%= defaultValueAttrs.build() %>><%
            %><input <%= defaultValueWhenMissingAttrs.build() %>><%
        }

        if (renderWrapper) {
            if (fieldDesc != null) {
                %><coral-icon class="coral-Form-fieldinfo" icon="infoCircle"></coral-icon>
                <coral-tooltip target="_prev" placement="left">
                    <coral-tooltip-content><%= outVar(xssAPI, i18n, fieldDesc) %></coral-tooltip-content>
                </coral-tooltip><%
            }
            %></div><%
        }
    }
%>
