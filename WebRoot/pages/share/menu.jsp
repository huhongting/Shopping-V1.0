<%@page import="cn.edu.jnu.web.entity.user.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<link rel="stylesheet" type="text/css" href="/resources/css/css.css" >
<script type="text/javascript" src="/resources/js/com.js"></script>
<script type="text/javascript" src="/resources/js/h2012.js"></script>
<script type="text/javascript" src="/resources/js/ga.js"></script>
<script type="text/javascript" src="/resources/js/shoppingcar.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/js/login.js" charset="gbk"></script>

<!-- head start -->
<table id="outer" style="position: absolute; left: 175px; top: 422px; z-index: 9; display: none;" onmouseout="ot2(this);" onmouseover="ov2(this);" cellpadding="0" cellspacing="0"><tbody>
	<tr>
        <td>
            <div style="position: absolute; height: 42px; _height: 44px; width: 5px; background: #f1f6fe;
                margin: 1px 0 0 212px; _margin-right: -2px; z-index: 10;"></div>
            <div id="inner"></div>
        </td>
    </tr>
</tbody></table>
<table id="outerX" style="position: absolute; left: 854px; top: 80px; z-index: 100; display: none;" onmouseout="otX(this);" cellpadding="0" cellspacing="0">
    <tbody><tr>
        <td id="innerX" style="width: auto;">
                <div id="shop_cart_pos" class="shop_cart_pos" style="margin: auto auto auto 0px">
                    <div class="shop_cart_bor"></div>
                    <div id="cartbookarr"></div>
                    <div class="clear">
                    </div>
                </div>
        </td>
    </tr>
</tbody></table>

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
    <div class="w1002 mCenter pButtom15" id="gw_sh">
		<!-- 购物车 -->
        <div class="shopCart fRight">
            <div class="head_cart fLeft" onmouseover="updateCar()">
            	<a class="head_link" id="gwc" href="view/user/shoppingcar">&nbsp;
            		购物车&nbsp;<font color=red id="shopcount">0</font>&nbsp;件
            	</a>
            </div>
            <script type="text/javascript">updateCar();</script>
        </div>
		<!-- LOGO -->
        <div class="cp_logo fLeft">
            <a href=" "></a>
        </div>
		<!-- 搜索 -->
        <div class="head_search fLeft">
            <form id="wg001" style="margin: 0px;" name="wg001" onsubmit="return checkSubmit();">
				<input name="key" id="skeys" class="search_text" type="text" value="${keys}">
				<input class="search_button" type="submit" value=" ">
				<script type="text/javascript">
	            	function checkSubmit() {
	            		var key = document.getElementById('skeys');
	            		if(key.value.replace(/ /g, '').length == 0) return false;
	            		key = encodeURI(encodeURI(key.value));
	            		window.location.href = "view/search?key=" + key + 
        				"&type=0&start=0&limit=15&sorts=3";
	            		return false;
	            	}
            	</script>
            </form>
            <div class="clear"></div>
            <%--<div class="hotSearch">
                 	热门搜索：<a rel="nofollow" target="_blank" href=" ">Java&nbsp;&nbsp;</a>
                <a rel="nofollow" target="_blank" href=" ">iOS&nbsp;&nbsp;</a>
                <a rel="nofollow" target="_blank" href=" "><font color="red">普林斯顿数学</font>&nbsp;&nbsp;</a>
			</div>--%>
		</div>
        <div class="clear">
        </div>
    </div>
    <!-- 菜单导航栏 -->
    <div class="nav_bg" id="dh">
        <div class="w1002 mCenter" style="position: relative;">
            <h2 class="fLeft cartTitle">
                <a id="zhuti" v='[0,0]' class="bookSort" href="javascript:void(0);">全部图书分类</a>
			</h2>
            <div id="zhuti_data" style="display: none">
                <div style="position: absolute;" class="sortHidden" id="caidan">
                    <ul>
                        <c:forEach items="${sessionScope.menuList}" var="list" begin="0" end="8" varStatus="status">
                       	<li id="out_${status.count}" onmouseover="ov()" onmouseout="ot()">
                            <div class="sort_big">
                                <h4>
                                    <em><a target="_blank" href="view/type/list?type=${list.rootType.typeId}&start=0&limit=15">${list.rootType.typeName}</a></em>
								</h4>
                            </div>
                            <div class="sort_bottom">
                                <h3>
                                    <em>
 	                                   	<c:forEach items="${list.childTypes}" var="fm" begin="0" end="1" varStatus="s">
										<a target="_blank" href="view/type/list?type=${fm.typeId}&start=0&limit=15">${fm.typeName}</a>
										<c:if test="${s.count==1}">|</c:if>
										</c:forEach>
									</em>
								</h3>
                            </div>
                            <div style="display: none" id="out_${status.count}_data">
                                <div class="sort_float">
                                    <dl>
                                        <dt>
                                            <p>分类</p>
                                        </dt>
                                        <dd>
                                        	<c:forEach items="${list.childTypes}" var="sm" varStatus="vs">
											<em><a target="_blank" href="view/type/list?type=${sm.typeId}&start=0&limit=15">${sm.typeName}</a><c:if test="${vs.last==false}">|</c:if></em> 
											</c:forEach>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
			<!-- 导航栏 -->
            <div class="head_nav">
                <a href="index">首页</a>
				<img class="vCenter mLeft5" src="/resources/images/nav_line.jpg">
                <a target="_blank" href="view/new/list">新书</a>
				<img class="vCenter mLeft5" src="/resources/images/nav_line.jpg">
                <a target="_blank" href="view/tejia/list">特价书</a><%--
				<img class="vCenter mLeft5" src="/resources/images/nav_line.jpg">
                <a target="_blank" href="view/cuxiao/list">促销</a>
				<img class="vCenter mLeft5" src="/resources/images/nav_line.jpg">
                <a target="_blank" href="view/video/list">视频教程</a>
            --%></div>
        </div>
    </div>
</div>
<span id="xtip" style="top: 414px;left: 761.5px;display:none;"></span>

<script type="text/javascript">init();</script>