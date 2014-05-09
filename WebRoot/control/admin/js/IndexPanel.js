Ext.define('IndexView', {
	extend: 'Ext.tab.Panel',
	//id: 'indexview',
	border: false,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		this.items = [{
			title: 'ϵͳ֪ͨ',
			iconCls: 'advance',
			border: false,
			layout: 'fit',
			items: [new NotificationPanel()]
		}, {
			title: 'Ա������¼',
			iconCls: 'advance',
			border: false,
			layout: 'fit',
			items: [new EmpDatePanel()]	
		}]
		this.forceFit = true;
		this.callParent();
	}
});
