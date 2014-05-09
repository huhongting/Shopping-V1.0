<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="left">
   	<div class="my_left">
       	<h3><a href="view/user/account">我的IShare</a></h3>
           <dl>
           	<dt>订单管理</dt>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/orderlist?start=0&limit=12')">我的订单</a></dd>
               
               <dt>账户管理</dt>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/info')">账户信息</a></dd>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/resetpass')">修改密码</a></dd>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/emoney')">账户余额</a></dd>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/address')">地址管理</a></dd>
               
               <dt>我的商品</dt>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/buylist?start=0&limit=12')">已购商品</a></dd>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/mycommentlist?start=0&limit=12')">我的评论</a></dd>
               <dd><a href="javascript:void(0)" onclick="showpanel('view/user/account/waitcommentlist')">待评论商品</a></dd>

           </dl>
		</div>
   	</div>
   	<script type="text/javascript">
       		function showpanel(url) {
               	var req = createXMLHttpRequest();
               	sendRequest2(req, url, function () {
               		if(req.readyState == 4) {
              			if(req.status == 200) {
              				if(req.responseText.indexOf('IShare网上商店-用户登入') > -1) {
              					window.location.href = 'view/login';
              				} else 
			               		$('right').innerHTML = req.responseText;
              			}/** else {
              				window.location.href = 'view/login';
              			}**/
               		}
               	});        			
       		}
        </script>
        <script type="text/javascript">
	       	function submitForm(formId, tipId) {
	       		var req = createXMLHttpRequest();
	       		
	       		var e = $(formId);
	       		var tip = $(tipId);
	       		var url = e.action;
	       		var inputs = e.elements;
	       		var postData = "";
	       		for(var i=0; i<inputs.length; i++) {
	       			switch(inputs[i].type) {
	       				case "text":
	       					postData += inputs[i].name + "=" + inputs[i].value + "&";
	       				break;
	       				case "password":
	       					postData += inputs[i].name + "=" + inputs[i].value + "&";
	       				break;
	       				default:
	       					continue;
	       			}
	       		}
	       		postData += 'note=' + $('note').value;
	       		req.open("POST", url, true);
	       		req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
	       		req.onreadystatechange = function() {
	       			if(req.readyState == 4 && req.status == 200) {
	       				tip.innerHTML = req.responseText;
	       			}
	       		};
	       		req.send(postData);
	       	}
       	</script>
       	<script type="text/javascript">
       		function changepass() {
       			var pass = $('pass').value;
       			var newpass = $('newpass').value.trim();
       			var repass = $('repass').value.trim();
       			if(pass == '' || newpass == '' || newpass != repass) {
       				alert('密码输入错误！');
       				return;
       			}
       			
       			sendRequest('view/user/account/resetpass?cmd=reset&pass='+pass+'&newpass='+newpass,
       					function() {
	         				if(XMLHttpReq.readyState == 4) {
	              			if(XMLHttpReq.status == 200) {
	              				var success = XMLHttpReq.responseXML.getElementsByTagName("success")[0].firstChild.nodeValue;
	              				var msg = XMLHttpReq.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue;
	              				if(success == 'true') {
	               				$('msg').innerHTML = '<font color=green>' + msg + '</font>';
	              				} else {
	               				$('msg').innerHTML = '<font color=red>' + msg + '</font>';
	              				}
	              				
	              				$('pass').value = '';
	              				$('newpass').value = '';
	              				$('repass').value = '';
	              			}
	              		}
       			});
       		}
       	</script>
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
					showpanel('view/user/account/address');
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
					//window.location.reload();
					showpanel('view/user/account/address');
                });
			}
			
			
			function showpinglun(oid, bid){     
				var popUp = $("popupcontent");      
				popUp.style.top = "250px";     
				popUp.style.left = "500px";     
				popUp.style.width = "400px";     
				popUp.style.height = "200px";      
				popUp.innerHTML = "<div style='text-align:center'>" + 
					"<div style='text-align:left;border-bottom:1px solid #333;'><b>发表评论</b></div>" + 
					"<div style='text-align:left;padding:10 10 10 20'><form><input type=hidden id=oid value="+oid+"><input type=hidden id=bid value="+bid+">评论标题：<br><input type='text' id='pltitle' style='width:350'><br>评论内容：<br><textarea id='plcontent' style='width:350;height:60;resize:none'></textarea></form></div>" + 
					"<div id='statusbar' ><button onclick='savepl()'>确定</button><button onclick='hidepinglun();'>关闭</button></div></div>";      
				//var sbar = $("statusbar");     
				//sbar.style.marginTop = (parseInt(popUp.style.height)-50) + "px";     
				popUp.style.visibility = "visible";
			};
			function hidepinglun(){
				var popUp = $("popupcontent");
				popUp.style.visibility = "hidden";
			}
			function savepl() {
				var plt = $('pltitle').value.trim();
				var plc = $('plcontent').value.trim();
				var oid = $('oid').value;
				var bid = $('bid').value;
				if(plt == '' || plc == '') {
					alert('请填写内容后再提交！');
				} else {
					var req = createXMLHttpRequest();
					sendRequest2(req, 'view/user/account/comment/add?' + 
							'&oid=' + oid + 
							'&bid=' + bid + 
							'&title=' + plt + 
							'&content=' + plc, function() {
							if(req.readyState == 4) {
	              				if(req.status == 200) {
	              					var success = req.responseXML.getElementsByTagName("success")[0].firstChild.nodeValue;
									if(success == 'true') {
										$('pltitle').value = '';
										$('plcontent').value = '';
										hidepinglun();
										showpanel('view/user/account/waitcommentlist');
									} else {
										alert('发表评论失败！');
									}
	              				}
							}
					});
				}
			}
		</script>