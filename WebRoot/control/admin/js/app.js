Ext.onReady(function() {
	
	Ext.Ajax.on('requestcomplete', function(conn, resp, opt) {
		try {
			var v = Ext.JSON.decode(resp.responseText, true);
			if(v.sessionStatus == 'timeout') {
				Ext.MessageBox.show({
					title: 'ϵͳ��ʾ',
					msg: '������ʱ�������µ��룡',
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
			text: 'չ��ȫ��',
			handler: expandAll
		}, '-', {
			text: '����ȫ��',
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
		if(title == '�鼮������') {
			//return new BookTypePanel({parent: 'root'});
			return new BookTypeTreePanel();
			//return new BookTypeTree();
		} else if(title == '���������') {
			return new PressPanel();
		} else if(title == '�鼮����') {
			return new BookPanel();
		} else if(title == '��ɾ���鼮����') {
			return new DelBookPanel();
		} else if(title == '�û�����') {
			return new UserPanel();
		} else if(title == '�ѽ����û�����') {
			return new DelUserPanel();
		} else if(title == '�˵�����') {
			return new MenuSortPanel();
		} else if(title == '������') {
			return new PicPanel();
		} else if(title == '�ȴ���˶���') {
			return new WaitConfirmOrderPanel();
		} else if(title == '��ȡ������') {
			return new CancelOrderPanel();
		} else if(title == '�ȴ������') {
			return new WaitPaymentOrderPanel();
		} else if(title == '�ȴ��������') {
			return new WaitProductOrderPanel();
		} else if(title == '�ȴ���������') {
			return new WaitDeliverOrderPanel();
		} else if(title == '�ѷ�������') {
			return new DeliveredOrderPanel();
		} else if(title == '���ջ�����') {
			return new ReceivedOrderPanel();
		} else if(title == '������ѯ') {
			//return new OrderSearchPanel();
			return new OrderSearchView();
		} else if(title == '��ְԱ������') {
			return new EmployeePanel();
		} else if(title == '��ְԱ������') {
			return new DelEmployeePanel();
		} else if(title == 'Ա����ѯ') {
			return new EmpSearchView();
		} else if(title == '���۹���') {
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
				text: '��ӭ����',
				iconCls: 'user',
				handler: showinfo
			}, '->', {
				text: '�˳�ϵͳ',
				iconCls: 'exit',
				handler: exit
			}]
		}), {
			region: 'west',
			title: 'ϵͳ���ܲ˵�',
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
				title: '��ҳ',
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
				     title:'ϵͳ��Ϣ',
				     msg: '��ǰ�����û�Ϊ��' + user.name,
				     buttons: Ext.Msg.YES,
				     icon: Ext.Msg.INFO
				});*/
              },
              failure: function (response, opts) {
              	Ext.Msg.alert("��ʾ", "ϵͳ����");
              }
		});
	}
	showUser();
	
	function exit(){
		Ext.MessageBox.confirm('�˳�', '��ȷ��Ҫ�˳�ϵͳ��', function(btn){
			if(btn == 'yes') {
				Ext.Ajax.request({
	                   url: 'control/login/logout.do',
	                      method: 'POST',
	                      success: function (response, opts) {
	                      	window.location.href = "control/login";
	                      },
	                      failure: function (response, opts) {
	                      	Ext.Msg.alert("��ʾ", "�˳�����");
	                      }
				});
			}
		});
	}
});

