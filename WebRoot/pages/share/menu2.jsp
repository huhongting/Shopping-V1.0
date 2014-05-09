<%@page import="cn.edu.jnu.web.entity.user.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<link rel="stylesheet" type="text/css" href="/resources/css/css.css" >
<script type="text/javascript" src="/resources/js/com.js"></script>
<script type="text/javascript" src="/resources/js/h2012.js"></script>
<script type="text/javascript" src="/resources/js/ga.js"></script>
<script type="text/javascript" src="/resources/js/shoppingcar.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/js/login.js" charset="gbk"></script>

<div id="head">
	<!-- 顶部工具栏 -->
    <div class="head_top" id="header_container">
        <div class="w1002 mCenter">
            <div class="wlecome" id="logon">
				<span class="black">欢迎光临&nbsp;<a href="index">IShare网上书店</a></span> 
				<%
				Object obj = request.getSession().getAttribute("user");
				if(obj == null) {
				%> 
				［<a rel="nofollow" href="/view/login">登录</a>］［<a href="/view/register">注册</a>］
				<%
				} else {
					User user = (User) obj;
				%>
				 [<a href="view/user/account"><%=user.getName() %></a>&nbsp;&nbsp;<a href="view/user/logout">离开</a>]
				<%
				}
				%>
			</div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<div style="padding-left: 170"><div class="cp_logo fLeft"></div></div>
<div class="clear"></div>
