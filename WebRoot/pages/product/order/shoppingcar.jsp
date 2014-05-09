<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/resources/js/shoppingcar.js"></script>
  </head>
  
  <body onload="countTotal()">
  <%@ include file="/pages/share/menu.jsp" %>
  <link rel="stylesheet" type="text/css" href="/resources/css/dingdan.css">
  <link rel="stylesheet" type="text/css" href="/resources/css/my_cp.css">
  <script type="text/javascript" src="/resources/js/login.js"></script>
  	<style type="text/css">
	    .book_img {
	        height: 91px;
	    }
    </style>

    <div>
	    <div id="tipdiv" class="box_window" style="top: 0px; left: 0px; display: none"></div>
	    <div id="tipdiv3" class="box_window" style="top: 0px; left: 0px; display: none"></div>
	    <div id="main">
	    	<div class="cp_logo fLeft"></div>
    		<div class="clear"></div>
	        <div class="cart_box">
	            <div class="flow_step">
	                <ul class="cols_1">
	                    <li class="step_1">1.我的购物车</li>
	                    <li class="step_2">2.确认订单信息</li>
	                    <li class="step_3">3.成功提交订单</li>
					</ul>
	            </div>
	            <div class="flow_step_title">
	                <h2 id="mycar">购物车</h2>
	                <label id="tipTag" style="float: left"></label>
	                <div class="clear"></div>
	            </div>
	            <script type="text/javascript">
					function valueUp(id, n) {
						var node = document.getElementById('cbnum' + id);
						var v = node.value;
						v = parseInt(v) + parseInt(n);
						if(v < 1) v = 1;
						node.value = v;
						countTotal();
						updateCartNum(id, 'cbnum'+id);
					}
					function valueChange(id) {
						var node = document.getElementById('cbnum' + id);
						node.value = parseInt(node.value);
						if(node.value < 1) node.value = 1;
						countTotal();
						updateCartNum(id, 'cbnum'+id);
					}
					function getTotal(id) {
						var tnode = document.getElementById('td_dz_' + id);
						var pnode = document.getElementById('tdp_' + id);
						var nnode = document.getElementById('cbnum' + id);
						var tp = parseFloat(pnode.innerHTML.replace('￥', '')) * parseInt(nnode.value);
						tnode.innerHTML = tp.toFixed(2);
					}
					function countTotal() {
						/*var totalBooks = 0;
						var normalPrice = 0.00;
						var totalPrice = 0.00;
						
						var nps = document.getElementsByName('bnp');
						var ps = document.getElementsByName('bp');
						var bns = document.getElementsByName('bn');
						
						for(var i=0; i<nps.length; i++) {
							var v = parseInt(bns[i].value);
							totalBooks = totalBooks + v;
							normalPrice = parseFloat(normalPrice + parseFloat(nps[i].innerHTML.replace('￥', '')) * v);
							totalPrice = totalPrice + parseFloat(ps[i].innerHTML.replace('￥', '')) * v;
							getTotal(bns[i].id.substring(bns[i].id.indexOf('num')+3));
							
						}
						
						document.getElementById('bookce').innerHTML = totalBooks;
						document.getElementById('total_yuanjia').innerHTML = '￥' + normalPrice;
						document.getElementById('total_account').innerHTML = '￥' + totalPrice;
						document.getElementById('total_economy').innerHTML = '￥' + (normalPrice - totalPrice);
						*/
						changeCheAll();
					}
					function changeCheAll() {
						var totalBooks = 0;
						var normalPrice = 0.00;
						var totalPrice = 0.00;
						
						var nps = document.getElementsByName('bnp');
						var ps = document.getElementsByName('bp');
						var bns = document.getElementsByName('bn');
						var ids = document.getElementsByName('idarray');
						
						for(var i=0; i<nps.length; i++) {
							if(ids[i].checked) {
								var v = parseInt(bns[i].value);
								totalBooks = totalBooks + v;
								normalPrice = parseFloat(normalPrice + parseFloat(nps[i].innerHTML.replace('￥', '')) * v);
								totalPrice = totalPrice + parseFloat(ps[i].innerHTML.replace('￥', '')) * v;
								getTotal(bns[i].id.substring(bns[i].id.indexOf('num')+3));
							}
						}
						
						try {
							document.getElementById('bookce').innerHTML = totalBooks;
							document.getElementById('total_yuanjia').innerHTML = '￥' + normalPrice.toFixed(2);
							document.getElementById('total_account').innerHTML = '￥' + totalPrice.toFixed(2);
							document.getElementById('total_economy').innerHTML = '￥' + (normalPrice - totalPrice).toFixed(2);
						} catch(e) {}
					}
					function isselectall() {
						var arr = document.getElementsByName('idarray');
						var n = document.getElementById('chkall');
						for(var i=0; i<arr.length; i++) {
							if(arr[i].checked == '') {
								n.checked = '';
								return;
							}
						}
						n.checked = 'checked';
					}
					function selectall() {
						var n = document.getElementById('chkall');
						var arr = document.getElementsByName('idarray');
						if(n.checked) {
							for(var i=0; i<arr.length; i++) {
								arr[i].checked = 'checked';
							}
						} else {
							for(var i=0; i<arr.length; i++) {
								arr[i].checked = '';
							}
						}
						countTotal();
					}
					function del_chek() {
						var arr = document.getElementsByName('idarray');
						for(var i=0; i<arr.length; i++) {
							if(arr[i].checked) {
								del_fromchek(arr[i].id);
							}
						}
					}
					function del_fromchek(id) {
						/*delCookie('car.ishare.' + id);
						if(isCarNull) window.location.reload();
						else {
							window.location.reload();
							countTotal();
						}*/
						var req = createXMLHttpRequest();
						sendRequest2(req, 'view/shopping/cart/del?bid=' + id, function() {
							if(req.readyState == 4) {
								if(req.status == 200) {
									window.location.reload();
								}
							}
						});
					}
					function buygoon(v) {
						if(v) {
							document.getElementById('flow_buy_goon').className = 'flow_clear';						
							document.getElementById('flow_buy_goon').href = 'view/user/confirmorder';
						} else {
							document.getElementById('flow_buy_goon').className = 'flow_buy_goon';
						}
					}
				</script>
				<% boolean go = false; %>
				<div id="cartbook" class="flow_pro_list">
					<table id="tb" border="0" cellpadding="0" cellspacing="0"><tbody>
						<tr>
							<th width="5%">&nbsp;</th>
							<th width="47%">商品</th>
							<th width="8%">定价</th>
							<th width="10%">您的价格</th>
							<th width="9%">数量</th>
							<th width="8%">单品总价</th>
							<th id="tbac" width="5%">操作</th>
						</tr>
						<tr id="order_0">
                    		<td class="pro_order_type" colspan="8">
                        		订单类型：<span>纸版书</span>
                        	</td>
		                </tr>
		                <c:choose>
		                <c:when test="${cart.cartNumber == 0}">
						<tr id="nbookstip">
							<td colspan="8">
								<div class="flow_cart">
									<p>您的购物车中没有商品</p>
									<p><a href="index">现在就去选购吧！</a></p>
								</div>
							</td>
						</tr>
						</c:when>
						<c:otherwise>
						<c:forEach items="${cart.carBooks}" var="cb">
						<tr class="booktr">
		                	<td><input name="idarray" id="${cb.book.id}" onclick="changeCheAll(); isselectall();" checked="checked" type="checkbox"></td>
							<td class="0">
								<div class="cart_book">
									<ul>
										<li style="height:91px;width:65px;"><a target="_blank" href="view/book/show?id=${cb.book.id}"><img style="height: 91px;" src="${cb.book.picUrl}"></a></li>
										<li class="cart_book_name"><a target="_blank" href="view/book/show?id=${cb.book.id}">${cb.book.bookName}</a></li>
									</ul>
								</div>
							</td>
							<td class="book_price" name="bnp" id="tdnp_${cb.book.id}">￥${cb.book.normalPrice}</td>
							<td class="book_dis" name="bp" id="tdp_${cb.book.id}">￥${cb.book.price}</td>
							<td class="571-0">
								<a class="lower_book_q" href="javascript:void(0)" onclick="valueUp(${cb.book.id}, -1)"></a>
								<input class="book_amount" name="bn" id="cbnum${cb.book.id}" value="${cb.num}" onchange="valueChange(${cb.book.id})" type="text">
								<a class="add_book_q" href="javascript:void(0)" onclick="valueUp(${cb.book.id}, 1)"></a>
							</td>
							<td id="td_dz_${cb.book.id}"></td>
							<td>
								<a href="javascript:void(0)" onclick="del_fromchek(${cb.book.id})">删除</a>
							</td>
						</tr>
						</c:forEach> 
						<tr>
		                    <td colspan="8">
		                        <div class="pro_clear">共<span id="bookce"></span>件商品，商品金额共计<span id="total_yuanjia"></span>，优惠<span id="total_economy"></span><br>总计（不含运费）<span id="total_account" class="cart_all_p"></span></div>
		                        <div class="pro_choose">
		                        	<label>
		                        		<input checked="checked" id="chkall" onclick="selectall()" type="checkbox"><span>全选</span>
		                            </label>
		                            <a id="pro_quit" class="pro_quit" href="javascript:void(0)" onclick="del_chek()">删 除</a>
		                        </div>
		                        <div class="clear"></div>
		                    </td>
		                </tr>
						<% go = true; %>		                
		                </c:otherwise>
		                </c:choose>
					</tbody></table>
				</div>
	           
				<a id="flow_buy_goon" class="flow_buy_goon" href="index"></a>
				<script type="text/javascript">
					var v = <%=go %>;
					buygoon(v);
				</script>
	            <div class="clear"></div>
				
	            <div class="flow_cart_l">
	                <div class="flow_cart_nav">
	                    <a class="onhover" id="a2">最新浏览</a>
	                    <div class="clear"></div>
	                </div>
	                <div id="con_a_2" style="overflow:hidden;height:240px;" class="flow_show_b">
                		<ul id="bookhis" class="book_nav"></ul>
	                	<div class="clear"></div>
	                </div>
	                <script type="text/javascript">
	                	document.getElementById('bookhis').innerHTML = '<img src="/resources/images/load.gif" style="width:14;height:14">&nbsp;数据正在加载...';
	                	sendRequest('view/product/carhis', function () {
	                		document.getElementById('bookhis').innerHTML = XMLHttpReq.responseText;
	                	});
	         	       function buy(bid) {
	         	       		var req = createXMLHttpRequest();
	         				sendRequest2(req, 'view/shopping/cart/add?bid=' + bid + '&num=1', function() {
	         					if(req.readyState == 4) {
	         						if(req.status == 200) {
	         							window.location.reload();
	         						}
	         					}
	         				});
	         	       	}
	                </script>
	            </div>
	        </div>
	    </div>
	    <div id="addone"></div>
    </div>
    
    <% 
    Object ddo = request.getSession().getAttribute("user");
    String ddname = "";
    if(ddo != null) {
    	ddname = ((User) ddo).getName();
    }
    %>
    
    <script type="text/javascript">
    	document.getElementById('dh').style.display = 'none';
    	document.getElementById('gw_sh').style.display = 'none';
    	var ddn = '<%=ddname%>';
	    document.title = ddn + '购物车';
    </script>
    
    <%@ include file="/pages/share/bottom.jsp" %>
    
  </body>
</html>
