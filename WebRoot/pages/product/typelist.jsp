<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.jnu.web.util.PagingUtil,cn.edu.jnu.web.util.PositionUtil,cn.edu.jnu.web.view.dao.TypeBookList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上二手教材书店-${typeBooks.type.typeName}</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body><a id="top"></a>
    <%@ include file="/pages/share/menu.jsp" %>
    <link rel="stylesheet" type="text/css" href="/resources/css/product.css">
    
    <div id="main">
		
		<div class="go_back">
			<a href="javascript:void(0);" onclick="document.getElementById('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
		</div>
		
		<div class="view_path">
			<a href="index">首页</a> &gt; 
			<a href="javascript:void(0);">分类浏览</a> &gt; 
			<%=PositionUtil.getPosition(((TypeBookList)request.getAttribute("typeBooks")).getType(), "view/type/list") %>
			<br>
		</div>
		
		<div id="left">
			<div class="left_t">
				<h3>${typeBooks.type.typeName}</h3>
				<div class="sort_second">
					<ul class="left_t_c">
						<c:forEach items="${typeBooks.childTypes}" var="ct">
						<li>
							<a href="view/type/list?type=${ct.typeId}&start=0&limit=15">·${ct.typeName}</a> 
						</li>
						</c:forEach>
					</ul>
					<div class="clear"></div>
					<p>&nbsp;&nbsp;</p>
					<p><a href=" ">查看全部图书分类&gt;&gt;</a></p>
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
						showtypelist(0, 15, param);
					}
				</script>
			</div>
			<div style="display:none;" id="type"><%=request.getParameter("type") %></div>
			<div id="mypros">正在加载...</div>
			<script type="text/javascript">
				function showtypelist(start, limit, sort) {
					var params = 'start=' + start + 
								 '&limit=' + limit + 
								 '&type='+$('type').innerHTML;
					if(sort != '') params = params + '&sort=' + sort;
					sendRequest('view/ajax/type/list?' + params, function() {
						$('mypros').innerHTML = XMLHttpReq.responseText;
					});
				}
				showtypelist(0, 15, '');
			</script>
			
		</div>
		<div class="clear"></div>
	</div>
	
    <%@include file="/pages/share/bottom.jsp" %>
  </body>
</html>
