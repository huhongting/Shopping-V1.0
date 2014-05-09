Ext.define('BookForm', {
	extend: 'Ext.form.Panel',
	alias: 'bookform',
	frame: true,
	//width: 400,
	autoScroll: true,
	initComponent: function() {
		this.items = [{
			xtype: 'textfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�鼮����',
			id: 'bookname',
			name: 'bookName',
			allowBlank: false,
			emptyText: '����д�鼮����',
			blankText: '�鼮���Ʋ���Ϊ�գ�',
			width: 350
		}, {
			xtype: 'textfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '����',
			id: 'auther',
			name: 'auther',
			allowBlank: false,
			emptyText: '����д�鼮����',
			blankText: '�鼮���߲���Ϊ�գ�',
			width: 350
		}, {
			xtype: 'textfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�鼮���',
			id: 'booktype',
			name: 'bookType',
			allowBlank: false,
			emptyText: '����д�鼮���',
			blankText: '�鼮�����Ϊ�գ�',
			width: 350,
			listeners: {
				focus: {
					fn: function(com, e, o) {
						// show book type tree
						var win = new Ext.Window({
							title: '�鼮���',
							height: 300,
							width: 350,
							resizable: false,
							autoScroll: true,
							modal: true,
							border: false,
							buttonAlign: 'center',
							items: new Ext.tree.Panel({
										//height: 300,
										width: 320,
										border: false,
										rootVisible: false,
										store: new Ext.data.TreeStore({
											root: {
												id: 'root',
												expanded: true
											},
											proxy: {
												type: 'ajax',
												url: 'control/admin/booktype.do?method=tree'
											}
										})
									}),
								buttons: [{
									text: 'ȷ��',
									handler: selectItem
								}]
						}).show();
						function selectItem() {
							var s = win.getComponent(0).getSelectionModel().getSelection();	
							if(s[0]) {
								com.setValue(s[0].get('text'));
								win.hide();
							}
						}
					}
				}
			}
		}, {
			xtype: 'combo',
			id: 'press',
			name: 'press',
			width: 350,
			editable: false,
			labelAlign: 'right',
			labelWidth: 70,
			emptyText: '��ѡ�������',
			fieldLabel: '������',
			allowBlank: false,
			blankText: '�����粻��Ϊ�գ�',
			valueField: 'id',
			displayField: 'name',
			triggerAction: 'all',
			//mode: 'remote',
			store: new Ext.data.Store({
				autoLoad: true,
				fields: ['name', 'id'],
				proxy: {
					type: 'ajax',
					url: 'control/admin/press.do?method=listcombo',
					reader: {
						type: 'json',
						root: 'records'
					}
				}
			})
		}, {
			xtype: 'datefield',
			format: 'Y-m-d',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '����ʱ��',
			emptyText: '����д����ʱ��',
			id: 'pubtime',
			name: 'pubTime',
			allowBlank: false,
			blankText: '����ʱ�䲻��Ϊ�գ�',
			width: 350
		}, {
			xtype: 'numberfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�ۼ�',
			id: 'price',
			name: 'price',
			allowBlank: false,
			minValue: 0,
			negativeText: '�۸���Ϊ����',
			emptyText: '����д�鼮�۸�',
			blankText: '�鼮�۸���Ϊ�գ�',
			width: 350
		}, {
			xtype: 'numberfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�г��۸�',
			minValue: 0,
			negativeText: '�۸���Ϊ����',
			emptyText: '����д�г��۸�',
			allowBlank: false,
			blankText: '�г��۸���Ϊ�գ�',
			name: 'normalPrice',
			id: 'normalprice',
			width: 350
		}, {
			xtype: 'numberfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '���',
			minValue: 1,
			negativeText: '��治��Ϊ����',
			emptyText: '����д���',
			allowBlank: false,
			blankText: '��治��Ϊ�գ�',
			name: 'number',
			id: 'number',
			width: 350
		}, {
			xtype: 'filefield',
			buttonText: 'ѡ��ͼƬ',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: 'ͼƬ',
			id: 'picurl',
			name: 'pic',
			allowBlank: false,
			emptyText: '���ϴ��鼮����',
			blankText: '�鼮���治��Ϊ�գ�',
			width: 350
		}, {
			xtype: 'htmleditor',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '������Ϣ',
			emptyText: '����Ϊ��',
			name: 'saleInfo',
			id: 'saleinfo',
			width: 350
		}, {
			xtype: 'htmleditor',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�鼮����',
			emptyText: '����Ϊ��',
			id: 'note',
			name: 'note',
			width: 350
		}, {
			xtype: 'textareafield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '��ע',
			emptyText: '����Ϊ��',
			id: 'remark',
			name: 'remark',
			width: 350
		}, {
			xtype: 'combo',
			id: 'delflag',
			name: 'delFlag',
			editable: false,
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�Ƿ�ɾ��',
			valueField: 'value',
			displayField: 'name',
			triggerAction: 'all',
			width: 350,
			mode: 'local',
			store: new Ext.data.SimpleStore({
				fields: ['name', 'value'],
				data: [['��', true], ['��', false]]
			})
		}, {
			xtype: 'textfield',
			id: 'bookid',
			name: 'bookId',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '�鼮���',
			readOnly: true,
			hidden: true,
			disabled: true
		}];
	this.callParent();
	}
});