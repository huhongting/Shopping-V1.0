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
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/user.do?method=list',  //请求的服务器地址
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
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "用户编号", dataIndex: 'userid', sortable: true},
            {header: "用户名称", dataIndex: 'username', sortable: true},
            {header: "电子邮件", dataIndex: 'email', sortable: true},
            {header: "用户类型", dataIndex: 'usertype', sortable: true, hidden: true},
            {header: "注册时间", dataIndex: 'regtime', sortable: true},
            {header: "上次登入时间", dataIndex: 'lastlogin', sortable: true},
            {header: "其他说明", dataIndex: 'note', sortable: false},
            {header: '操作', dataIndex: 'userid',
            	renderer: function(value) {
            		return '<div align="center">' +
            				'<a href="javascript:delUser(' + value + ');">禁用</a>' +
            				'</div>';
            	}
            }
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
        	fieldLabel: '姓名搜索',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '请输入用户姓名',
        	store: this.store
        }];
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            store: this.store,   //这里需要指定与表格相同的store
            displayInfo: true,
            beforePageText: '第',
            afterPageText: '页/共 {0} 页',
            displayMsg: '第 {0} 条到 {1} 条记录，共 {2} 条记录',
      		emptyMsg: "没有记录"
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
				Ext.MessageBox.confirm('提示', '确定要禁用该用户？', 
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
												title: '提示',
												msg: '禁用操作失败！',
												icon: Ext.MessageBox.ERROR,
												buttons: Ext.MessageBox.YES
											});
										}
									},
									failure: function(response, opt) {
										Ext.MessageBox.show({
											title: '提示',
											msg: '禁用操作失败！',
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

