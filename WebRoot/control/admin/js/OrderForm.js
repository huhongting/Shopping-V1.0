Ext.define('OrderForm', {
	extend: 'Ext.form.Panel',
	alias: 'orderform',
	//height: 343,
	//frame: true,
	//width: 700,
	border: false,
	frame: true,
	//autoScroll: true,
	layout: 'form',
	columnWidth: 5,
	initComponent: function() {
		this.items = [{
			xtype: 'panel',
			layout: 'table',
			border: false,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '订单编号',
				id: 'oforderid',
				name: 'orderid'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '下单者',
				id: 'username',
				name: 'username'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '下单时间',
				id: 'createdate',
				name: 'createdate'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '订单状态',
				id: 'state',
				name: 'state'
			}]
		}, {
			xtype: 'panel',
			height: 115,
			border: false,
			frame: true,
			//autoScroll: true,
			id: 'ood'/*,
			layout: {
			    type: 'hbox',
			    align: 'middle ',
			    pack: 'center'
			}*/
		}, {
			xtype: 'panel',
			layout: 'table',
			border: false,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '支付方式',
				id: 'paymentway',
				name: 'paymentway'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '配送方式',
				id: 'deliverway',
				name: 'deliverway'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '商品总额',
				id: 'producttotalprice',
				name: 'producttotalprice'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '配送费用',
				id: 'deliverfee',
				name: 'deliverfee'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 95,
				fieldLabel: '电子货币支付',
				id: 'epay',
				name: 'epay'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '已支付',
				id: 'alreadypay',
				name: 'alreadypay'
			}]
		}, {
			xtype: 'panel',
			layout: 'table',
			border: false,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '收货人',
				id: 'delivername',
				name: 'delivername'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '联系方式',
				id: 'contact',
				name: 'contact'
			},  {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '配送地址',
				id: 'deliveraddr',
				name: 'deliveraddr'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '邮政编码',
				id: 'postcode',
				name: 'postcode'
			}]
		}, {
			xtype: 'panel',
			layout: 'table',
			border: false,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '发票',
				id: 'fapiao',
				name: 'fapiao'
			}]
		}, {
			xtype: 'panel',
			layout: 'table',
			border: false,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '其他说明',
				id: 'note',
				name: 'note'
			}]
		}, {
			xtype: 'panel',
			height: 140,
			border: false,
			frame: true,
			//autoScroll: true,
			id: 'omd'
		}];
		this.callParent();
	},
	listeners: {
		afterrender: function() {
			var oid = Ext.getCmp('oforderid').getValue();
			Ext.getCmp('ood').add(new OrderItemPanel({id: 'oipanel', orderid: oid}));
			Ext.getCmp('omd').add(new OrderMsgPanel({id: 'ompanel', orderid: oid}));
		}
	}
});