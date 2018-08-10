/* 
 * CustomAudioInsertPlugin - CUI RTE Plugin 
 * This is custom CUI RTE plug-in to support insertion of Audio within RTE with 
 * specific dimensions and alt text 
 */  
CQ.Ext.ns('CustomInsertAudioPlugin');  
CustomInsertAudioPlugin.insertAudio = {  
    ADD_AUDIO_CMD: 'insertAudio'};  
  
CustomInsertAudioPlugin.insertAudio.Plugin = new Class({  
    toString: 'insertAudioPlugin',  
    extend: CUI.rte.plugins.Plugin,  
    P: CustomInsertAudioPlugin.insertAudio,  
  
  
    addAudioUI: null,  
  
  
    getFeatures: function() {  
        return [this.P.ADD_AUDIO_CMD];  
    },  
    initializeUI: function(tbGenerator) {  
        const plg = CUI.rte.plugins;  
  
  
        if (this.isFeatureEnabled(this.P.ADD_AUDIO_CMD)) {  
            this.addAudioUI = tbGenerator.createElement(this.P.ADD_AUDIO_CMD, this, true, this.getTooltip('insertAudio'));  
            tbGenerator.addElement('insertAudio', plg.Plugin.insert_audio, this.addAudioUI, 1000);  
        }  
    },  
  
  
    execute: function(cmd, value, env) {  
        if (cmd === this.P.ADD_AUDIO_CMD) {  
            this.showDialog(env.editContext);  
        }  
    },  
    showDialog: function() {  
        const editorKernel = this.editorKernel,  
            dm = editorKernel.getDialogManager();  
  
  
        const dialogConfig = {  
            'jcr:primaryType': 'cq:Dialog',  
            title: 'Insert Audio',  
            modal: true,  
            width: 400,  
            height: 300,  
            items: [{  
                xtype: 'panel',  
                padding: '20px 0 0 10px',  
                items: [{  
                    xtype: 'panel',  
                    layout: 'form',  
                    border: false,  
                    items: [{  
                            name: 'audio_path',  
                            xtype: 'pathfield',  
                            fieldLabel: 'Audio Path',  
                            rootPath: '/content/dam',  
                            width: 250  
                        },  
                        {  
                            xtype: 'textfield',  
                            name: 'altName',  
                            fieldLabel: 'Alternative Text',  
                            fieldDescription: 'Provide a textual alternative of the content and function of the audio',  
                            width: 250  
                        }]  
                }]  
            }],  
            ok: function() {  
                const alt = this.findByType('textfield')[0];  
                const mp3 = this.findByType('pathfield')[0];  
  
  
                const value = {  
                    alt_text: alt.getValue(),  
                    mp3_path: mp3.getValue()                      
                };  
  
  
                this.close();  
                editorKernel.relayCmd(CustomInsertAudioPlugin.insertAudio.ADD_AUDIO_CMD, value);  
            },  
            listeners: {  
                show: function() {  
                    editorKernel.fireUIEvent('dialogshow');  
                },  
                hide: function() {  
                    editorKernel.fireUIEvent('dialoghide');  
                }  
            }  
        };  
        dm.show(CQ.WCM.getDialog(dialogConfig));  
    },  
    notifyPluginConfig: function(pluginConfig) {  
        pluginConfig = pluginConfig || {};  
        CUI.rte.Utils.applyDefaults(pluginConfig, {  
            'tooltips': {  
                'insertAudio': {  
                    'title': 'Insert Audio',  
                    'text': 'inserts audio'  
                }  
            }  
        });  
        this.config = pluginConfig;  
    },  
    updateState: function() {  
        this.addAudioUI.setSelected(false);  
        }  
});  
CUI.rte.plugins.PluginRegistry.register('insertaudio', CustomInsertAudioPlugin.insertAudio.Plugin);  
CustomInsertAudioPlugin.insertAudio.Cmd = new Class({  
    toString: 'insertAudio',  
    extend: CUI.rte.commands.Command,  
    P: CustomInsertAudioPlugin.insertAudio,  
    isCommand: function(cmdStr) {  
        return (cmdStr === this.P.ADD_AUDIO_CMD);  
    },  
    getProcessingOptions: function() {  
        const cmd = CUI.rte.commands.Command;  
        return cmd.PO_SELECTION | cmd.PO_NODELIST;  
    },  
  
  
    addAudioToRTE: function(execDef) {  
        const value = execDef.value;  
        const selection = execDef.selection;  
        const node = CUI.rte.DomProcessor.createNode(execDef.editContext, 'span');  
        CUI.rte.Common.insertNode(node, selection.startNode, selection.startOffset);  
        const mp3Path = value.mp3_path;  
        node.innerHTML = "<audio controls='controls' data-src='" + mp3Path +   
        "' _rte_src='" + mp3Path +   
        "' src='" + mp3Path +   
        "'>Your browser does not support the audio tag.</audio><p>'"+value.alt_text+"'</p>";  
    },  
  
  
    execute: function(execDef) {  
        if (execDef.command === this.P.ADD_AUDIO_CMD) {  
            this.addAudioToRTE(execDef);  
        }  
    }  
});  
  
  
CUI.rte.commands.CommandRegistry.register('insertaudio', CustomInsertAudioPlugin.insertAudio.Cmd); 
