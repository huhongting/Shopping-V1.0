<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String basePath = path+request.getContextPath()+"/";
String extPath = path + "/ext.4.2/";
pageContext.setAttribute("extpath", extPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare系统初始化</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="${extpath}resources/css/ext-all.css">
	<script type="text/javascript" src="${extpath}bootstrap.js"></script>
	<script type="text/javascript" src="${extpath}locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript">
		Ext.onReady(function() {
			Ext.MessageBox.confirm(
				'提示',
				'确定要初始化系统？',
				function(btn) {
					if(btn == 'yes') {
						var myMask = new Ext.LoadMask(Ext.getBody(), {  
		                    msg : "系统正在初始化,请稍候..."  
		                });
						myMask.show();
						Ext.Ajax.request({
							url: 'control/system/init.do',
							success: function(resp, opt) {
								var result = Ext.JSON.decode(resp.responseText);
								myMask.hide();
								if(result.success) {
									Ext.MessageBox.confirm(
										'提示',
										'系统初始化成功！',
										function(btn) {
											window.location.href = '/control/login';
										}
									);
									//closeWindow();
									//Ext.Msg.alert('提示', '系统初始化成功！');
								} else {
									Ext.Msg.alert('提示', '系统初始化失败！');
								}
							},
							failure: function(resp, opt) {
								myMask.hide();
								Ext.Msg.Alert('提示', '系统初始化失败！');
							}
						});
					} else {
						closeWindow();
					}
				}
			);
		});
		function closeWindow() {
			window.opener=null;
			window.open('','_self');
			window.close();
		}
	</script>
	
  </head>
  
  <body>
    
  </body>
</html>
