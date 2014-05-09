Ext.onReady(function() {
	var winLogin = Ext.create("Ext.window.Window", {
        width: 400,
        modal: true, // 窗口弹出，其他地方不可操作  
        title: '授权登陆 ',
        collapsible: true,  // 收缩按钮  
        closable: false, // 是否显示关闭窗口按钮  
        iconCls: 'key', // cog , database_gear  
        resizable: false, // 窗体是否可以拉伸  
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
          		title: '身份验证',
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
                    fieldLabel: '账&nbsp;&nbsp;&nbsp;&nbsp;号',
                    emptyText:'请填写用户名',//输入为空时提示的默认信息
                    blankText:'用户名不能为空',
                    minLength: 2,
                    minLengthText: '用户名错误！',
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
                    fieldLabel: '密&nbsp;&nbsp;&nbsp;&nbsp;码',
                    emptyText:'请填写密码',//输入为空时提示的默认信息
                    blankText:'密码不能为空',
                    minLength: 6,
                    minLengthText: '密码错误！',
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
			                    fieldLabel: '&nbsp;&nbsp;验证码&nbsp;',
			                    emptyText:'请填写验证码',//输入为空时提示的默认信息
			                    blankText:'验证码不能为空',
			                    minLength: 4,
			                    minLengthText: '验证码错误！',
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
                    text: '登陆',
                    layout: 'fit',
                    type: 'submit',
                    handler: function () {
                        var _username = Ext.getCmp('username').getValue();
                        var _password = Ext.getCmp('password').getValue();
                        var _valid = Ext.getCmp('validatecode').getValue();

                        if(_username == "" || _username.length < 2) {
                            Ext.MessageBox.show({
                            	title: '提示',
                            	msg: '用户名错误，请重新输入！',
                            	icon: Ext.MessageBox.WARNING,
                            	buttons: Ext.MessageBox.OK
                            });
                        } else if (_password == "" || _password.length < 6) {
                            Ext.MessageBox.show({
                            	title: '提示',
                            	msg: '密码错误，请重新输入！',
                            	icon: Ext.MessageBox.WARNING,
                            	buttons: Ext.MessageBox.OK
                            });
                        } else if (_valid == "" || _valid.length < 4) {
                            Ext.MessageBox.show({
                            	title: '提示',
                            	msg: '验证码错误，请重新输入！',
                            	icon: Ext.MessageBox.WARNING,
                            	buttons: Ext.MessageBox.OK
                            });
                        } else {
                        	var myMask = new Ext.LoadMask(Ext.getBody(), {  
			                    msg : "正在登入,请稍候..."  
			                });
							myMask.show();
                            Ext.Ajax.request({
                                url: 'control/login/login.do',// 处理登入验证
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
                    text: '重置',
                    handler: function () {
                    	changeImage();
                        Ext.getCmp('myform').form.reset();
                    }
                }, {
                	text: '忘记密码',
                	handler: passback
                }]
          	}, {
            	title: '信息公告栏',
            	layout: '',
            	//frame: true,
            	contentEl: 'info'
            }, {
            	title: '关于',
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
		title: '忘记密码',
		modal: true,
		buttonAlign: 'center',
		items: [{
			xtype: 'textfield',
            id: 'uname',
            fieldLabel: '账号',
            labelWidth: 30,
            emptyText:'请填写用户名',//输入为空时提示的默认信息
            blankText:'用户名不能为空',
            minLength: 2,
            minLengthText: '用户名错误！',
            width: 200,
            margin: '10,10,10,10',
            labelAlign: 'right',
            allowBlank:false
		}, {
			xtype: 'textfield',
            id: 'umail',
            fieldLabel: '邮箱',
            labelWidth: 30,
            emptyText:'请填写电子邮箱',//输入为空时提示的默认信息
            blankText:'电子邮箱不能为空',
            vtype: 'email',
			vtypeText: '不是有效的电子邮箱格式',
            width: 200,
            margin: '10,10,10,10',
            labelAlign: 'right',
            allowBlank:false
		}],
		buttons: [{
			text: '确定',
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
			                    	title: '提示',
			                    	msg: '新密码已经发送到注册邮箱！',
			                    	icon: Ext.MessageBox.INFO,
			                    	buttons: Ext.MessageBox.OK
			                    });
			                    w.close();
                        	} else {
                        		Ext.MessageBox.show({
			                    	title: '提示',
			                    	msg: res.msg,
			                    	icon: Ext.MessageBox.WARNING,
			                    	buttons: Ext.MessageBox.OK
			                    });
                        	}
                        },
                        failure: function (response, opts) {
                        	Ext.MessageBox.show({
		                    	title: '提示',
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
			text: '取消',
			handler: function() {
				w.close();
			}
		}]
	}).show();
}

function loginError() {
	Ext.MessageBox.show({
    	title: '提示',
    	msg: '登入失败！',
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