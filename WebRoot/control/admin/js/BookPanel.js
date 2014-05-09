Ext.define('BookPanel', {
	extend: 'Ext.grid.Panel',
	pageSize: 25,
	border: false,
	id: 'bpanel',
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
                url: 'control/admin/book.do?method=list',  //请求的服务器地址
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
            {header: "书籍编号",  dataIndex: 'bookid', sortable: true, hidden: true},
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
        	text: '添加',
        	id: 'mbsa',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	text: '修改',
        	id: 'mbse',
        	iconCls: 'edit',
        	handler: this.onEditHandler
        }, '-', {
        	text: '删除',
        	id: 'mbsd',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }, '-', {
        	text: '推荐',
        	id: 'mbsr',
        	iconCls: 'recom',
        	handler: this.onRecomHandler
        }, '-', {
        	text: '不推荐',
        	id: 'mbsnr',
        	iconCls: '_recom',
        	handler: this.onNRecomHandler
        }, '-', {
        	text: '上架',
        	id: 'mbsu',
        	//iconCls: 'down',
        	handler: this.onUpHandler
        }, '-', {
        	text: '下架',
        	id: 'mbsdw',
        	//iconCls: 'up',
        	handler: this.onDownHandler
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
					url: 'control/admin/check/book.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mbsa').disabled = !r.book;
						Ext.getCmp('mbse').disabled = !r.book;
						Ext.getCmp('mbsd').disabled = !r.book;
						Ext.getCmp('mbsr').disabled = !r.book;
						Ext.getCmp('mbsnr').disabled = !r.book;
						Ext.getCmp('mbsu').disabled = !r.book;
						Ext.getCmp('mbsdw').disabled = !r.book;
					}
				});
    		}
    	}
	},
	onAddHandler: function() {
		var win = new Ext.Window({
			width: 400,
			height: 500,
			modal: true,
			title: '添加书籍',
			id: 'badd',
			//closeAction: 'hide',
			layout: 'fit',
			buttonAlign: 'center',
			border: false,
			resizable: false,
			animateTarget: 'mbsa',
			items: new BookForm({id: 'bookform'}),				
			buttons: [{
				text: '保存',
				handler: function() {
					var form = Ext.getCmp('bookform').getForm();
					if(form.isValid()) {
						form.submit({
							clientValidation: true,
							url: 'control/admin/book.do?method=add',
							waitMsg: '正在提交数据',
							waitTiele: '提示',
							method: 'POST',
							success: function(form, action) {
								if(action.result.success) {
									Ext.MessageBox.show({
		                            	title: '提示',
		                            	msg: '书籍添加成功！',
		                            	icon: Ext.MessageBox.INFO,
		                            	buttons: Ext.MessageBox.OK
		                            });
									Ext.getCmp('bookform').findParentByType('window').close();
		                            Ext.getCmp('bpanel').getStore().reload();
								} else {
									Ext.MessageBox.show({
		                            	title: '提示',
		                            	msg: action.result.msg,
		                            	icon: Ext.MessageBox.ERROR,
		                            	buttons: Ext.MessageBox.OK
		                            });
								}
							}, 
							failure: function(form, action) {
								Ext.MessageBox.show({
	                            	title: '提示',
	                            	msg: '添加失败！',
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
			}, {
				text: '重置',
				handler: function() {
					Ext.getCmp('bookform').form.reset();
				}
			}]
		});
		Ext.getCmp('bookform').queryById('delflag').disabled = true;
		Ext.getCmp('bookform').queryById('delflag').hidden = true;
		win.show();
	},
	onDelHandler: function() {
		var sm = Ext.getCmp('bpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('提示', '您确定要删除所选记录吗？', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						if(s[i].get('delflag') == false) ids.push(s[i].get('bookid'));
					}
					if(ids.length == 0) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '没有需要删除的记录！',
							icon: Ext.MessageBox.WARNING,
							buttons: Ext.MessageBox.YES
						});
						return;
					}
					
					Ext.Ajax.request({
						url: 'control/admin/book.do?method=del',
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
								Ext.getCmp('bpanel').getStore().reload();
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
			});
			
		} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要删除的记录！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
		
	},
	onRecomHandler: function() {
		// recommend
		var sm = Ext.getCmp('bpanel').getSelectionModel();
		if(sm.hasSelection()) {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					if(s[i].get('recom') == false) ids.push(s[i].get('bookid'));
				}
				if(ids.length == 0) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '没有需要推荐的图书！',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/book.do?method=recom',
					params: {ids: ids, recom: true},
					method: 'POST',
					success: function(response, opt) {
						var res = Ext.JSON.decode(response.responseText);
						if(res.success) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '推荐成功！',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '提示',
								msg: '推荐失败！',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '推荐失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要推荐的图书！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	},
	onNRecomHandler: function() {
		// not recommend
		var sm = Ext.getCmp('bpanel').getSelectionModel();
		if(sm.hasSelection()) {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					if(s[i].get('recom') == true) ids.push(s[i].get('bookid'));
				}
				if(ids.length == 0) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '没有需要取消推荐的图书！',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/book.do?method=recom',
					params: {ids: ids, recom: false},
					method: 'POST',
					success: function(response, opt) {
						var res = Ext.JSON.decode(response.responseText);
						if(res.success) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '取消推荐成功！',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '提示',
								msg: '取消推荐失败！',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '取消推荐失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要取消推荐的图书！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	},
	onDownHandler: function() {
		var sm = Ext.getCmp('bpanel').getSelectionModel();
		if(sm.hasSelection()) {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					if(s[i].get('downtime') == 'null') ids.push(s[i].get('bookid'));
				}
				if(ids.length == 0) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '没有需要下架的图书！',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/book.do?method=updown',
					params: {ids: ids, down: true},
					method: 'POST',
					success: function(response, opt) {
						var res = Ext.JSON.decode(response.responseText);
						if(res.success) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '下架成功！',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '提示',
								msg: '下架失败！',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '下架失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要下架的图书！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	},
	onUpHandler: function() {
		var sm = Ext.getCmp('bpanel').getSelectionModel();
		if(sm.hasSelection()) {
				var s = sm.getSelection();
				var ids = [];
				for(var i=0; i<s.length; i++) {
					if(s[i].get('downtime') != 'null') ids.push(s[i].get('bookid'));
				}
				if(ids.length == 0) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '没有需要重新上架的图书！',
						icon: Ext.MessageBox.WARNING,
						buttons: Ext.MessageBox.YES
					});
					return;
				}
				
				Ext.Ajax.request({
					url: 'control/admin/book.do?method=updown',
					params: {ids: ids, down: false},
					method: 'POST',
					success: function(response, opt) {
						var res = Ext.JSON.decode(response.responseText);
						if(res.success) {
							Ext.MessageBox.show({
								title: '提示',
								msg: '重新上架成功！',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '提示',
								msg: '重新上架失败！',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '提示',
							msg: '重新上架失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择需要重新上架的图书！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	},
	onEditHandler: function() {
		var win = null;
		var sm = Ext.getCmp('bpanel').getSelectionModel();
		if(sm.hasSelection()) {
			var record = sm.getSelection();
			if(record.length > 1) {
				Ext.MessageBox.show({
					title: '提示',
					msg: '请选择一条记录！',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.OK
				});
				return;
			}
			
			win = new Ext.Window({
				title: '编辑',
				buttonAlign: 'center',
				modal: true,
				width: 400,
				height: 500,
				border: false,
				resizable: false,
				autoScroll: true,
				animateTarget: 'mbse',
				items: new BookForm(),
				buttons: [{
					text: '提交',
					handler: submit
				}, {
					text: '取消',
					handler: close
				}]
			});
			win.getComponent(0).queryById('bookid').disabled = false;
			win.getComponent(0).child('filefield').disabled = true;
			win.getComponent(0).child('filefield').hidden = true;
			
			win.getComponent(0).loadRecord(record[0]);
			//Ext.getCmp('remark').setValue((Ext.getCmp('remark').value=='null'?'':Ext.getCmp('remark').value));
			win.show();
		} else {
			Ext.MessageBox.show({
				title: '提示',
				msg: '请选择一条记录！',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.OK
			});
		}
		function close() { if(win != null) win.close(); }
		function submit() {
			var form = win.getComponent(0);
			form.submit({
				clientValidation: true,
				url: 'control/admin/book.do?method=update',
				waitMsg: '正在提交数据',
				waitTiele: '提示',
				method: 'POST',
				success: function(form, action) {
					Ext.MessageBox.show({
                    	title: '提示',
                    	msg: '书籍更新成功！',
                    	icon: Ext.MessageBox.INFO,
                    	buttons: Ext.MessageBox.OK
                    });
					win.close();
                    Ext.getCmp('bpanel').getStore().reload();
				}, 
				failure: function(form, action) {
					Ext.MessageBox.show({
                    	title: '提示',
                    	msg: '书籍更新失败！',
                    	icon: Ext.MessageBox.ERROR,
                    	buttons: Ext.MessageBox.OK
                    });
				}
			});
		}
	}
});


