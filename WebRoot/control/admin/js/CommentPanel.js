var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
    groupHeaderTpl: '{name} ({rows.length}������)',
    hideGroupedHeader: true,
    startCollapsed: true,
    groupByText: '�Ը��ֶη���',
    showGroupsText: '������ʾ'
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
			new Ext.grid.RowNumberer({header: '�к�', width: 30}),
	        {text: '������', dataIndex: 'username', flex: 1, sortable: true},
	        {text: 'ͼ������', dataIndex: 'bookname', flex: 3, sortable: true, hidden: true},
	        {text: '����ʱ��', dataIndex: 'date', flex: 2, sortable: true},
	        {text: '���۱���', dataIndex: 'title', flex: 3, sortable: true},
	        {text: '��������', dataIndex: 'content', flex: 5, sortable: true},
	        {text: '��Ϊ����', dataIndex: 'usefull', flex: 1, sortable: true},
	        {text: '��Ϊû��', dataIndex: 'useless', flex: 1, sortable: true},
	        {text: '����״̬', dataIndex: 'delflag', flex: 1, sortable: true,
	        	renderer: function(value) {
	        		if(value == true) {
	        			return "<span style='color:red;font-weight:bold;font-size:12'>��ɾ��</span>" +
	            				"&nbsp;<img src='/resources/images/error.gif'/>";
	        		} else {
	        			return "<span style='color:green;font-weight:bold;font-size:12'>�ɼ�</span>" +
	            				"&nbsp;<img src='/resources/images/right.gif'/>";
	        		}
	        	}
	        }
    	];
    	
    	this.tbar = [{
    		text: '���ÿɼ�',
    		id: 'mce',
    		iconCls: 'edit',
    		handler: this.onShowHandler
    	}, {
        	text: 'ɾ��',
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
			Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ����ѡ������', function(btn) {
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
									title: '��ʾ',
									msg: 'ɾ���ɹ���',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('cmpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: 'ɾ��ʧ�ܣ�',
									icon: Ext.MessageBox.WARNING,
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
				msg: '��ѡ����Ҫɾ�������ۣ�',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	},
	onShowHandler: function() {
		var sm = Ext.getCmp('cmpanel').getSelectionModel();
		if(sm.hasSelection()) {
			Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫ��ʾ��ѡ������', function(btn) {
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
									title: '��ʾ',
									msg: '���ÿɼ��ɹ���',
									icon: Ext.MessageBox.INFO,
									buttons: Ext.MessageBox.YES
								});
								Ext.getCmp('cmpanel').getStore().reload();
							} else {
								Ext.MessageBox.show({
									title: '��ʾ',
									msg: '���ÿɼ�ʧ�ܣ�',
									icon: Ext.MessageBox.WARNING,
									buttons: Ext.MessageBox.YES
								});
							}
						},
						failure: function(response, opt) {
							Ext.MessageBox.show({
								title: '��ʾ',
								msg: '���ÿɼ�ʧ�ܣ�',
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
				msg: '��ѡ����Ҫ��ʾ�����ۣ�',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
		}
	}
});
