<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.jnu.web.util.PositionUtil,cn.edu.jnu.web.entity.Book" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>IShare网上书店-${bbook.bookName}</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body><a id="top"> </a>
	<%@ include file="/pages/share/menu.jsp" %>
	<link rel="stylesheet" type="text/css" href="/resources/css/product.css">
	<link rel="stylesheet" type="text/css" href="/resources/css/shupinginfo.css">
	<script type="text/javascript" src="/resources/js/login.js"></script>
	
	<div id="main">
		<c:choose>
			<c:when test="${bbook == null}"><div style="text-align:center;height:250"><h1>&nbsp;</h1><h1>书籍不存在或者已经下架</h1></div></c:when>
			<c:otherwise>
		
		<div class="go_back">
			<a href="javascript:void(0);" onclick="document.getElementById('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
		</div>
	
		<div class="view_path" id="infonav">
			<a href="index" target="_blank">IShare网上书店</a>&nbsp;&gt;&nbsp;
			<%=PositionUtil.getPosition(((Book)request.getAttribute("bbook")).getBookType(), "view/type/list") %>
		</div>
	
		<div id="right">
	        <div class="pro_book">
				<div class="pro_book">
					<h1>${bbook.bookName}</h1>
					<%-- <div class="pro_name_intr">
						<span>${bbook.saleInfo}</span>
					</div> --%>
					<c:if test="${bbook.number == 0}">
					<div style="position: absolute;margin: 40 0 0 100;z-index:3"><img src="/resources/images/nobook.png"></div>
					</c:if>
					<div class="pro_book_img">
						<dl>
							<dt class="book_s"> 
								<img src="${bbook.picUrl}" n="-1" s="1" border="0">
							</dt>
						</dl>
					</div>
	            </div>        	 
	
	            <div class="pro_buy_intr">
	            	<ul>
	                	<li>定价：<span class="pro_buy_pri">￥${bbook.normalPrice}</span></li>
						<li>优惠价：<span class="pro_buy_sen">￥${bbook.price}</span></li>	
	                    <li>
							<span class="fLeft">促销活动：</span>
							<span class="pro_buy_act">${bbook.saleInfo}</span>
							<div class="clear"></div>
						</li>
	                    <li class="pro_buy_bg">
	                    	<span class="fLeft">我要买：</span>
							<a class="lower_book_q" onclick="valueUp(-1)"></a>
							<input type="text" value="1" id="booknum" name="booknum">
							<a class="add_book_q" onclick="valueUp(1)"></a>
							<div class="clear"></div>
	                        <div>
	                        	<c:choose>
	                        		<c:when test="${bbook.number == 0}">
	                        		<a class="pro_add_shopcart" id="bck"></a>
	                        		</c:when>
	                        		<c:otherwise>
	                        		<a class="pro_add_shopcart" id="bck" onclick="putCar(${bbook.id}, 'booknum')"></a>
	                        		</c:otherwise>
	                        	</c:choose>
	                        </div>
	                        <div class="clear"></div>
	                    </li>
	                </ul>
	                <div class="odiv" id="box" style="display:none">
						<table border="0" cellpadding="0" cellspacing="0">
							<tbody><tr><td class="box"> <div id="spnum">  </div></td></tr></tbody></table>
		            </div>
	            </div>
	            <div class="clear"></div>
	        </div>
	
			<!--图书展示区end-->
			<div class="right_pro_intr">
				<div id="con_a_1" name="con_a_1" style="display:">
					<div class="pro_r_deta">
						<h3>基本信息</h3>
						<ul>
							<li>书名：${bbook.bookName}</li>
						</ul>
						<ul> 
							<li>作者：  
								<strong>${bbook.auther}</strong>
							</li>
							<%--<li>译者：
								<a href=" " target="_blank">${bbook.auther}</a>  
							</li>--%>
							<li>出版社：${bbook.press.name}</li>
							<%--<li>ISBN：<strong>9787115347237</strong></li> --%>
							<li>上架时间：${bbook.upTime}</li>
							<li>出版日期：${bbook.pubTime}</li>
							<%--<li>开本：16开</li>
							<li>页码：250</li>
							<li>版次：1-1</li> --%>
							<li>
								<div style="display:block; float:left">所属分类：</div>
								<span style="display:block; float:left"> 
									<style type="text/css">
										.red14 h1 {
											font-size: 14px;
											line-height: 20px;
											display: inline;
										}
									</style>
									<%=PositionUtil.getPosition(((Book)request.getAttribute("bbook")), "view/type/list") %>
								</span>
								<div style="clear:both"></div>
							</li> 
						</ul>
					</div>
					<c:if test="${bbook.note != ''}">
					<div class="pro_r_deta">
						<h3 id="chatu_title">商品描述</h3>
						${bbook.note}
					</div>
					</c:if>
					<c:if test="${total > 0}">
					<div class="pro_r_deta">
						<h3 id="chatu_title">商品评论</h3>
						<div id="pro_r_data" class="pro_r_data"></div>
						<c:if test="${total > 10}">
						<div id="showall" style="text-align:right;padding:5 12 0 0;"><a href="javascript:void(0)" onclick="loadpl(-1, -1, '${bbook.id}')">显示所有</a></div>
						</c:if>
						<script type="text/javascript">
							function loadpl(start, limit, bid) {
								var req = createXMLHttpRequest();
								sendRequest2(req, 'view/comment/list?' + 
										'bid=' + bid + 
										'&start=' + start + 
										'&limit=' + limit, function() {
										if(req.readyState == 4) {
				              				if(req.status == 200) {
				              					$('pro_r_data').innerHTML = req.responseText;
				              				}
										}
								});
							}
							loadpl(0, 10, '${bbook.id}');
							function newfollow(cid, f) {
								var req = createXMLHttpRequest();
								sendRequest2(req, 'view/comment/update?' + 
										'cid=' + cid + 
										'&usefull=' + f, function() {
										if(req.readyState == 4) {
				              				if(req.status == 200) {
				              					var success = req.responseXML.getElementsByTagName("success")[0].firstChild.nodeValue;
				              					if(success) {
				              						var uf = req.responseXML.getElementsByTagName("usefull")[0].firstChild.nodeValue;
				              						if(f == true) {
				              							$('uf').innerHTML = uf;
				              						} else {
				              							$('ul').innerHTML = uf;
				              						}
				              					}
				              				}
										}
								});
							}
						</script>
					</div>
					</c:if>
				</div>
			 </div>
		    <div class="clear"></div>
		</div>
	
		<!-- Left -->        
		<div id="left">
			<div id="banner_FreqBoughtTogether2">
				<div class="left_t">
					<h3>同类畅销商品</h3> 
					<div id="hotnews2_content" style="height: auto">
	                    <ul id="cxb"></ul>
	                </div>
					<div class="clear"></div>
				</div>
			</div>
		
			<div id="llls">
				<div class="left_t">
					<h3>最近浏览</h3>
					<ul class="left_t_c" id="yhis">
					</ul>
					<div class="clear"></div>
					<script type="text/javascript">
						$('cxb').innerHTML = '<img src="/resources/images/load.gif" style="width:14;height:14">&nbsp;数据正在加载...';
	                	$('yhis').innerHTML = '<img src="/resources/images/load.gif" style="width:14;height:14">&nbsp;数据正在加载...';
	                	var req1 = createXMLHttpRequest();
	                	sendRequest2(req1, 'view/product/userhis', function () {
	                		$('yhis').innerHTML = req1.responseText;
	                	});
	                	var req2 = createXMLHttpRequest();
	                	sendRequest2(req2, 'view/product/changxiao?tid=${bbook.bookType.typeId}', function () {
	                		$('cxb').innerHTML = req2.responseText;
	                	});
	                </script>
				</div>
			</div>
			<div class="clear"></div> 
		</div>
    	<div class="clear"></div>
		<!-- Left end -->	
	</c:otherwise>
	</c:choose>
	</div>


	
	<div id="footer">
		<%@ include file="/pages/share/bottom.jsp" %>
	</div>
	
  </body>
</html>
