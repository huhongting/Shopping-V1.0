Ext.define('EmpDatePanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'empdatepanel',
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
	
		this.tbar = [{
			text: '添加',
			iconCls: 'add',
			id: 'edadd',
			handler: this.addEmpDate
		}, {
			text: '删除',
			iconCls: 'del',
			handler: this.delEmpDate
		}];
		
		this.store = new Ext.data.Store({
        	autoLoad: {
            	start: 0, 
            	limit: this.pageSize
            },
            fields:['id', 'title', 'content', 'logger', 
            	'logtime', 'activetime', 'remark'],
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/empdate.do?method=list',
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'activetime',
            	direction: 'DESC'
            }]
        });
		
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.columns = [
			new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "编号", dataIndex: 'id', sortable: true},
            {header: "标题", dataIndex: 'title', flex: 2, sortable: true},
            {header: "内容", dataIndex: 'content', flex: 4, sortable: false},
            {header: "记录者", dataIndex: 'logger', sortable: true},
            {header: "记录时间", dataIndex: 'logtime', flex: 2, sortable: true},
            {header: "活动时间", dataIndex: 'activetime', flex: 2, sortable: true},
            {header: "备注", dataIndex: 'remark', flex: 1, sortable: false},
            {header: "状态", dataIndex: 'activetime', sortable: true,
            	renderer: function(value) {
            		var v = [];
            		var a = [];
            		v[0] = Ext.util.Format.date(value, 'Y');
            		v[1] = Ext.util.Format.date(value, 'm');
            		v[2] = Ext.util.Format.date(value, 'd');
            		a[0] = Ext.util.Format.date(new Date(), 'Y');
            		a[1] = Ext.util.Format.date(new Date(), 'm');
            		a[2] = Ext.util.Format.date(new Date(), 'd');
            		if(v[0] == a[0]) {
            			if(v[1] == a[1]) {
            				if(v[2] == a[2]) {
            					return '<font color=red><b>今天</b></font>';
            				} else if((parseInt(v[2])-parseInt(a[2])) == 1) {
            					return '<font color=green>明天</font>';
            				}
            				return value;
            			}
            			return value;
            		}
            		return value;
            	}
            }
		];
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
    	itemdblclick: {fn: onLoadEmpDate}
    },
	addEmpDate: function() {
		var w = new Ext.Window({
			title: '添加备忘录',
			width: 400,
			border: false,
			resizable: false,
			modal: true,
			animateTarget: 'edadd',
			buttonAlign: 'center',
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
					emptyText: '请填写备忘录标题',
					blankText: '备忘录标题不能为空！',
					width: 350
				}, {
					xtype: 'datefield',
					format: 'Y-m-d',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '活动日期',
					emptyText: '请填活动日期',
					name: 'activedate',
					allowBlank: false,
					blankText: '活动日期不能为空！',
					width: 350
				}, {
					xtype: 'timefield',
					format: 'H:i',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '活动时间',
					emptyText: '请填活动时间',
					name: 'activetime',
					allowBlank: false,
					blankText: '活动时间不能为空！',
					width: 350
				}, {
					xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '内容',
					alowBlank: false,
					blankText: '备忘录内容不能为空！',
					name: 'content',
					width: 350
				}, {
					xtype: 'textareafield',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '备注',
					emptyText: '可以为空',
					name: 'remark',
					width: 350
				}]
			}],
			buttons: [{
				text: '确定',
				handler: function() {
					var f = w.getComponent(0).getForm();
					if(f.isValid()) {
						f.submit({
							url: 'control/admin/empdate.do?method=add',
							success: function(form, action) {
								if(action.result.success) {
									Ext.Msg.alert('提示', '备忘录保存成功！');
									Ext.getCmp('empdatepanel').getStore().reload();
									w.close();
								} else {
									Ext.Msg.alert('提示', '备忘录保存失败！');
								}
							}, 
							failure: function(form, action) {
								Ext.Msg.alert('提示', '备忘录保存失败！');
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
	delEmpDate: function() {
		var sm = Ext.getCmp('empdatepanel').getSelectionModel();
		if(sm.hasSelection()) {
			var s = sm.getSelection();
			var ids = [];
			for(var i=0; i<s.length; i++) {
				ids.push(s[i].get('id'));
			}
			if(ids.length == 0) {
				Ext.MessageBox.show({
					title: '提示',
					msg: '没有需要删除的备忘录！',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.YES
				});
				return;
			}
			Ext.Ajax.request({
				url: 'control/admin/empdate.do?method=del',
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
						Ext.getCmp('empdatepanel').getStore().reload();
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

function onLoadEmpDate() {
	var sm = Ext.getCmp('empdatepanel').getSelectionModel();
	var record = sm.getSelection();
	var w = new Ext.Window({
		title: '员工备忘录',
		width: 700,
		border: false,
		modal: true,
		resizable: false,
		animateTarget: 'empdatepanel',
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
					fieldLabel: '记录者',
					name: 'logger'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '记录时间',
					name: 'logtime'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '活动时间',
					name: 'activetime'
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
					fieldLabel: '备注',
					name: 'remark'
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