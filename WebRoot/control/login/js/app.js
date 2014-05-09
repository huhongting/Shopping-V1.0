Ext.onReady(function() {
	var winLogin = Ext.create("Ext.window.Window", {
        width: 400,
        modal: true, // ���ڵ����������ط����ɲ���  
        title: '��Ȩ��½ ',
        collapsible: true,  // ������ť  
        closable: false, // �Ƿ���ʾ�رմ��ڰ�ť  
        iconCls: 'key', // cog , database_gear  
        resizable: false, // �����Ƿ��������  
        constrain: true,
        border: false,
        items:[{
            xtype: 'panel',
            width: '100%',
            height: 100,
            padding: '1px',
            //frame: true,
            html: "<img src='/resources/images/logo/logo.png' height='100%' width='100%'/>"
        }, {
        	xtype:'tabpanel',
            activeTab: 0,
            //border: false,
            defaults:{autoHeight:true, bodyStyle:'padding:10px'},
          	items: [{
          		title: '�����֤',
          		xtype: 'form',
          		loadMask: true,
          		id: 'myform',
          		buttonAlign: 'center',
          		//frame: true,
          		items: [{
                    xtype: 'textfield',
                    id: 'username',
                    name: 'username',
                    fieldCls: 'login_account',
                    fieldLabel: '��&nbsp;&nbsp;&nbsp;&nbsp;��',
                    emptyText:'����д�û���',//����Ϊ��ʱ��ʾ��Ĭ����Ϣ
                    blankText:'�û�������Ϊ��',
                    minLength: 2,
                    minLengthText: '�û�������',
                    width: 300,
                    margin: '10,10,10,10',
                    labelAlign: 'right',
                    allowBlank:false
            	}, {
                    xtype: "textfield",
                    id: 'password',
                    name: 'password',
                    fieldCls: 'login_password',
                    width: 300,
                    fieldLabel: '��&nbsp;&nbsp;&nbsp;&nbsp;��',
                    emptyText:'����д����',//����Ϊ��ʱ��ʾ��Ĭ����Ϣ
                    blankText:'���벻��Ϊ��',
                    minLength: 6,
                    minLengthText: '�������',
                    margin: '10,10,10,10',
                    labelAlign: 'right',
                    inputType: 'password',
                    allowBlank: false
            	}, {
            		border: false,
            		items: [{
	        			layout: 'table',
            			//frame: true,
	        			border: false,
            			items: [{
            				border: false,
            				width: 10
            			}, {
            				layout: 'form',
            				width: 210,
            				//frame: true,
            				border: false,
	            			buttonAlign: 'center',
            				items: [{
            					xtype: 'textfield',
			                    id: 'validatecode',
			                    name: 'validatecode',
			                    fieldCls: 'login_valid',
			                    fieldLabel: '&nbsp;&nbsp;��֤��&nbsp;',
			                    emptyText:'����д��֤��',//����Ϊ��ʱ��ʾ��Ĭ����Ϣ
			                    blankText:'��֤�벻��Ϊ��',
			                    minLength: 4,
			                    minLengthText: '��֤�����',
			                    width: 200,
			                    margin: '10,10,10,10',
			                    labelAlign: 'right',
			                    allowBlank:false
            				}]
            			}, {
            				//frame: true,
            				border: false,
            				padding: '0 0 0 10',
            				width: 90,
							html: '<img id="valid" src="control/login/validate.do" ' +
									'onmousedown="changeImage()">'
        				}]
            		}]
            	}],
                buttons: [{
                    text: '��½',
                    layout: 'fit',
                    type: 'submit',
                    handler: function () {
                        var _username = Ext.getCmp('username').getValue();
                        var _password = Ext.getCmp('password').getValue();
                        var _valid = Ext.getCmp('validatecode').getValue();

                        if(_username == "" || _username.length < 2) {
                            Ext.MessageBox.show({
                            	title: '��ʾ',
                            	msg: '�û����������������룡',
                            	icon: Ext.MessageBox.WARNING,
                            	buttons: Ext.MessageBox.OK
                            });
                        } else if (_password == "" || _password.length < 6) {
                            Ext.MessageBox.show({
                            	title: '��ʾ',
                            	msg: '����������������룡',
                            	icon: Ext.MessageBox.WARNING,
                            	buttons: Ext.MessageBox.OK
                            });
                        } else if (_valid == "" || _valid.length < 4) {
                            Ext.MessageBox.show({
                            	title: '��ʾ',
                            	msg: '��֤��������������룡',
                            	icon: Ext.MessageBox.WARNING,
                            	buttons: Ext.MessageBox.OK
                            });
                        } else {
                        	var myMask = new Ext.LoadMask(Ext.getBody(), {  
			                    msg : "���ڵ���,���Ժ�..."  
			                });
							myMask.show();
                            Ext.Ajax.request({
                                url: 'control/login/login.do',// ���������֤
                                method: 'POST',
                                success: function (response, opts) {
                                	myMask.hide();
                                	var res = Ext.JSON.decode(response.responseText);
                                	if(res.success) {
	                                    window.location.href = "/control/admin/index";
                                	} else {
                                		loginError();
                                	}
                                },
                                failure: function (response, opts) {
                                	myMask.hide();
                                    loginError();
                                },
                                params: {
                                    username: _username,
                                    password: _password,
                                    validatecode: _valid
                                }
                            });
                        }
                    }
                }, {
                    text: '����',
                    handler: function () {
                    	changeImage();
                        Ext.getCmp('myform').form.reset();
                    }
                }, {
                	text: '��������',
                	handler: passback
                }]
          	}, {
            	title: '��Ϣ������',
            	layout: '',
            	//frame: true,
            	contentEl: 'info'
            }, {
            	title: '����',
            	//frame: true,
            	layout: '',
            	contentEl: 'about'
            }]  
        }]
	});
	winLogin.show();
});

