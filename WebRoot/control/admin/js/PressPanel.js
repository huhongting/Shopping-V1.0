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
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/press.do?method=list',  //请求的服务器地址
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
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "出版社编号", dataIndex: 'pressid', sortable: true},
            {header: "出版社名称", dataIndex: 'pressname', sortable: true},
            {header: "备注", dataIndex: 'remark', sortable: true,
            	renderer: function(value) {
            		if(value == 'null') {
            			return '暂无';
            		} else {
            			return value;
            		}
            	}
            },
            {header: "状态", dataIndex: 'delflag', sortable: true,
	            renderer: function(value) {
	            	if(value == false) {
	            		return "<span style='color:green;font-weight:bold;font-size:12'>可用</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
	            	} else {
	            		return "<span style='color:red;font-weight:bold;font-size:12'>不可用</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
	            	}
	            }
            }
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
        	text: '添加',
        	id: 'mpa',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	text: '修改',
        	id: 'mpe',
        	iconCls: 'edit',
        	handler: this.onEditHandler
        }, '-', {
        	text: '删除',
        	id: 'mpd',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }, '->', {
        	fieldLabel: '名称搜索',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	store: this.store,
        	width: 300,
        	emptyText: '请输入出版社名称'
        }];
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            store: this.store,   //这里需要指定与表格相同的store
            displayInfo: true,
            beforePageText: '第',
            afterPageText: '页/共 {0} 页',
            displayMsg: '第 {0} 条到 {1} 条记录，共 {2} 条记录',
      		emptyMsg: "没有记录"
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
			title: '添加类别',
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
					fieldLabel: '出版社名称',
					id: 'pressname',
					allowBlank: false,
					emptyText: '请填写出版社名称',
					blankText: '出版社名称不能为空！',
					width: 300
				}, {
					xtype: 'textareafield',
					//xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '备注',
					emptyText: '可以为空',
					id: 'remark',
					width: 300
				}]
			}],
			buttons: [{
				text: '保存',
				handler: function() {
					var _pressname = Ext.util.Format.trim(Ext.getCmp('pressname').getValue());
					var _remark = Ext.getCmp('remark').getValue();
					if(_pressname == "") {
                        Ext.MessageBox.show({
                        	title: '保存',
                        	msg: '输入有误，请重新输入!',
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
		                            	title: '保存',
		                            	msg: '数据保存成功!',
		                            	icon: Ext.MessageBox.INFO,
		                            	buttons: Ext.MessageBox.OK
		                            });
		                            Ext.getCmp('padd').close();
		                            Ext.getCmp('ppanel').getStore().reload();
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
	                            pressname: _pressname,
	                            remark: _remark
	                        }
	                    });
                    }
				}
			}, {
				text: '重置',
				handler: function() {
					Ext.getCmp('pressform').form.reset();
				}
			}]
		}).show();
	},
	onDelHandler: function() {
		var sm = Ext.getCmp('ppanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('提示', '您确定要删除所选记录吗？', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						if(s[i].get('delflag') == false) ids.push(s[i].get('pressid'));
					}
					if(ids.length == 0) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '没有需要删除的记录！',
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
									title: '提示',
									msg: '删除成功！',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('ppanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '提示',
									msg: '删除失败！',
									icon: Ext.MessageBox.ERROR,
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
		var sm = Ext.getCmp('ppanel').getSelectionModel();
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
						fieldLabel: '出版社编号',
						readOnly: true,
						hidden: true
					}, {
						xtype: 'textfield',
						id: 'pressname',
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '出版社名称'
					}, {
						xtype: 'textareafield',
						id: 'remark',
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '备注'
					}, {
						xtype: 'combo',
						id: 'delflag',
						editable: false,
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '是否删除',
						valueField: 'value',
						displayField: 'name',
						triggerAction: 'all',
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
			Ext.getCmp('remark').setValue((Ext.getCmp('remark').value=='null'?'':Ext.getCmp('remark').value));
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
							title: '提示',
							msg: '修改成功！',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						close();
						Ext.getCmp('ppanel').getStore().reload();
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


