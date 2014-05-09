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
			text: '���Ȩ����',
			id: 'mgpga',
			iconCls: 'add',
			handler: addPermission
		}, {
			text: 'ɾ��Ȩ����',
			id: 'mpgd',
			iconCls: 'del',
			handler: delPermission
		}];
		
		this.columns = [
			new Ext.grid.RowNumberer({header: '�к�', width: 30}),
			{header: "Ȩ������", dataIndex: 'id', sortable: true, hidden: true},
            {header: "Ȩ��������", dataIndex: 'name', sortable: true},
            {header: "����ʱ��", dataIndex: 'createdate', sortable: true},
            {header: "������", dataIndex: 'creater', sortable: true},
            {header: "��ע", dataIndex: 'remark', sortable: false}
		];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		
		this.store = Ext.create('Ext.data.Store', {
            autoLoad: {
            	start: 0, 
            	limit: this.pageSize
            },
            fields:['id', 'name', 'createdate', 'creater', 'remark', 'authority'],
            pageSize: this.pageSize, //���÷�ҳ��С
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
            store: this.store,   //������Ҫָ��������ͬ��store
            displayInfo: true,
            beforePageText: '��',
            afterPageText: 'ҳ/�� {0} ҳ',
            displayMsg: '�� {0} ���� {1} ����¼���� {2} ����¼',
      		emptyMsg: "û�м�¼"
        }];
		
		this.forceFit = true;
		this.callParent();
	}
});

function loadPermission() {
	var w = new Ext.Window({
		title: 'Ȩ�޲鿴',
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
				fieldLabel: 'Ȩ���б�',
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
		title: '���Ȩ����',
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
				fieldLabel: 'Ȩ��������',
				name: 'permissionName',
				allowBlank: false,
				emptyText: '����дȨ��������',
				blankText: 'Ȩ�������Ʋ���Ϊ�գ�',
				width: 350
			}, {
				xtype: 'fieldset',
				id: 'pmfieldset',
				title: 'Ȩ���б�',
				collapsible: true,
				defaultType: 'checkbox',
				layout: 'column'
			}, {
				xtype: 'textareafield',
				name: 'remark',
				width: 380,
				labelAlign: 'right',
				labelWidth: 40,
				fieldLabel: '��ע'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: 'ȷ��',
			handler: function() {
				var f = w.getComponent(0).getForm();
				if(f.isValid()) {
					f.submit({
						url: 'control/admin/permissiongroup.do?method=add',
						success: function(form, action) {
							if(action.result.success) {
								w.close();
								Ext.getCmp('permissionview').getStore().reload();
								Ext.Msg.alert('��ʾ', '����ɹ���');
							} else {
								Ext.Msg.alert('��ʾ', '����ʧ�ܣ�');
							}
						},
						failure: function(form, action) {
							Ext.Msg.alert('��ʾ', '����ʧ�ܣ�');
						}
					});
				}
			}
		}, {
			text: 'ȡ��',
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
			title: '��ʾ',
			msg: '��ѡ����Ҫɾ����Ȩ�ޣ�',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	} else {
		Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����ѡȨ����', function(btn) {
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
							Ext.Msg.alert('��ʾ', 'ɾ���ɹ���');
							Ext.getCmp('permissionview').getStore().reload();
						} else {
							Ext.Msg.alert('��ʾ', 'ɾ��ʧ�ܣ�');
						}
					},
					failure: function(resp, opt) {
						Ext.Msg.alert('��ʾ', 'ɾ��ʧ�ܣ�');
					}
				});
			}
		});
	}
}