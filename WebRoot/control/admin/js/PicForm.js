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
			fieldLabel: 'ͼƬ����',
			id: 'picname',
			name: 'name',
			allowBlank: false,
			emptyText: '����дͼƬ����',
			blankText: 'ͼƬ���Ʋ���Ϊ�գ�',
			width: 300
		}, {
			xtype: 'textfield',
			labelAlign: 'right',
			value: '/view/book/show?id=',
			labelWidth: 70,
			fieldLabel: '��ת����',
			id: 'action',
			name: 'action',
			allowBlank: false,
			emptyText: '����д��ת����',
			blankText: '��ת���Ӳ���Ϊ�գ�',
			width: 300
		}, {
			xtype: 'textareafield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: 'ͼƬ˵��',
			id: 'note',
			name: 'note',
			width: 300
		}, {
			xtype: 'displayfield',
			fieldLabel: '֧�ָ�ʽ',
			labelAlign: 'right',
			labelWidth: 70,
			value: 'jpg/jpeg/png/bmp/gif/ico',
			width: 300
		}, {
			xtype: 'filefield',
			buttonText: 'ѡ��ͼƬ',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: 'ͼƬ',
			id: 'pic',
			name: 'pic',
			allowBlank: false,
			emptyText: '���ϴ����ͼƬ',
			blankText: '���ͼƬ����Ϊ�գ�',
			width: 300
		}];
		this.callParent();
	}
});