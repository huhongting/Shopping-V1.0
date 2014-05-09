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
			fieldLabel: '书籍名称',
			id: 'bookname',
			name: 'bookName',
			allowBlank: false,
			emptyText: '请填写书籍名称',
			blankText: '书籍名称不能为空！',
			width: 350
		}, {
			xtype: 'textfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '作者',
			id: 'auther',
			name: 'auther',
			allowBlank: false,
			emptyText: '请填写书籍作者',
			blankText: '书籍作者不能为空！',
			width: 350
		}, {
			xtype: 'textfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '书籍类别',
			id: 'booktype',
			name: 'bookType',
			allowBlank: false,
			emptyText: '请填写书籍类别',
			blankText: '书籍类别不能为空！',
			width: 350,
			listeners: {
				focus: {
					fn: function(com, e, o) {
						// show book type tree
						var win = new Ext.Window({
							title: '书籍类别',
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
									text: '确定',
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
			emptyText: '请选择出版社',
			fieldLabel: '出版社',
			allowBlank: false,
			blankText: '出版社不能为空！',
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
			fieldLabel: '出版时间',
			emptyText: '请填写出版时间',
			id: 'pubtime',
			name: 'pubTime',
			allowBlank: false,
			blankText: '出版时间不能为空！',
			width: 350
		}, {
			xtype: 'numberfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '售价',
			id: 'price',
			name: 'price',
			allowBlank: false,
			minValue: 0,
			negativeText: '价格不能为负！',
			emptyText: '请填写书籍价格',
			blankText: '书籍价格不能为空！',
			width: 350
		}, {
			xtype: 'numberfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '市场价格',
			minValue: 0,
			negativeText: '价格不能为负！',
			emptyText: '请填写市场价格',
			allowBlank: false,
			blankText: '市场价格不能为空！',
			name: 'normalPrice',
			id: 'normalprice',
			width: 350
		}, {
			xtype: 'numberfield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '库存',
			minValue: 1,
			negativeText: '库存不能为负！',
			emptyText: '请填写库存',
			allowBlank: false,
			blankText: '库存不能为空！',
			name: 'number',
			id: 'number',
			width: 350
		}, {
			xtype: 'filefield',
			buttonText: '选择图片',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '图片',
			id: 'picurl',
			name: 'pic',
			allowBlank: false,
			emptyText: '请上传书籍封面',
			blankText: '书籍封面不能为空！',
			width: 350
		}, {
			xtype: 'htmleditor',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '促销信息',
			emptyText: '可以为空',
			name: 'saleInfo',
			id: 'saleinfo',
			width: 350
		}, {
			xtype: 'htmleditor',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '书籍描述',
			emptyText: '可以为空',
			id: 'note',
			name: 'note',
			width: 350
		}, {
			xtype: 'textareafield',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '备注',
			emptyText: '可以为空',
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
			fieldLabel: '是否删除',
			valueField: 'value',
			displayField: 'name',
			triggerAction: 'all',
			width: 350,
			mode: 'local',
			store: new Ext.data.SimpleStore({
				fields: ['name', 'value'],
				data: [['是', true], ['否', false]]
			})
		}, {
			xtype: 'textfield',
			id: 'bookid',
			name: 'bookId',
			labelAlign: 'right',
			labelWidth: 70,
			fieldLabel: '书籍编号',
			readOnly: true,
			hidden: true,
			disabled: true
		}];
	this.callParent();
	}
});