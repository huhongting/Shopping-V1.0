Ext.define('NewItemPanel', {
	extend: 'Ext.grid.Panel',
	//border: false,
	//id: 'noipanel',
	height: 100,
	//width: 720,
	border: false,
	autoScroll: true,
	constructor: function(config) {
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent();
		return this;
	},
	initComponent: function() {
        this.columns =  [//配置表格列
        	new Ext.grid.RowNumberer({header: '行号', width: 30}),
            {header: "图书名称", dataIndex: 'bookname', sortable: true},
            {header: "图书单价", dataIndex: 'price', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "原单数量", dataIndex: 'number', sortable: true},
            {header: "新单数量", dataIndex: 'newnum', sortable: true,
            	renderer: function(value) {
            		return '<font color=green>' + value + '</font>';
            	},
            	field: {
            		xtype: 'numberfield',
            		allowBlank: false,
            		minValue: 0
            	}
            },
            {header: "图书库存", dataIndex: 'kucun', sortable: true,
            	renderer: function(value) {
            		return '<font color=red>' + value + '</font>';
            	}
            },
            {header: "小计", dataIndex: 'count', sortable: true,
            	renderer: function(value) {
            		return '￥' + Ext.util.Format.number(value, '0.00');
            	}
            }
        ];
        this.plugins = [
	        Ext.create('Ext.grid.plugin.CellEditing', {
	            clicksToEdit: 1
	        })
	    ];
        this.store = Ext.create('Ext.data.JsonStore', {
        	autoLoad: true,
        	fields: ['id', 'bookname', 'price', 'number', 'newnum', 'count', 'kucun'],
	        proxy: {
	            type: 'ajax',
	            url: 'control/admin/orderitem.do?method=list&oid=' + this.orderid,
	            reader: {
	                type: 'json',
	                root: 'records'
	            }
	        }
	    });
        this.forceFit = true;
		this.callParent();
	}
});
