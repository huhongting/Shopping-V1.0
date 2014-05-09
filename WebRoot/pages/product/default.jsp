<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上二手教材店-网上买教材的网站</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  
  </head>
  
  <body><a href="javascript:void(0);" id="top"> </a>
	<%@ include file="/pages/share/menu.jsp" %>

	<!-- main start -->
	<div id="main" class="w1002 mCenter">
		<div class="go_back">
			<a href="javascript:void(0);" onclick="$('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
		</div>
    	<p>&nbsp;</p>
    	
		<div id="ifocus2">
		    <!-- 图片展示 -->
		    <script type="text/javascript" src="/resources/images/show/SlideTrans.js"></script>
		    <style type="text/css">
				.container, .container img{width:772px; height:400px;}
				.container img{border:0;vertical-align:top;}
				.container ul, .container li{list-style:none;margin:0;padding:0;}
				.num{ position:absolute; right:5px; bottom:5px; font:12px/1.5 tahoma, arial; height:18px;}
				.num li{
					float: left;
					color: #d94b01;
					text-align: center;
					line-height: 16px;
					width: 16px;
					height: 16px;
					font-family: Arial;
					font-size: 11px;
					cursor: pointer;
					margin-left: 3px;
					border: 1px solid #f47500;
					background-color: #fcf2cf;
				}
				.num li.on{
					line-height: 18px;
					width: 18px;
					height: 18px;
					font-size: 14px;
					margin-top:-2px;
					background-color: #ff9415;
					font-weight: bold;
					color:#FFF;
				}
			</style>
		    <div class="container" id="idContainer2">
				<ul id="idSlider2">
					<c:forEach items="${showpics.results}" var="p">
					<li><a href="${p.action}"><img src="${p.url}" /></a></li>
					</c:forEach>
				</ul>
				<ul class="num" id="idNum">
				</ul>
			</div>
			<script>
				var nums = [], timer, n = $$("idSlider2").getElementsByTagName("li").length,
					st = new SlideTrans("idContainer2", "idSlider2", n, {
						onStart: function(){//设置按钮样式
							forEach(nums, function(o, i){ o.className = st.Index == i ? "on" : ""; });
						}
					});
				for(var i = 1; i <= n; AddNum(i++)){};
				function AddNum(i){
					var num = $$("idNum").appendChild(document.createElement("li"));
					num.innerHTML = i--;
					num.onmouseover = function(){
						timer = setTimeout(function(){ num.className = "on"; st.Auto = false; st.Run(i); }, 200);
					};
					num.onmouseout = function(){ clearTimeout(timer); num.className = ""; st.Auto = true; st.Run(); };
					nums[i] = num;
				}
				st.Run();
			</script>
		</div>
		<div class="clear"></div>
	    
		<!-- 第一版左侧 -->
	    <div class="mTop10">
	    	<div class="w762 fLeft">
	        	<div class="main_nav w762">
	        		<script type="text/javascript">
	        			function showItem(n, id) {
	        				var c = document.getElementsByName('tj');
	        				for(var i=0; i<c.length; i++)
	        					c[i].className = "";
	        				n.className = "cur";
	        				var d = document.getElementsByName('tjinfo');
	        				for(var i=0; i<d.length; i++)
	        					d[i].style.display = 'none';
	        				document.getElementById(id).parentNode.parentNode.style.display='';
	        			}
	        		</script>
	            	<div id="tab2_">
	                    <h4 name="tj" class="cur" onclick="showItem(this, 'TB1475')">新书推荐</h4>
	                    <h4 name="tj" class="" onclick="showItem(this, 'TB1476')">特价图书</h4>
	                    <h4 name="tj" class="" onclick="showItem(this, 'TB1477')">促销图书</h4>
	                    <div name="tjinfo" class="c" style="">
	                        <div id="tab2_1">
	                        	<h4></h4>
	                            <div class="c" id="TB1475">
									<div class="book">
										<ul class="book_nav">
											<c:forEach items="${newBooks}" var="book" varStatus="vs">
											<li>
												<div class="book_rank">
													<div class="book_s">
														<a target="_blank" href="view/book/show?id=${book.id}">
															<img src="${book.picUrl}" border="0" width="104">
														</a>
													</div>
													<p class="mTop10 price_height">
														<a target="_blank" href="view/book/show?id=${book.id}">${book.bookName}</a>
													</p>
													<p><span class="book_price">￥${book.normalPrice}</span><span class="book_dis">￥${book.price}</span></p>
												</div>
											</li>
											</c:forEach>
										</ul>
										<div class="clear"></div>
									</div>
								</div>
								
	                        </div>
						</div>
						<div name="tjinfo" class="c" style="display:none">
	                        <div id="tab2_2">
	                        	<h4></h4>
	                            <div class="c" id="TB1476">
									<div class="book">
										<ul class="book_nav">
											<c:forEach items="${tjBooks}" var="book" varStatus="vs">
											<li>
												<div class="book_rank">
													<div class="book_s">
														<a target="_blank" href="view/book/show?id=${book.id}">
															<img src="${book.picUrl}" border="0" width="104">
														</a>
													</div>
													<p class="mTop10 price_height">
														<a target="_blank" href="view/book/show?id=${book.id}">${book.bookName}</a>
													</p>
													<p><span class="book_price">￥${book.normalPrice}</span><span class="book_dis">￥${book.price}</span></p>
												</div>
											</li>
											</c:forEach>
										</ul>
										<div class="clear"></div>
									</div>
								</div>
								
	                        </div>
						</div>
						<div name="tjinfo" class="c" style="display:none">
	                        <div id="tab2_3">
	                        	<h4></h4>
	                            <div class="c" id="TB1477">
									<div class="book">
										<ul class="book_nav">
											<c:forEach items="${cxBooks}" var="book" varStatus="vs">
											<li>
												<div class="book_rank">
													<div class="book_s">
														<a target="_blank" href="view/book/show?id=${book.id}">
															<img src="${book.picUrl}" border="0" width="104">
														</a>
													</div>
													<p class="mTop10 price_height">
														<a target="_blank" href="view/book/show?id=${book.id}">${book.bookName}</a>
													</p>
													<p><span class="book_price">￥${book.normalPrice}</span><span class="book_dis">￥${book.price}</span></p>
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
	        </div>
	   
	        <!-- 第一版右侧 -->
	        <div class="cp_cx fRight">
	            <div id="hotnews2_content">
                    <ul id="cxb"></ul>
	                <script type="text/javascript">
	                	$('cxb').innerHTML = '正在加载...';
	                	sendRequest('view/product/changxiao?tid=0', function(){
	                		$('cxb').innerHTML = XMLHttpReq.responseText;
	                	});
	                </script>
	            </div>
	        </div>
	        <div class="clear"></div>
	    </div>
	    
	    <c:if test="${itBooks != null}">
	    <!-- 第二版 -->
	    <div class="mTop10 title_bg">
			<h2>
	            <a href="javascript:void(0);">${fbt.typeName}</a>
	        </h2>
	        
	        <div class="book">
	            <ul class="book_nav">
					<!-- 1 -->
					<c:forEach items="${itBooks}" var="book" begin="0" end="5">
	               <li>
						<div class="book_rank">
							<div class="book_s">
								<a target="_blank" href="view/book/show?id=${book.id}">
									<img src="${book.picUrl}" border="0" width="104">
								</a>
							</div>
							<p class="mTop10 price_height">
								<a target="_blank" href="view/book/show?id=${book.id}">${book.bookName}</a>
							</p>
							<p><span class="book_price">￥${book.normalPrice}</span><span class="book_dis">￥${book.price}</span></p>
						</div>
					</li>
					</c:forEach>
	            </ul>
	            <div class="clear"></div>
	        </div>
	        
	        <div class="book_word">
	        	 <ul>
	            	<c:forEach items="${itBooks}" var="b" begin="6" end="12">
	            	<li><a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</li>
	            	</c:forEach>
	             </ul>
	            <div class="clear"></div>
	        </div>
	        
	    </div>
	    </c:if>
	    
	    <c:if test="${slhBooks != null}">
	    <div class="mTop10 title_bg">
			<h2>
	            <a href="javascript:void(0);">${sbt.typeName}</a>
	        </h2>
	        
	        <div class="book">
	            <ul class="book_nav">
					<c:forEach items="${slhBooks}" var="b" begin="0" end="5">
	               <li>
						<div class="book_rank">
							<div class="book_s">
								<a target="_blank" href="view/book/show?id=${b.id}">
									<img src="${b.picUrl}" border="0" width="104">
								</a>
							</div>
							<p class="mTop10 price_height">
								<a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a>
							</p>
							<p><span class="book_price">￥${b.normalPrice}</span><span class="book_dis">￥${b.price}</span></p>
						</div>
					</li>
					</c:forEach>
	            </ul>
	            <div class="clear"></div>
	        </div>
	        
	        <div class="book_word">
	        	 <ul>
	            	<c:forEach items="${slhBooks}" var="b" begin="6" end="12">
	            	<li><a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a></li>
	            	</c:forEach>
	             </ul>
	            <div class="clear"></div>
	        </div>
	        
	    </div>
	    </c:if>
	    
	</div>
	           
	<%@ include file="/pages/share/bottom.jsp" %>

</body></html>