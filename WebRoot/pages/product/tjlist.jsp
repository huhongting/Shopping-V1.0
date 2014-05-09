<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
  
  </head>
  
  <body><a id="top"> </a>
	<%@ include file="/pages/share/menu.jsp" %>
	<link rel="stylesheet" type="text/css" href="/resources/css/product.css">
	
	<div id="main">
		
		<div class="go_back">
			<a href="javascript:void(0);" onclick="document.getElementById('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
		</div>
	
        <div class="view_path" id="infonav">
            
			<em><a href="index">IShare网上书店</a>&nbsp;&gt;&nbsp;</em>
		 	<h1>特价书</h1>
			<div class="clear"></div>
			<div id="right">
				<c:forEach items="${tjBooks}" var="list">
				<div class="content_a"> 
					<h3> 
						<em><a target="_blank" href="view/tejia/type/list?type=${list.type.typeId}&start=0&limit=15">更多&gt;&gt;</a></em> 
						<span class="second_h"></span>
						<span class="second_c"><font color=white>${list.type.typeName}</font></span>
						<span class="second_e"></span> 
						<div class="clear"></div> 
					</h3>
					<div class="content">
						<div class="c">
							<ul class="nav"></ul> 
							<div class="clear"></div>
							<div id="con_a1604_1" style="display:block"> 
								<div class="c">
									<div class="pro_book">
										<ul class="book_nav"> 
											<c:forEach items="${list.books.results}" var="b">
											<li>
												<div class="book_rank">
													<div class="book_s">
														<a target="_blank" href="view/book/show?id=${b.id}">
															<img src="${b.picUrl}" style="width:100px;height:146px;" n="-1">
														</a>
													</div>
													<p class="mTop10 price_height">
														<a target="_blank" href="view/book/show?id=${b.id}">(特价书)${b.bookName}</a>
													</p> 
													<p><span class="book_price">￥${b.normalPrice}</span><span class="book_dis">￥${b.price}</span></p>
													<a class="add_shopcart" href="javascript:void(0);" onclick="putCar2(${b.id}, 1)"></a>
												</div>
											</li> 
											</c:forEach>
										</ul>
										<div class="clear"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				</c:forEach>
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
										<input type="hidden" name="type" value="0">
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
					<div id="ctl00_ContentPlaceHolder2_treecount" name="treecount">
						<dl class="search_sort">
							<c:forEach items="${tjBooks}" var="tj">
							<dt>
								<a target="_blank" href="view/tejia/type/list?start=0&limit=15&type=${tj.type.typeId}&sort=upt_down">${tj.type.typeName}</a> 
								（ ${tj.books.total} ）
							</dt>
							</c:forEach>
						</dl>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	<%@ include file="/pages/share/bottom.jsp" %>

</body></html>