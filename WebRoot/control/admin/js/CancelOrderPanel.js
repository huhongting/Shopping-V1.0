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
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/order.do?method=list&os=0',  //请求的服务器地址
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
            {header: "支付方式", dataIndex: 'paymentway', sortable: true},
            {header: "配送方式", dataIndex: 'deliverway', sortable: true, hidden: true},
            {header: "配送费用", dataIndex: 'deliverfee', sortable: true, hidden: true,
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
					text: '恢复订单',
					id: 'mcoro',
					handler: resetOrder
				}, {
					text: '客服留言',
					id: 'mcolm',
					handler: msgHandler
				}, {
					text: '解锁退出',
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
				title: '提示',
				msg: '订单已经被锁定！',
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
							title: '提示',
							msg: '订单' + _oid + '已恢复！',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
		        	} else {
		        		Ext.MessageBox.show({
							title: '提示',
							msg: '订单' + _oid + '恢复失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
		        	}
		        },
		        failure: function (response, opts) {
		            Ext.MessageBox.show({
						title: '提示',
						msg: '订单' + _oid + '恢复失败！',
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
		title: '客服留言',
		buttonAlign: 'center',
		items: [{
			xtype: 'textareafield',
			labelAlign: 'right',
			labelWidth: 40,
			fieldLabel: '留言',
			emptyText: '不可以为空',
			allowBlank: false,
			blankText: '留言不能为空！',
			id: 'kfmsg',
			name: 'kfmsg',
			width: 230
		}],
		buttons: [{
			text: '保存',
			handler: function() {
				var _oid = Ext.util.Format.trim(Ext.getCmp('oforderid').getValue());
				var _msg = Ext.getCmp('kfmsg').getValue();
				var _name = Ext.getCmp('sysname').getText();
				if(Ext.getCmp('kfmsg').isValid() == false) {
                    Ext.MessageBox.show({
                    	title: '保存',
                    	msg: '输入有误，请重新输入!',
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
	                            	title: '保存',
	                            	msg: '留言成功!',
	                            	icon: Ext.MessageBox.INFO,
	                            	buttons: Ext.MessageBox.OK
	                            });
	                            w.close();
	                            Ext.getCmp('ompanel').getStore().reload();
                        	} else {
                        		Ext.MessageBox.show({
	                            	title: '保存',
	                            	msg: '留言失败!',
	                            	icon: Ext.MessageBox.ERROR,
	                            	buttons: Ext.MessageBox.OK
                            	});
                        	}
                        },
                        failure: function (response, opts) {
                            Ext.MessageBox.show({
                            	title: '保存',
                            	msg: '留言失败!',
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
			text: '取消',
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
					title: '提示',
					msg: '订单' + _oid + '解锁失败！',
					icon: Ext.MessageBox.ERROR,
					buttons: Ext.MessageBox.YES
				});
        	}
        },
        failure: function (response, opts) {
    		Ext.MessageBox.show({
				title: '提示',
				msg: '订单' + _oid + '解锁失败！',
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

