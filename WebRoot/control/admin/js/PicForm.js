Ext.define('PicForm', {
	extend: 'Ext.form.Panel',
	alias: 'picform',
	frame: true,
	//width: 400,
	autoScroll: true,
	initComponent: function() {
		this.items = [{
			xtype: 'textfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '图片名称',
			id: 'picname',
			name: 'name',
			allowBlank: false,
			emptyText: '请填写图片名称',
			blankText: '图片名称不能为空！',
			width: 300
		}, {
			xtype: 'textfield',
			labelAlign: 'right',
			value: '/view/book/show?id=',
			labelWidth: 70,
			fieldLabel: '跳转链接',
			id: 'action',
			name: 'action',
			allowBlank: false,
			emptyText: '请填写跳转链接',
			blankText: '跳转链接不能为空！',
			width: 300
		}, {
			xtype: 'textareafield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '图片说明',
			id: 'note',
			name: 'note',
			width: 300
		}, {
			xtype: 'displayfield',
			fieldLabel: '支持格式',
			labelAlign: 'right',
			labelWidth: 70,
			value: 'jpg/jpeg/png/bmp/gif/ico',
			width: 300
		}, {
			xtype: 'filefield',
			buttonText: '选择图片',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '图片',
			id: 'pic',
			name: 'pic',
			allowBlank: false,
			emptyText: '请上传广告图片',
			blankText: '广告图片不能为空！',
			width: 300
		}];
		this.callParent();
	}
});