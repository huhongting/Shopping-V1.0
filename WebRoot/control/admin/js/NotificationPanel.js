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
			text: '����֪ͨ',
			iconCls: 'add',
			id: 'mna',
			handler: this.addNotification
		}, {
			text: 'ɾ��֪ͨ',
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
            pageSize: this.pageSize, //���÷�ҳ��С
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
			new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "֪ͨ���", dataIndex: 'id', sortable: true},
            {header: "֪ͨ����", dataIndex: 'title', flex: 2, sortable: true},
            {header: "֪ͨ����", dataIndex: 'content', flex: 4, sortable: false},
            {header: "������", dataIndex: 'creater', sortable: true},
            {header: "����ʱ��", dataIndex: 'createdate', flex: 1, sortable: true},
            {header: "֪ͨ״̬", dataIndex: 'status', sortable: true,
            	renderer: function(value) {
            		if(value == 'IMPORTANT') return '<font color=red><b>��Ҫ</b></font>';
            		return '��ͨ';
            	}
            }
		];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
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
			title: '���֪ͨ',
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
					fieldLabel: '����',
					name: 'title',
					allowBlank: false,
					emptyText: '����д֪ͨ����',
					blankText: '֪ͨ���ⲻ��Ϊ�գ�',
					width: 350
				}, {
					xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '����',
					alowBlank: false,
					blankText: '֪ͨ���ݲ���Ϊ�գ�',
					name: 'content',
					width: 350
				}, {
					xtype: 'combo',
					name: 'status',
					width: 350,
					editable: false,
					labelAlign: 'right',
					labelWidth: 70,
					emptyText: '��ѡ��֪ͨ״̬',
					fieldLabel: '֪ͨ״̬',
					allowBlank: false,
					blankText: '֪ͨ״̬����Ϊ�գ�',
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
				text: 'ȷ��',
				handler: function() {
					var f = w.getComponent(0).getForm();
					if(f.isValid()) {
						f.submit({
							url: 'control/admin/notification.do?method=add',
							success: function(form, action) {
								if(action.result.success) {
									Ext.Msg.alert('��ʾ', '֪ͨ����ɹ���');
									Ext.getCmp('notifypanel').getStore().reload();
									w.close();
								} else {
									Ext.Msg.alert('��ʾ', '֪ͨ����ʧ�ܣ�');
								}
							}, 
							failure: function(form, action) {
								Ext.Msg.alert('��ʾ', '֪ͨ����ʧ�ܣ�');
							}
						})
					}
				}
			}, {
				text: '����',
				handler: function() {
					w.getComponent(0).getForm().reset();
				}
			}, {
				text: 'ȡ��',
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
					title: '��ʾ',
					msg: 'û����Ҫɾ����֪ͨ��',
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
							title: '��ʾ',
							msg: 'ɾ���ɹ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						Ext.getCmp('notifypanel').getStore().reload();
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
	}
});

function onLoadNotify() {
	var sm = Ext.getCmp('notifypanel').getSelectionModel();
	var record = sm.getSelection();
	var w = new Ext.Window({
		title: 'ϵͳ֪ͨ',
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
					fieldLabel: '������',
					name: 'creater'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '����ʱ��',
					name: 'createdate'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '֪ͨ״̬',
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
					fieldLabel: '����',
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
					fieldLabel: '����',
					name: 'content'
				}]
			}]
		})],
		buttonAlign: 'center',
		border: false,
		buttons: [{
			text: '�ر�',
			handler: function() {
				w.close();
			}
		}]
	});
	w.getComponent(0).getForm().loadRecord(record[0]);
	w.show();
}