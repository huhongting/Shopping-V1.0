Ext.define('BookTypeTreePanel', {
    extend: 'Ext.tree.Panel',
    useArrows: true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: true,
    border: false,
    id: 'bttpanel',
    initComponent: function() {
        Ext.apply(this, {
            store: new Ext.data.TreeStore({
                fields: ['typeid', 'typename', 'parent', 'note', 'delflag'],
                proxy: {
                    type: 'ajax',
                    url: 'control/admin/booktype.do?method=listtree'
                },
                folderSort: true
            }),
            columns: [{
                xtype: 'treecolumn',
                text: 'ͼ�����',
                flex: 3,
                sortable: true,
                dataIndex: 'typename'
            },{
                text: '�ϼ����',
                flex: 3,
                dataIndex: 'parent',
                sortable: true
            },{
                text: '����˵��',
                flex: 3,
                dataIndex: 'note',
                sortable: true
            },{
                text: '�Ƿ����',
                flex: 1,
                dataIndex: 'delflag',
                sortable: true,
                renderer: function(value) {
                	if(value == true) return "<span style='color:red;font-weight:bold;font-size:12'>������</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
                	else return "<span style='color:green;font-weight:bold;font-size:12'>����</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
                }
            }]
        });
        this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
        this.tbar = [{
        	text: '���',
        	id: 'mbta',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	text: '�޸�',
        	id: 'mbte',
        	iconCls: 'edit',
        	handler: this.onEditHandler
        }, '-', {
        	text: 'ɾ��',
        	id: 'mbtd',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }];
        
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
					url: 'control/admin/check/booktype.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mbta').disabled = !r.booktype;
						Ext.getCmp('mbte').disabled = !r.booktype;
						Ext.getCmp('mbtd').disabled = !r.booktype;
					}
				});
    		}
    	}
    },
	onAddHandler: function() {
		new Ext.Window({
			width: 400,
			modal: true,
			title: '������',
			id: 'btadd',
			//closeAction: 'hide',
			layout: 'fit',
			buttonAlign: 'center',
			border: false,
			resizable: false,
			animateTarget: 'mbta',
			items: [{
				xtype: 'form',
				frame: true,
				id: 'typeform',
				items: [{
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '�ϼ����',
					id: 'parent',
					name: 'parent',
					emptyText: '����д�ϼ����',
					width: 350,
					listeners: {
						focus: {
							fn: function(com, e, o) {
								// show book type tree
								var win = new Ext.Window({
									title: '�鼮���',
									height: 300,
									width: 350,
									resizable: false,
									autoScroll: true,
									modal: true,
									border: false,
									buttonAlign: 'center',
									items: new Ext.tree.Panel({
												//height: 300,
												width: 320,
												border: false,
												rootVisible: false,
												store: new Ext.data.TreeStore({
													root: {
														id: 'root',
														expanded: true
													},
													proxy: {
														type: 'ajax',
														url: 'control/admin/booktype.do?method=tree'
													}
												})
											}),
										buttons: [{
											text: 'ȷ��',
											handler: selectItem
										}]
								}).show();
								function selectItem() {
									var s = win.getComponent(0).getSelectionModel().getSelection();	
									com.setValue(s[0].get('text'));
									win.hide();
								}
							}
						}
					}
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '�������',
					id: 'typename',
					allowBlank: false,
					emptyText: '����д�������',
					blankText: '������Ʋ���Ϊ�գ�',
					width: 350
				}, {
					//xtype: 'textareafield',
					xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '�������',
					emptyText: '����Ϊ��',
					id: 'note',
					width: 350
				}]
			}],
			buttons: [{
				text: '����',
				handler: function() {
					var _typename = Ext.util.Format.trim(Ext.getCmp('typename').getValue());
					var _note = Ext.getCmp('note').getValue();
					var _parent = Ext.getCmp('parent').getValue();
					if(_typename == "") {
                        Ext.MessageBox.show({
                        	title: '����',
                        	msg: '������������������!',
                        	icon: Ext.MessageBox.WARNING,
                        	buttons: Ext.MessageBox.OK
                        });
                    } else { 
						Ext.Ajax.request({
	                        url: 'control/admin/booktype.do?method=add',
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
		                            Ext.getCmp('btadd').close();
		                            Ext.getCmp('bttpanel').getStore().reload();
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
	                            typename: _typename,
	                            note: _note,
	                            parent: _parent
	                        }
	                    });
                    }
				}
			}, {
				text: '����',
				handler: function() {
					Ext.getCmp('typeform').form.reset();
				}
			}]
		}).show();
	},
	onDelHandler: function() {
		var sm = Ext.getCmp('bttpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����ѡ��¼��', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						ids.push(s[i].get('typeid'));
					}
					
					Ext.Ajax.request({
						url: 'control/admin/booktype.do?method=del',
						params: {id: ids},
						method: 'POST',
						success: function(response, opt) {
							var res = Ext.JSON.decode(response.responseText);
							if(res.success) {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: 'ɾ���ɹ���',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('bttpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: "ɾ��ʧ�ܣ�",
									icon: Ext.MessageBox.WARNING,
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
		var sm = Ext.getCmp('bttpanel').getSelectionModel();
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
				width: 400,
				border: false,
				resizable: false,
				animateTarget: 'mbte',
				items: [{
					xtype: 'form',
					frame: true,
					items: [{
						xtype: 'textfield',
						id: 'typeid',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�����',
						readOnly: true,
						hidden: true
					}, {
						xtype: 'textfield',
						id: 'typename',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�������',
						width: 350
					}, {
						xtype: 'hidden',
						id: 'parent',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�ϼ����',
						value: null
					}, {
						xtype: 'htmleditor',
						id: 'note',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�������',
						width: 350
					}, {
						xtype: 'combo',
						id: 'delflag',
						editable: false,
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�Ƿ�ɾ��',
						valueField: 'value',
						displayField: 'name',
						triggerAction: 'all',
						width: 350,
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
			//Ext.getCmp('note').setValue((Ext.getCmp('note').value=='null'?'':Ext.getCmp('note').value));
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
			var id = form.getComponent('typeid').value;
			var typename = form.getComponent('typename').value;
			//var parent = form.getComponent('parent').value;
			var note = form.getComponent('note').value;
			var delflag = form.getComponent('delflag').value;
			Ext.Ajax.request({
				url: 'control/admin/booktype.do?method=update',
				params: {
					id: id,
					typename: typename,
					note: note,
					delflag: delflag
				},
				method: 'POST',
				success: function(response, opt) {
					var res = Ext.JSON.decode(response.responseText);
					if(res.success) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�޸ĳɹ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						close();
						Ext.getCmp('bttpanel').getStore().reload();
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