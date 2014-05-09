Ext.define('EmpInfo', {
	extend: 'Ext.form.Panel',
	alias: 'empinfo',
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
			frame: true,
			border: false,
			layout: 'table',
			columnWidth: 5,
			border: false,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '员工姓名',
				name: 'username'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '电子邮箱',
				name: 'email'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '员工类型',
				name: 'usertype'
			}]
		}, {
			//xtype: 'panel',
			border: false,
			layout: 'table',
			columnWidth: 5,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '固定电话',
				name: 'phone'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '移动电话',
				name: 'mobile'
			}]
		}, {
			//xtype: 'panel',
			border: false,
			layout: 'table',
			columnWidth: 5,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '联系地址',
				name: 'address'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '邮政编码',
				name: 'postcode'
			}]
		}, {
			//xtype: 'panel',
			border: false,
			layout: 'table',
			columnWidth: 5,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '注册时间',
				name: 'regtime'
			}]
		}, {
			//xtype: 'panel',
			border: false,
			//layout: 'table',
			//columnWidth: 5,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				width: 560,
				fieldLabel: '账户权限',
				name: 'authority',
				readOnly: true
			}]
		}, {
			//xtype: 'panel',
			border: false,
			//layout: 'table',
			//columnWidth: 5,
			frame: true,
			items: [{
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				width: 560,
				fieldLabel: '其他说明',
				name: 'note',
				readOnly: true
			}]
		}];
		this.callParent();
	}
});