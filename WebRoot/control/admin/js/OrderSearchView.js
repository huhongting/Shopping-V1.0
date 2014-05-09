Ext.define('OrderSearchView', {
	extend: 'Ext.panel.Panel',
	id: 'osview',
	layout: 'border',
	border: false,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		this.items = [{
			region: 'north',
			title: '高级搜索',
			iconCls: 'advance',
			height: 230,
			border: false,
			id: 'osmenu',
			collapsible: true,
			animcollapse: true,
			layout: 'fit',
			items: [{
				xtype: 'panel',
				layout: 'fit',
				border: false,
				padding: '0 1 0 1',
				items: [new Ext.form.Panel({
					frame: true,
					layout: 'form',
					columnWidth: 5,
					buttonAlign: 'center',
					items: [{
						border: false,
						layout: 'table',
						columnWidth: 5,
						frame: true,
						items: [{
							xtype: 'textfield',
							labelAlign: 'right',
							labelWidth: 126,
							fieldLabel: '订单编号',
							allowBlank: true,
							name: 'orderid'
						}, {
							xtype: 'checkbox',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '开具发票',
							allowBlank: true,
							name: 'fapiao'
						}, {
							xtype: 'checkbox',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '已锁定订单',
							allowBlank: true,
							name: 'lock'
						}, {
							xtype: 'textfield',
							labelAlign: 'right',
							labelWidth: 45,
							fieldLabel: '锁定人',
							allowBlank: true,
							name: 'employee'
						}]
					}, {
						border: false,
						layout: 'table',
						columnWidth: 5,
						frame: true,
						items: [{
							xtype: 'datefield',
							labelAlign: 'right',
							labelWidth: 150,
							fieldLabel: '下单开始时间',
							format: 'Y-m-d',
							allowBlank: true,
							name: 'startdate'
						}, {
							xtype: 'timefield',
							labelAlign: 'right',
							format: 'H:i',
							width: 70,
							allowBlank: true,
							name: 'starttime'
						}, {
							xtype: 'datefield',
							labelAlign: 'right',
							labelWidth: 150,
							fieldLabel: '下单结束时间',
							format: 'Y-m-d',
							allowBlank: true,
							name: 'enddate'
						}, {
							xtype: 'timefield',
							labelAlign: 'right',
							format: 'H:i',
							width: 70,
							allowBlank: true,
							name: 'endtime'
						}]
					}, {
						border: false,
						layout: 'table',
						columnWidth: 5,
						frame: true,
						items: [{
							xtype: 'combo',
							labelAlign: 'right',
							labelWidth: 126,
							fieldLabel: '订单状态',
							allowBlank: true,
							name: 'state',
							emptyText: '请选择订单状态',
							valueField: 'value',
							displayField: 'name',
							triggerAction: 'all',
							editable: false,
							store: new Ext.data.Store({
								autoLoad: true,
								fields: ['name', 'value'],
								proxy: {
									type: 'ajax',
									url: 'control/admin/ordersearch.do?method=state',
									reader: {
										type: 'json',
										root: 'records'
									}
								}
							})
						}, {
							xtype: 'combo',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '支付方式',
							allowBlank: true,
							name: 'paymentway',
							emptyText: '请选择支付方式',
							valueField: 'value',
							displayField: 'name',
							triggerAction: 'all',
							editable: false,
							store: new Ext.data.Store({
								autoLoad: true,
								fields: ['name', 'value'],
								proxy: {
									type: 'ajax',
									url: 'control/admin/ordersearch.do?method=paymentway',
									reader: {
										type: 'json',
										root: 'records'
									}
								}
							})
						}, {
							xtype: 'combo',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '配送方式',
							allowBlank: true,
							name: 'deliverway',
							emptyText: '请选择配送方式',
							valueField: 'value',
							displayField: 'name',
							triggerAction: 'all',
							editable: false,
							store: new Ext.data.Store({
								autoLoad: true,
								fields: ['name', 'value'],
								proxy: {
									type: 'ajax',
									url: 'control/admin/ordersearch.do?method=deliverway',
									reader: {
										type: 'json',
										root: 'records'
									}
								}
							})
						}]
					}, {
						border: false,
						layout: 'table',
						columnWidth: 5,
						frame: true,
						items: [{
							xtype: 'textfield',
							labelAlign: 'right',
							labelWidth: 116,
							fieldLabel: '购买者',
							allowBlank: true,
							name: 'buyer'
						}, {
							xtype: 'numberfield',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '最小订单额',
							allowBlank: true,
							minValue: 0,
							name: 'mintotalprice',
							id: 'mintotalprice'
						}, {
							xtype: 'numberfield',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '最大订单额',
							allowBlank: true,
							name: 'maxtotalprice',
							minValue: 0
						}, {
							xtype: 'checkbox',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '电子货币支付',
							allowBlank: true,
							name: 'epay'
						}]
					}],
					buttons: [{
						text: '查询',
						handler: function() {
							var f = Ext.getCmp('osmenu').getComponent(0)
														.getComponent(0)
														.getForm();
							submit(f);
						}
					}, {
						text: '重置',
						handler: function() {
							Ext.getCmp('osmenu').getComponent(0)
												.getComponent(0)
												.getForm().reset();
						}
					}, {
						text: '收起',
						handler: function() {
							Ext.getCmp('osmenu').collapse();
						}
					}]
				})]
			}]
		}, {
			region: 'center',
			border: false,
			layout: 'fit',
			items: [new OrderSearchPanel()]
		}]
		this.forceFit = true;
		this.callParent();
	}
});

