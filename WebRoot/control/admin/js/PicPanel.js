Ext.define('PicPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'picpanel',
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
            fields:['id', 'name', 'action', 'uptime', 'isshow', 'url', 'note'],
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/picture.do?method=list',  //请求的服务器地址
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'uptime',
            	direction: 'DESC'
            }]
        });
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "编号", dataIndex: 'id', sortable: true},
            {header: "名称", dataIndex: 'name', sortable: true},
            {header: "链接", dataIndex: 'action', sortable: true},
            {header: "上传时间", dataIndex: 'uptime', sortable: true},
            {header: "首页显示", dataIndex: 'isshow', sortable: true,
            	renderer: function(value) {
            		if(value == true || value == 'true') 
            			return "<font color=red>显示</font>";
					return "不显示";            		
            	}
            },
            {header: "图片路径", dataIndex: 'url', sortable: true,
            	renderer: function(value) {
            		return value;
            	}
            },
            {header: "其他说明", dataIndex: 'note', sortable: false}
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
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
        this.tbar = [{
        	id: 'mspa',
        	text: '添加',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	id: 'mspr',
        	text: '显示',
        	iconCls: 'recom',
        	handler: this.onShowPic
        }, '-', {
        	id: 'mspnr',
        	text: '不显示',
        	iconCls: '_recom',
        	handler: this.onNotShowPic
        }, '-', {
        	id: 'mspd',
        	text: '删除',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }];
        
        this.listeners = {
        	'itemcontextmenu': function(view, record, item, index, e, opt) {
				e.preventDefault();
				e.stopEvent();
			},
        	itemdblclick: {fn: this.onShow},
        	afterrender: {
        		fn: function() {
        			Ext.Ajax.request({
						url: 'control/admin/check/webpicshow.do',
						async: false,
						method: 'POST',
						success: function(response, opt) {
							var r = Ext.JSON.decode(response.responseText);
							var v = !r.webpicshow;
							Ext.getCmp('mspa').disabled = v;
							Ext.getCmp('mspr').disabled = v;
							Ext.getCmp('mspnr').disabled = v;
							Ext.getCmp('mspd').disabled = v;
						}
					});	
				}
			}
        };
        
        this.forceFit = true;
		this.callParent();
	},
	onShow: function() {
		var sm = Ext.getCmp('picpanel').getSelectionModel();
		var record = sm.getSelection();
		var w = new Ext.Window({
			title: '图片预览',
			width: 650,
			height: 350,
			modal: true,
			border: false,
			animateTarget: 'picpanel',
			items: [{
				xtype: 'box',
				width: 650,
				height: 350,
				autoEl: {
					tag: 'img',
					src: record[0].get('url')
				}
			}]
		});
		w.show();
	},
	onAddHandler: function() {
		var win = new Ext.Window({
			width: 330,
			height: 243,
			modal: true,
			title: '添加',
			id: 'picadd',
			label: 'form',
			animateTarget: 'mspa',
			//closeAction: 'hide',
			buttonAlign: 'center',
			border: false,
			resizable: false,
			items: [new PicForm({id: 'picform'})],
			buttons: [{
				text: '保存',
				handler: savePic
			}, {
				text: '重置',
				handler: function() {
					Ext.getCmp('picform').form.reset();
				}
			}]
		});
		win.show();
	},
	onDelHandler: function() {
		var ids = getSelectionItems('picpanel', 'id');
		if(ids == null) return;
		else {
			Ext.MessageBox.confirm('提示', '您确认要删除图片？', function(btn) {
				if(btn == 'yes')
					delPic('control/admin/picture.do?method=del', ids);
			});
		}
	},
	onShowPic: function() {
		var ids = getSelectionItems('picpanel', 'id')
		if(ids == null) return;
		else {
			updatePicShow('control/admin/picture.do?method=updateshow', ids, true);
		}
	},
	onNotShowPic: function() {
		var ids = getSelectionItems('picpanel', 'id')
		if(ids == null) return;
		else {
			updatePicShow('control/admin/picture.do?method=updateshow', ids, false);
		}
	}
});


function savePic() {
	var form = Ext.getCmp('picform').getForm();
	if(form.isValid()) {
		form.submit({
			clientValidation: true,
			url: 'control/admin/picture.do?method=add',
			waitMsg: '正在提交数据',
			waitTiele: '提示',
			method: 'POST',
			success: function(form, action) {
				Ext.MessageBox.show({
                	title: '提示',
                	msg: '图片添加成功！',
                	icon: Ext.MessageBox.INFO,
                	buttons: Ext.MessageBox.OK
                });
				Ext.getCmp('picadd').close();
                Ext.getCmp('picpanel').getStore().reload();
			}, 
			failure: function(form, action) {
				Ext.MessageBox.show({
                	title: '提示',
                	msg: '图片添加失败！',
                	icon: Ext.MessageBox.ERROR,
                	buttons: Ext.MessageBox.OK
                });
			}
		});
	} else {
		Ext.MessageBox.show({
        	title: '提示',
        	msg: '输入有误，请重新输入!',
        	icon: Ext.MessageBox.WARNING,
        	buttons: Ext.MessageBox.OK
        });
	}
}

function updatePicShow(url, ids, value) {
	Ext.Ajax.request({
		url: url,
		params: {ids: ids, show: value},
		method: 'POST',
		success: function(response, opt) {
			var res = Ext.JSON.decode(response.responseText);
			if(res.success) {
				Ext.MessageBox.show({
					title: '提示',
					msg: '已设置为首页显示！',
					icon: Ext.MessageBox.INFO,
					buttons: Ext.MessageBox.YES
				});
				Ext.getCmp('picpanel').getStore().reload();
			} else {
				Ext.MessageBox.show({
					title: '提示',
					msg: '设置首页显示失败！',
					icon: Ext.MessageBox.ERROR,
					buttons: Ext.MessageBox.YES
				});
			}
		},
		failure: function(response, opt) {
			Ext.MessageBox.show({
				title: '提示',
				msg: '设置首页显示失败！',
				icon: Ext.MessageBox.ERROR,
				buttons: Ext.MessageBox.YES
			});
		}
	});
}
function delPic(url, ids) {
	Ext.Ajax.request({
		url: url,
		params: {ids: ids},
		method: 'POST',
		success: function(response, opt) {
			var res = Ext.JSON.decode(response.responseText);
			if(res.success) {
				Ext.MessageBox.show({
					title: '提示',
					msg: '删除成功！',
					icon: Ext.MessageBox.INFO,
					buttons: Ext.MessageBox.YES
				});
				Ext.getCmp('picpanel').getStore().reload();
			} else {
				Ext.MessageBox.show({
					title: '提示',
					msg: '删除失败！',
					icon: Ext.MessageBox.ERROR,
					buttons: Ext.MessageBox.YES
				});
			}
		},
		failure: function(response, opt) {
			Ext.MessageBox.show({
				title: '提示',
				msg: '删除失败！',
				icon: Ext.MessageBox.ERROR,
				buttons: Ext.MessageBox.YES
			});
		}
	});
}
function getSelectionItems(comid, colid) {
	var sm = Ext.getCmp(comid).getSelectionModel();
	if(sm.hasSelection()) {
		var s = sm.getSelection();
		var ids = [];
		for(var i=0; i<s.length; i++) {
			ids.push(s[i].get(colid));
		}
		if(ids.length == 0) {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请至少选择一条记录！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
			return null;
		} else {
			return ids;
		}
	} else {
		Ext.MessageBox.show({
			title: '提示',
			msg: '请至少选择一条记录！',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	}
}
