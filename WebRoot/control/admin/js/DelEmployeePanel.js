Ext.define('DelEmployeePanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'delemppanel',
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
            fields:['userid', 'username', 'email', 'usertype', 'regtime', 'note'],
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/employee.do?method=listdel',  //请求的服务器地址
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
            {header: "用户类型", dataIndex: 'usertype', sortable: true,
            	renderer: function(value) {
            		if(value  == 2) {
            			return "<font color=red>超级管理员</font>";
            		} else if(value == 1) {
            			return "<font color=green>公司职员</font>";
            		} else {
            			return "";
            		}
            	}
            },
            {header: "注册时间", dataIndex: 'regtime', sortable: true},
            {header: "其他说明", dataIndex: 'note', sortable: false}
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		
		this.tbar = [{
			text: '恢复',
			id: 'mdee',
			iconCls: 'edit',
			handler: resetEmp
		}, '->', {
        	fieldLabel: '姓名搜索',
        	labelWidth: 60,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '请输入员工姓名',
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
        
        this.addListener('itemdblclick', this.click, this);
        this.addListener('itemcontextmenu', function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		}, this);
        this.forceFit = true;
		this.callParent();
	},
	listeners: {
		afterrender: {
    		fn: function() {
    			Ext.Ajax.request({
					url: 'control/admin/check/delemployee.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mdee').disabled = !r.delemp;
					}
				});
    		}
    	}
	},
	click: function (grid, record) {
		var uid = record.get('userid');
		var w = new Ext.Window({
			title: '员工信息',
			width: 600,
			modal: true,
			resizable: false,
			animateTarget: 'delemppanel',
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

function resetEmp() {
	var sm = Ext.getCmp('delemppanel').getSelectionModel();
	if(sm.hasSelection()) {
		Ext.MessageBox.confirm('提示', '您确定要恢复所选记录吗？', function(btn) {
			if(btn == 'yes') {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					ids.push(s[i].get('userid'));
				}
				if(ids.length == 0 || ids.length > 1) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '请选择一条记录！',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/employee.do?method=reset',
					params: {id: ids},
					method: 'POST',
					success: function(response, opt) {
						var res = Ext.JSON.decode(response.responseText);
						if(res.success) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '恢复成功！',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('delemppanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '提示',
								msg: '恢复失败！',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '恢复失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			}
		});
		
	} else {
		Ext.MessageBox.show({
			title: '提示',
			msg: '请选择需要恢复的记录！',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	}
}
