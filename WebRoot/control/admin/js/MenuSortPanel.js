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
            pageSize: this.pageSize, //设置分页大小
            proxy: {
                type: 'ajax',
                url: 'control/admin/booktype.do?method=sort',  //请求的服务器地址
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
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "类别编号", dataIndex: 'typeid', sortable: true},
            {header: "类别名称", dataIndex: 'typename', sortable: true},
            {header: "类别描述", dataIndex: 'note', sortable: true,
            	renderer: function(value) {
            		if(value == 'null') {
            			return '';
            		} else {
            			return value;
            		}
            	}
            },
            {header: '菜单栏显示顺序', dataIndex: 'sortid', sortable: true}
        ];
		//this.selModel = new Ext.selection.CheckboxModel({model: 'MULTI'});
        this.tbar = [{
        	text: '修改',
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
						fieldLabel: '类别编号'
					}, {
						xtype: 'textfield',
						id: 'typename',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '类别名称',
						width: 350,
						readOnly: true
					}, {
						xtype: 'textareafield',
						id: 'note',
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '类别描述',
						width: 350,
						readOnly: true
					}, {
						xtype: 'combo',
						id: 'sortid',
						editable: false,
						labelAlign: 'right',
						labelWidth: 60,
						fieldLabel: '菜单排序',
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
					text: '提交',
					handler: submit
				}, {
					text: '取消',
					handler: close
				}]
			});
			win.getComponent(0).loadRecord(record[0]);
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
							title: '提示',
							msg: '修改成功！',
							icon: Ext.MessageBox.INFO,
							buttons: Ext.MessageBox.YES
						});
						close();
						Ext.getCmp('btspanel').getStore().reload();
					} else {
						Ext.MessageBox.show({
							title: '提示',
							msg: '修改失败！',
							icon: Ext.MessageBox.ERROR,
							buttons: Ext.MessageBox.YES
						});
					}
				},
				failure: function(response, opt) {
					Ext.MessageBox.show({
						title: '提示',
						msg: '修改失败！',
						icon: Ext.MessageBox.ERROR,
						buttons: Ext.MessageBox.YES
					});
				}
			});
		}
	}
});
