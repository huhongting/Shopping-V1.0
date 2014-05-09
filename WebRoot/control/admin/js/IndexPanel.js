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
			title: '系统通知',
			iconCls: 'advance',
			border: false,
			layout: 'fit',
			items: [new NotificationPanel()]
		}, {
			title: '员工备忘录',
			iconCls: 'advance',
			border: false,
			layout: 'fit',
			items: [new EmpDatePanel()]	
		}]
		this.forceFit = true;
		this.callParent();
	}
});
