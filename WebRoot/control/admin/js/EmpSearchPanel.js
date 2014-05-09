Ext.define('EmpSearchPanel', {
	extend: 'Ext.grid.Panel',
	border: false,
	pageSize: 25,
	id: 'espanel',
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
            {header: "�û����", dataIndex: 'userid', sortable: true},
            {header: "�û�����", dataIndex: 'username', sortable: true},
            {header: "�����ʼ�", dataIndex: 'email', sortable: true},
            {header: "�û�����", dataIndex: 'usertype', sortable: true,
            	renderer: function(value) {
            		if(value  == 2) {
            			return "<font color=red>��������Ա</font>";
            		} else if(value == 1) {
            			return "<font color=green>��˾ְԱ</font>";
            		} else {
            			return value;
            		}
            	}
            },
            {header: "ע��ʱ��", dataIndex: 'regtime', sortable: true},
            {header: "����˵��", dataIndex: 'note', sortable: false}
        ];
        
        this.dockedItems = [{
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            id: 'esptb',
            store: this.store,   //������Ҫָ��������ͬ��store
            displayInfo: true,
            beforePageText: '��',
            afterPageText: 'ҳ/�� {0} ҳ',
            displayMsg: '�� {0} ���� {1} ����¼���� {2} ����¼',
      		emptyMsg: "û�м�¼"
        }];
        this.addListener('itemdblclick', this.click, this);
        this.addListener('itemcontextmenu', function(view, record, item, index, e, opt) {
			e.preventDefault();
			e.stopEvent();
		}, this);
        this.forceFit = true;
		this.callParent();
	},
	click: function (grid, record) {
		var uid = record.get('userid');
		var w = new Ext.Window({
			title: 'Ա����Ϣ',
			width: 600,
			modal: true,
			resizable: false,
			animateTarget: 'espanel',
			items: [new EmpInfo()],
			buttonAlign: 'center',
			border: false,
			buttons: [{
				text: '�ر�',
				handler: function() {
					w.close();
				}
			}]
		});
		w.getComponent(0).getForm().load({
			url: 'control/admin/employeeinfo.do?uid=' + uid,
			method: 'POST',
			autoLoad: true,
			success: function(form, action) {
				//Ext.Msg.alert('��ʾ', '���سɹ���');
			},
			failure: function(form, action) {
				Ext.Msg.alert('��ʾ', '����ʧ�ܣ�');
			}
		});
		w.show();
	}
});