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
                text: '图书类别',
                flex: 3,
                sortable: true,
                dataIndex: 'typename'
            },{
                text: '上级类别',
                flex: 3,
                dataIndex: 'parent',
                sortable: true
            },{
                text: '其他说明',
                flex: 3,
                dataIndex: 'note',
                sortable: true
            },{
                text: '是否可用',
                flex: 1,
                dataIndex: 'delflag',
                sortable: true,
                renderer: function(value) {
                	if(value == true) return "<span style='color:red;font-weight:bold;font-size:12'>不可用</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
                	else return "<span style='color:green;font-weight:bold;font-size:12'>可用</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
                }
            }]
        });
        this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
        this.tbar = [{
        	text: '添加',
        	id: 'mbta',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	text: '修改',
        	id: 'mbte',
        	iconCls: 'edit',
        	handler: this.onEditHandler
        }, '-', {
        	text: '删除',
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
			title: '添加类别',
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
					fieldLabel: '上级类别',
					id: 'parent',
					name: 'parent',
					emptyText: '请填写上级类别',
					width: 350,
					listeners: {
						focus: {
							fn: function(com, e, o) {
								// show book type tree
								var win = new Ext.Window({
									title: '书籍类别',
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
											text: '确定',
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
					fieldLabel: '类别名称',
					id: 'typename',
					allowBlank: false,
					emptyText: '请填写类别名称',
					blankText: '类别名称不能为空！',
					width: 350
				}, {
					//xtype: 'textareafield',
					xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '类别描述',
					emptyText: '可以为空',
					id: 'note',
					width: 350
				}]
			}],
			buttons: [{
				text: '保存',
				handler: function() {
					var _typename = Ext.util.Format.trim(Ext.getCmp('typename').getValue());
					var _note = Ext.getCmp('note').getValue();
					var _parent = Ext.getCmp('parent').getValue();
					if(_typename == "") {
                        Ext.MessageBox.show({
                        	title: '保存',
                        	msg: '输入有误，请重新输入!',
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
		                            	title: '保存',
		                            	msg: '数据保存成功!',
		                            	icon: Ext.MessageBox.INFO,
		                            	buttons: Ext.MessageBox.OK
		                            });
		                            Ext.getCmp('btadd').close();
		                            Ext.getCmp('bttpanel').getStore().reload();
	                        	} else {
	                        		Ext.MessageBox.show({
		                            	title: '保存',
		                            	msg: '数据保存失败!',
		                            	icon: Ext.MessageBox.ERROR,
		                            	buttons: Ext.MessageBox.OK
	                            	});
	                        	}
	                        },
	                        failure: function (response, opts) {
	                            Ext.MessageBox.show({
	                            	title: '保存',
	                            	msg: '数据保存失败!',
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
				text: '重置',
				handler: function() {
					Ext.getCmp('typeform').form.reset();
				}
			}]
		}).show();
	},
	onDelHandler: function() {
		var sm = Ext.getCmp('bttpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('提示', '您确定要删除所选记录吗？', function(btn) {
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
									title: '提示',
									msg: '删除成功！',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('bttpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '提示',
									msg: "删除失败！",
									icon: Ext.MessageBox.WARNING,
									buttons: Ext.MessageBox.YES
								});
							}
						},
						failure: function(response, opt) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '删除失败！',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					});
				}
			});
			
		} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要删除的记录！',
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
					title: '提示',
					msg: '请选择一条记录！',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.OK
				});
				return;
			}
			
			win = new Ext.Window({
				title: '编辑',
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
						fieldLabel: '类别编号',
						readOnly: true,
						hidden: true
					}, {
						xtype: 'textfield',
						id: 'typename',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '类别名称',
						width: 350
					}, {
						xtype: 'hidden',
						id: 'parent',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '上级类别',
						value: null
					}, {
						xtype: 'htmleditor',
						id: 'note',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '类别描述',
						width: 350
					}, {
						xtype: 'combo',
						id: 'delflag',
						editable: false,
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '是否删除',
						valueField: 'value',
						displayField: 'name',
						triggerAction: 'all',
						width: 350,
						mode: 'local',
						store: new Ext.data.SimpleStore({
							fields: ['name', 'value'],
							data: [['是', true], ['否', false]]
						})
					}]
				}],
				buttons: [{
					text: '提交',
					handler: submit
				}, {
					text: '取消',
					handler: close
				}]
			});
			
			win.getComponent(0).loadRecord(record[0]);
			//Ext.getCmp('note').setValue((Ext.getCmp('note').value=='null'?'':Ext.getCmp('note').value));
			win.show();
		} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择一条记录！',
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
							title: '提示',
							msg: '修改成功！',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						close();
						Ext.getCmp('bttpanel').getStore().reload();
					} else {
						Ext.MessageBox.show({
							title: '提示',
							msg: '修改失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				},
				failure: function(response, opt) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '修改失败！',
						icon: Ext.MessageBox.ERROR,
						buttons: Ext.MessageBox.YES
					});
				}
			});
		}
	}
});