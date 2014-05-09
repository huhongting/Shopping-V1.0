/*function ck(){
	var p=0;
	nm(p);
	mm(p);	
	yzm(p);
	if(p==0)$('fm1').submit();
}*/
/*function nm(p){
	var t;
	t = $('uid');
	if(V(t) == ''){
		$('uid_m').innerHTML = ('用户名不能为空！');
		if(p)p++;
		return false;
	}
	if(!V(t).check('[0-9a-zA-Z_]+')){
		$('uid_m').innerHTML = ('用户名只能由数字字母下划线组成！');
		if(p)p++;
		return false;
	}	
	$('uid_m').innerHTML = '';	
}*/
/*function mm(p){
	var t = $('pwd');
	if(V(t) == ''){
		$('pwd_m').innerHTML = ('密码不能为空！');
		if(p)p++;
		return false;
	}
	$('pwd_m').innerHTML = '';
}*/
/*function yzm(p){
	var t = $('yzm2')
	var y = V(t);
	if( y == ''){
		$('yzm2_m').innerHTML = ('验证码不能为空！');
		if(p)p++;
		return false;
	}
	if( !y.check('^[0-9]{4}$')){
		$('yzm2_m').innerHTML = ('验证码错！');
		if(p)p++;
		return false;
	}	
	$('yzm2_m').innerHTML = '';
}*/

function nm2(p){
	var nm,t;
	nm = 'uid2';
	t = $(nm);
	if( V(t)== ''){
		msg(nm,'用户名不能为空！');
		if(p)p++;
		return false;
	}
	if(!V(t).check('^[0-9a-zA-Z_]{1,20}$')){
		$('uid2_m').innerHTML = ('用户名只能由数字字母下划线组成！');
		if(p)p++;
		return false;
	}	
	return true;
}
function mm2(p){
	var nm = 'pwd2';
	var t = $(nm);
	if( V(t)== ''){
		msg(nm,'密码不能为空！');
		if(p)p++;
		return false;
	}else{$(nm+'_m').innerHTML = r;}
}
function mm22(p){
	var nm = 'pwd22';
	var t = $(nm);
	if( V(t)!=''){
	if(V(t)!= $V('pwd2')){
		msg(nm,'两次密码不一样！');
		if(p)p++;
		return false;
	}else{$(nm+'_m').innerHTML = r;}
	}
}
function cemail(p){
	var nm = 'email';
	var t = $(nm);
	var em = V(t);
	if(em == ''){
		msg(nm,'EMAIL不能为空！');
		if(p)p++;
		return false;
	}
	if(!isMail(em)){
		msg(nm,'EMAIL格式错误！');
		if(p)p++;
		return false;
	}
	return true;
}
function yzm2(p){
	var nm = 'yzm';
	var t = $(nm);
	var y = V(t);
	if( y == ''){
		msg(nm,'验证码不能为空！');
		if(p)p++;
		return false;
	}else{$(nm+'_m').innerHTML = r;}
	if( y.length!=4){
		msg(nm,'验证码错！');
		if(p)p++;
		return false;
	}else{$(nm+'_m').innerHTML = r;}	
	var x = parseInt(y);
	if (x.toString()!=y.toString()){
		msg(nm,'非数字！');
		if(p)p++;
		return false;
	}else{$(nm+'_m').innerHTML = r;}
}
/*function ck1(){
	var p=0;
	nm2(p);
	mm2(p);
	mm22(p);
	cemail(p);
	yzm2(p);
	if(p==0)$('fm2').submit();
}*/

/*function adde(object,e,fun){
	if(window.addEventListener){ // Mozilla, Netscape, Firefox
		alert(fun);
		object.addEventListener(e, fun, false);
	} else { // IE
		object.attachEvent(e, fun);
	}
}*/