function showinfo() {
	var uname = Ext.getCmp('sysname').getText();
	var w = new Ext.Window({
		title: 'Ա����Ϣ',
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
					fieldLabel: 'Ա������',
					name: 'username'
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '��������',
					name: 'email'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: 'Ա������',
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
					fieldLabel: '�̶��绰',
					name: 'phone'
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '�ƶ��绰',
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
					fieldLabel: '��ϵ��ַ',
					name: 'address'
				}, {
					xtype: 'textfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '��������',
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
					fieldLabel: 'ע��ʱ��',
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
					fieldLabel: '����˵��',
					name: 'note'
				}]
			}]
		})],
		buttonAlign: 'center',
		border: false,
		buttons: [{
			text: '����',
			handler: function() {
				var f = w.getComponent(0).getForm();
				if(f.isValid()) {
					Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫ�����û���Ϣ��', function(btn) {
						if(btn == 'yes') {
							f.submit({
								url: 'control/admin/employee.do?method=update',
								success: function(form, action) {
									if(action.result.success) {
										Ext.Msg.alert('��ʾ', '�û���Ϣ���³ɹ���');
										w.close();
									} else {
										Ext.Msg.alert('��ʾ', '�û���Ϣ����ʧ�ܣ�');
									}
								},
								failure: function(form, action) {
									Ext.Msg.alert('��ʾ', '�û���Ϣ����ʧ�ܣ�');
								}
							});
						}
					});
				}
			}
		}, {
			text: '��������',
			handler: function() {
				var w = new Ext.Window({
					title: '��������',
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
							fieldLabel: 'ԭʼ����',
							name: 'pass',
							allowBlank: false,
							emptyText: '����дԭʼ����',
							blankText: 'ԭʼ���벻��Ϊ�գ�'
						}, {
							xtype: 'textfield',
							inputType: 'password',
							labelAlign: 'right',
							labelWidth: 70,
							fieldLabel: '������',
							name: 'newpass',
							allowBlank: false,
							emptyText: '����д������',
							blankText: '�����벻��Ϊ�գ�'
						}]
					}],
					buttons: [{
						text: 'ȷ��',
						handler: function() {
							var f = w.getComponent(0).getForm();
							if(f.isValid()) {
								f.submit({
									url: 'control/admin/employee.do?method=resetpass',
									success: function(form, action) {
										if(action.result.success) {
											Ext.Msg.alert('��ʾ', '�޸�����ɹ���');
											w.close();
										} else {
											Ext.Msg.alert('��ʾ', '�޸�����ʧ�ܣ�');
											f.reset();
										}
									}, 
									failure: function(form, action) {
										Ext.Msg.alert('��ʾ', '�޸�����ʧ�ܣ�');
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
			//Ext.Msg.alert('��ʾ', '���سɹ���');
		},
		failure: function(form, action) {
			Ext.Msg.alert('��ʾ', '����ʧ�ܣ�');
		},
		params: {
			uname: uname
		}
	});
	w.show();
}

function contactinfo(uname) {
	var w = new Ext.Window({
		title: '��ϵ��ʽ',
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
				fieldLabel: '�̶��绰',
				name: 'phone',
				width: 400
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '�ƶ��绰',
				name: 'mobile',
				allowBlank: false,
				emptyText: '����д�ֻ�����',
				blankText: '�ֻ����벻��Ϊ�գ�',
				width: 400
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '��ϵ��ַ',
				name: 'address',
				allowBlank: false,
				emptyText: '����д��ϵ��ַ',
				blankText: '��ϵ��ַ����Ϊ�գ�',
				width: 400
			}, {
				xtype: 'textfield',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '��������',
				name: 'postcode',
				allowBlank: false,
				emptyText: '����д��������',
				blankText: '�������벻��Ϊ�գ�',
				width: 400
			}]
		}],
		buttons: [{
			text: 'ȷ��',
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
	                            	title: '��ʾ',
	                            	msg: '��ϵ��ʽ��ӳɹ���',
	                            	icon: Ext.MessageBox.INFO,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                            w.close();
							} else {
								Ext.MessageBox.show({
	                            	title: '��ʾ',
	                            	msg: '��ϵ��ʽ���ʧ�ܣ�',
	                            	icon: Ext.MessageBox.WARNING,
	                            	buttons: Ext.MessageBox.OK
	                            });
							}
						}, 
						failure: function(form, action) {
							Ext.MessageBox.show({
                            	title: '��ʾ',
                            	msg: '��ϵ��ʽ���ʧ�ܣ�',
                            	icon: Ext.MessageBox.ERROR,
                            	buttons: Ext.MessageBox.OK
                            });
						}
					});
				} else {
					Ext.Msg.alert('��ʾ', '����ȷ��д��Ϣ��');
				}
			}
		}]
	});
	w.show();
}