function passback() {
	var w = new Ext.Window({
		title: '��������',
		modal: true,
		buttonAlign: 'center',
		items: [{
			xtype: 'textfield',
            id: 'uname',
            fieldLabel: '�˺�',
            labelWidth: 30,
            emptyText:'����д�û���',//����Ϊ��ʱ��ʾ��Ĭ����Ϣ
            blankText:'�û�������Ϊ��',
            minLength: 2,
            minLengthText: '�û�������',
            width: 200,
            margin: '10,10,10,10',
            labelAlign: 'right',
            allowBlank:false
		}, {
			xtype: 'textfield',
            id: 'umail',
            fieldLabel: '����',
            labelWidth: 30,
            emptyText:'����д��������',//����Ϊ��ʱ��ʾ��Ĭ����Ϣ
            blankText:'�������䲻��Ϊ��',
            vtype: 'email',
			vtypeText: '������Ч�ĵ��������ʽ',
            width: 200,
            margin: '10,10,10,10',
            labelAlign: 'right',
            allowBlank:false
		}],
		buttons: [{
			text: 'ȷ��',
			handler: function() {
				var nf = Ext.getCmp('uname');
				var mf = Ext.getCmp('umail');
				if(nf.isValid() && mf.isValid()) {
					Ext.Ajax.request({
                        url: 'employee/passback',
                        method: 'POST',
                        success: function (response, opts) {
                        	var res = Ext.JSON.decode(response.responseText);
                        	if(res.success) {
                                Ext.MessageBox.show({
			                    	title: '��ʾ',
			                    	msg: '�������Ѿ����͵�ע�����䣡',
			                    	icon: Ext.MessageBox.INFO,
			                    	buttons: Ext.MessageBox.OK
			                    });
			                    w.close();
                        	} else {
                        		Ext.MessageBox.show({
			                    	title: '��ʾ',
			                    	msg: res.msg,
			                    	icon: Ext.MessageBox.WARNING,
			                    	buttons: Ext.MessageBox.OK
			                    });
                        	}
                        },
                        failure: function (response, opts) {
                        	Ext.MessageBox.show({
		                    	title: '��ʾ',
		                    	msg: res.msg,
		                    	icon: Ext.MessageBox.ERROR,
		                    	buttons: Ext.MessageBox.OK
		                    });
                        },
                        params: {
                            uname: nf.getValue(),
                            umail: mf.getValue()
                        }
                    });
				}
			}
		}, {
			text: 'ȡ��',
			handler: function() {
				w.close();
			}
		}]
	}).show();
}

function loginError() {
	Ext.MessageBox.show({
    	title: '��ʾ',
    	msg: '����ʧ�ܣ�',
    	icon: Ext.MessageBox.ERROR,
    	buttons: Ext.MessageBox.OK
    });
    changeImage();
    Ext.getCmp('password').setValue('');
    Ext.getCmp('validatecode').setValue('');
}
function changeImage() {
	var obj = document.getElementById('valid');
	obj.src = "control/login/validate.do?t="+new Date().getTime();
}