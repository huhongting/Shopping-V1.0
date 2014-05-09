Ext.define('EmpSearchView', {
	extend: 'Ext.panel.Panel',
	id: 'empsview',
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
			height: 150,
			border: false,
			id: 'empsmenu',
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
							fieldLabel: '员工姓名',
							//name: 'empname'
							id: 'esname'
						}, {
							xtype: 'textfield',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '电子邮箱',
							//name: 'empmail'
							id: 'esmail'
						}, {
							xtype: 'numberfield',
							labelAlign: 'right',
							labelWidth: 150,
							fieldLabel: '手机号码',
							hideTrigger: true,
							//name: 'mobile'
							id: 'esmobile'
						}, {
							xtype: 'checkbox',
							labelAlign: 'right',
							labelWidth: 120,
							fieldLabel: '显示删除',
							//name: 'delflag'
							id: 'esdelflag'
						}]
					}, {
						border: false,
						layout: 'table',
						columnWidth: 5,
						frame: true,
						items: [{
							xtype: 'textfield',
							labelAlign: 'right',
							labelWidth: 126,
							fieldLabel: '联系地址',
							width: 1000,
							//name: 'address'
							id: 'esaddress'
						}]
					}],
					buttons: [{
						text: '查询',
						handler: function() {
							var f = Ext.getCmp('empsmenu').getComponent(0)
														.getComponent(0)
														.getForm();
							var name = f.findField('esname').getValue();
							var mail = f.findField('esmail').getValue();
							var mobile = f.findField('esmobile').getValue();
							var delflag = f.findField('esdelflag').getValue();
							if(delflag == true) delflag = 'on';
							else delflag = null;
							var address = f.findField('esaddress').getValue();
							
							var url = 'control/admin/empsearch.do?' + 
								"empname=" + name + 
								"&empmail=" + mail + 
								"&mobile=" + mobile + 
								"&delflag=" + delflag + 
								"&address=" + address;
							
							var store = Ext.create('Ext.data.Store', {
					            fields:['userid', 'username', 'email', 'usertype', 'regtime', 'note'],
					            pageSize: Ext.getCmp('espanel').pageSize,
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
					            	property: 'userid',
					            	direction: 'ASC'
					            }]
					        });
					        Ext.getCmp('espanel').reconfigure(store);
							Ext.getCmp('espanel').getStore().load({params:{start:0,limit:Ext.getCmp('espanel').pageSize}});
							Ext.getCmp('espanel').getComponent('esptb').bindStore(store);
						}
					}, {
						text: '重置',
						handler: function() {
							Ext.getCmp('empsmenu').getComponent(0)
												.getComponent(0)
												.getForm().reset();
						}
					}, {
						text: '收起',
						handler: function() {
							Ext.getCmp('empsmenu').collapse();
						}
					}]
				})]
			}]
		}, {
			region: 'center',
			border: false,
			layout: 'fit',
			items: [new EmpSearchPanel()]
		}]
		this.forceFit = true;
		this.callParent();
	}
});