var XMLHttpReq = false;
function createXMLHttpRequest() {
	if(window.XMLHttpRequest) {
		XMLHttpReq = new XMLHttpRequest();
	} else if(window.ActiveXObject) {
		try {
			XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				// not support ajax
			}
		}
	}
	return XMLHttpReq;
}
function sendRequest(url, callBack) {
	createXMLHttpRequest();
	XMLHttpReq.open("POST", url, true);
	/*if(callBack != null) */
	XMLHttpReq.onreadystatechange = callBack;
	XMLHttpReq.send(null);
}
function sendRequest2(req, url, callBack) {
	req.open("POST", url, true);
	if(callBack != null) req.onreadystatechange = callBack;
	req.send(null);
}

function chkusername(){
	var id = "uid2";
	var t = $(id).value.Trim();
	var n = $('old'+id);
	if(n.value.Trim()!=t && t.len()>0 && t.len()<=20){
		$(id+'_m').innerHTML = l;
		n.value=t;
		sendRequest('view/register/check?cmd=name&v='+t, getname);
	}
}

function chkemail(){
	var id = "email";
	var n = $('old'+id);
	var t = $(id).value.Trim();
	if(n.value.Trim()!=t){
		$(id+'_m').innerHTML = l;
		n.value=t;
		sendRequest('view/register/check?cmd=email&v='+t, getemail);
	}
}



function getname(p){
	var nm = "uid2";
	//alert(p.Trim());
	if(XMLHttpReq.readyState == 4) {
		if(XMLHttpReq.status == 200) {
			var num = XMLHttpReq.responseXML.getElementsByTagName("num")[0].firstChild.nodeValue;
			switch(num){
				case '1':
					msg(nm,'此用户名已被注册！');break;
				case '0':
					$(nm+'_m').innerHTML = r;break;
				default:msg(nm,'出错了！'+p);break;
			}
		} else {
			msg(nm,'出错了！');
		}
	} else {
		msg(nm,'出错了！');
	}
}

function getemail(p){
	var nm = "email";
	if(XMLHttpReq.readyState == 4) {
		if(XMLHttpReq.status == 200) {
			var num = XMLHttpReq.responseXML.getElementsByTagName("num")[0].firstChild.nodeValue;
			switch(num){
				case '0':
					$(nm+'_m').innerHTML = r;break;
				case '1':
					msg(nm,'此Emial已被注册！');break;
				default:msg(nm,'出错了！'+p);break;
			}
		} else {
			msg(nm,'出错了！');
		}
	} else {
		msg(nm,'出错了！');
	}
}

function chcls(o){
	 if(o.className == "normal") {
	 	o.className = "onhover";
	 } else {
	 	o.className = "normal";
	 }
 }

 function yzm22(p){
	var nm = 'yzm';
	var t = $(nm);
	var y = V(t);
	var x = parseInt(y);
	if( y == ''){
		msg(nm,'验证码不能为空！');
		if(p)p++;
		return false;
	}else if( y.length!=4){
		msg(nm,'验证码错！');
		if(p)p++;
		return false;
	}else if (x.toString()!=y.toString()){
		msg(nm,'非数字！');
		if(p)p++;
		return false;
	}else{ 
		var tt = $(nm).value.trim();
		sendRequest('view/register/check?cmd=valicode&v='+tt,getyzm2); 
	}
}
function getyzm2(p){
	var nm = "yzm";
   //$(nm+'_m').innerHTML = p; 
	if(XMLHttpReq.readyState == 4) {
		if(XMLHttpReq.status == 200) {
			var num = XMLHttpReq.responseXML.getElementsByTagName("num")[0].firstChild.nodeValue;
			switch(num){
				case '0':
					$(nm+'_m').innerHTML = r;break;
				case '1':
					msg(nm,'验证码错误！');break;
				default:msg(nm,'出错了！'+p);break;
			}
		} else {
			msg(nm,'出错了！');
		}
	} else {
		msg(nm,'出错了！');
	}
}
