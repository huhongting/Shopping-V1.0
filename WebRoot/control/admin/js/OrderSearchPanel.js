Ext.define('OrderSearchPanel', {
	extend: 'Ext.grid.Panel',
	border: false,
	id: 'ospanel',
	layout: 'fit',
	pageSize: 20,
	autoScroll: true,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "�������", dataIndex: 'orderid', sortable: true},
            {header: "�µ���", dataIndex: 'username', sortable: true},
            {header: "�µ�ʱ��", dataIndex: 'createdate', sortable: true},
            {header: "����״̬", dataIndex: 'state', sortable: true},
            {header: "��Ʒ�ܶ�", dataIndex: 'producttotalprice', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "���ӻ���֧��", dataIndex: 'epay', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "��֧��", dataIndex: 'payable', sortable: true,
            	renderer: function(value) {
            		if(value < 0) {
            			return '<font color=red>��' + Ext.util.Format.number(value, '0.00') + '</font>';
            		}
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "��֧��", dataIndex: 'alreadypay', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "֧����ʽ", dataIndex: 'paymentway', sortable: true},
            {header: "���ͷ�ʽ", dataIndex: 'deliverway', sortable: true, hidden: true},
            {header: "���ͷ���", dataIndex: 'deliverfee', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}, hidden: true
            },
            {header: "�ջ���", dataIndex: 'delivername', sortable: true, hidden: true},
            {header: "��ϵ��ʽ", dataIndex: 'contact', sortable: true, hidden: true},
            {header: "���͵�ַ", dataIndex: 'deliveraddr', sortable: true, hidden: true},
            {header: "��������", dataIndex: 'postcode', sortable: true, hidden: true},
            {header: "��Ʊ", dataIndex: 'fapiao', sortable: false},
            {header: "����˵��", dataIndex: 'note', sortable: false, hidden: true},
            {header: "�Ƿ�����", dataIndex: 'employee', sortable: false,
            	renderer: function(value) {
            		if(value == null || value == 'null')
            			return '<font color=green>δ����</font>';
            		else return '<font color=red>�ѱ�[' + value + ']����</font>';
            	}
            }
        ];
		//this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
        
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            id: 'osptb',
            store: this.store,   //������Ҫָ��������ͬ��store
            displayInfo: true,
            beforePageText: '��',
            afterPageText: 'ҳ/�� {0} ҳ',
            displayMsg: '�� {0} ���� {1} ����¼���� {2} ����¼',
      		emptyMsg: "û�м�¼"
        }];
        
        this.listeners = {
        	'itemcontextmenu': function(view, record, item, index, e, opt) {
				e.preventDefault();
				e.stopEvent();
			},
        	itemdblclick: {fn: this.onLoadOrder}
        };
        this.forceFit = true;
		this.callParent();
	},
	onLoadOrder: function() {
		var win;
		var sm = Ext.getCmp('ospanel').getSelectionModel();
		var record = sm.getSelection();
		
		win = new Ext.Window({
			width: 800,
			height: 486,
			modal: true,
			title: '����',
			label: 'form',
			//closeAction: 'hide',
			buttonAlign: 'center',
			closable: false,
			border: false,
			resizable: false,
			items: [new OrderForm()],
			buttons: [{
				text: '��������',
				id: 'moul',
				handler: function() {
					unlock(record[0].get('orderid'), Ext.getCmp('sysname').getText());
					var f = Ext.getCmp('osmenu').getComponent(0).getComponent(0).getForm();
					submit(f);
					win.close();
				}
			}, {
				text: '�ر�',
				handler: function() {
					win.close();
				}
			}],
			listeners: {
				afterrender: {
	        		fn: function() {
	        			Ext.Ajax.request({
							url: 'control/admin/check/adminunlock.do',
							async: false,
							method: 'POST',
							success: function(response, opt) {
								var r = Ext.JSON.decode(response.responseText);
								Ext.getCmp('moul').disabled = !r.unlock;
							}
						});
	        		}
	        	}
			}
		});
		win.getComponent(0).loadRecord(record[0]);
		win.show();
	}
});
