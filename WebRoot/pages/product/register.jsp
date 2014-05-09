<%@page import="cn.edu.jnu.web.util.WebUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上书店-用户注册</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="/resources/css/css.css">
	<script type="text/javascript" src="/resources/js/com.js"></script>
	<script language="JavaScript" type="text/javascript" src="/resources/js/passmode2012.js"></script>
	<script language="JavaScript" type="text/javascript" src="/resources/js/login.js" charset="gbk"></script>
  </head>
  
  <body>
	<div id="head">
		<div class="head_top" id="header_container">
	        <div class="w1002 mCenter">
	            <div class="wlecome" id="logon">
					<span class="black">欢迎光临&nbsp;<a href="index">IShare网上书店</a></span>  
					［<a rel="nofollow" href="view/login">登录</a>］［<a href="view/register">注册</a>］
				</div>
	            <div class="clear"></div>
	        </div>
	    </div>
	</div>
	<br><br>
	<%
	if(WebUtil.getCurrentUser(request) != null) {
	%>
	<script type="text/javascript">window.location.href = 'index';</script>
	<%
	}
	%>
	<div id="main">
		<div class="sign">
	    	<h3 class="sign_title">
	        	<span class="sign_d">已注册用户，请&nbsp;<a href="view/login">登录</a></span>
	            <span>用户注册</span>
	        </h3>
	        <div class="user_list"></div>
			
	        <div class="login_box">
			<form method="post" action="view/register/reg" name="regform" id="regform">
	        	<div id="con_a_1" style="display: block;">
	            	<div class="item">
	                	<span class="float1"><span>*</span>用户名：</span>
	                    <div class="item_inp">
							<input style="width:200px;height:25px;" class="normal" onfocus="chcls(this);" type="text" name="uid2" id="uid2" onblur="if(nm2())chkusername();chcls(this);" maxlength="40"> 
							<input name="olduid2" id="olduid2" type="hidden">  
						</div>
	                    <div class="item_r">
							<span id="uid2_m" class="red12">
								<span class="gray12">由大小写英文字母、数字和下划线组成，长度4-20位。</span>
							</span>
						</div>
	                </div>
	                <div class="item">
	                	<span class="float1"><span>*</span>密&nbsp;&nbsp;码：</span>
						<div class="item_inp">
							<input style="width:200px;height:25px;" class="normal" onfocus="chcls(this);" type="password" name="pwd2" onblur="mm2();chcls(this);" id="pwd2" maxlength="40">
	                    </div>
	                    <div class="item_r">
							<span id="pwd2_m" class="red12">
								<span class="gray12">建议8位以上的数字字母和下划线组成</span>
							</span>
						</div>
	                </div>
	                <div class="item">
	                	<span class="float1"><span>*</span>确认密码：</span>
	                    <div class="item_inp">
							<input style="width:200px;height:25px;" class="normal" onfocus="chcls(this);" type="password" name="pwd22" onblur="mm22();chcls(this);" id="pwd22" maxlength="40">
	                    </div>
	                    <div class="item_r">
							<span id="pwd22_m" class="red12">请再次输入密码</span>
						</div>
	                </div>
	                <div class="item">
	                	<span class="float1"><span>*</span>邮&nbsp;&nbsp;箱：</span>
	                    <div class="item_inp">
							<input style="width:200px;height:25px;" class="normal" onfocus="chcls(this);" type="text" name="email" value="" id="email" onblur="if(cemail())chkemail();chcls(this);" maxlength="50">
							<input name="oldemail" id="oldemail" type="hidden"> 
							<input name="outemail" id="outemail" type="hidden">
							<input name="gwc" id="gwc" type="hidden" value="">
						</div>
	                    <div class="item_r">
							<span class="gray12">
								<span id="email_m" class="red12">Email是“找回密码”的唯一途径</span>
							</span>
						</div>
	                </div>
	                <div class="item">
	                	<span class="float1"><span>*</span>验证码：</span>
	                    <div class="item_inp">
							<input style="width:100px;height:25px;" class="normal" type="text" name="yzm" id="yzm" onfocus="chcls(this);" onblur="yzm22();chcls(this);" maxlength="4">
						</div>
						<div class="item_i"><img id="imag" src="view/validate" onclick="changeImage()" border="0">&nbsp;&nbsp;</div>
	                    <div class="item_r">
	                    	<label>
								<span class="gray12">
									<span id="yzm_m" class="red12">请输入图片中的数字</span>
								</span>
	                        </label> 
	                    </div>
	                </div>
	                <div class="item">
	                	<span class="float1"></span>
	                    <div class="item_inp"> 
							 <input class="button" type="image" src="/resources/images/login_up.jpg" name="Submit2" onclick="return form_onsubmit()">
	                    </div>
	                    <script type="text/javascript">
	                    	function changeImage() {
	                    		document.getElementById('imag').src='view/validate';
	                    	}
		                    function form_onsubmit() {
			                    if($('uid2').value.trim()=="" || !nm2()) {
			                    	$('uid2').value = '';
			                    	alert("请输入您想申请的会员名!\n");
			                    	return false;
			                    }
			                    if($('pwd2').value.trim()=="") {
			                    	alert("请输入您的密码!\n");
			                    	return false;
			                    }
			                    if($('pwd22').value.trim()=="") {
			                    	alert("请输入确认密码！\n");
			                    	return false;
			                    }
			                    if($('pwd2').value!=$('pwd22').value) {
			                    	alert("您的两次密码输入不相同！\n");
			                    	return false;
			                    }
			                    //EF新加前台提交时验证邮箱格式
			                    if($('email').value=="" || !/^([a-zA-Z0-9_]+[_|\-|\.]?)*[a-zA-Z0-9_]+@([a-zA-Z0-9_]+[_|\-|\.]?)*[a-zA-Z0-9_]+\.[a-zA-Z]{2,3}$/.test($('email').value)) {
			                    	alert("请正确输入您的E-mail地址！\n");
			                    	return false;
			                    }
			                    if($('yzm').value=="") {
			                    	alert("请输入您的验证码！\n");
			                    	return false;
			                    }
			                    
			                    reg();
			                    
			                    return false;
		                    }
		                    function reg() {
		                    	var name = $('uid2').value.trim();
		                    	var pass = $('pwd2').value.trim();
		                    	var email = $('email').value.trim();
		                    	var yzm = $('yzm').value;
		                    	sendRequest('view/register/reg?name='+name+'&pass='+pass+'&email='+email+'&yzm='+yzm,
		                    			function() {
				                    		if(XMLHttpReq.readyState == 4) {
				                    			if(XMLHttpReq.status == 200) {
				                    				var success = XMLHttpReq.responseXML.getElementsByTagName("success")[0].firstChild.nodeValue;
				                    				if(success == 'true') {
					                    				var url = XMLHttpReq.responseXML.getElementsByTagName("url")[0].firstChild.nodeValue;
				                    					window.location.href = url;
				                    				} else {
					                    				var msg = XMLHttpReq.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue;
				                    					alert(msg);
				                    					changeImage();
				                    				}
				                    			}
				                    		}
		                    	}); 
		                    }
	                    </script>
	                </div>
	            </div>
			</form>
	             
	        </div>
	        <div class="clear"></div>
	    </div>
	</div>

	<div id="bottom">
		<div class="w1002 mCenter bottom_c">
	        <div class="cp_copyright">
	        	<p>
					<a target="_blank" href=" ">首页</a> | 
					<a target="_blank" rel="nofollow" href=" ">关于我们</a> | 
					<a target="_blank" href=" " rel="nofollow">联系我们</a> 
				</p>
	            <p>Copyright 2013-2014 IShare网上书店,All Rights Reserved</p>
	            <div class="clear"></div>
	    	</div>
	    </div>
	</div>

  </body>
</html>
