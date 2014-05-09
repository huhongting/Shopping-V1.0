var shopcarname = "car.ishare.";
function putCar2(bid, num) {
	/*var cn = shopcarname + bid;
	var value;
	
	if(getCookie(cn) == null) { 
		value = createValue(bid, num);
		setCookie(cn, value);
	} else {
		value = createValue(bid, getNum(cn)+1);
		setCookie(cn, value);
	}
	updateCar();
	*/
	var params = 'bid=' + bid + '&num=' + num;
	var req = createXMLHttpRequest();
	sendRequest2(req, 'view/shopping/cart/add?' + params, function() {
		updateCar();
	});
}
function putCar(bid, nid) {
	var num = document.getElementById(nid).value;// book number element id
	/*var cn = shopcarname + bid;
	var value;
	value = createValue(bid, num);
	setCookie(cn, value);
	updateCar();*/
	var params = 'bid=' + bid + '&num=' + num;
	var req = createXMLHttpRequest();
	sendRequest2(req, 'view/shopping/cart/add?' + params, function() {
		updateCar();	
	});
}

function updateCartNum(bid, nid) {
	var num = document.getElementById(nid).value;
	var params = 'bid=' + bid + '&num=' + num;
	var req = createXMLHttpRequest();
	sendRequest2(req, 'view/shopping/cart/update?' + params, function() {
		updateCar();	
	});
}

function updateCar() {
	/*var car = $('shopcount');
	if(car != null) {
		var n = 0;
		var arr = document.cookie.split("; ");
		for(var i=0; i<arr.length; i++) {  
			var temp = arr[i].split("=");
			if(temp[0].indexOf(shopcarname) > -1) {
				n  = n + getNum(temp[0]);
			}
		}
		car.innerHTML = n;
	}*/
	var req = createXMLHttpRequest();
	sendRequest2(req, 'view/load/cart', function() {
		if(req.readyState == 4) {
			if(req.status == 200) {
        		var num = req.responseXML.getElementsByTagName("num")[0].firstChild.nodeValue;
        		$('shopcount').innerHTML = num;
			}
		}
	});
}

function setCookie(name, value) {
	var days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + days*24*60*60*1000);
	document.cookie = name + "=" + escape(value) + ";path=/;expires=" + exp.toGMTString();
}
function setCookie2(name, value) {
	document.cookie = name + "=" + escape(value) + ";path=/;";
}

function getCookie(cn) {
	var arr = document.cookie.split('; ');
	for(var i=0; i<arr.length; i++) {
		//alert(arr[i].split('=')[0]);
		if(arr[i].split('=')[0] == cn) return unescape(arr[i].split('=')[1]);
	}
	return null;
}
function getPreHis() {
	var arr = document.cookie.split('; ');
	for(var i=0; i<arr.length; i++) {
		//alert(arr[i].split('=')[0]);
		if(arr[i].split('=')[0] == 'prehis.ishare') {
			delCookie('prehis.ishare');
			var v = arr[i].split('=');
			if(v.length == 2) return v[1];
			else {
				var url = v[1];
				for(var i=2; i<v.length; i++) {
					url += '=' + v[i];
				}
				return url;
			}
		}
	}
	return null;
}

function isCarNull() {
	var arr = document.cookie.split('; ');
	for(var i=0; i<arr.length; i++) {
		if(arr[i].split('=')[0].indexOf(shopcarname) > -1) 
			return false;
	}
	return true;
}

function delCookie(name) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval = getCookie(name);
	if(cval != null) {
		document.cookie = name + "=" + cval + ";path=/;expires=" + exp.toGMTString();
		return 'delete succeed!';
	}
	return 'not found cookie!';
}

function getNum(name) {
	var c = getCookie(name);
	if(c == null) return 0;
	else {
		return parseInt(c.split('-')[1]);
	}
}

function createValue(param1, param2) {
	return param1 + "-" + param2;
}

var hisName = "ishare.his.";
function addHistory(name, id) {
	var value = createValue(name, id);
	var cn = hisName + id;
	setCookie(cn, value);
}

/*function loadHistory(id) {
	var p = document.getElementById(id);
	var cookies = document.cookie.split('; ');
	var f = false;
	for(var i=0; i<cookies.length; i++) {
		var v = cookies[i].split('=');
		if(v[0].indexOf(hisName) > -1) {
			f = true;
			document.write('<li>¡¤<a href=" ">' + unescape(v[1]).split('-')[0] + '</a></li>');
		}
	}
	if(f) {
		document.write('<li class="browncenter" style="clear:both"><a class="btn" href="javascript:cleanHistory();" target="_parent">Çå³ý</a></li>');
	}
}*/

function cleanHistory() {
	var cookies = document.cookie.split('; ');
	for(var i=0; i<cookies.length; i++) {
		var v = cookies[i].split('=');
		if(v[0].indexOf(hisName) > -1) {
			delCookie(v[0]);
		}
	}
	document.location.reload();
}

function valueUp(i) {
	var node = document.getElementById('booknum');
	var num = node.value;
	num = parseInt(num) + parseInt(i);
	if(num < 1) num = 1;
	node.value = num;
}
