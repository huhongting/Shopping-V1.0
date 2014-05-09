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
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/order.do?method=list&os=2',  //请求的服务器地址
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
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "订单编号", dataIndex: 'orderid', sortable: true},
            {header: "下单者", dataIndex: 'username', sortable: true},
            {header: "下单时间", dataIndex: 'createdate', sortable: true},
            {header: "订单状态", dataIndex: 'state', sortable: true},
            {header: "商品总额", dataIndex: 'producttotalprice', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "电子货币支付", dataIndex: 'epay', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "支付方式", dataIndex: 'paymentway', sortable: true},
            {header: "配送方式", dataIndex: 'deliverway', sortable: true, hidden: true},
            {header: "配送费用", dataIndex: 'deliverfee', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}, hidden: true
            },
            {header: "需支付", dataIndex: 'payable', sortable: true,
            	renderer: function(value) {
            		if(value < 0) {
            			return '<font color=red>￥' + Ext.util.Format.number(value, '0.00') + '</font>';
            		}
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "已支付", dataIndex: 'alreadypay', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "收货人", dataIndex: 'delivername', sortable: true, hidden: true},
            {header: "联系方式", dataIndex: 'contact', sortable: true, hidden: true},
            {header: "配送地址", dataIndex: 'deliveraddr', sortable: true, hidden: true},
            {header: "邮政编码", dataIndex: 'postcode', sortable: true, hidden: true},
            {header: "其他说明", dataIndex: 'note', sortable: false},
            {header: "是否锁定", dataIndex: 'employee', sortable: false,
            	renderer: function(value) {
            		if(value == null || value == 'null')
            			return '<font color=green>未锁定</font>';
            		else return '<font color=red>已被[' + value + ']锁定</font>';
            	}
            }
        ];
		//this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            store: this.store,   //这里需要指定与表格相同的store
            displayInfo: true,
            beforePageText: '第',
            afterPageText: '页/共 {0} 页',
            displayMsg: '第 {0} 条到 {1} 条记录，共 {2} 条记录',
      		emptyMsg: "没有记录"
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
				title: '提示',
				msg: '请选择一条记录！',
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
				title: '订单',
				label: 'form',
				//closeAction: 'hide',
				buttonAlign: 'center',
				closable: false,
				border: false,
				resizable: false,
				items: [new OrderForm()],
				buttons: [{
					text: '订单已付款',
					id: 'mpop',
					handler: nextState
				}, {
					text: '已收部分账款',
					id: 'mponp',
					handler: receivePart
				}, {
					text: '取消订单',
					id: 'mpod',
					handler: cancelOrder
				}, {
					text: '客服留言',
					id: 'mpolm',
					handler: msgHandler
				}, {
					text: '解锁退出',
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
				title: '提示',
				msg: '订单已经被锁定！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
			return;
		}
		
		function receivePart() {
			var w = new Ext.Window({
				title: '已收部分账款',
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
						fieldLabel: '已收账款',
						name: 'arprice',
						allowBlank: false,
						minValue: 0,
						negativeText: '账款不能为负！',
						emptyText: '请填已收账款金额！',
						blankText: '账款金额不能为空！',
						width: 200
					}]
				}],
				buttons: [{
					text: '确定',
					handler: function() {
						var f = w.getComponent(0).getForm();
						if(f.isValid()) {
							f.submit({
								url: 'control/admin/order.do?method=bufen',
								params: {oid: Ext.getCmp('oforderid').getValue()},
								success: function(form, action) {
									if(action.result.success) {
										Ext.Msg.alert('提示', '订单操作成功！');
										Ext.getCmp('wpopanel').getStore().reload();
										Ext.getCmp('alreadypay').setValue(action.result.msg);
										w.close();
									} else {
										Ext.Msg.alert('提示', '订单操作失败！');
									}
								},
								failure: function(form, action) {
									Ext.Msg.alert('提示', '订单操作失败！');
								}
							});
						}
					}
				}, {
					text: '取消',
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
							title: '提示',
							msg: '订单' + _oid + '已取消！',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
		        	} else {
		        		Ext.MessageBox.show({
							title: '提示',
							msg: '订单' + _oid + '取消失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
		        	}
		        },
		        failure: function (response, opts) {
		            Ext.MessageBox.show({
						title: '提示',
						msg: '订单' + _oid + '取消失败！',
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
							title: '提示',
							msg: '订单' + _oid + '已处理！',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
		        	} else {
		        		Ext.MessageBox.show({
							title: '提示',
							msg: '订单' + _oid + '处理失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
		        	}
		        },
		        failure: function (response, opts) {
		            Ext.MessageBox.show({
						title: '提示',
						msg: '订单' + _oid + '处理失败！',
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
