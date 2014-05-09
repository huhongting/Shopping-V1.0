Ext.onReady(function() {
	
	Ext.Ajax.on('requestcomplete', function(conn, resp, opt) {
		try {
			var v = Ext.JSON.decode(resp.responseText, true);
			if(v.sessionStatus == 'timeout') {
				Ext.MessageBox.show({
					title: '系统提示',
					msg: '操作超时，请重新登入！',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.YES,
					fn: function() {
						window.location.href = 'control/login';
					}
				});
			}
		} catch(e) {
			
		}
		
	});
	
	var treeMenu = new Ext.tree.Panel({
		id: 'menu',
		border: false,
		rootVisible: false,
		animate: true,
		store: new Ext.data.TreeStore({
			root: {
				id: 'root',
				expanded: true
			},
			proxy: {
				type: 'ajax',
				url: 'control/admin/view.do?method=menu'
			}
		}),
		tbar: ['->', {
			text: '展开全部',
			handler: expandAll
		}, '-', {
			text: '收起全部',
			handler: closeAll
		}],
		listeners: {
			'itemcontextmenu': function(view, record, item, index, e, opt) {
				e.preventDefault();
				e.stopEvent();
			}
		}
	});
	function expandAll() { treeMenu.expandAll(); }
	function closeAll() { treeMenu.collapseAll(); }
	
	function addTabItem(id, title/*, icon, type, url*/) {
		Ext.getCmp('tabview').add({
			id: id,
			title: title,
			layout: 'fit',
			closable: true,
			closeAction: 'hide',
			items: createItem(title)
		}).show();
	}
	
	function createItem(title) {
		if(title == '书籍类别管理') {
			//return new BookTypePanel({parent: 'root'});
			return new BookTypeTreePanel();
			//return new BookTypeTree();
		} else if(title == '出版社管理') {
			return new PressPanel();
		} else if(title == '书籍管理') {
			return new BookPanel();
		} else if(title == '已删除书籍管理') {
			return new DelBookPanel();
		} else if(title == '用户管理') {
			return new UserPanel();
		} else if(title == '已禁用用户管理') {
			return new DelUserPanel();
		} else if(title == '菜单排序') {
			return new MenuSortPanel();
		} else if(title == '广告管理') {
			return new PicPanel();
		} else if(title == '等待审核订单') {
			return new WaitConfirmOrderPanel();
		} else if(title == '已取消订单') {
			return new CancelOrderPanel();
		} else if(title == '等待付款订单') {
			return new WaitPaymentOrderPanel();
		} else if(title == '等待配货订单') {
			return new WaitProductOrderPanel();
		} else if(title == '等待发货订单') {
			return new WaitDeliverOrderPanel();
		} else if(title == '已发货订单') {
			return new DeliveredOrderPanel();
		} else if(title == '已收货订单') {
			return new ReceivedOrderPanel();
		} else if(title == '订单查询') {
			//return new OrderSearchPanel();
			return new OrderSearchView();
		} else if(title == '在职员工管理') {
			return new EmployeePanel();
		} else if(title == '离职员工管理') {
			return new DelEmployeePanel();
		} else if(title == '员工查询') {
			return new EmpSearchView();
		} else if(title == '评论管理') {
			return new CommentPanel();
		} else {
			return new PermissionView();
		}
	}
	
	treeMenu.addListener('itemclick', function(item, record) {
		var node = record.raw;
		if(node.leaf) {
			var view = Ext.getCmp('tabview');
			var tab = view.getComponent(node.text);
			/*if(tab) {
				//view.remove(tab);
				view.setActiveTab(tab);
			} else {
				addTabItem(node.text, node.text);
			}*/
			if(tab) {
				view.remove(tab);
			}
			addTabItem(node.text, node.text);
		}
	});
	
	new Ext.Viewport({
		layout: 'border',
		items: [Ext.create('Ext.Panel', {
			region: 'north',
			frame: true,
			//border: false,
			margins: '0 0 5 0',
			height: 100,
			loader: {
				url: 'control/admin/head.jsp',
				autoLoad: true
			},
			bbar: [{
				//xtype: 'button',
				id: 'sysname',
				text: '欢迎登入',
				iconCls: 'user',
				handler: showinfo
			}, '->', {
				text: '退出系统',
				iconCls: 'exit',
				handler: exit
			}]
		}), {
			region: 'west',
			title: '系统功能菜单',
			width: 180,
			iconCls: 'cog',
			maxWidth: 300,
			minWidth: 180,
			split: true,
			collapsible: true,
			animcollapse: true,
			autoScroll: true,
            layout: 'fit',
			items: [treeMenu]
		}, Ext.create('Ext.Component', {
			region: 'south',
			html: '<div class="box" align=center>CopyRight (C) Huht 2014, All Rights Reserved.</div>',
			width: 10
		}), {
			region: 'center',
			xtype: 'tabpanel',
			id: 'tabview',
			animcollapse: true,
			closable: false,
			activeTab: 0,
			//autoScroll: true,
			items: [{
				title: '首页',
				layout: 'fit',
				border: false,
				items: [new IndexView()]
			}]
		}]
	});
	
	function showUser() {
		Ext.Ajax.request({
           url: 'control/admin/view.do?method=user',
              method: 'POST',
              success: function (response, opts) {
              	var user = Ext.JSON.decode(response.responseText);
              	Ext.getCmp('sysname').setText(user.name);
          		if(user.info == false) {
          			contactinfo(user.name);
          		}
              	/*Ext.Msg.show({
				     title:'系统信息',
				     msg: '当前登入用户为：' + user.name,
				     buttons: Ext.Msg.YES,
				     icon: Ext.Msg.INFO
				});*/
              },
              failure: function (response, opts) {
              	Ext.Msg.alert("提示", "系统错误！");
              }
		});
	}
	showUser();
	
	function exit(){
		Ext.MessageBox.confirm('退出', '您确定要退出系统吗？', function(btn){
			if(btn == 'yes') {
				Ext.Ajax.request({
	                   url: 'control/login/logout.do',
	                      method: 'POST',
	                      success: function (response, opts) {
	                      	window.location.href = "control/login";
	                      },
	                      failure: function (response, opts) {
	                      	Ext.Msg.alert("提示", "退出错误！");
	                      }
				});
			}
		});
	}
});