function submit(f) {
	var date;
	var orderid = f.findField('orderid').getValue();
	var fapiao = f.findField('fapiao').getValue();
	if(fapiao == true) fapiao = 'on';
	else fapiao = null;
	var lock = f.findField('lock').getValue();
	if(lock == true) lock = 'on';
	else lock = null;
	var employee = f.findField('employee').getValue();
	var startdate = f.findField('startdate').getValue();
	startdate = Ext.util.Format.date(startdate, "Y-m-d");
	var starttime = f.findField('starttime').getValue();
	if(starttime != null && starttime != '') {
		date = new Date(starttime);
		starttime = date.getHours() + ":" + date.getMinutes();
	}
	var enddate = f.findField('enddate').getValue();
	enddate = Ext.util.Format.date(enddate, "Y-m-d");
	var endtime = f.findField('endtime').getValue();
	if(endtime != null && endtime != '') {
		date = new Date(endtime);
		endtime = date.getHours() + ":" + date.getMinutes();
	}
	var state = f.findField('state').getValue();
	var paymentway = f.findField('paymentway').getValue();
	var deliverway = f.findField('deliverway').getValue();
	var buyer = f.findField('buyer').getValue();
	var mintotalprice = f.findField('mintotalprice').getValue();
	var maxtotalprice = f.findField('maxtotalprice').getValue();
	var epay = f.findField('epay').getValue();
	if(epay == true) epay = 'on';
	else epay = null;
	
	var url = 'control/admin/ordersearch.do?' + 
		"orderid=" + orderid + 
		"&fapiao=" + fapiao + 
		"&lock=" + lock + 
		"&employee=" + employee + 
		"&startdate=" + startdate + 
		"&starttime=" + starttime + 
		"&enddate=" + enddate + 
		"&endtime=" + endtime + 
		"&state=" + state + 
		"&paymentway=" + paymentway + 
		"&deliverway=" + deliverway + 
		"&buyer=" + buyer + 
		"&mintotalprice=" + mintotalprice + 
		"&maxtotalprice=" + maxtotalprice + 
		"&epay=" + epay;
	
	var store = Ext.create('Ext.data.Store', {
        fields: ['orderid', 'username', 'createdate', 'state', 
        	'producttotalprice', 'deliverway', 'deliveraddr', 
        	'delivername', 'postcode', 'contact', 'paymentway', 
        	'deliverfee', 'epay', 'note', 'fapiao', 'employee',
        	'payable', 'alreadypay'
        ],
        pageSize: Ext.getCmp('ospanel').pageSize,
        proxy: {
            type: 'ajax',
            url: url,
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
    Ext.getCmp('ospanel').reconfigure(store);
	Ext.getCmp('ospanel').getStore().load({params:{start:0,limit:Ext.getCmp('ospanel').pageSize}});
	Ext.getCmp('ospanel').getComponent('osptb').bindStore(store);
}
