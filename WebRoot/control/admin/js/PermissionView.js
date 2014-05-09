Ext.define('PermissionView', {
	extend: 'Ext.grid.Panel',
	id: 'permissionview',
	//layout: 'border',
	pageSize: 25,
	border: false,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		
		this.tbar = [{
			text: '添加权限组',
			id: 'mgpga',
			iconCls: 'add',
			handler: addPermission
		}, {
			text: '删除权限组',
			id: 'mpgd',
			iconCls: 'del',
			handler: delPermission
		}];
		
		this.columns = [
			new Ext.grid.RowNumberer({header: '行号', width: 30}),
			{header: "权限组编号", dataIndex: 'id', sortable: true, hidden: true},
            {header: "权限组名称", dataIndex: 'name', sortable: true},
            {header: "创建时间", dataIndex: 'createdate', sortable: true},
            {header: "创建人", dataIndex: 'creater', sortable: true},
            {header: "备注", dataIndex: 'remark', sortable: false}
		];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		
		this.store = Ext.create('Ext.data.Store', {
            autoLoad: {
            	start: 0, 
            	limit: this.pageSize
            },
            fields:['id', 'name', 'createdate', 'creater', 'remark', 'authority'],
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/permissiongroup.do?method=list',
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
        
        this.listeners = {
        	'itemcontextmenu': function(view, record, item, index, e, opt) {
				e.preventDefault();
				e.stopEvent();
			},
        	itemdblclick: {fn: loadPermission},
        	afterrender: {
        		fn: function() {
        			Ext.Ajax.request({
						url: 'control/admin/check/permission.do',
						async: false,
						method: 'POST',
						success: function(response, opt) {
							var r = Ext.JSON.decode(response.responseText);
							Ext.getCmp('mgpga').disabled = !r.permission;
							Ext.getCmp('mpgd').disabled = !r.permission;
						}
					});
        		}
        	}
        };
        
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
	}
});

function loadPermission() {
	var w = new Ext.Window({
		title: '权限查看',
		width: 350,
		border: false,
		modal: true,
		resizable: false,
		animateTarget: 'permissionview',
		items: [{
			xtype: 'form',
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelWidth: 80,
				fieldLabel: '权限列表',
				name: 'authority'
			}]
		}]
	});
	var r = Ext.getCmp('permissionview').getSelectionModel().getSelection();
	w.getComponent(0).getForm().loadRecord(r[0]);
	w.show();
}

function addPermission() {
	var w = new Ext.Window({
		title: '添加权限组',
		modal: true,
		resizable: false,
		width: 400,
		border: false,
		animateTarget: 'mgpga',
		items: [{
			xtype: 'form',
			frame: true,
			items: [{
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 80,
				fieldLabel: '权限组名称',
				name: 'permissionName',
				allowBlank: false,
				emptyText: '请填写权限组名称',
				blankText: '权限组名称不能为空！',
				width: 350
			}, {
				xtype: 'fieldset',
				id: 'pmfieldset',
				title: '权限列表',
				collapsible: true,
				defaultType: 'checkbox',
				layout: 'column'
			}, {
				xtype: 'textareafield',
				name: 'remark',
				width: 380,
				labelAlign: 'right',
				labelWidth: 40,
				fieldLabel: '备注'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '确定',
			handler: function() {
				var f = w.getComponent(0).getForm();
				if(f.isValid()) {
					f.submit({
						url: 'control/admin/permissiongroup.do?method=add',
						success: function(form, action) {
							if(action.result.success) {
								w.close();
								Ext.getCmp('permissionview').getStore().reload();
								Ext.Msg.alert('提示', '保存成功！');
							} else {
								Ext.Msg.alert('提示', '保存失败！');
							}
						},
						failure: function(form, action) {
							Ext.Msg.alert('提示', '保存失败！');
						}
					});
				}
			}
		}, {
			text: '取消',
			handler: function() {
				w.close();
			}
		}],
		listeners: {
			afterrender: {
				fn: function() {
					Ext.Ajax.request({
				        url: 'control/admin/permissiongroup.do?method=listpermission',
				        method: 'POST',
				        async: false,
				        success: function (response, opts) {
				        	var res = Ext.JSON.decode(response.responseText);
				        	var fieldset = Ext.getCmp('pmfieldset');
				        	for(var i=0; i<res.count; i++) {
								var id = res.records[i].index;
								var name = res.records[i].name;
								var cb = new Ext.form.Checkbox({
									name: id,
									boxLabel: name + '&nbsp;&nbsp;',
									labelAlign: 'right'
								});
								fieldset.add(cb);
				        	}
				        },
				        failure: function (response, opts) {
				        }
				    });
				}
			}
		}
	});
	w.show();
}

function delPermission() {
	var sm = Ext.getCmp('permissionview').getSelectionModel();
	if(sm.hasSelection() == false) {
		Ext.MessageBox.show({
			title: '提示',
			msg: '请选择需要删除的权限！',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	} else {
		Ext.MessageBox.confirm('提示', '您确定要删除所选权限吗？', function(btn) {
			if(btn == 'yes') {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					ids.push(s[i].get('id'));
				}
				
				Ext.Ajax.request({
					url: 'control/admin/permissiongroup.do?method=del',
					params: {gid: ids},
					success: function(resp, opt) {
						var res = Ext.JSON.decode(resp.responseText)
						if(res.success) {
							Ext.Msg.alert('提示', '删除成功！');
							Ext.getCmp('permissionview').getStore().reload();
						} else {
							Ext.Msg.alert('提示', '删除失败！');
						}
					},
					failure: function(resp, opt) {
						Ext.Msg.alert('提示', '删除失败！');
					}
				});
			}
		});
	}
}