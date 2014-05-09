Ext.define('CancelOrderPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'clopanel',
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
                url: 'control/admin/order.do?method=list&os=0',  //����ķ�������ַ
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
        this.columns =  [//���ñ�����
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
            {header: "���ͷ���", dataIndex: 'deliverfee', sortable: true, hidden: true,
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
            store: this.store,   //������Ҫָ���������ͬ��store
            displayInfo: true,
            beforePageText: '��',
            afterPageText: 'ҳ/�� {0} ҳ',
            displayMsg: '�� {0} ���� {1} ����¼���� {2} ����¼',
      		emptyMsg: "û�м�¼"
        }];
        //this.addListener('itemdblclick', loadOrder, this);
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
		var sm = Ext.getCmp('clopanel').getSelectionModel();
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
				//id: 'cancelorder',
				label: 'form',
				//closeAction: 'hide',
				buttonAlign: 'center',
				closable: false,
				border: false,
				resizable: false,
				//animateTarget: 'clopanel',
				items: [new OrderForm()],
				buttons: [{
					text: '�ָ�����',
					id: 'mcoro',
					handler: resetOrder
				}, {
					text: '�ͷ�����',
					id: 'mcolm',
					handler: msgHandler
				}, {
					text: '�����˳�',
					handler: function() {
						unlock(record[0].get('orderid'), Ext.getCmp('sysname').getText());
						Ext.getCmp('clopanel').getStore().reload();
						win.close();
					}
				}],
				listeners: {
					afterrender: {
		        		fn: function() {
		        			Ext.Ajax.request({
								url: 'control/admin/check/reorder.do',
								async: false,
								method: 'POST',
								success: function(response, opt) {
									var r = Ext.JSON.decode(response.responseText);
									Ext.getCmp('mcoro').disabled = !r.reorder;
									Ext.getCmp('mcolm').disabled = !r.leafmsg;
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
		
		function resetOrder() {
			var _oid = Ext.getCmp('oforderid').getValue();
			Ext.Ajax.request({
		        url: 'control/admin/order.do?method=reset',
		        method: 'POST',
		        success: function (response, opts) {
		        	var res = Ext.JSON.decode(response.responseText);
		        	if(res.success) {
		        		Ext.MessageBox.show({
							title: '��ʾ',
							msg: '����' + _oid + '�ѻָ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
		        	} else {
		        		Ext.MessageBox.show({
							title: '��ʾ',
							msg: '����' + _oid + '�ָ�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
		        	}
		        },
		        failure: function (response, opts) {
		            Ext.MessageBox.show({
						title: '��ʾ',
						msg: '����' + _oid + '�ָ�ʧ�ܣ�',
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

function msgHandler() {
	var w = new Ext.Window({
		width: 260,
		height: 125,
		modal: true,
		border: false,
		resizable: false,
		title: '�ͷ�����',
		buttonAlign: 'center',
		items: [{
			xtype: 'textareafield',
			labelAlign: 'right',
			labelWidth: 40,
			fieldLabel: '����',
			emptyText: '������Ϊ��',
			allowBlank: false,
			blankText: '���Բ���Ϊ�գ�',
			id: 'kfmsg',
			name: 'kfmsg',
			width: 230
		}],
		buttons: [{
			text: '����',
			handler: function() {
				var _oid = Ext.util.Format.trim(Ext.getCmp('oforderid').getValue());
				var _msg = Ext.getCmp('kfmsg').getValue();
				var _name = Ext.getCmp('sysname').getText();
				if(Ext.getCmp('kfmsg').isValid() == false) {
                    Ext.MessageBox.show({
                    	title: '����',
                    	msg: '������������������!',
                    	icon: Ext.MessageBox.WARNING,
                    	buttons: Ext.MessageBox.OK
                    });
                } else { 
					Ext.Ajax.request({
                        url: 'control/admin/ordermsg.do?method=add',
                        method: 'POST',
                        success: function (response, opts) {
                        	var res = Ext.JSON.decode(response.responseText);
                        	if(res.success) {
	                            Ext.MessageBox.show({
	                            	title: '����',
	                            	msg: '���Գɹ�!',
	                            	icon: Ext.MessageBox.INFO,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                            w.close();
	                            Ext.getCmp('ompanel').getStore().reload();
                        	} else {
                        		Ext.MessageBox.show({
	                            	title: '����',
	                            	msg: '����ʧ��!',
	                            	icon: Ext.MessageBox.ERROR,
	                            	buttons: Ext.MessageBox.OK
                            	});
                        	}
                        },
                        failure: function (response, opts) {
                            Ext.MessageBox.show({
                            	title: '����',
                            	msg: '����ʧ��!',
                            	icon: Ext.MessageBox.ERROR,
                            	buttons: Ext.MessageBox.OK
                            });
                        },
                        params: {
                            oid: _oid,
                            msg: _msg,
                            name: _name
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

function checkLock(_oid, _name) {
	var lock = false;
	Ext.Ajax.request({
        url: 'control/admin/order.do?method=chklock',
        method: 'POST',
        async: false,
        success: function (response, opts) {
        	var res = Ext.JSON.decode(response.responseText);
        	if(res.success) {
               lock = true;
        	} else {
        		lock = false;
        	}
        },
        failure: function (response, opts) {
            lock = false;
        },
        params: {
            oid: _oid,
            name: _name
        }
    });
    return lock;
}
function unlock(_oid, _name) {
	Ext.Ajax.request({
        url: 'control/admin/order.do?method=unlock',
        method: 'POST',
        async: false,
        success: function (response, opts) {
        	var res = Ext.JSON.decode(response.responseText);
        	if(res.success) {
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
            oid: _oid,
            name: _name
        }
    });
}
