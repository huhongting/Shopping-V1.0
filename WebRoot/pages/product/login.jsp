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
    
    <title>IShare网上商店-用户登入</title>
    
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
	<script type="text/javascript" src="/resources/js/shoppingcar.js"></script>
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
	<%
   	String value = WebUtil.getCookie(request, "login.ishare");
   	String name = "";
   	String pass = "";
   	if(value == null) value = "";
   	else {	
   		String[] values = value.split(",");
   		name = values[0];
   		if(values.length > 1) pass = value.split(",")[1];
   	}
   	%>
	<div id="main">
		<div class="sign">
	    	<h3 class="sign_title">用户登录</h3>
	    	<script type="text/javascript">
		    	function chcls(o){
		    		 var le = document.getElementById('lerror');
		    		 if(le.style.display == '') {
		    			 le.style.display = 'none';
		    		 }
		    		 if(o.className == "normal" && o.value.trim() == '') {
		    		 	o.className = "onhover";
		    		 } else if(o.className == 'onhover' && o.value.trim() == '') {
		    			
		    		 } else {
		    		 	o.className = "normal";
		    		 }
		    	 }
		    	function check() {
		    		var name = document.getElementById('ln').value.trim();
		    		var pass = document.getElementById('lm').value.trim();
		    		var chk = document.getElementById('lc').checked;
		    		
		    		if(name == '') {
		    			chcls(document.getElementById('ln'));
		    		} else if(pass == '') {
		    			chcls(document.getElementById('lm'));
		    		} else {
		    			var md = <%=pass.equals("") ? false : true %>;
		    			sendRequest('view/login?cmd=login&name='+name+'&pass='+pass+'&chk='+chk+'&md='+md,
                    			function() {
		                    		if(XMLHttpReq.readyState == 4) {
		                    			if(XMLHttpReq.status == 200) {
		                    				var success = XMLHttpReq.responseXML.getElementsByTagName("success")[0].firstChild.nodeValue;
		                    				if(success == 'true') {
		                    					//window.location.href = '';
		                    					var url = getPreHis();
		                    					if(url != null) {
			                    					url = url.replace(/"/g, "");
		                    						window.location.href = url;
		                    					}
		                    					else window.location.href = 'index';
		                    				} else {
		                    					document.getElementById('lerror').style.display = '';
		                    				}
		                    			}
		                    		}
                    	});
		    		}
		    		return false;
		    	}
	    	</script>
			<form method="post" name="form1" action=" " onsubmit="return check()">
	        <div class="sign_box">
	        	<div class="item">
	            	<span class="float1">用户名：</span>
	                <div class="item_inp"><input id="ln" value="<%=name %>" style="height:25px;" type="text" name="zh" size="16" class="normal" onfocus="chcls(this);" onblur="chcls(this);"></div>
	                <div class="clear"></div>
	            </div>
	            <div class="item">
	            	<span class="float1">密&nbsp;&nbsp;码：</span>
	            	<div class="item_inp"><input id="lm" value="<%=pass %>" style="height:25px;" class="normal" onfocus="chcls(this);" onblur="chcls(this);" name="mima" type="password" size="16"><span><a target="_blank" href="view/passback">忘记密码？</a></span></div>
	            	<div class="clear"></div>
	            </div>
	            <div class="item">
	                <span class="float2">Cookie：</span>
	                <div class="item_inp"><input id="lc" class="check" type="checkbox" name="UserLogin_SaveState"><lable>记住密码两周</lable></div>
	                <div class="clear"></div>
	                <p class="cookie_ts"><font color=red>如果您在公共场所使用，建议不选择此项！</font></p>
	            </div>
	            <div class="item t_center">
	            	<input type="image" src="/resources/images/sign.jpg" width="114" height="37" border="0" name="image">
	            	<%--<input type="image" src="/resources/images/sign_qq.jpg" border="0" name="qq" onclick="alert('qq login')">	
	            --%></div>
            	<div id="lerror" class="item_inp" style="width:100%;text-align: center;display:none"><img src="/resources/images/item_w.png" /><font color=red>用户名或密码不正确！</font></div>
	            <div class="item"></div>
	        </div>
	        <div class="sign_login">
	        	<h3>还不是IShare用户？</h3>
	            <p>免费注册IShare用户，立刻享受海量便宜教材书籍。</p>
	            <div class="sign_l"><a target="_self" href="view/register"><img src="/resources/images/sign_login.jpg"></a></div>
	        </div>
	        <div class="clear"></div></form>
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
