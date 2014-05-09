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
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/picture.do?method=list',  //����ķ�������ַ
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
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "���", dataIndex: 'id', sortable: true},
            {header: "����", dataIndex: 'name', sortable: true},
            {header: "����", dataIndex: 'action', sortable: true},
            {header: "�ϴ�ʱ��", dataIndex: 'uptime', sortable: true},
            {header: "��ҳ��ʾ", dataIndex: 'isshow', sortable: true,
            	renderer: function(value) {
            		if(value == true || value == 'true') 
            			return "<font color=red>��ʾ</font>";
					return "����ʾ";            		
            	}
            },
            {header: "ͼƬ·��", dataIndex: 'url', sortable: true,
            	renderer: function(value) {
            		return value;
            	}
            },
            {header: "����˵��", dataIndex: 'note', sortable: false}
        ];
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
        this.tbar = [{
        	id: 'mspa',
        	text: '���',
        	iconCls: 'add',
        	handler: this.onAddHandler
        }, '-', {
        	id: 'mspr',
        	text: '��ʾ',
        	iconCls: 'recom',
        	handler: this.onShowPic
        }, '-', {
        	id: 'mspnr',
        	text: '����ʾ',
        	iconCls: '_recom',
        	handler: this.onNotShowPic
        }, '-', {
        	id: 'mspd',
        	text: 'ɾ��',
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
			title: 'ͼƬԤ��',
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
			title: '���',
			id: 'picadd',
			label: 'form',
			animateTarget: 'mspa',
			//closeAction: 'hide',
			buttonAlign: 'center',
			border: false,
			resizable: false,
			items: [new PicForm({id: 'picform'})],
			buttons: [{
				text: '����',
				handler: savePic
			}, {
				text: '����',
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
			Ext.MessageBox.confirm('��ʾ', '��ȷ��Ҫɾ��ͼƬ��', function(btn) {
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
			waitMsg: '�����ύ����',
			waitTiele: '��ʾ',
			method: 'POST',
			success: function(form, action) {
				Ext.MessageBox.show({
                	title: '��ʾ',
                	msg: 'ͼƬ��ӳɹ���',
                	icon: Ext.MessageBox.INFO,
                	buttons: Ext.MessageBox.OK
                });
				Ext.getCmp('picadd').close();
                Ext.getCmp('picpanel').getStore().reload();
			}, 
			failure: function(form, action) {
				Ext.MessageBox.show({
                	title: '��ʾ',
                	msg: 'ͼƬ���ʧ�ܣ�',
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

function updatePicShow(url, ids, value) {
	Ext.Ajax.request({
		url: url,
		params: {ids: ids, show: value},
		method: 'POST',
		success: function(response, opt) {
			var res = Ext.JSON.decode(response.responseText);
			if(res.success) {
				Ext.MessageBox.show({
					title: '��ʾ',
					msg: '������Ϊ��ҳ��ʾ��',
					icon: Ext.MessageBox.INFO,
					buttons: Ext.MessageBox.YES
				});
				Ext.getCmp('picpanel').getStore().reload();
			} else {
				Ext.MessageBox.show({
					title: '��ʾ',
					msg: '������ҳ��ʾʧ�ܣ�',
					icon: Ext.MessageBox.ERROR,
					buttons: Ext.MessageBox.YES
				});
			}
		},
		failure: function(response, opt) {
			Ext.MessageBox.show({
				title: '��ʾ',
				msg: '������ҳ��ʾʧ�ܣ�',
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
					title: '��ʾ',
					msg: 'ɾ���ɹ���',
					icon: Ext.MessageBox.INFO,
					buttons: Ext.MessageBox.YES
				});
				Ext.getCmp('picpanel').getStore().reload();
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
				title: '��ʾ',
				msg: '������ѡ��һ����¼��',
				icon: Ext.MessageBox.WARNING,
				buttons: Ext.MessageBox.YES
			});
			return null;
		} else {
			return ids;
		}
	} else {
		Ext.MessageBox.show({
			title: '��ʾ',
			msg: '������ѡ��һ����¼��',
			icon: Ext.MessageBox.WARNING,
			buttons: Ext.MessageBox.YES
		});
	}
}
