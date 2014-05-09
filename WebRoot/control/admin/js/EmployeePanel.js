Ext.define('EmployeePanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'emppanel',
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
            fields:['userid', 'username', 'email', 'usertype', 'regtime', 'note', 'group', 'authority'],
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/employee.do?method=list',  //请求的服务器地址
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'userid',
            	direction: 'ASC'
            }]
        });
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "用户编号", dataIndex: 'userid', sortable: true},
            {header: "用户名称", dataIndex: 'username', sortable: true},
            {header: "电子邮件", dataIndex: 'email', sortable: true},
            {header: "用户类型", dataIndex: 'usertype', sortable: true,
            	renderer: function(value) {
            		if(value  == 2) {
            			return "<font color=red>超级管理员</font>";
            		} else if(value == 1) {
            			return "<font color=green>公司职员</font>";
            		} else {
            			return "";
            		}
            	}
            },
            {header: "注册时间", dataIndex: 'regtime', sortable: true},
            {header: "所属权限组", dataIndex: 'group', sortable: true}
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
			text: '添加新员工',
			id: 'mea',
			iconCls: 'add',
			handler: addEmp
		}, {
			text: '修改用户权限',
			id: 'mee',
			iconCls: 'edit',
			handler: editEmp
		}, {
			text: '删除员工',
			id: 'med',
			iconCls: 'del',
			handler: delEmp
		}, '->', {
        	fieldLabel: '姓名搜索',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '请输入员工姓名',
        	store: this.store
        }];
		
		this.addListener('itemdblclick', click, this);
		this.addListener('itemcontextmenu', function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		}, this);
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
		afterrender: {
    		fn: function() {
    			Ext.Ajax.request({
					url: 'control/admin/check/employee.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mea').disabled = !r.emp;
						Ext.getCmp('mee').disabled = !r.emp;
						Ext.getCmp('med').disabled = !r.emp;
					}
				});
    		}
    	}
	}
});

function click(grid, record) {
	var uid = record.get('userid');
	var w = new Ext.Window({
		title: '员工信息',
		width: 600,
		modal: true,
		resizable: false,
		animateTarget: 'emppanel',
		items: [new EmpInfo()],
		buttonAlign: 'center',
		border: false,
		buttons: [{
			text: '关闭',
			handler: function() {
				w.close();
			}
		}]
	});
	w.getComponent(0).getForm().load({
		url: 'control/admin/employeeinfo.do?uid=' + uid,
		method: 'POST',
		autoLoad: true,
		success: function(form, action) {
			//Ext.Msg.alert('提示', '加载成功！');
		},
		failure: function(form, action) {
			Ext.Msg.alert('提示', '加载失败！');
		}
	});
	w.show();
}

