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
			text: '���',
			iconCls: 'add',
			id: 'edadd',
			handler: this.addEmpDate
		}, {
			text: 'ɾ��',
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
            pageSize: this.pageSize, //���÷�ҳ��С
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
			new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "���", dataIndex: 'id', sortable: true},
            {header: "����", dataIndex: 'title', flex: 2, sortable: true},
            {header: "����", dataIndex: 'content', flex: 4, sortable: false},
            {header: "��¼��", dataIndex: 'logger', sortable: true},
            {header: "��¼ʱ��", dataIndex: 'logtime', flex: 2, sortable: true},
            {header: "�ʱ��", dataIndex: 'activetime', flex: 2, sortable: true},
            {header: "��ע", dataIndex: 'remark', flex: 1, sortable: false},
            {header: "״̬", dataIndex: 'activetime', sortable: true,
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
            					return '<font color=red><b>����</b></font>';
            				} else if((parseInt(v[2])-parseInt(a[2])) == 1) {
            					return '<font color=green>����</font>';
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
    	itemdblclick: {fn: onLoadEmpDate}
    },
	addEmpDate: function() {
		var w = new Ext.Window({
			title: '��ӱ���¼',
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
					fieldLabel: '����',
					name: 'title',
					allowBlank: false,
					emptyText: '����д����¼����',
					blankText: '����¼���ⲻ��Ϊ�գ�',
					width: 350
				}, {
					xtype: 'datefield',
					format: 'Y-m-d',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '�����',
					emptyText: '��������',
					name: 'activedate',
					allowBlank: false,
					blankText: '����ڲ���Ϊ�գ�',
					width: 350
				}, {
					xtype: 'timefield',
					format: 'H:i',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '�ʱ��',
					emptyText: '����ʱ��',
					name: 'activetime',
					allowBlank: false,
					blankText: '�ʱ�䲻��Ϊ�գ�',
					width: 350
				}, {
					xtype: 'htmleditor',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '����',
					alowBlank: false,
					blankText: '����¼���ݲ���Ϊ�գ�',
					name: 'content',
					width: 350
				}, {
					xtype: 'textareafield',
					labelAlign: 'right',
					labelWidth: 70,
					fieldLabel: '��ע',
					emptyText: '����Ϊ��',
					name: 'remark',
					width: 350
				}]
			}],
			buttons: [{
				text: 'ȷ��',
				handler: function() {
					var f = w.getComponent(0).getForm();
					if(f.isValid()) {
						f.submit({
							url: 'control/admin/empdate.do?method=add',
							success: function(form, action) {
								if(action.result.success) {
									Ext.Msg.alert('��ʾ', '����¼����ɹ���');
									Ext.getCmp('empdatepanel').getStore().reload();
									w.close();
								} else {
									Ext.Msg.alert('��ʾ', '����¼����ʧ�ܣ�');
								}
							}, 
							failure: function(form, action) {
								Ext.Msg.alert('��ʾ', '����¼����ʧ�ܣ�');
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
					title: '��ʾ',
					msg: 'û����Ҫɾ���ı���¼��',
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
							title: '��ʾ',
							msg: 'ɾ���ɹ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						Ext.getCmp('empdatepanel').getStore().reload();
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

function onLoadEmpDate() {
	var sm = Ext.getCmp('empdatepanel').getSelectionModel();
	var record = sm.getSelection();
	var w = new Ext.Window({
		title: 'Ա������¼',
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
					fieldLabel: '��¼��',
					name: 'logger'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '��¼ʱ��',
					name: 'logtime'
				}, {
					xtype: 'displayfield',
					labelAlign: 'right',
					labelWidth: 85,
					fieldLabel: '�ʱ��',
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
					fieldLabel: '��ע',
					name: 'remark'
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