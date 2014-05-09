Ext.define('MenuSortPanel', {
	extend: 'Ext.grid.Panel',
	//pageSize: 25,
	border: false,
	id: 'btspanel',
	autoHeight: true,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
		this.store = Ext.create('Ext.data.Store', {
			autoLoad: {
				start: -1,
				limit: -1
			},
            fields:['typeid', 'typename', 'note', 'sortid'],
            pageSize: this.pageSize, //���÷�ҳ��С
            proxy: {
                type: 'ajax',
                url: 'control/admin/booktype.do?method=sort',  //����ķ�������ַ
                reader: {
                    type: 'json',
                    root: 'records',
                    totalProperty: 'count'
                }
            },
            sorters: [{
            	property: 'sortid',
            	direction: 'ASC'
            }]
        });
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "�����", dataIndex: 'typeid', sortable: true},
            {header: "�������", dataIndex: 'typename', sortable: true},
            {header: "�������", dataIndex: 'note', sortable: true,
            	renderer: function(value) {
            		if(value == 'null') {
            			return '';
            		} else {
            			return value;
            		}
            	}
            },
            {header: '�˵�����ʾ˳��', dataIndex: 'sortid', sortable: true}
        ];
		//this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
        this.tbar = [{
        	text: '�޸�',
        	iconCls: 'edit',
        	id: 'mme',
        	handler: this.onEditHandler
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
					url: 'control/admin/check/menusort.do',
					async: false,
					method: 'POST',
					success: function(response, opt) {
						var r = Ext.JSON.decode(response.responseText);
						Ext.getCmp('mme').disabled = !r.menusort;
					}
				});
    		}
    	}
	},
	onEditHandler: function() {
		var win = null;
		var sm = Ext.getCmp('btspanel').getSelectionModel();
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
				border: false,
				resizable: false,
				animateTarget: 'mme',
				items: [{
					xtype: 'form',
					frame: true,
					items: [{
						xtype: 'hidden',
						id: 'typeid',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�����'
					}, {
						xtype: 'textfield',
						id: 'typename',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�������',
						width: 350,
						readOnly: true
					}, {
						xtype: 'textareafield',
						id: 'note',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�������',
						width: 350,
						readOnly: true
					}, {
						xtype: 'combo',
						id: 'sortid',
						editable: false,
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '�˵�����',
						valueField: 'value',
						displayField: 'name',
						triggerAction: 'all',
						width: 350,
						mode: 'local',
						store: new Ext.data.SimpleStore({
							fields: ['name', 'value'],
							data: [
								['1', 1], ['2', 2], ['3', 3], ['4', 4], ['5', 5], 
								['6', 6], ['7', 7], ['8', 8], ['9', 9], ['10', 10]
							]
						})
					}]
				}],
				buttons: [{
					text: '�ύ',
					handler: submit
				}, {
					text: 'ȡ��',
					handler: close
				}]
			});
			win.getComponent(0).loadRecord(record[0]);
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
			var id = form.getComponent('typeid').value;
			var sortid = form.getComponent('sortid').value;
			Ext.Ajax.request({
				url: 'control/admin/booktype.do?method=updateSort',
				params: {
					id: id,
					sid: sortid
				},
				method: 'POST',
				success: function(response, opt) {
					var res = Ext.JSON.decode(response.responseText);
					if(res.success == true) {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�޸ĳɹ���',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						close();
						Ext.getCmp('btspanel').getStore().reload();
					} else {
						Ext.MessageBox.show({
							title: '��ʾ',
							msg: '�޸�ʧ�ܣ�',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				},
				failure: function(response, opt) {
					Ext.MessageBox.show({
						title: '��ʾ',
						msg: '�޸�ʧ�ܣ�',
						icon: Ext.MessageBox.ERROR,
						buttons: Ext.MessageBox.YES
					});
				}
			});
		}
	}
});
