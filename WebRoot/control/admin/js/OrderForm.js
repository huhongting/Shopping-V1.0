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
				fieldLabel: '�������',
				id: 'oforderid',
				name: 'orderid'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '�µ���',
				id: 'username',
				name: 'username'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '�µ�ʱ��',
				id: 'createdate',
				name: 'createdate'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '����״̬',
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
				fieldLabel: '֧����ʽ',
				id: 'paymentway',
				name: 'paymentway'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '���ͷ�ʽ',
				id: 'deliverway',
				name: 'deliverway'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '��Ʒ�ܶ�',
				id: 'producttotalprice',
				name: 'producttotalprice'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '���ͷ���',
				id: 'deliverfee',
				name: 'deliverfee'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 95,
				fieldLabel: '���ӻ���֧��',
				id: 'epay',
				name: 'epay'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '��֧��',
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
				fieldLabel: '�ջ���',
				id: 'delivername',
				name: 'delivername'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '��ϵ��ʽ',
				id: 'contact',
				name: 'contact'
			},  {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '���͵�ַ',
				id: 'deliveraddr',
				name: 'deliveraddr'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '��������',
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
				fieldLabel: '��Ʊ',
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
				fieldLabel: '����˵��',
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