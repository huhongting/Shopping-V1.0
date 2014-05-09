var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
    groupHeaderTpl: '{name} ({rows.length}条评论)',
    hideGroupedHeader: true,
    startCollapsed: true,
    groupByText: '以该字段分组',
    showGroupsText: '分组显示'
});

Ext.define('CommentPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
    features: [groupingFeature],
    id: 'cmpanel',
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		this.columns = [
			new Ext.grid.RowNumberer({header: '行号', width: 30}),
	        {text: '发布者', dataIndex: 'username', flex: 1, sortable: true},
	        {text: '图书名称', dataIndex: 'bookname', flex: 3, sortable: true, hidden: true},
	        {text: '发布时间', dataIndex: 'date', flex: 2, sortable: true},
	        {text: '评论标题', dataIndex: 'title', flex: 3, sortable: true},
	        {text: '评论内容', dataIndex: 'content', flex: 5, sortable: true},
	        {text: '认为有用', dataIndex: 'usefull', flex: 1, sortable: true},
	        {text: '认为没用', dataIndex: 'useless', flex: 1, sortable: true},
	        {text: '评论状态', dataIndex: 'delflag', flex: 1, sortable: true,
	        	renderer: function(value) {
	        		if(value == true) {
	        			return "<span style='color:red;font-weight:bold;font-size:12'>已删除</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
	        		} else {
	        			return "<span style='color:green;font-weight:bold;font-size:12'>可见</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
	        		}
	        	}
	        }
    	];
    	
    	this.tbar = [{
    		text: '设置可见',
    		id: 'mce',
    		iconCls: 'edit',
    		handler: this.onShowHandler
    	}, {
        	text: '删除',
        	id: 'mcd',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }];
    	
		this.store = Ext.create('Ext.data.Store', {
			pageSize: this.pageSize,
		    fields: ['cid', 'username', 'bookname', 'date', 'title', 'content', 'usefull', 'useless', 'delflag'],
		    groupField: 'bookname',
		    proxy: {
		        type: 'ajax',
		        url: 'control/admin/comment.do?method=list',
		        reader: {
		            type: 'json',
		            root: 'records',
		            totalProperty: 'count'
		        }
		    },
		    autoLoad: {
		    	start: 0,
		    	limit: this.pageSize
		    }
		});
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
					url: 'control/admin/check/comment.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mce').disabled = !r.comment;
						Ext.getCmp('mcd').disabled = !r.comment;
					}
				});
    		}
    	}
	},
	onDelHandler: function() {
		var sm = Ext.getCmp('cmpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('提示', '您确定要删除所选评论吗？', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						ids.push(s[i].get('cid'));
					}
					Ext.Ajax.request({
						url: 'control/admin/comment.do?method=del',
						params: {id: ids},
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
								Ext.getCmp('cmpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '提示',
									msg: '删除失败！',
									icon: Ext.MessageBox.WARNING,
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
			});
			
		} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要删除的评论！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	},
	onShowHandler: function() {
		var sm = Ext.getCmp('cmpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('提示', '您确定要显示所选评论吗？', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						ids.push(s[i].get('cid'));
					}
					Ext.Ajax.request({
						url: 'control/admin/comment.do?method=show',
						params: {id: ids},
						method: 'POST',
						success: function(response, opt) {
							var res = Ext.JSON.decode(response.responseText);
							if(res.success) {
								Ext.MessageBox.show({
									title: '提示',
									msg: '设置可见成功！',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('cmpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '提示',
									msg: '设置可见失败！',
									icon: Ext.MessageBox.WARNING,
									buttons: Ext.MessageBox.YES
								});
							}
						},
						failure: function(response, opt) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '设置可见失败！',
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
				msg: '请选择需要显示的评论！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	}
});
