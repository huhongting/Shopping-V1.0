Ext.define('PressPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'ppanel',
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		this.store = Ext.create('Ext.data.Store', {
            autoLoad: {
            	start: 0, 
            	limit: this.pageSize
            },
            fields:['pressid', 'pressname', 'remark', 'delflag'],
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/press.do?method=list',  //����ķ�������ַ
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'pressid',
            	direction: 'ASC'
            }]
        });
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "��������", dataIndex: 'pressid', sortable: true},
            {header: "����������", dataIndex: 'pressname', sortable: true},
            {header: "��ע", dataIndex: 'remark', sortable: true,
            	renderer: function(value) {
            		if(value == 'null') {
            			return '����';
            		} else {
            			return value;
            		}
            	}
            },
            {header: "״̬", dataIndex: 'delflag', sortable: true,
	            renderer: function(value) {
	            	if(value == false) {
	            		return "<span style='color:green;font-weight:bold;font-size:12'>����</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
	            	} else {
	            		return "<span style='color:red;font-weight:bold;font-size:12'>������</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
	            	}
	            }
            }
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
        	text: '���',
        	id: 'mpa',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	text: '�޸�',
        	id: 'mpe',
        	iconCls: 'edit',
        	handler: this.onEditHandler
        }, '-', {
        	text: 'ɾ��',
        	id: 'mpd',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }, '->', {
        	fieldLabel: '��������',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	store: this.store,
        	width: 300,
        	emptyText: '���������������'
        }];
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            store: this.store,   //������Ҫָ��������ͬ��store
            displayInfo: true,
            beforePageText: '��',
            afterPageText: 'ҳ/�� {0} ҳ',
            displayMsg: '�� {0} ���� {1} ����¼���� {2} ����¼',
      		emptyMsg: "û�м�¼"
        }];
        this.forceFit = true;
		this.callParent();
	},
	listeners: {
		'itemcontextmenu': function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		},
		afterrender: {
    		fn: function() {
    			Ext.Ajax.request({
					url: 'control/admin/check/press.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mpa').disabled = !r.press;
						Ext.getCmp('mpe').disabled = !r.press;
						Ext.getCmp('mpd').disabled = !r.press;
					}
				});
    		}
    	}
	},
	onAddHandler: function() {
		new Ext.Window({
			width: 350,
			modal: true,
			title: '������',
			id: 'padd',
			//closeAction: 'hide',
			layout: 'fit',
			buttonAlign: 'center',
			border: false,
			resizable: false,
			animateTarget: 'mpa',
			items: [{
				xtype: 'form',
				frame: true,
				id: 'pressform',
				items: [{
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '����������',
					id: 'pressname',
					allowBlank: false,
					emptyText: '����д����������',
					blankText: '���������Ʋ���Ϊ�գ�',
					width: 300
				}, {
					xtype: 'textareafield',
					//xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '��ע',
					emptyText: '����Ϊ��',
					id: 'remark',
					width: 300
				}]
			}],
			buttons: [{
				text: '����',
				handler: function() {
					var _pressname = Ext.util.Format.trim(Ext.getCmp('pressname').getValue());
					var _remark = Ext.getCmp('remark').getValue();
					if(_pressname == "") {
                        Ext.MessageBox.show({
                        	title: '����',
                        	msg: '������������������!',
                        	icon: Ext.MessageBox.WARNING,
                        	buttons: Ext.MessageBox.OK
                        });
                    } else { 
						Ext.Ajax.request({
	                        url: 'control/admin/press.do?method=add',
	                        method: 'POST',
	                        success: function (response, opts) {
	                        	var res = Ext.JSON.decode(response.responseText);
	                        	if(res.success) {
		                            Ext.MessageBox.show({
		                            	title: '����',
		                            	msg: '���ݱ���ɹ�!',
		                            	icon: Ext.MessageBox.INFO,
		                            	buttons: Ext.MessageBox.OK
		                            });
		                            Ext.getCmp('padd').close();
		                            Ext.getCmp('ppanel').getStore().reload();
	                        	} else {
	                        		Ext.MessageBox.show({
		                            	title: '����',
		                            	msg: '���ݱ���ʧ��!',
		                            	icon: Ext.MessageBox.ERROR,
		                            	buttons: Ext.MessageBox.OK
	                            	});
	                        	}
	                        },
	                        failure: function (response, opts) {
	                            Ext.MessageBox.show({
	                            	title: '����',
	                            	msg: '���ݱ���ʧ��!',
	                            	icon: Ext.MessageBox.ERROR,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                        },
	                        params: {
	                            pressname: _pressname,
	                            remark: _remark
	                        }
	                    });
                    }
				}
			}, {
				text: '����',
				handler: function() {
					Ext.getCmp('pressform').form.reset();
				}
			}]
		}).show();
	},
	onDelHandler: function() {
		var sm = Ext.getCmp('ppanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����ѡ��¼��', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						if(s[i].get('delflag') == false) ids.push(s[i].get('pressid'));
					}
					if(ids.length == 0) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: 'û����Ҫɾ���ļ�¼��',
							icon: Ext.MessageBox.WARNING,
							buttons: Ext.MessageBox.YES
						});
						return;
					}
					
					Ext.Ajax.request({
						url: 'control/admin/press.do?method=del',
						params: {ids: ids},
						method: 'POST',
						success: function(response, opt) {
							var res = Ext.JSON.decode(response.responseText);
							if(res.success == true) {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: 'ɾ���ɹ���',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('ppanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: 'ɾ��ʧ�ܣ�',
									icon: Ext.MessageBox.ERROR,
									buttons: Ext.MessageBox.YES
								});
							}
						},
						failure: function(response, opt) {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: 'ɾ��ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					});
				}
			});
			
		} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ����Ҫɾ���ļ�¼��',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
		
	},
	onEditHandler: function() {
		var win = null;
		var sm = Ext.getCmp('ppanel').getSelectionModel();
		if(sm.hasSelection()) {
			var record = sm.getSelection();
			if(record.length > 1) {
				Ext.MessageBox.show({
					title: '��ʾ',
					msg: '��ѡ��һ����¼��',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.OK
				});
				return;
			}
			
			win = new Ext.Window({
				title: '�༭',
				buttonAlign: 'center',
				modal: true,
				width: 270,
				border: false,
				resizable: false,
				animateTarget: 'mpe',
				items: [{
					xtype: 'form',
					frame: true,
					items: [{
						xtype: 'textfield',
						id: 'pressid',
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '��������',
						readOnly: true,
						hidden: true
					}, {
						xtype: 'textfield',
						id: 'pressname',
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '����������'
					}, {
						xtype: 'textareafield',
						id: 'remark',
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '��ע'
					}, {
						xtype: 'combo',
						id: 'delflag',
						editable: false,
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '�Ƿ�ɾ��',
						valueField: 'value',
						displayField: 'name',
						triggerAction: 'all',
						mode: 'local',
						store: new Ext.data.SimpleStore({
							fields: ['name', 'value'],
							data: [['��', true], ['��', false]]
						})
					}]
				}],
				buttons: [{
					text: '�ύ',
					handler: submit
				}, {
					text: 'ȡ��',
					handler: close
				}]
			});
			
			win.getComponent(0).loadRecord(record[0]);
			Ext.getCmp('remark').setValue((Ext.getCmp('remark').value=='null'?'':Ext.getCmp('remark').value));
			win.show();
		} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ��һ����¼��',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.OK
			});
		}
		function close() { if(win != null) win.close(); }
		function submit() {
			var form = win.getComponent(0);
			var id = form.getComponent('pressid').value;
			var pressname = form.getComponent('pressname').value;
			var remark = form.getComponent('remark').value;
			var delflag = form.getComponent('delflag').value;
			Ext.Ajax.request({
				url: 'control/admin/press.do?method=update',
				params: {
					id: id,
					pressname: pressname,
					remark: remark,
					delflag: delflag
				},
				method: 'POST',
				success: function(response, opt) {
					var res = Ext.JSON.decode(response.responseText);
					if(res.success == true) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�޸ĳɹ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						close();
						Ext.getCmp('ppanel').getStore().reload();
					} else {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�޸�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				},
				failure: function(response, opt) {
					Ext.MessageBox.show({
						title: '��ʾ',
						msg: '�޸�ʧ�ܣ�',
						icon: Ext.MessageBox.ERROR,
						buttons: Ext.MessageBox.YES
					});
				}
			});
		}
	}
});


