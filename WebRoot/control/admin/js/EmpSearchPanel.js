Ext.define('EmpSearchPanel', {
	extend: 'Ext.grid.Panel',
	border: false,
	pageSize: 25,
	id: 'espanel',
	autoScroll: true,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "用户编号", dataIndex: 'userid', sortable: true},
            {header: "用户名称", dataIndex: 'username', sortable: true},
            {header: "电子邮件", dataIndex: 'email', sortable: true},
            {header: "用户类型", dataIndex: 'usertype', sortable: true,
            	renderer: function(value) {
            		if(value  == 2) {
            			return "<font color=red>超级管理员</font>";
            		} else if(value == 1) {
            			return "<font color=green>公司职员</font>";
            		} else {
            			return value;
            		}
            	}
            },
            {header: "注册时间", dataIndex: 'regtime', sortable: true},
            {header: "其他说明", dataIndex: 'note', sortable: false}
        ];
        
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            id: 'esptb',
            store: this.store,   //这里需要指定与表格相同的store
            displayInfo: true,
            beforePageText: '第',
            afterPageText: '页/共 {0} 页',
            displayMsg: '第 {0} 条到 {1} 条记录，共 {2} 条记录',
      		emptyMsg: "没有记录"
        }];
        this.addListener('itemdblclick', this.click, this);
        this.addListener('itemcontextmenu', function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		}, this);
        this.forceFit = true;
		this.callParent();
	},
	click: function (grid, record) {
		var uid = record.get('userid');
		var w = new Ext.Window({
			title: '员工信息',
			width: 600,
			modal: true,
			resizable: false,
			animateTarget: 'espanel',
			items: [new EmpInfo()],
			buttonAlign: 'center',
			border: false,
			buttons: [{
				text: '关闭',
				handler: function() {
					w.close();
				}
			}]
		});
		w.getComponent(0).getForm().load({
			url: 'control/admin/employeeinfo.do?uid=' + uid,
			method: 'POST',
			autoLoad: true,
			success: function(form, action) {
				//Ext.Msg.alert('提示', '加载成功！');
			},
			failure: function(form, action) {
				Ext.Msg.alert('提示', '加载失败！');
			}
		});
		w.show();
	}
});