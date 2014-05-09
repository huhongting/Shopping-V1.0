<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.jnu.web.view.dao.TypeBookList,
				 cn.edu.jnu.web.util.PositionUtil,
				 cn.edu.jnu.web.util.PagingUtil" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上书店-特价图书</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <%@ include file="/pages/share/menu.jsp" %>
    <link rel="stylesheet" type="text/css" href="/resources/css/product.css">
    
    <div id="main">
		
		<div class="go_back">
			<a href="javascript:void(0);" onclick="document.getElementById('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
		</div>
		
		<div class="view_path">
			<a href="index">首页</a> &gt; 
			<a href="javascript:void(0);">分类浏览</a> &gt;
			<a href="view/tejia/list">特价书</a><c:if test="${typeBooks.type != null}"> &gt; 
			<%=PositionUtil.getPosition(((TypeBookList)request.getAttribute("typeBooks")).getType(), "view/tejia/type/list") %>
			</c:if>
			<br>
		</div>
		
		<div id="left">
			<div class="left_t">
				<h3>浏览筛选</h3>
				<div class="sort_second_m">
					<form name="webf1" action="view/tejia/list/s" method="POST" style="margin: 0px; padding: 0ox;">
							<table border="0" cellpadding="0" cellspacing="0" style="font-size: 10px;">
								<tbody><tr>
									<td class="t_right">售价区间</td>
									<td><input type="hidden" name="start" value="0">
										<input type="hidden" name="limit" value="15">
										<input type="hidden" name="type" value="<%=request.getParameter("type") %>">
										<input type="text" id="jia1" name="jia1">
										——
										<input type="text" id="jia2" name="jia2">
									</td>
								</tr>
								<tr>
									<td class="t_right">关键词</td>
									<td>
										<input class="input_l" type="text" id="keyw" name="key">
									</td>
								</tr>
								<tr>
									<td class="t_right">出版社</td>
									<td>
										<input class="input_l" type="text" id="cbs" name="cbs">
									</td>
								</tr>
								<tr>
									<td class="t_right">&nbsp;</td>
									<td>
										<%--<a class="pro_ans_y" style="display: none;" href="javascript:void(0)" id="searchbtn" onclick="searchBook()">提 交</a>--%>
										<input type="submit" id="subBtn" style="height: 20px;">
									</td>
								</tr>
							</tbody></table>
						</form>
				</div>
				<b>&nbsp;按分类浏览</b>
				<div id="ctl00_ContentPlaceHolder2_treecount" name="treecount"><h3>图书分类</h3>
					<dl class="search_sort">
						<c:forEach items="${typeBooks.childTypes}" var="type">
						<dt>
							<a target="_blank" href="view/tejia/type/list?start=0&limit=15&type=${type.typeId}&sort=upt_down">${type.typeName}</a>
						</dt>
						</c:forEach>
					</dl>
				</div>
			</div>
		</div>
		
		<div id="right">
			<div class="search_seq">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody><tr>
						<td width="40" style="font-size:12px">排序：</td>
							<td class="search_seq_bg">
								<ul>
									<li id="hot"><a href="javascript:void(0)" onclick="mysort(this)">图书销量</a></li>
									<li id="upt"><a href="javascript:void(0)" onclick="mysort(this)">上架时间</a></li>
									<li id="pri"><a href="javascript:void(0)" onclick="mysort(this)">价格升序</a></li>
									<li id="pub"><a href="javascript:void(0)" onclick="mysort(this)">出版日期</a></li>
								</ul>
							</td>
					</tr></tbody>
				</table>
				<div id="ms" style="display:none"><%=request.getParameter("sort") %></div>
				<script type="text/javascript">
					function mysort(n) {
						var nodes = [$('hot'), $('upt'), $('pri'), $('pub')];
						var node = n.parentNode;
						var param = '';
						if(node.className.indexOf('search_up') > -1) {
							node.className = 'search_down search_now';
							param = node.id + '_down';
						} else {
							node.className = 'search_up search_now';
							param = node.id + '_up';
						}
						for(var i=0; i<nodes.length; i++) {
							if(nodes[i].id != node.id) nodes[i].className = '';
						}
						window.location.href = 'view/tejia/list/s?type='+ $('type').innerHTML +'&jia1=${jia1}&jia2=${jia2}&key=${key}&cbs=${cbs}&start=0&limit=15&sort=' + param;
					}
					function ss() {
                		var s = $('ms').innerHTML;
                		if(s == 'hot_down') {
                			$('hot').className = 'search_down search_now';
                		} else if( s == 'hot_up') {
                			$('hot').className = 'search_up search_now';
                		} else if( s == 'upt_down') {
                			$('upt').className = 'search_down search_now';
                		} else if( s == 'upt_up') {
                			$('upt').className = 'search_up search_now';
                		} else if( s == 'pri_down') {
                			$('pri').className = 'search_down search_now';
                		} else if( s == 'pri_up') {
                			$('pri').className = 'search_up search_now';
                		} else if( s == 'pub_down') {
                			$('pub').className = 'search_down search_now';
                		} else if( s == 'pub_up') {
                			$('pub').className = 'search_up search_now';
                		}
                	}
					ss();
				</script>
			</div>
			<div id="type" style="display:none"><%=request.getParameter("type") %></div>
			
			<div id="mypros">

			<div class="search_result">
				<c:forEach items="${typeBooks.books.results}" var="b">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody><tr>
						<td valign="top" width="100"><a target="_blank" href="view/book/show?id=${b.id}"><img src="${b.picUrl}" n="-1" border="0" width="88"></a></td>
						<td valign="top">
							<ul>
								<li class="result_name">
									<a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a>
								</li>
								<li style="font-size:10px;">${b.auther} （著）</li>
								<li style="font-size:10px;">${b.press.name} | ${b.pubTime} 出版</li>
								<li style="font-size:10px;"><fmt:formatDate value="${b.upTime}" /> 上架</li>
								<li style="font-size:10px;">销量：${b.saleCount}</li>
								<li class="result_book">
									<ul>
										<li class="book_dis"><b>￥${b.price}</b></li>
										<li class="book_price">￥${b.normalPrice}</li>
										<li class="book_opera">
											<a class="book_buy" href="javascript:putCar2(${b.id}, 1)"></a>
										</li>
									</ul>
									<div class="clear"></div>
								</li>
							</ul>
						</td>
					</tr>
				</tbody></table>
				</c:forEach>
			</div>
			
			<div class="pro_pag"> 
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody><tr>
						<% PagingUtil.createPagingData(request, "typeBooks", "type"); %>
						<c:if test="${totalPage > 1}">
							<c:choose>
								<c:when test="${cur == 1}">
									<td><a href="javascript:void(0);">上一页</a></td>
								</c:when>
								<c:otherwise>
									<td><a href="view/tejia/list/s?type=${type}&jia1=${jia1}&jia2=${jia2}&key=${key}&cbs=${cbs}&start=${pre}&limit=${limit}">上一页</a></td>
								</c:otherwise>
							</c:choose>
							<c:forEach begin="1" end="${totalPage}" varStatus="v">
								<c:choose>
									<c:when test="${v.count == cur}">
										<td><span class="pro_pag_on">${v.count}</span></td>
									</c:when>
									<c:otherwise>
										<td><a href="view/tejia/list/s?type=${type}&jia1=${jia1}&jia2=${jia2}&key=${key}&cbs=${cbs}&start=${(v.count - 1) * limit}&limit=${limit}">${v.count}</a></td>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${cur == totalPage}">
									<td><a href="javascript:void(0);">下一页</a></td>
								</c:when>
								<c:otherwise>
									<td><a href="view/tejia/list/s?type=${type}&jia1=${jia1}&jia2=${jia2}&key=${key}&cbs=${cbs}&start=${next}&limit=${limit}">下一页</a></td>
								</c:otherwise>
							</c:choose>
						</c:if>
						</tr></tbody>
				</table>
			</div>

			</div>
		</div>
		<div class="clear"></div>
	</div>
	
    <%@ include file="/pages/share/bottom.jsp" %>
  </body>
</html>
