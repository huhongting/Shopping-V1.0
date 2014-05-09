<%@page import="cn.edu.jnu.web.entity.order.PaymentWay"%>
<%@page import="cn.edu.jnu.web.entity.order.DeliverWay"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>确认订单信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body onload="countTotal()">
  <%@ include file="/pages/share/menu.jsp" %>
  <link rel="stylesheet" type="text/css" href="/resources/css/dingdan.css">
  <link rel="stylesheet" type="text/css" href="/resources/css/my_cp.css">
    
    <div id="main">
    	<div class="cp_logo fLeft"></div>
   		<div class="clear"></div>
        <div class="cart_box">
            <div id="stepCount" class="flow_step">
                <ul class="cols_2">
                    <li class="step_1">1.我的购物车</li>
                    <li class="step_2">2.确认订单信息</li>
                    <li class="step_3">3.成功提交订单</li>
                </ul>
            </div>
            <div class="flow_step_title">
                <h2>确认订单信息</h2>
                <span class="login_step"></span>
                <div class="clear">
                </div>
            </div>
            <div class="flow_order_list">
                <table border="0" cellpadding="0" cellspacing="0">
                    <tbody><tr>
                        <td class="con_title">收货人信息<a href="javascript:void(0);" onclick="add()">[添加新地址]</a></td>
                    </tr>
                    <tr><td id="addnewaddr" style="display:none">
						<div style="padding-left: 100">
							<input type="hidden" id="uid" value="${user.id}">
							姓名：<input type="text" id="name">&nbsp;<font color=red>[不能为空]</font><br>
							固话：<input type="text" id="tell"><br>
							手机：<input type="text" id="mobile">&nbsp;<font color=red>[不能为空,长度为11]</font><br>
							邮箱：<input type="text" id="email">&nbsp;<font color=red>[不能为空]</font><br>
							邮编：<input type="text" id="post">&nbsp;<font color=red>[不能为空,长度为6]</font><br>
							地址：<input type="text" id="addr">&nbsp;<font color=red>[不能为空]</font><br>
							<input type="button" value="确定" onclick="submit()">
							<input type="button" value="取消" onclick="cancel()">
							<script type="text/javascript">
								function check() {
									var name = $('name').value.replace(/ /g, '');
									var mobile = $('mobile').value;
									var post = $('post').value;
									var email = $('email').value;
									var addr = $('addr').value;
									if(name == '') {
										//alert('post error');
										return false;
									}
									if(isNaN(parseInt(post)) || post.length != 6) {
										//alert('post error');
										return false;
									}
									if(isNaN(parseInt(mobile)) || mobile.length != 11) {
										//alert('mobile error');
										return false;
									}
									if(!isMail(email)) {
										//alert('email error');
										return false;
									}
									if(addr == '' || addr.length == 0) {
										//alert('address error');
										return false;
									}
									return true;
								}
								function submit() {
									if(!check()) {
										alert('填写信息错误!');
										return;
									}
									// add a new contact
									var tell = $('tell').value;
									var mobile = $('mobile').value;
									var email = $('email').value;
									var post = $('post').value;
									var addr = $('addr').value;
									var uid = $('uid').value;
									var name = $('name').value.replace(/ /g, '');
									sendRequest('view/user/contact/add?uid=' + uid + 
														'&name=' + encodeURI(encodeURI(name)) + 
														'&tell=' + tell + 
														'&mobile=' + mobile + 
														'&email=' + email + 
														'&post=' + post + 
														'&addr=' + encodeURI(encodeURI(addr)), function() {
										loadDelivers();
									});
									cancel();
								}
								function add() {
									$('addnewaddr').style.display = '';
								}
								function cancel() {
									$('addnewaddr').style.display = 'none';
									$('tell').value = '';
									$('mobile').value = '';
									$('email').value = '';
									$('post').value = '';
									$('addr').value = '';
								}
								function del_t(id) {
									sendRequest('view/user/contact/del?id='+id, function() {
										loadDelivers();
	                            	});
								}
							</script>
						</div>
					</td></tr>
                    <tr>
                        <td id="myinfodiv"><div class="address_useful">
                        	<p>常用收货地址</p>
                            <dl id="deliverinfo"></dl>
                            <script type="text/javascript">
                            	function loadDelivers() {
                            		var req = createXMLHttpRequest();
                                	sendRequest2(req, 'view/user/contact/list?uid=${user.id}', function() {
                                		$('deliverinfo').innerHTML = req.responseText;
                                	});
                            	}
                            	loadDelivers();
                            </script>
                           </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="con_title">送货信息</td>
                    </tr>
                    <tr>
                        <td id="sendmodediv">
							<div class="delive_infor">
								<script type="text/javascript">
									function sdf(n) {
										var f = $('dfee');
										if(n.value == '特快专递') {
											f.innerHTML = parseFloat(20).toFixed(2);
										} else if(n.value == '普通快递') {
											f.innerHTML = parseFloat(10).toFixed(2);
										} else if(n.value == '平邮') {
											f.innerHTML = parseFloat(5).toFixed(2);
										} else if(n.value == '邮政EMS') {
											f.innerHTML = parseFloat(38).toFixed(2);
										} else if(n.value == '宅急送') {
											f.innerHTML = parseFloat(18).toFixed(2);
										}
										countTotal();
									}
								</script>
								<table border="0" cellpadding="0" cellspacing="0">
									<tbody>
									<%
									for(DeliverWay dw : DeliverWay.values()) {
									%>
									<tr>
										<td class="del_name">
											<input name="sendtype" value="<%=dw.getName() %>" checked="checked" onclick="sdf(this);" type="radio"><%=dw.getName() %>
										</td>
										<td class="del_price"><%=dw.getPrice() %></td>
										<td class="del_inf" style="width:500px;"><%=dw.getNote() %></td>
									</tr>
									<%	
									}
									%>
								</tbody></table>
							</div>
						</td>
                    </tr>
                    <tr>
                        <td class="con_title">支付方式</td>
                    </tr>
                    <tr>
                        <td id="paymodediv">
							<div class="pay_type">
								<table border="0" cellpadding="0" cellspacing="0"><tbody>
									<%
									for(PaymentWay pw : PaymentWay.values()) {
									%>
									<tr><td>
										<input name="paytype" value="<%=pw.getName() %>" checked="checked" type="radio"><%=pw.getName() %>
									</td></tr>
									<%
									}
									%>
									</tbody></table>
								</div>
							</td>
						</tr>
						<script type="text/javascript">
							function setfapiao() {
								$('fapiaodiv').style.display = '';
								$('fapiao').style.display = 'none';
							}
							function fp() {
								fpc($('fpo'));
							}
							function fpc(n) {
								if(n.checked == false) {
									$('fapiaoinfo').style.display = 'none';
								} else {
									$('fapiaoinfo').style.display = '';
								}
							}
							function getfapiao() {
								$('fpo').checked = '';
								$('fycheck').checked = '';
								$('fapiaodiv').style.display = 'none';
								$('fapiaoinfo').style.display = 'none';
							}
							function upfapiao() {
								var tt = '&nbsp;&nbsp;&nbsp;&nbsp;抬头：';
								var nr = '内容：';
								var fj = '附加信息：';
								
								//getfapiao();
								//$('fapiao').style.display = '';
								
								if($('fpo').checked == true) {
									tt = tt + $('fapiao0').value;
									nr = nr + $('fapiao1').value;
									if($('fycheck').checked == true) {
										fj = fj + $('fy').value;
									} else {
										fj = fj + '无';
									}
									$('fapiao').innerHTML = tt + '&nbsp;&nbsp;&nbsp;&nbsp;' + nr + '&nbsp;&nbsp;&nbsp;&nbsp;' + fj;
									getfapiao();
									$('fapiao').style.display = '';
									$('ofp').value = $('fapiao').innerHTML.replace(/&nbsp;/g, ' ');
								}
							}
						</script>
						<tr>
							<td class="con_title">
								发票及附言<a href="javascript:void(0)" onclick="setfapiao()">[修改]</a>
							</td>
						</tr>
						<tr><td id="fapiao"></td></tr>
						<tr>
							<td id="fapiaodiv" style="display:none"><div class="inv_a_pos">
								<table border="0" cellpadding="0" cellspacing="0"><tbody>
									<tr>
										<td class="inv_name" style="width:100px">
											<input class="normal" name="fpo" id="fpo" onclick="fp();" onchange="fpc(this)" value="1" type="checkbox">开具发票&nbsp;&nbsp;
										</td>
										<td></td>
									</tr>
									<tr>                                	
										<td colspan="2">
											<div id="fapiaoinfo" style="display:none">
												<table border="0" cellpadding="0" cellspacing="0">
													<tbody><tr>
														<td class="inv_name" style="width:100px">
															<span id="fpTag1" style="margin-left:20px">发票抬头</span>
														</td>
														<td>
															<select id="fapiao0">
																<option selected="selected" value="个人">个人</option>
																<option value="公司">公司</option>
															</select>   
														</td>
													</tr>
													<tr> 
														<td class="inv_name">
															<span id="fpTag3" style="margin-left:20px">发票内容</span>
														</td>
														<td>
															<select name="fapiao1" id="fapiao1">
																<option selected="selected" value="图书">图书</option>
																<option value="资料">资料</option>
																<option value="教材">教材</option>
															</select>
														</td>
													</tr>
													</tbody></table>
												</div>
											</td>
										</tr>
										<tr>
											<td class="inv_name" style="width:100px" valign="top">
												<input id="fycheck" class="normal" type="checkbox">附加留言
											</td>
											<td align="left">
												<textarea name="fy" style="width:500px;resize:none" maxlength="100" wrap="virtual" class="buy_border1" id="fy"></textarea>
											</td>
										</tr>
										<tr>
											<td colspan="2">
												<div class="submit">
													<a class="pro_ans_y" onclick="upfapiao();" id="fapiaobn">保存信息</a>
													<a style="margin-left:10px;" class="pro_ans_n" onclick="getfapiao()">暂不保存</a>
												</div>
											</td>
										</tr>
									</tbody></table>
								</div>
							</td>
						</tr>
                    <tr>
                        <td class="con_title">
                            商品清单<span><a href="view/user/shoppingcar">返回修改购物车</a></span>
                        </td>
                    </tr>
                    <tr>
                        <td id="confirm_bookdiv">
							<div class="pro_list">
								<table class="mTop10" border="0" cellpadding="0" cellspacing="0">
								<tbody><tr>
									<th style="width:750px">商品名称</th>
									<th>优惠价</th>
									<th style="width:60px">数量</th>
									<th>小计</th>
								</tr>
								<c:forEach items="${cart.carBooks}" var="cb">
								<tr>
									<td class="cart_book_n">
										<div style="width:730px;">
											<a href="view/book/show?id=${cb.book.id}" target="_blank">${cb.book.bookName}</a>
										</div>
									</td>
									<td>￥${cb.book.price}</td>
									<td>${cb.num}</td>
									<td>￥<span name="scount"><fmt:formatNumber value="${cb.book.price * cb.num}" minFractionDigits="2" maxFractionDigits="2" /></span></td>
								</tr>
								</c:forEach>
								</tbody></table>
							</div>
						</td>
                    </tr>
                    <tr>
                        <td class="con_title">
                        	结算信息
                        </td>
                    </tr>
                    <tr>
                        <td id="confirmdiv">
                        	
                        	<div class="checkout_infor">
                        		<script type="text/javascript">
                        			function changeULstate(n) {
                        				if(n.innerHTML.indexOf('+') > -1) {
	                        				n.innerHTML = '（-）使用本站电子货币支付';
    	                    				$('ulList').style.display = '';
                        				} else {
                        					n.innerHTML = '（+）使用本站电子货币支付';
    	                    				$('ulList').style.display = 'none';
                        				}
                        			}
                        		</script>
                    			<h3 id="ulH" onclick="changeULstate(this);">（+）使用本站电子货币支付</h3>
                            	<ul id="ulList" style="display:none;"> 
                            		<c:choose>
                            		<c:when test="${user.account.money <= 0}"> 
                            		<li>
                                    	<div>
                                      		<span style="padding-left:15px">您目前没有存在本站的余额可供使用</span>
                                      	</div>
                                	</li>
                                	</c:when>
                                	<c:otherwise>
                                	<li>
                                		<script type="text/javascript">
                                			function oepsubmit() {
                                				var v = $('oep').value;
                                				var reg = new RegExp("^[0-9]*$");
                                				if(v == '') return alert('输入错误！');
                                				if(reg.test(v)) {
	                                				v = parseFloat(v);
	                                				$('epay').value = v;
	                                				alert('支付成功！');
                                				} else
	                                				alert('输入错误！');
                                			}
                                		</script>
                                		<div class="mTop10">
                                        	<b>当前帐号剩余本站电子货币：￥<fmt:formatNumber value="${user.account.money}" minFractionDigits="2" /></b>
                                        	<div id="goushukaDiv">
        										<%-- <p>温馨提示：每单使用电子货币支付不能超过总额的50%</p> --%>
                                           		为本订单支付：<input id="oep" type="text"><input type="button" value="确定" onclick="oepsubmit()">
                                           		<div class="clear"></div>
                                        	</div>
                                    	</div>
                                	</li>
                                </c:otherwise>
                                </c:choose>
                            </ul>
                    	</div>
                        
							<div id="checkout_list" class="checkout_list">
								<div id="upcountDiv">
									<table style="_margin-right:30px" border="0" cellpadding="5" cellspacing="0" width="100%">
										<tbody><tr style="height:10px">
										<td class="check_l_t" style="height:0px"></td>
										</tr> 
										<tr>
											<td class="check_l_c">
												<div class="check_result">商品金额总计：￥<span id="total"></span><br>运费总计：￥<span id="dfee">18.00</span></div>
												<script type="text/javascript">
													function countTotal() {
														var nodes = document.getElementsByName("scount");
														var n = 0;
														for(var i=0; i<nodes.length; i++) {
															n = parseFloat(n) + parseFloat(nodes[i].innerHTML);
														}
														n = n.toFixed(2);
														$("total").innerHTML = n;
														var df = parseFloat($("dfee").innerHTML).toFixed(2);
														$("ptotal").innerHTML = (parseFloat(n) + parseFloat(df)).toFixed(2);
													}
												</script>
											</td>
										</tr>
										<tr>
											<td class="check_l_b"></td>
										</tr>
									</tbody></table>
									<div class="clear"></div>
									<h2 style="_padding-right:60px">应付总计：￥<span id="ptotal"></span></h2>
								</div>
								<p style="_padding-right:210px">请确认以上信息无误</p>
								<a id="check_end" class="check_end" href="javascript:void(0)" onclick="setorderinfo()">提交订单</a>
								<form id="oform" action="view/user/submitorder" method="POST">
									<input type=hidden name="epay" id="epay" value="0">
									<input type=hidden value="${user.id}" name="uid" id="uid">
									<input type=hidden name="deliverid" id="deliverid">
									<input type=hidden name="deliverway" id="deliverway">
									<input type=hidden name="paymentway" id="paymentway">
									<input type=hidden name="fapiao" id="ofp">
								</form>
								<script type="text/javascript">
									function setorderinfo() {
										var sendtype = document.getElementsByName('sendtype');
										for(var i=0; i<sendtype.length; i++) {
											if(sendtype[i].checked == true) {
												$('deliverway').value = sendtype[i].value;
											}
										}
										var paytype = document.getElementsByName('paytype');
										for(var i=0; i<paytype.length; i++) {
											if(paytype[i].checked == true) {
												$('paymentway').value = paytype[i].value;
											}
										}
										var addrid = document.getElementsByName('addrid');
										for(var i=0; i<addrid.length; i++) {
											if(addrid[i].checked == true) {
												$('deliverid').value = addrid[i].value;
											}
										}
										
										var form = $('oform');
										if(form.deliverid.value == '') {
											alert('请检查订单，并正确填写信息！');
											return;
										}
										form.submit();
									}
								</script>
							</div>
						</td>
					</tr>
                </tbody></table>
            </div>
        </div>
    </div>
    
    <script type="text/javascript">
    	document.getElementById('dh').style.display = 'none';
    	document.getElementById('gw_sh').style.display = 'none';
    </script>
    
    <%@ include file="/pages/share/bottom.jsp" %>
    
  </body>
</html>