function addEmp() {
	var w = new Ext.Window({
		width: 350,
		modal: true,
		title: '添加新员工',
		layout: 'fit',
		buttonAlign: 'center',
		border: false,
		resizable: false,
		animateTarget: 'mea',
		items: [{
			xtype: 'form',
			frame: true,
			id: 'empform',
			items: [{
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '员工姓名',
				id: 'empname',
				allowBlank: false,
				emptyText: '请填写员工姓名',
				blankText: '员工姓名不能为空！',
				width: 300
			}, {
				xtype: 'textfield',
				vtype: 'email',
				vtypeText: '不是有效的电子邮箱格式',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '电子邮箱',
				id: 'empmail',
				allowBlank: false,
				emptyText: '请填写员工邮箱',
				blankText: '电子邮箱不能为空！',
				width: 300
			}]
		}],
		buttons: [{
			text: '保存',
			handler: function() {
				var _empname = Ext.util.Format.trim(Ext.getCmp('empname').getValue());
				var _empmail = Ext.getCmp('empmail').getValue();
				if(!Ext.getCmp('empform').isValid() || _empname == "") {
                    Ext.MessageBox.show({
                    	title: '保存',
                    	msg: '输入有误，请重新输入!',
                    	icon: Ext.MessageBox.WARNING,
                    	buttons: Ext.MessageBox.OK
                    });
                } else {
					Ext.Ajax.request({
                        url: 'control/admin/employee.do?method=add',
                        method: 'POST',
                        success: function (response, opts) {
                        	var res = Ext.JSON.decode(response.responseText);
                        	if(res.success) {
	                            Ext.MessageBox.show({
	                            	title: '保存',
	                            	msg: '员工添加成功!',
	                            	icon: Ext.MessageBox.INFO,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                            w.close();
	                            Ext.getCmp('emppanel').getStore().reload();
                        	} else {
                        		Ext.MessageBox.show({
	                            	title: '保存',
	                            	msg: res.msg,
	                            	icon: Ext.MessageBox.ERROR,
	                            	buttons: Ext.MessageBox.OK
                            	});
                        	}
                        },
                        failure: function (response, opts) {
                        	var res = Ext.JSON.decode(response.responseText);
                            Ext.MessageBox.show({
                            	title: '保存',
                            	msg: res.msg,
                            	icon: Ext.MessageBox.ERROR,
                            	buttons: Ext.MessageBox.OK
                            });
                        },
                        params: {
                            empname: _empname,
                            empmail: _empmail
                        }
                    });
                }
			}
		}, {
			text: '重置',
			handler: function() {
				Ext.getCmp('empform').form.reset();
			}
		}]
	});
	w.show();
}

function delEmp() {
	var sm = Ext.getCmp('emppanel').getSelectionModel();
	if(sm.hasSelection()) {
		Ext.MessageBox.confirm('提示', '您确定要删除所选记录吗？', function(btn) {
			if(btn == 'yes') {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					ids.push(s[i].get('userid'));
				}
				if(ids.length == 0) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '请选择一条记录！',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/employee.do?method=del',
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
							Ext.getCmp('emppanel').getStore().reload();
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
}

function editEmp() {
	var sm = Ext.getCmp('emppanel').getSelectionModel();
	if(sm.hasSelection()) {
		var w = new Ext.Window({
			title: '修改用户类型',
			//width: 270,
			modal: true,
			buttonAlign: 'center',
			border: false,
			items: [{
				xtype: 'form',
				layout: 'column',
				frame: true,
				items: [/*{
					xtype: 'combo',
					name: 'utype',
					id: 'utype',
					editable: false,
					labelAlign: 'right',
					labelWidth: 70,
					emptyText: '请选择用户类型',
					fieldLabel: '用户类型',
					allowBlank: false,
					blankText: '用户类型不能为空！',
					valueField: 'type',
					displayField: 'value',
					triggerAction: 'all',
					//mode: 'remote',
					store: new Ext.data.Store({
						autoLoad: true,
						fields: ['value', 'type'],
						proxy: {
							type: 'ajax',
							url: 'control/admin/employee.do?method=listutype',
							reader: {
								type: 'json',
								root: 'records'
							}
						}
					})
				}, */{
					xtype: 'combo',
					name: 'group',
					editable: false,
					labelAlign: 'right',
					labelWidth: 70,
					emptyText: '请选择用户所在组',
					fieldLabel: '用户权限',
					allowBlank: false,
					blankText: '用户权限不能为空！',
					valueField: 'id',
					displayField: 'name',
					triggerAction: 'all',
					//mode: 'remote',
					store: new Ext.data.Store({
						autoLoad: true,
						fields: ['id', 'name'],
						proxy: {
							type: 'ajax',
							url: 'control/admin/permissiongroup.do?method=list',
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
						var s = sm.getSelection();
						var ids = [];
						for(var i=0; i<s.length; i++) {
							ids.push(s[i].get('userid'));
						}
						f.submit({
							url: 'control/admin/employee.do?method=setgroup',
							params: {
								ids: ids
							},
							success: function(form, action) {
								if(action.result.success) {
									Ext.MessageBox.show({
										title: '提示',
										msg: '修改成功！',
										icon: Ext.MessageBox.INFO,
										buttons: Ext.MessageBox.YES
									});
									Ext.getCmp('emppanel').getStore().reload();
									w.close();
								} else {
									Ext.MessageBox.show({
										title: '提示',
										msg: '修改失败！',
										icon: Ext.MessageBox.WARNING,
										buttons: Ext.MessageBox.YES
									});
								}
							},
							failure: function(form, action) {
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
			}]
		});
		w.show();
	} else {
		Ext.MessageBox.show({
			title: '提示',
			msg: '请选择需要修改的记录！',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	}
}

