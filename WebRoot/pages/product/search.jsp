<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.jnu.web.util.PagingUtil" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上书店-站内搜索</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body onload="ss();">
    <%@ include file="/pages/share/menu.jsp" %>
    <link rel="stylesheet" type="text/css" href="/resources/css/product.css">

    <div id="main">
		<div class="view_path">
	    	<span class="fRight">
	    		使用 
	    		<c:forEach items="${keys}" var="k" varStatus="vs">
	    		<b class="orange">${k}</b>
	    		</c:forEach>
	    		搜索，共有 
	    		<b class="orange">${sbooks.books.total}  种</b> 
	    		商品
	    	</span>
	    	<a href="index">IShare网上书店</a> > 搜索
	    </div>
     	<div id="right">
	        <div class="search_seq">
	        	<table border="0" cellpadding="0" cellspacing="0">
	            	<tbody><tr>
	                	<td width="40" style="font-size:12px">排序：</td>
	                    <td class="search_seq_bg">
	                    	<ul id="sort_ul"> 
	                    		<li id="s1"><a href="javascript:void(0)" onclick="selectSort('s1')">图书销量</a></li>
                                <li id="s2"><a href="javascript:void(0)" onclick="selectSort('s2')">上架时间</a></li>
								<li id="s3"><a href="javascript:void(0)" onclick="selectSort('s3')">价格升序</a></li>
								<li id="s4"><a href="javascript:void(0)" onclick="selectSort('s4')">出版日期</a></li>
                            </ul>
                            <div id="ms" style="display:none;"><%=request.getParameter("sorts") %></div>
                            <div id="mt" style="display:none;"><%=request.getParameter("type") %></div>
                            <script type="text/javascript">
                            	function ss() {
                            		var s = $('ms').innerHTML;
                            		if(s == '1') {
                            			$('s3').className = 'search_down search_now';
                            		} else if( s == '2') {
                            			$('s3').className = 'search_up search_now';
                            		} else if( s == '3') {
                            			$('s2').className = 'search_down search_now';
                            		} else if( s == '4') {
                            			$('s2').className = 'search_up search_now';
                            		} else if( s == '5') {
                            			$('s4').className = 'search_down search_now';
                            		} else if( s == '6') {
                            			$('s4').className = 'search_up search_now';
                            		} else if( s == '7') {
                            			$('s1').className = 'search_down search_now';
                            		} else if( s == '8') {
                            			$('s1').className = 'search_up search_now';
                            		}
                            	}
                            	function selectSort(v) {
                            		var nodes = document.getElementById('sort_ul').childNodes;
                            		for(var i=0; i<nodes.length; i++) {
                            			var node = nodes[i];
                            			if(node.id != "undefined" && node.id == v) {
                            				if(node.className.indexOf('search_up') > -1) {
                            					node.className = 'search_now search_down';
                            				} else {
                            					node.className = 'search_now search_up';
                            				}
                            			} else {
                            				node.className = '';
                            			}
                            		}
                            		var node = document.getElementById(v);
                           			var url = window.location.href;
                           			url = url.substring(url.lastIndexOf('=', 0), url.length-1);
                            		if(node.className.indexOf('search_down') > -1) {
                            			if(node.id == 's1') {
                            				url = url + '7';
                            			} else if(node.id == 's2') {
                            				url = url + '3';
                            			} else if(node.id == 's3') {
                            				url = url + '1';
                            			} else if(node.id == 's4') {
                            				url = url + '5';
                            			}
                           				document.location.href = url;
                            		} else {
                            			if(node.id == 's1') {
                            				url = url + '8';
                            			} else if(node.id == 's2') {
                            				url = url + '4';
                            			} else if(node.id == 's3') {
                            				url = url + '2';
                            			} else if(node.id == 's4') {
                            				url = url + '6';
                            			}
                           				document.location.href = url;
                           			}
                            	}
                            </script>
	                    </td>
	                </tr>
                </tbody></table>
	        </div>
		  	<div class="search_result">
		  		<c:forEach items="${sbooks.books.results}" var="book">
	        	<table border="0" cellpadding="0" cellspacing="0">
	            	<tbody><tr>
	                	<td width="100" valign="top">
							<a target="_blank" href="view/book/show?id=${book.id}">
								<img border="0" height="128" width="90" src="${book.picUrl}"  n="-1">
							</a>
	                    </td>
	                    <td valign="top">
	                    	<ul>
	                        	<li class="result_name"> 
									<a target="_blank" href="view/book/show?id=${book.id}">${book.bookName}</a> 
								</li>
	                            <li style="font-size:10px;">  
									<p>
										${book.auther}<span>(著) | </span> ${book.auther}<span>(译)</span><br>
										${book.press.name}<br>
										上架时间：<fmt:formatDate value="${book.upTime}" /><br>
										图书销量：${book.saleCount}
									</p>
								</li>
								<li class="result_book">
	                            	<ul>
	                                	<li class="book_dis">优惠价:<b>￥${book.price}</b></li>
	                                    <li class="book_price">￥${book.normalPrice}</li>
	                                    <li class="book_opera"><a href="javascript:void(0)" onclick="putCar2(${book.id}, 1);" class="book_buy"></a></li>
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
	        	<script type="text/javascript">
	        		function showpage(start, limit) {
	        			var key = encodeURI(encodeURI($('skeys').value));
	        			var sorts = $('ms').innerHTML;
	        			var type = $('mt').innerHTML;
	        			window.location.href = 'view/search?key=' + key + 
	        									"&type=" + type + 
					        					"&start=" + start + 
					        					"&limit=" + limit + 
					        					"&sorts=" + sorts;
	        		}
	        	</script>
		        <table border="0" cellpadding="0" cellspacing="0">
					<tbody><tr>
						<% PagingUtil.createPagingData(request, "sbooks", "type"); %>
						<c:if test="${totalPage > 1}">
							<c:choose>
								<c:when test="${cur == 1}">
									<td><a href="javascript:void(0);">上一页</a></td>
								</c:when>
								<c:otherwise>
									<td><a href="javascript:void(0)" onclick="showpage(${pre}, ${limit})">上一页</a></td>
								</c:otherwise>
							</c:choose>
							<c:forEach begin="1" end="${totalPage}" varStatus="v">
								<c:choose>
									<c:when test="${v.count == cur}">
										<td><span class="pro_pag_on">${v.count}</span></td>
									</c:when>
									<c:otherwise>
										<td><a href="javascript:void(0)" onclick="showpage(${(v.count - 1) * limit}, ${limit})">${v.count}</a></td>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${cur == totalPage}">
									<td><a href="javascript:void(0);">下一页</a></td>
								</c:when>
								<c:otherwise>
									<td><a href="javascript:void(0)" onclick="showpage(${next}, ${limit})">下一页</a></td>
								</c:otherwise>
							</c:choose>
						</c:if>
						</tr></tbody>
				</table>
            </div>
        	<div class="clear"></div>
	    </div>
    
    	<div id="left">
    		<div class="left_t">
        		<h3>图书分类</h3>
           		<dl class="search_sort">
           			<c:forEach items="${sbooks.childTypes}" var="ct">
           			<dd><a href="view/search?key=<%=URLEncoder.encode(request.getParameter("key"), "utf-8") %>&type=${ct.typeId}&start=0&limit=15&sorts=3">${ct.typeName}</a></dd>
           			</c:forEach>
           		</dl>
	        </div>
		    <div id="llls">
				<div class="left_t">
					<h3>您最近浏览了</h3>
					<ul class="left_t_c" id="yhis">
					</ul>
					<div class="clear"></div>
					<script type="text/javascript">
						$('yhis').innerHTML = '<img src="/resources/images/load.gif" style="width:14;height:14">&nbsp;数据正在加载...';
	                	var req1 = createXMLHttpRequest();
	                	sendRequest2(req1, 'view/product/userhis', function () {
	                		$('yhis').innerHTML = req1.responseText;
	                	});
					</script>
				</div>
			</div>
	    	<div class="clear"></div>
		</div>
		<div class="clear"></div>
	</div>
    
    <%@ include file="/pages/share/bottom.jsp" %>
  </body>
</html>
