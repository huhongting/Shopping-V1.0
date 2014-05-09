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
				fieldLabel: 'Ա������',
				name: 'username'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '��������',
				name: 'email'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: 'Ա������',
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
				fieldLabel: '�̶��绰',
				name: 'phone'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '�ƶ��绰',
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
				fieldLabel: '��ϵ��ַ',
				name: 'address'
			}, {
				xtype: 'displayfield',
				labelAlign: 'right',
				labelWidth: 85,
				fieldLabel: '��������',
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
				fieldLabel: 'ע��ʱ��',
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
				fieldLabel: '�˻�Ȩ��',
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
				fieldLabel: '����˵��',
				name: 'note',
				readOnly: true
			}]
		}];
		this.callParent();
	}
});