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
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/book.do?method=list',  //����ķ�������ַ
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
            {header: "�鼮���",  dataIndex: 'bookid', sortable: true, hidden: true},
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
        	text: '���',
        	id: 'mbsa',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	text: '�޸�',
        	id: 'mbse',
        	iconCls: 'edit',
        	handler: this.onEditHandler
        }, '-', {
        	text: 'ɾ��',
        	id: 'mbsd',
        	iconCls: 'del',
        	handler: this.onDelHandler
        }, '-', {
        	text: '�Ƽ�',
        	id: 'mbsr',
        	iconCls: 'recom',
        	handler: this.onRecomHandler
        }, '-', {
        	text: '���Ƽ�',
        	id: 'mbsnr',
        	iconCls: '_recom',
        	handler: this.onNRecomHandler
        }, '-', {
        	text: '�ϼ�',
        	id: 'mbsu',
        	//iconCls: 'down',
        	handler: this.onUpHandler
        }, '-', {
        	text: '�¼�',
        	id: 'mbsdw',
        	//iconCls: 'up',
        	handler: this.onDownHandler
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
			title: '����鼮',
			id: 'badd',
			//closeAction: 'hide',
			layout: 'fit',
			buttonAlign: 'center',
			border: false,
			resizable: false,
			animateTarget: 'mbsa',
			items: new BookForm({id: 'bookform'}),				
			buttons: [{
				text: '����',
				handler: function() {
					var form = Ext.getCmp('bookform').getForm();
					if(form.isValid()) {
						form.submit({
							clientValidation: true,
							url: 'control/admin/book.do?method=add',
							waitMsg: '�����ύ����',
							waitTiele: '��ʾ',
							method: 'POST',
							success: function(form, action) {
								if(action.result.success) {
									Ext.MessageBox.show({
		                            	title: '��ʾ',
		                            	msg: '�鼮��ӳɹ���',
		                            	icon: Ext.MessageBox.INFO,
		                            	buttons: Ext.MessageBox.OK
		                            });
									Ext.getCmp('bookform').findParentByType('window').close();
		                            Ext.getCmp('bpanel').getStore().reload();
								} else {
									Ext.MessageBox.show({
		                            	title: '��ʾ',
		                            	msg: action.result.msg,
		                            	icon: Ext.MessageBox.ERROR,
		                            	buttons: Ext.MessageBox.OK
		                            });
								}
							}, 
							failure: function(form, action) {
								Ext.MessageBox.show({
	                            	title: '��ʾ',
	                            	msg: '���ʧ�ܣ�',
	                            	icon: Ext.MessageBox.ERROR,
	                            	buttons: Ext.MessageBox.OK
	                            });
							}
						});
					} else {
						Ext.MessageBox.show({
                        	title: '��ʾ',
                        	msg: '������������������!',
                        	icon: Ext.MessageBox.WARNING,
                        	buttons: Ext.MessageBox.OK
                        });
					}
				}
			}, {
				text: '����',
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
			Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����ѡ��¼��', function(btn) {
				if(btn == 'yes') {
					var s = sm.getSelection();
					var ids = [];
					for(var i=0; i<s.length; i++) {
						if(s[i].get('delflag') == false) ids.push(s[i].get('bookid'));
					}
					if(ids.length == 0) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: 'û����Ҫɾ���ļ�¼��',
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
									title: '��ʾ',
									msg: 'ɾ���ɹ���',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('bpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: 'ɾ��ʧ�ܣ�',
									icon: Ext.MessageBox.ERROR,
									buttons: Ext.MessageBox.YES
								});
							}
						},
						failure: function(response, opt) {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: 'ɾ��ʧ�ܣ�',
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
				msg: '��ѡ����Ҫɾ���ļ�¼��',
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
						title: '��ʾ',
						msg: 'û����Ҫ�Ƽ���ͼ�飡',
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
								title: '��ʾ',
								msg: '�Ƽ��ɹ���',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '�Ƽ�ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�Ƽ�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ����Ҫ�Ƽ���ͼ�飡',
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
						title: '��ʾ',
						msg: 'û����Ҫȡ���Ƽ���ͼ�飡',
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
								title: '��ʾ',
								msg: 'ȡ���Ƽ��ɹ���',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: 'ȡ���Ƽ�ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: 'ȡ���Ƽ�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ����Ҫȡ���Ƽ���ͼ�飡',
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
						title: '��ʾ',
						msg: 'û����Ҫ�¼ܵ�ͼ�飡',
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
								title: '��ʾ',
								msg: '�¼ܳɹ���',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '�¼�ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�¼�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ����Ҫ�¼ܵ�ͼ�飡',
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
						title: '��ʾ',
						msg: 'û����Ҫ�����ϼܵ�ͼ�飡',
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
								title: '��ʾ',
								msg: '�����ϼܳɹ���',
								icon: Ext.MessageBox.INFO,
								buttons: Ext.MessageBox.YES
							});
							Ext.getCmp('bpanel').getStore().reload();
						} else {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '�����ϼ�ʧ�ܣ�',
								icon: Ext.MessageBox.ERROR,
								buttons: Ext.MessageBox.YES
							});
						}
					},
					failure: function(response, opt) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�����ϼ�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				});
			} else {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '��ѡ����Ҫ�����ϼܵ�ͼ�飡',
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
					title: '��ʾ',
					msg: '��ѡ��һ����¼��',
					icon: Ext.MessageBox.WARNING,
					buttons: Ext.MessageBox.OK
				});
				return;
			}
			
			win = new Ext.Window({
				title: '�༭',
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
					text: '�ύ',
					handler: submit
				}, {
					text: 'ȡ��',
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
				title: '��ʾ',
				msg: '��ѡ��һ����¼��',
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
				waitMsg: '�����ύ����',
				waitTiele: '��ʾ',
				method: 'POST',
				success: function(form, action) {
					Ext.MessageBox.show({
                    	title: '��ʾ',
                    	msg: '�鼮���³ɹ���',
                    	icon: Ext.MessageBox.INFO,
                    	buttons: Ext.MessageBox.OK
                    });
					win.close();
                    Ext.getCmp('bpanel').getStore().reload();
				}, 
				failure: function(form, action) {
					Ext.MessageBox.show({
                    	title: '��ʾ',
                    	msg: '�鼮����ʧ�ܣ�',
                    	icon: Ext.MessageBox.ERROR,
                    	buttons: Ext.MessageBox.OK
                    });
				}
			});
		}
	}
});


