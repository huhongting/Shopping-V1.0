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
            {header: "发票", dataIndex: 'fapiao', sortable: false},
            {header: "其他说明", dataIndex: 'note', sortable: false, hidden: true},
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
            id: 'osptb',
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
		var sm = Ext.getCmp('ospanel').getSelectionModel();
		var record = sm.getSelection();
		
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
				text: '解锁订单',
				id: 'moul',
				handler: function() {
					unlock(record[0].get('orderid'), Ext.getCmp('sysname').getText());
					var f = Ext.getCmp('osmenu').getComponent(0).getComponent(0).getForm();
					submit(f);
					win.close();
				}
			}, {
				text: '关闭',
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
