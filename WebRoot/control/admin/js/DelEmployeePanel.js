Ext.define('DelEmployeePanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'delemppanel',
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
            fields:['userid', 'username', 'email', 'usertype', 'regtime', 'note'],
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/employee.do?method=listdel',  //����ķ�������ַ
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
            {header: "����˵��", dataIndex: 'note', sortable: false}
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		
		this.tbar = [{
			text: '�ָ�',
			id: 'mdee',
			iconCls: 'edit',
			handler: resetEmp
		}, '->', {
        	fieldLabel: '��������',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '������Ա������',
        	store: this.store
        }];
		
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
        
        this.addListener('itemdblclick', this.click, this);
        this.addListener('itemcontextmenu', function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		}, this);
        this.forceFit = true;
		this.callParent();
	},
	listeners: {
		afterrender: {
    		fn: function() {
    			Ext.Ajax.request({
					url: 'control/admin/check/delemployee.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mdee').disabled = !r.delemp;
					}
				});
    		}
    	}
	},
	click: function (grid, record) {
		var uid = record.get('userid');
		var w = new Ext.Window({
			title: 'Ա����Ϣ',
			width: 600,
			modal: true,
			resizable: false,
			animateTarget: 'delemppanel',
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
});

function resetEmp() {
	var sm = Ext.getCmp('delemppanel').getSelectionModel();
	if(sm.hasSelection()) {
		Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ�ָ���ѡ��¼��', function(btn) {
			if(btn == 'yes') {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					ids.push(s[i].get('userid'));
				}
				if(ids.length == 0 || ids.length > 1) {
					Ext.MessageBox.show({
						title: '��ʾ',
						msg: '��ѡ��һ����¼��',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/employee.do?method=reset',
					params: {id: ids},
					method: 'POST',
					success: function(response, opt) {
						var res = Ext.JSON.decode(response.responseText);
						if(res.success) {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '�ָ��ɹ���',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('delemppanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '�ָ�ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�ָ�ʧ�ܣ�',
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
			msg: '��ѡ����Ҫ�ָ��ļ�¼��',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	}
}
