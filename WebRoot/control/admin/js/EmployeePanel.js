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
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/employee.do?method=list',  //����ķ�������ַ
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
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "�û����", dataIndex: 'userid', sortable: true},
            {header: "�û�����", dataIndex: 'username', sortable: true},
            {header: "�����ʼ�", dataIndex: 'email', sortable: true},
            {header: "�û�����", dataIndex: 'usertype', sortable: true,
            	renderer: function(value) {
            		if(value  == 2) {
            			return "<font color=red>��������Ա</font>";
            		} else if(value == 1) {
            			return "<font color=green>��˾ְԱ</font>";
            		} else {
            			return "";
            		}
            	}
            },
            {header: "ע��ʱ��", dataIndex: 'regtime', sortable: true},
            {header: "����Ȩ����", dataIndex: 'group', sortable: true}
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
			text: '�����Ա��',
			id: 'mea',
			iconCls: 'add',
			handler: addEmp
		}, {
			text: '�޸��û�Ȩ��',
			id: 'mee',
			iconCls: 'edit',
			handler: editEmp
		}, {
			text: 'ɾ��Ա��',
			id: 'med',
			iconCls: 'del',
			handler: delEmp
		}, '->', {
        	fieldLabel: '��������',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '������Ա������',
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
		title: 'Ա����Ϣ',
		width: 600,
		modal: true,
		resizable: false,
		animateTarget: 'emppanel',
		items: [new EmpInfo()],
		buttonAlign: 'center',
		border: false,
		buttons: [{
			text: '�ر�',
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
			//Ext.Msg.alert('��ʾ', '���سɹ���');
		},
		failure: function(form, action) {
			Ext.Msg.alert('��ʾ', '����ʧ�ܣ�');
		}
	});
	w.show();
}

function addEmp() {
	var w = new Ext.Window({
		width: 350,
		modal: true,
		title: '�����Ա��',
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
				fieldLabel: 'Ա������',
				id: 'empname',
				allowBlank: false,
				emptyText: '����дԱ������',
				blankText: 'Ա����������Ϊ�գ�',
				width: 300
			}, {
				xtype: 'textfield',
				vtype: 'email',
				vtypeText: '������Ч�ĵ��������ʽ',
				labelAlign: 'right',
				labelWidth: 70,
				fieldLabel: '��������',
				id: 'empmail',
				allowBlank: false,
				emptyText: '����дԱ������',
				blankText: '�������䲻��Ϊ�գ�',
				width: 300
			}]
		}],
		buttons: [{
			text: '����',
			handler: function() {
				var _empname = Ext.util.Format.trim(Ext.getCmp('empname').getValue());
				var _empmail = Ext.getCmp('empmail').getValue();
				if(!Ext.getCmp('empform').isValid() || _empname == "") {
                    Ext.MessageBox.show({
                    	title: '����',
                    	msg: '������������������!',
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
	                            	title: '����',
	                            	msg: 'Ա����ӳɹ�!',
	                            	icon: Ext.MessageBox.INFO,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                            w.close();
	                            Ext.getCmp('emppanel').getStore().reload();
                        	} else {
                        		Ext.MessageBox.show({
	                            	title: '����',
	                            	msg: res.msg,
	                            	icon: Ext.MessageBox.ERROR,
	                            	buttons: Ext.MessageBox.OK
                            	});
                        	}
                        },
                        failure: function (response, opts) {
                        	var res = Ext.JSON.decode(response.responseText);
                            Ext.MessageBox.show({
                            	title: '����',
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
			text: '����',
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
		Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����ѡ��¼��', function(btn) {
			if(btn == 'yes') {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					ids.push(s[i].get('userid'));
				}
				if(ids.length == 0) {
					Ext.MessageBox.show({
						title: '��ʾ',
						msg: '��ѡ��һ����¼��',
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
								title: '��ʾ',
								msg: 'ɾ���ɹ���',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('emppanel').getStore().reload();
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
}

function editEmp() {
	var sm = Ext.getCmp('emppanel').getSelectionModel();
	if(sm.hasSelection()) {
		var w = new Ext.Window({
			title: '�޸��û�����',
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
					emptyText: '��ѡ���û�����',
					fieldLabel: '�û�����',
					allowBlank: false,
					blankText: '�û����Ͳ���Ϊ�գ�',
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
					emptyText: '��ѡ���û�������',
					fieldLabel: '�û�Ȩ��',
					allowBlank: false,
					blankText: '�û�Ȩ�޲���Ϊ�գ�',
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
				text: 'ȷ��',
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
										title: '��ʾ',
										msg: '�޸ĳɹ���',
										icon: Ext.MessageBox.INFO,
										buttons: Ext.MessageBox.YES
									});
									Ext.getCmp('emppanel').getStore().reload();
									w.close();
								} else {
									Ext.MessageBox.show({
										title: '��ʾ',
										msg: '�޸�ʧ�ܣ�',
										icon: Ext.MessageBox.WARNING,
										buttons: Ext.MessageBox.YES
									});
								}
							},
							failure: function(form, action) {
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
			}]
		});
		w.show();
	} else {
		Ext.MessageBox.show({
			title: '��ʾ',
			msg: '��ѡ����Ҫ�޸ĵļ�¼��',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	}
}

