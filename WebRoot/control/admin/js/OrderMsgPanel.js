Ext.define('OrderMsgPanel', {
	extend: 'Ext.grid.Panel',
	//border: false,
	id: 'ompanel',
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
            {header: "客服姓名", dataIndex: 'name', sortable: true},
            {header: "留言内容", dataIndex: 'content', sortable: true},
            {header: "留言时间", dataIndex: 'time', sortable: true}
        ];
        
        this.store = Ext.create('Ext.data.JsonStore', {
        	autoLoad: true,
        	fields: ['id', 'name', 'content', 'time'],
	        proxy: {
	            type: 'ajax',
	            url: 'control/admin/ordermsg.do?method=list&oid=' + this.orderid,
	            reader: {
	                type: 'json',
	                root: 'records'
	            }
	        }
	    });
        this.forceFit = true;
		this.callParent();
	},
	listeners: {
		'itemcontextmenu': function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		}
	}
});