function showinfo() {
	var uname = Ext.getCmp('sysname').getText();
	var w = new Ext.Window({
		title: '员工信息',
		width: 700,
		border: false,
		modal: true,
		resizable: false,
		animateTarget: 'sysname',
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
					xtype: 'hidden',
					name: 'uname',
					value: uname
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '员工姓名',
					name: 'username'
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '电子邮箱',
					name: 'email'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '员工类型',
					name: 'usertype',
					readOnly: true
				}]
			}, {
				//xtype: 'panel',
				border: false,
				layout: 'table',
				columnWidth: 5,
				frame: true,
				items: [{
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '固定电话',
					name: 'phone'
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '移动电话',
					name: 'mobile'
				}]
			}, {
				//xtype: 'panel',
				border: false,
				layout: 'table',
				columnWidth: 5,
				frame: true,
				items: [{
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '联系地址',
					name: 'address'
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '邮政编码',
					name: 'postcode'
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
					fieldLabel: '注册时间',
					name: 'regtime'
				}]
			}, {
				//xtype: 'panel',
				border: false,
				//layout: 'table',
				//columnWidth: 5,
				frame: true,
				items: [{
					xtype: 'textareafield',
					labelAlign: 'right',
					labelWidth: 85,
					width: 560,
					fieldLabel: '其他说明',
					name: 'note'
				}]
			}]
		})],
		buttonAlign: 'center',
		border: false,
		buttons: [{
			text: '更新',
			handler: function() {
				var f = w.getComponent(0).getForm();
				if(f.isValid()) {
					Ext.MessageBox.confirm('提示', '确定要更新用户信息？', function(btn) {
						if(btn == 'yes') {
							f.submit({
								url: 'control/admin/employee.do?method=update',
								success: function(form, action) {
									if(action.result.success) {
										Ext.Msg.alert('提示', '用户信息更新成功！');
										w.close();
									} else {
										Ext.Msg.alert('提示', '用户信息更新失败！');
									}
								},
								failure: function(form, action) {
									Ext.Msg.alert('提示', '用户信息更新失败！');
								}
							});
						}
					});
				}
			}
		}, {
			text: '更改密码',
			handler: function() {
				var w = new Ext.Window({
					title: '更改密码',
					width: 280,
					modal: true,
					buttonAlign: 'center',
					border: false,
					items: [{
						xtype: 'form',
						frame: true,
						items: [{
							xtype: 'hidden',
							name: 'uname',
							value: uname
						}, {
							xtype: 'textfield',
							inputType: 'password',
							labelAlign: 'right',
							labelWidth: 70,
							fieldLabel: '原始密码',
							name: 'pass',
							allowBlank: false,
							emptyText: '请填写原始密码',
							blankText: '原始密码不能为空！'
						}, {
							xtype: 'textfield',
							inputType: 'password',
							labelAlign: 'right',
							labelWidth: 70,
							fieldLabel: '新密码',
							name: 'newpass',
							allowBlank: false,
							emptyText: '请填写新密码',
							blankText: '新密码不能为空！'
						}]
					}],
					buttons: [{
						text: '确定',
						handler: function() {
							var f = w.getComponent(0).getForm();
							if(f.isValid()) {
								f.submit({
									url: 'control/admin/employee.do?method=resetpass',
									success: function(form, action) {
										if(action.result.success) {
											Ext.Msg.alert('提示', '修改密码成功！');
											w.close();
										} else {
											Ext.Msg.alert('提示', '修改密码失败！');
											f.reset();
										}
									}, 
									failure: function(form, action) {
										Ext.Msg.alert('提示', '修改密码失败！');
										f.reset();
									}
								});
							}
						}
					}]
				});
				w.show();
			}
		}]
	});
	w.getComponent(0).getForm().load({
		url: 'control/admin/employeeinfo.do?tag=name',
		method: 'POST',
		autoLoad: true,
		success: function(form, action) {
			//Ext.Msg.alert('提示', '加载成功！');
		},
		failure: function(form, action) {
			Ext.Msg.alert('提示', '加载失败！');
		},
		params: {
			uname: uname
		}
	});
	w.show();
}

