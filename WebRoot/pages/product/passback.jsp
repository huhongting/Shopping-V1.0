<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上商店-找回密码</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="/resources/css/css.css">
	<script type="text/javascript" src="/resources/js/login.js"></script>
  </head>
  
  <body>
    <div id="head">
		<div class="head_top" id="header_container">
	        <div class="w1002 mCenter">
	            <div class="wlecome" id="logon">
					<span class="black">欢迎光临&nbsp;<a href="index">IShare网上书店</a></span>  
					［<a rel="nofollow" href="view/login">登录</a>］  
					［<a href="view/register">注册</a>］
				</div>
	            <div class="clear"></div>
	        </div>
	    </div>
	</div>
	<br><br>
	
    <div id="main">
		<div class="sign">
	    	<h3 class="sign_title">找回密码</h3>
	        <div class="password_s ">
	        	<div class="item">
	            	<p>输入您的邮箱地址，系统将随机生成一个新的登录密码并发送至邮箱。请注意查看哦！</p>
	            </div>
	            <div class="item">
	                <span class="float1">邮箱地址：</span>
					<div class="item_inp"><input style="height:25px;" class="normal" type="text" name="email" id="email" maxlength="40"> 
						<span id="email_m" class="red12"></span> 
					</div>
	            </div>
				<div class="item" style="margin-bottom:20px">
	            	<span class="float1">验证码：</span>
	            	<div class="item_inp">
						<input type="text" style="width:100px; height:25px;" name="yzm" size="8" class="normal" id="yzm"> &nbsp;&nbsp; 
						<img src="view/validate" id="Image1" align="absmiddle" border="0">&nbsp;&nbsp;
						<a href="javascript:change()">看不清,换一张</a>&nbsp;&nbsp;
						<script type="text/javascript">
							function change() {
								document.getElementById('Image1').src = 'view/validate';
							}
							function cemail_2(){
								var t = document.getElementById('email').value; 
								if( t== ''|| !/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(t)){
									document.getElementById('email_m').innerHTML = ('<img src="/resources/images/item_w.png" />请正确输入您的E-mail地址！');
									return false;
								}else{document.getElementById('email_m').innerHTML = ('<img src="/resources/images/item_r.png" />');}
								return true;
							}
							function sendPassEmail() {
								if(!cemail_2()) return;
								// ajax send emial
								var email = document.getElementById('email').value;
								var yzm = document.getElementById('yzm').value;
								document.getElementById('yzm_m').innerHTML = '<img src="/resources/images/load.gif">';
                				document.getElementById('yzm_m').style.display = '';
								sendRequest('user/passback?email='+email+'&yzm='+yzm,
		                    			function() {
				                    		if(XMLHttpReq.readyState == 4) {
				                    			if(XMLHttpReq.status == 200) {
				                    				var success = XMLHttpReq.responseXML.getElementsByTagName("success")[0].firstChild.nodeValue;
				                    				if(success == 'true') {
				                    					document.getElementById('yzm_m').innerHTML = '<font color=green>新密码已经发送，请注意查收！</font>';
					                    				document.getElementById('yzm_m').style.display = '';
					                    				document.getElementById('yzm').value = '';
				                    					change();
				                    				} else {
					                    				var msg = XMLHttpReq.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue;
					                    				document.getElementById('yzm_m').innerHTML = '<font color=red>' + msg + '</font>';
					                    				document.getElementById('yzm_m').style.display = '';
					                    				document.getElementById('yzm').value = '';
				                    					change();
				                    				}
				                    			}
				                    		}
		                    	}); 
							}
						</script>
						<div style="margin:10px 0px;">
							<span id="yzm_m" style="display:none" class="red12"></span>
						</div>
					</div>
	            	<div class="clear"></div>
	            </div>
				<div id="ck_m">
		            <div style="padding:0 5px 10px 130px;"><span id="ck_m" class="red12"></span></div>
		            <div class="item">
		                <span class="float1"></span>
		                <div class="item_inp" id="dzdz">
		                	<img style="cursor:pointer" onclick="sendPassEmail();" src="/resources/images/s_pass.jpg">   
						</div>
		            </div>
				</div>
	        </div>
	    </div>
	</div>
    
  </body>
</html>
