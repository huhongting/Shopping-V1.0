Ext.define('WaitProductOrderPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'wproopanel',
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
                url: 'control/admin/order.do?method=list&os=3',  //请求的服务器地址
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
            {header: "配送费用", dataIndex: 'deliverfee', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}, hidden: true
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
		var sm = Ext.getCmp('wproopanel').getSelectionModel();
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
					text: '配货完成',
					id: 'mpdop',
					handler: nextState
				}, {
					text: '生成新订单',
					id: 'mpdorn',
					handler: createNewOrder
				}, {
					text: '取消订单',
					id: 'mpdod',
					handler: cancelOrder
				}, {
					text: '客服留言',
					id: 'mpdolm',
					handler: msgHandler
				}, {
					text: '解锁退出',
					handler: function() {
						unlock(record[0].get('orderid'), Ext.getCmp('sysname').getText());
						Ext.getCmp('wproopanel').getStore().reload();
						//Ext.getCmp('orderconfirm').close();
						win.close();
					}
				}],
				listeners: {
					afterrender: {
		        		fn: function() {
		        			Ext.Ajax.request({
								url: 'control/admin/check/product.do',
								async: false,
								method: 'POST',
								success: function(response, opt) {
									var r = Ext.JSON.decode(response.responseText);
									Ext.getCmp('mpdop').disabled = !r.odrpro;
									Ext.getCmp('mpdorn').disabled = !r.resetorder;
									Ext.getCmp('mpdod').disabled = !r.cancel;
									Ext.getCmp('mpdolm').disabled = !r.leafmsg;
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
		
		function createNewOrder() {
			var store = Ext.getCmp('oipanel').getStore();
			var total = store.getCount();
			var ids = [];
			for(var i=0; i<total; i++) {
				var r = store.getAt(i);
				ids.push(r.get('bookname'));
			}
			
			var now = new Ext.Window({
				title: '订单商品修改',
				width: 400,
				modal: true,
				//border: false,
				buttonAlign: 'center',
				items: [new NewItemPanel({id: 'noipanel', orderid: record[0].get('orderid')})],
				buttons: [{
					text: '确定',
					handler: function() {
						Ext.MessageBox.confirm('提示', '确定要重新生成订单吗？', function(btn) {
							if(btn == 'yes') {
								var rs = Ext.getCmp('noipanel').getStore().getModifiedRecords();
				                var nums = [];
				                var names = [];
				                if(rs.length == 0) return;
				                for(var i=0; i<rs.length; i++) {
				                	if(rs[i].get('newnum') == 0 || rs[i].get('newnum') == '0')
				                		continue;
				                	nums.push(rs[i].get('newnum'));
				                	names.push(rs[i].get('bookname'));
				                }
				                if(nums.length == 0) {
			                		Ext.Msg.alert('提示', '新订单商品总量不能为0！');
			                		return;
			                	}
				                for(var i=0; i<nums.length; i++) {
				                	if(nums[i] > rs[i].get('number')) {
				                		Ext.Msg.alert('提示', '新订单商品数量不能大于原订单数量！');
				                		return;
				                	}
				                	/*if(nums[i] == 0 || nums[i] == '0') {
				                		Ext.Msg.alert('提示', '新订单商品数量不能为0！');
				                		return;
				                	}*/
				                }
				                Ext.Ajax.request({
				                    method:'POST',
				                    url:'control/admin/order.do?method=reorder',
				                    success:function(response, opt){
				                    	var res = Ext.JSON.decode(response.responseText);
				                        if(res.success) {
					                        Ext.Msg.alert('提示', '订单替换成功！');
				                            Ext.getCmp('wproopanel').getStore().reload();
					                        now.close();
				                        } else {
				                        	Ext.Msg.alert('提示', res.msg);
				                        }
				                    },
				                    failure:function(){
				                        Ext.Msg.alert("错误", "与后台联系的时候出现了问题！");
				                    },
				                    params: {
				                    	nums: nums,
				                    	names: names,
				                    	oid: record[0].get('orderid')
				                    }
				                });
							}
						});
					}
				}, {
					text: '取消',
					handler: function() {
						now.close();
					}
				}]
			});
			now.show();
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
