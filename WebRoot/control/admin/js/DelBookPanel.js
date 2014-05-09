Ext.define('DelBookPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'dbpanel',
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		this.store = Ext.create('Ext.data.Store', {
            autoLoad: {
            	start: 0, 
            	limit: this.pageSize
            },
            fields:['bookid', 'bookname', 'saleinfo', 
            		'auther', 'pubtime', 'note', 
            		'price', 'normalprice', 'salecount', 'remark', 'press', 
            		'booktype', 'delflag', 'number', 'recom', 
            		'uptime', 'downtime'],
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/book.do?method=listdel',  //����ķ�������ַ
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'bookid',
            	direction: 'ASC'
            }]
        });
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "�鼮���",  dataIndex: 'bookid', sortable: true},
            {header: "�鼮����", dataIndex: 'bookname', sortable: true},
            {header: "����", dataIndex: 'auther', sortable: true},
            {header: "���", dataIndex: 'booktype', sortable: true},
            {header: "������", dataIndex: 'press', sortable: true},
            {header: "����ʱ��", dataIndex: 'pubtime', sortable: true},
            {header: "�ۼ�(Ԫ/��)", dataIndex: 'price', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "�г���(Ԫ/��)", dataIndex: 'normalprice', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "����(��)", dataIndex: 'salecount', sortable: true},
            {header: "�����(��)", dataIndex: 'number', sortable: true, 
            	renderer: function(value) {
            		if(value < 5) {
            			return "<span style='color:red;'>" + value + "</span>";
            		} else {
            			return "<span style='color:green;'>" + value + "</span>";
            		}
            	}
            },
            {header: "������Ϣ", dataIndex: 'saleinfo', sortable: true, hidden: true},
            {header: "�鼮����", dataIndex: 'note', sortable: true, hidden: true},
            {header: "��ע", dataIndex: 'remark', sortable: true, hidden: true,
            	renderer: function(value) {
            		if(value == 'null') {
            			return '';
            		} else {
            			return value;
            		}
            	}
            },
            {header: "�Ƽ�", dataIndex: 'recom', sortable: true,
	            renderer: function(value) {
	            	if(value == false) {
	            		return "<img src='/resources/images/_recom.png'/>";
	            	} else {
	            		return "<img src='/resources/images/recom.png'/>";
	            	}
	            }
            },
            {header: "�ϼ�ʱ��", dataIndex: 'uptime', sortable: true,
            	renderer: function(value) {
            		return Ext.String.splitWords(value)[0];
            	}
            },
            {header: "�¼�ʱ��", dataIndex: 'downtime', sortable: true,
	            renderer: function(value) {
	            	if(value == 'null') {
	            		return "<span style='color:green;'>������...</span>";
	            	} else {
	            		return "<span style='color:red;'>" + Ext.String.splitWords(value)[0] + "</span>";
	            	}
	            }
            },
            {header: "״̬", dataIndex: 'delflag', sortable: true,
	            renderer: function(value) {
	            	if(value == false) {
	            		return "<span style='color:green;font-weight:bold;font-size:12'>����</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
	            	} else {
	            		return "<span style='color:red;font-weight:bold;font-size:12'>������</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
	            	}
	            }
            }
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
        	text: '�ָ�',
        	id: 'mdbe',
        	iconCls: 'edit',
        	handler: this.onResetHandler
        }, '->', {
        	fieldLabel: '����/��������',
        	labelWidth: 90,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '�������鼮���ƻ�����',
        	store: this.store
        }];
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            store: this.store,   //������Ҫָ��������ͬ��store
            displayInfo: true,
            beforePageText: '��',
            afterPageText: 'ҳ/�� {0} ҳ',
            displayMsg: '�� {0} ���� {1} ����¼���� {2} ����¼',
      		emptyMsg: "û�м�¼"
        }];
        this.forceFit = true;
		this.callParent();
	},
	listeners: {
		'itemcontextmenu': function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		},
		afterrender: {
    		fn: function() {
    			Ext.Ajax.request({
					url: 'control/admin/check/delbook.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mdbe').disabled = !r.delbook;
					}
				});
    		}
    	}
	},
	onSearchHandler: function() {
		Ext.Msg.alert('Key', Ext.getCmp('_search').getValue());
	},
	onResetHandler: function() {
		var sm = Ext.getCmp('dbpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫ�ָ���ѡ��¼��', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						ids.push(s[i].get('bookid'));
					}
					if(ids.length == 0) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: 'û����Ҫ�ָ��ļ�¼��',
							icon: Ext.MessageBox.WARNING,
							buttons: Ext.MessageBox.YES
						});
						return;
					}
					
					Ext.Ajax.request({
						url: 'control/admin/book.do?method=reset',
						params: {id: ids},
						method: 'POST',
						success: function(response, opt) {
							var res = Ext.JSON.decode(response.responseText);
							if(res.success) {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: '�ָ��ɹ���',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('dbpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: '�ָ�ʧ�ܣ�',
									icon: Ext.MessageBox.ERROR,
									buttons: Ext.MessageBox.YES
								});
							}
						},
						failure: function(response, opt) {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '�ָ�ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					});
				}
			});
			
		} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ����Ҫ�ָ��ļ�¼��',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	}
});


