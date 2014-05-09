Ext.define('WaitPaymentOrderPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'wpopanel',
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
            fields:['orderid', 'username', 'createdate', 'state', 
            	'producttotalprice', 'deliverway', 'deliveraddr', 
            	'delivername', 'postcode', 'contact', 'paymentway', 
            	'deliverfee', 'epay', 'note', 'fapiao', 'employee',
            	'payable', 'alreadypay'],
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/order.do?method=list&os=2',  //����ķ�������ַ
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
            {header: "֧����ʽ", dataIndex: 'paymentway', sortable: true},
            {header: "���ͷ�ʽ", dataIndex: 'deliverway', sortable: true, hidden: true},
            {header: "���ͷ���", dataIndex: 'deliverfee', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}, hidden: true
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
            {header: "�ջ���", dataIndex: 'delivername', sortable: true, hidden: true},
            {header: "��ϵ��ʽ", dataIndex: 'contact', sortable: true, hidden: true},
            {header: "���͵�ַ", dataIndex: 'deliveraddr', sortable: true, hidden: true},
            {header: "��������", dataIndex: 'postcode', sortable: true, hidden: true},
            {header: "����˵��", dataIndex: 'note', sortable: false},
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
		var sm = Ext.getCmp('wpopanel').getSelectionModel();
		if(sm.hasSelection() == false || sm.getSelection().length > 1) {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ��һ����¼��',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.OK
			});
			return;
		}
		var record = sm.getSelection();
		
		var lock = checkLock(record[0].get('orderid'), Ext.getCmp('sysname').getText());
		if(lock) {
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
					text: '�����Ѹ���',
					id: 'mpop',
					handler: nextState
				}, {
					text: '���ղ����˿�',
					id: 'mponp',
					handler: receivePart
				}, {
					text: 'ȡ������',
					id: 'mpod',
					handler: cancelOrder
				}, {
					text: '�ͷ�����',
					id: 'mpolm',
					handler: msgHandler
				}, {
					text: '�����˳�',
					handler: function() {
						unlock(record[0].get('orderid'), Ext.getCmp('sysname').getText());
						Ext.getCmp('wpopanel').getStore().reload();
						//Ext.getCmp('orderconfirm').close();
						win.close();
					}
				}],
				listeners: {
					afterrender: {
		        		fn: function() {
		        			Ext.Ajax.request({
								url: 'control/admin/check/payorder.do',
								async: false,
								method: 'POST',
								success: function(response, opt) {
									var r = Ext.JSON.decode(response.responseText);
									Ext.getCmp('mpop').disabled = !r.chkmoney;
									Ext.getCmp('mponp').disabled = !r.chkmoney;
									Ext.getCmp('mpod').disabled = !r.cancel;
									Ext.getCmp('mpolm').disabled = !r.leafmsg;
								}
							});
		        		}
		        	}
				}
			});
			win.getComponent(0).loadRecord(record[0]);
			win.show();
		} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '�����Ѿ���������',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
			return;
		}
		
		function receivePart() {
			var w = new Ext.Window({
				title: '���ղ����˿�',
				width: 250,
				buttonAlign: 'center',
				border: false,
				modal: true,
				items: [{
					xtype: 'form',
					frame: true,
					items: [{
						xtype: 'numberfield',
						labelAlign: 'right',
						labelWidth: 70,
						fieldLabel: '�����˿�',
						name: 'arprice',
						allowBlank: false,
						minValue: 0,
						negativeText: '�˿��Ϊ����',
						emptyText: '���������˿��',
						blankText: '�˿����Ϊ�գ�',
						width: 200
					}]
				}],
				buttons: [{
					text: 'ȷ��',
					handler: function() {
						var f = w.getComponent(0).getForm();
						if(f.isValid()) {
							f.submit({
								url: 'control/admin/order.do?method=bufen',
								params: {oid: Ext.getCmp('oforderid').getValue()},
								success: function(form, action) {
									if(action.result.success) {
										Ext.Msg.alert('��ʾ', '���������ɹ���');
										Ext.getCmp('wpopanel').getStore().reload();
										Ext.getCmp('alreadypay').setValue(action.result.msg);
										w.close();
									} else {
										Ext.Msg.alert('��ʾ', '��������ʧ�ܣ�');
									}
								},
								failure: function(form, action) {
									Ext.Msg.alert('��ʾ', '��������ʧ�ܣ�');
								}
							});
						}
					}
				}, {
					text: 'ȡ��',
					handler: function() {
						w.close();
					}
				}]
			});
			w.show();
		}
		
		function cancelOrder() {
			var _oid = Ext.getCmp('oforderid').getValue();
			Ext.Ajax.request({
		        url: 'control/admin/order.do?method=cancel',
		        method: 'POST',
		        success: function (response, opts) {
		        	var res = Ext.JSON.decode(response.responseText);
		        	if(res.success) {
		        		Ext.MessageBox.show({
							title: '��ʾ',
							msg: '����' + _oid + '��ȡ����',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
		        	} else {
		        		Ext.MessageBox.show({
							title: '��ʾ',
							msg: '����' + _oid + 'ȡ��ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
		        	}
		        },
		        failure: function (response, opts) {
		            Ext.MessageBox.show({
						title: '��ʾ',
						msg: '����' + _oid + 'ȡ��ʧ�ܣ�',
						icon: Ext.MessageBox.ERROR,
						buttons: Ext.MessageBox.YES
					});
		        },
		        params: {
		            oid: _oid
		        }
		    });	
		}
		
		function nextState() {
			var _oid = Ext.getCmp('oforderid').getValue();
			Ext.Ajax.request({
		        url: 'control/admin/order.do?method=next',
		        method: 'POST',
		        success: function (response, opts) {
		        	var res = Ext.JSON.decode(response.responseText);
		        	if(res.success) {
		        		Ext.MessageBox.show({
							title: '��ʾ',
							msg: '����' + _oid + '�Ѵ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
		        	} else {
		        		Ext.MessageBox.show({
							title: '��ʾ',
							msg: '����' + _oid + '����ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
		        	}
		        },
		        failure: function (response, opts) {
		            Ext.MessageBox.show({
						title: '��ʾ',
						msg: '����' + _oid + '����ʧ�ܣ�',
						icon: Ext.MessageBox.ERROR,
						buttons: Ext.MessageBox.YES
					});
		        },
		        params: {
		            oid: _oid
		        }
		    });	
		}
	}
});