function contactinfo(uname) {
	var w = new Ext.Window({
		title: '联系方式',
		width: 450,
		modal: true,
		resizable: false,
		closable: false,
		border: false,
		buttonAlign: 'center',
		items: [{
			xtype: 'form',
			frame: true,
			items: [{
				xtype: 'hidden',
				name: 'uname',
				value: uname
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '固定电话',
				name: 'phone',
				width: 400
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '移动电话',
				name: 'mobile',
				allowBlank: false,
				emptyText: '请填写手机号码',
				blankText: '手机号码不能为空！',
				width: 400
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '联系地址',
				name: 'address',
				allowBlank: false,
				emptyText: '请填写联系地址',
				blankText: '联系地址不能为空！',
				width: 400
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '邮政编码',
				name: 'postcode',
				allowBlank: false,
				emptyText: '请填写邮政编码',
				blankText: '邮政编码不能为空！',
				width: 400
			}]
		}],
		buttons: [{
			text: '确定',
			handler: function() {
				var conform = w.getComponent(0).getForm();
				if(conform.isValid()) {
					conform.submit({
						//clientValidation: true,
						url: 'control/admin/empcontact.do',
						method: 'POST',
						success: function(form, action) {
							if(action.result.success) {
								Ext.MessageBox.show({
	                            	title: '提示',
	                            	msg: '联系方式添加成功！',
	                            	icon: Ext.MessageBox.INFO,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                            w.close();
							} else {
								Ext.MessageBox.show({
	                            	title: '提示',
	                            	msg: '联系方式添加失败！',
	                            	icon: Ext.MessageBox.WARNING,
	                            	buttons: Ext.MessageBox.OK
	                            });
							}
						}, 
						failure: function(form, action) {
							Ext.MessageBox.show({
                            	title: '提示',
                            	msg: '联系方式添加失败！',
                            	icon: Ext.MessageBox.ERROR,
                            	buttons: Ext.MessageBox.OK
                            });
						}
					});
				} else {
					Ext.Msg.alert('提示', '请正确填写信息！');
				}
			}
		}]
	});
	w.show();
}