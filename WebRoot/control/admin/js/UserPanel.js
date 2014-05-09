Ext.define('UserPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'upanel',
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
            fields:['userid', 'username', 'email', 'usertype', 'regtime', 'lastlogin', 'note'],
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/user.do?method=list',  //����ķ�������ַ
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
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "�û����", dataIndex: 'userid', sortable: true},
            {header: "�û�����", dataIndex: 'username', sortable: true},
            {header: "�����ʼ�", dataIndex: 'email', sortable: true},
            {header: "�û�����", dataIndex: 'usertype', sortable: true, hidden: true},
            {header: "ע��ʱ��", dataIndex: 'regtime', sortable: true},
            {header: "�ϴε���ʱ��", dataIndex: 'lastlogin', sortable: true},
            {header: "����˵��", dataIndex: 'note', sortable: false},
            {header: '����', dataIndex: 'userid',
            	renderer: function(value) {
            		return '<div align="center">' +
            				'<a href="javascript:delUser(' + value + ');">����</a>' +
            				'</div>';
            	}
            }
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
        	fieldLabel: '��������',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '�������û�����',
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
		}
	}
});

function delUser(id) {
	Ext.Ajax.request({
		url: 'control/admin/check/user.do',
		async: false,
		method: 'POST',
		success: function(response, opt) {
			var r = Ext.JSON.decode(response.responseText);
			if(!r.user) return;
			else {
				Ext.MessageBox.confirm('��ʾ', 'ȷ��Ҫ���ø��û���', 
					function(btn) {
						if(btn == 'yes') {
							Ext.Ajax.request({
									url: 'control/admin/user.do?method=del',
									params: {id: id},
									method: 'POST',
									success: function(response, opt) {
										var res = Ext.JSON.decode(response.responseText);
										if(res.success == true)
											Ext.getCmp('upanel').getStore().reload();
										else {
											Ext.MessageBox.show({
												title: '��ʾ',
												msg: '���ò���ʧ�ܣ�',
												icon: Ext.MessageBox.ERROR,
												buttons: Ext.MessageBox.YES
											});
										}
									},
									failure: function(response, opt) {
										Ext.MessageBox.show({
											title: '��ʾ',
											msg: '���ò���ʧ�ܣ�',
											icon: Ext.MessageBox.ERROR,
											buttons: Ext.MessageBox.YES
										});
									}
								});
						}
					}
				);
			}
		},
		failure: function(response, opt) {
			return;
		}
	});
}

