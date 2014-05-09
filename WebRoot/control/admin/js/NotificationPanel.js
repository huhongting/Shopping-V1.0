Ext.define('NotificationPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'notifypanel',
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
	
		this.tbar = [{
			text: '发布通知',
			iconCls: 'add',
			id: 'mna',
			handler: this.addNotification
		}, {
			text: '删除通知',
			id: 'mnd',
			iconCls: 'del',
			handler: this.delNotification	
		}];
		
		this.store = new Ext.data.Store({
        	autoLoad: {
            	start: 0, 
            	limit: this.pageSize
            },
            fields:['id', 'title', 'content', 'creater', 
            	'createdate', 'status'],
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/notification.do?method=list',
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'createdate',
            	direction: 'DESC'
            }]
        });
		
		this.columns = [
			new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "通知编号", dataIndex: 'id', sortable: true},
            {header: "通知标题", dataIndex: 'title', flex: 2, sortable: true},
            {header: "通知内容", dataIndex: 'content', flex: 4, sortable: false},
            {header: "发布者", dataIndex: 'creater', sortable: true},
            {header: "发布时间", dataIndex: 'createdate', flex: 1, sortable: true},
            {header: "通知状态", dataIndex: 'status', sortable: true,
            	renderer: function(value) {
            		if(value == 'IMPORTANT') return '<font color=red><b>重要</b></font>';
            		return '普通';
            	}
            }
		];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
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
    	itemdblclick: {fn: onLoadNotify},
    	afterrender: {
    		fn: function() {
    			Ext.Ajax.request({
					url: 'control/admin/check/notify.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mna').disabled = !r.notify;
						Ext.getCmp('mnd').disabled = !r.notify;
					}
				});
    		}
    	}
    },
	addNotification: function() {
		var w = new Ext.Window({
			title: '添加通知',
			width: 400,
			border: false,
			buttonAlign: 'center',
			modal: true,
			resizable: false,
			animateTarget: 'mna',
			items: [{
				xtype: 'form',
				frame: true,
				items: [{
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '标题',
					name: 'title',
					allowBlank: false,
					emptyText: '请填写通知标题',
					blankText: '通知标题不能为空！',
					width: 350
				}, {
					xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '内容',
					alowBlank: false,
					blankText: '通知内容不能为空！',
					name: 'content',
					width: 350
				}, {
					xtype: 'combo',
					name: 'status',
					width: 350,
					editable: false,
					labelAlign: 'right',
					labelWidth: 70,
					emptyText: '请选择通知状态',
					fieldLabel: '通知状态',
					allowBlank: false,
					blankText: '通知状态不能为空！',
					valueField: 'value',
					displayField: 'name',
					triggerAction: 'all',
					//mode: 'remote',
					store: new Ext.data.Store({
						autoLoad: true,
						fields: ['name', 'value'],
						proxy: {
							type: 'ajax',
							url: 'control/admin/notification/status.do?method=list',
							reader: {
								type: 'json',
								root: 'records'
							}
						}
					})
				}]
			}],
			buttons: [{
				text: '确定',
				handler: function() {
					var f = w.getComponent(0).getForm();
					if(f.isValid()) {
						f.submit({
							url: 'control/admin/notification.do?method=add',
							success: function(form, action) {
								if(action.result.success) {
									Ext.Msg.alert('提示', '通知保存成功！');
									Ext.getCmp('notifypanel').getStore().reload();
									w.close();
								} else {
									Ext.Msg.alert('提示', '通知保存失败！');
								}
							}, 
							failure: function(form, action) {
								Ext.Msg.alert('提示', '通知保存失败！');
							}
						})
					}
				}
			}, {
				text: '重置',
				handler: function() {
					w.getComponent(0).getForm().reset();
				}
			}, {
				text: '取消',
				handler: function() {
					w.close();
				}
			}]
		});
		w.show();
	},
	delNotification: function() {
		var sm = Ext.getCmp('notifypanel').getSelectionModel();
		if(sm.hasSelection()) {
			var s = sm.getSelection();
			var ids = [];
			for(var i=0; i<s.length; i++) {
				ids.push(s[i].get('id'));
			}
			if(ids.length == 0) {
				Ext.MessageBox.show({
					title: '提示',
					msg: '没有需要删除的通知！',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.YES
				});
				return;
			}
			Ext.Ajax.request({
				url: 'control/admin/notification.do?method=del',
				params: {ids: ids},
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
						Ext.getCmp('notifypanel').getStore().reload();
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
	}
});

function onLoadNotify() {
	var sm = Ext.getCmp('notifypanel').getSelectionModel();
	var record = sm.getSelection();
	var w = new Ext.Window({
		title: '系统通知',
		width: 700,
		modal: true,
		border: false,
		resizable: false,
		animateTarget: 'notifypanel',
		items: [new Ext.form.Panel({
			border: false,
			frame: true,
			columnWidth: 5,
			layout: 'form',
			items: [{
				frame: true,
				border: false,
				layout: 'table',
				border: false,
				frame: true,
				columnWidth: 5,
				items: [{
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '发布者',
					name: 'creater'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '发布时间',
					name: 'createdate'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '通知状态',
					name: 'status'
				}]
			}, {
				//xtype: 'panel',
				border: false,
				layout: 'table',
				columnWidth: 5,
				frame: true,
				items: [{
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '标题',
					name: 'title'
				}]
			}, {
				//xtype: 'panel',
				border: false,
				layout: 'table',
				columnWidth: 5,
				frame: true,
				items: [{
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '内容',
					name: 'content'
				}]
			}]
		})],
		buttonAlign: 'center',
		border: false,
		buttons: [{
			text: '关闭',
			handler: function() {
				w.close();
			}
		}]
	});
	w.getComponent(0).getForm().loadRecord(record[0]);
	w.show();
}