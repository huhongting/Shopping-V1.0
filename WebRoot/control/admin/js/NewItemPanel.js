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
        this.columns =  [//���ñ����
        	new Ext.grid.RowNumberer({header: '�к�', width: 30}),
            {header: "ͼ������", dataIndex: 'bookname', sortable: true},
            {header: "ͼ�鵥��", dataIndex: 'price', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
            	}
            },
            {header: "ԭ������", dataIndex: 'number', sortable: true},
            {header: "�µ�����", dataIndex: 'newnum', sortable: true,
            	renderer: function(value) {
            		return '<font color=green>' + value + '</font>';
            	},
            	field: {
            		xtype: 'numberfield',
            		allowBlank: false,
            		minValue: 0
            	}
            },
            {header: "ͼ����", dataIndex: 'kucun', sortable: true,
            	renderer: function(value) {
            		return '<font color=red>' + value + '</font>';
            	}
            },
            {header: "С��", dataIndex: 'count', sortable: true,
            	renderer: function(value) {
            		return '��' + Ext.util.Format.number(value, '0.00');
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
