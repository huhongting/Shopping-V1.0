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
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/book.do?method=listdel',  //请求的服务器地址
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
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "书籍编号",  dataIndex: 'bookid', sortable: true},
            {header: "书籍名称", dataIndex: 'bookname', sortable: true},
            {header: "作者", dataIndex: 'auther', sortable: true},
            {header: "类别", dataIndex: 'booktype', sortable: true},
            {header: "出版社", dataIndex: 'press', sortable: true},
            {header: "出版时间", dataIndex: 'pubtime', sortable: true},
            {header: "售价(元/本)", dataIndex: 'price', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "市场价(元/本)", dataIndex: 'normalprice', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "销量(本)", dataIndex: 'salecount', sortable: true},
            {header: "库存量(本)", dataIndex: 'number', sortable: true, 
            	renderer: function(value) {
            		if(value < 5) {
            			return "<span style='color:red;'>" + value + "</span>";
            		} else {
            			return "<span style='color:green;'>" + value + "</span>";
            		}
            	}
            },
            {header: "促销信息", dataIndex: 'saleinfo', sortable: true, hidden: true},
            {header: "书籍描述", dataIndex: 'note', sortable: true, hidden: true},
            {header: "备注", dataIndex: 'remark', sortable: true, hidden: true,
            	renderer: function(value) {
            		if(value == 'null') {
            			return '';
            		} else {
            			return value;
            		}
            	}
            },
            {header: "推荐", dataIndex: 'recom', sortable: true,
	            renderer: function(value) {
	            	if(value == false) {
	            		return "<img src='/resources/images/_recom.png'/>";
	            	} else {
	            		return "<img src='/resources/images/recom.png'/>";
	            	}
	            }
            },
            {header: "上架时间", dataIndex: 'uptime', sortable: true,
            	renderer: function(value) {
            		return Ext.String.splitWords(value)[0];
            	}
            },
            {header: "下架时间", dataIndex: 'downtime', sortable: true,
	            renderer: function(value) {
	            	if(value == 'null') {
	            		return "<span style='color:green;'>销售中...</span>";
	            	} else {
	            		return "<span style='color:red;'>" + Ext.String.splitWords(value)[0] + "</span>";
	            	}
	            }
            },
            {header: "状态", dataIndex: 'delflag', sortable: true,
	            renderer: function(value) {
	            	if(value == false) {
	            		return "<span style='color:green;font-weight:bold;font-size:12'>可用</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
	            	} else {
	            		return "<span style='color:red;font-weight:bold;font-size:12'>不可用</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
	            	}
	            }
            }
        ];
		this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
		this.tbar = [{
        	text: '恢复',
        	id: 'mdbe',
        	iconCls: 'edit',
        	handler: this.onResetHandler
        }, '->', {
        	fieldLabel: '名称/作者搜索',
        	labelWidth: 90,
        	xtype: 'searchfield',
        	width: 300,
        	emptyText: '请输入书籍名称或作者',
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
			Ext.MessageBox.confirm('提示', '确定要恢复所选记录吗？', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						ids.push(s[i].get('bookid'));
					}
					if(ids.length == 0) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '没有需要恢复的记录！',
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
									title: '提示',
									msg: '恢复成功！',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('dbpanel').getStore().reload();
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
});


