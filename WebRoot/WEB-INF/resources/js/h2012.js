/*
author:withtao@126.com
qq:329864068
Copyright 2012 All rights reserved.
*/
var f = 0;
var to;
var b ;
var co;//current_object
var oa = [null,null,null];
var ifhidemenu = true;
var where = {};
var lh = location.href.toLowerCase().split('.com')[1];
//lh = 'http://www.china-pub.com/sub/57-04';

var listsnum=9;

function $(p){
return document.getElementById(p);
}

function hideall(id){
	for(var i=0;i<id.length;i++)
	{
		if(id[i]!='')if($(id[i])!=null)
		$(id[i]).style.display='none';
	}
}
function hide(id){
	id.style.display="none"; 
}

function recoverlink(p){

	$(p.id).className = '';
}
function setlink(p){
	$(p.id).className = 'hoveron';
}


function show(o,mp,pos){
	$('inner').innerHTML = o.innerHTML;
	o = $('outer');
	o.style.display = "";

    o.style.top=(mp[0]+pos[0])+"px";
	o.style.left=(mp[1]+pos[1])+"px";
	
}


function showX(o,mp,pos){
	
	$('innerX').innerHTML = o.innerHTML;
	
	o = $('outerX');
	o.style.display = "";
    o.style.top=(mp[0]-pos[0])+"px";
	o.style.left=(mp[1]-pos[1])+"px";
}


function getPos(o){   
	var t=o.offsetTop;   
	var l=o.offsetLeft;   
	while(o=o.offsetParent){   
		t+=o.offsetTop;   
		l+=o.offsetLeft;   
	}
	return [t,l];
}


function mysrc(ev){
	if(typeof(event)!='undefined'){
		return event.srcElement;
	}
	else if(ev){
		return ev.target;
	}
}


//parentNode
function ov(){
	if(to)clearTimeout(to);
	var e = gete();
	var id = mysrc(e);
	
	while(id.tagName.toLowerCase()!='li')
	id=id.parentNode;
	
	if(id.tagName.toLowerCase()=='li')
	{	
		var t;
		var n = id.getAttribute('id')||id.id;
		//alert(n+'----'+id+id.tagName);
		t = $(n+'_data');
		if(t!=null)
		{
			f = 1;
			if(oa[f])recoverlink(oa[1]);
			co = id;
			oa[f] = id;
			show(t,getPos(id),[0,0]);//[-57,-204]
			setlink(id);
		}
	}
}


function ov2(o){
	if(to)clearTimeout(to);
	f = 2;
	oa[f] = o;
	co = o;
}

function ovX(){
	
	var e = gete();
	var id = mysrc(e);
	{	
		var t;
		var n = id.getAttribute('id')||id.id;
		
		var v = id.getAttribute('v')||id.v;
		if(v==null) v='[0,0]';
		t = $(n+'_data');
		if(t!=null)
		{
			//alert(t.innerHTML);
			f = 0;
			co = id;
			showX(t,getPos(id),eval(v));
		}
	}
}


function ot(){
	var e = gete();
	var id = mysrc(e);
	var t;
		while(id.tagName.toLowerCase()!='li')
	id=id.parentNode;

	if(id.tagName.toLowerCase()=='li')
	{
		to = setTimeout(function(){
		if(f!= 2){
		hide($('outer'));
		
		if(oa[1])recoverlink(oa[1]);
		if(ifhidemenu){hide($('outerX'));f=0}
		
		}
		},1000);
	}

}
function ot2(o){
	var e = gete();
	if(e && !o.contains(e.toElement))
	{
		hide(o);
		var toe = e.toElement;
		
		
		while(toe.tagName&&(toe.tagName.toLowerCase()!='li'))
		toe=toe.parentNode;
		
		
		
		if(toe && toe.id) {
		toe = ((toe.id.indexOf('out_')!=-1)?true:false);
		}
		else toe = false;

		
		
		if(oa[1])recoverlink(oa[1]);
		if(ifhidemenu && !toe){hide($('outerX'));f=0}
		
	}
}
function otX(o){
	var e = gete();
	if(e && !o.contains(e.toElement))
	{
		if(f==0)hide(o);
	}
}


function debug(p){
	 $('debug').innerHTML = p;
}

function init(){
	//b = (b_t()=='ie'?true:false);
	
	dft()


	//login();
	//if(navigator.userAgent.toUpperCase().indexOf('MSIE 7')!=-1) 
	//b = false;

}





function dft(){


	updateCar();
	
	var i = where[lh];
	//alert(i);
	if(i==null)i='x';

	if(isindex())
	{
	//alert('i');
	$('zhuti_data').style.display='';
	//$('caidan').style.position='absolute';
	}
	else
	{
	//$('caidan').style.marginTop='40px';
	$('zhuti').onmouseover = ovX;
	}
	/*
	for(i=0;i<9;i++)
	{
		var p = $('out_'+i)
		p.onmouseover = ov;
		p.onmouseout = ot;
	}
	*/
	
	/*$('help').onmouseover = ovX;
	$('gwc').onmouseover = ovX;
	$('mycp').onmouseover = ovX;*/
}



function isindex(){
	if(location.href.indexOf('view')!=-1 ||
		location.href.indexOf('product')!=-1) return false;
	
	/*if(lh.indexOf('/head/test')!=-1) return true;
	var s = lh.split('/');
if(s.length==2)
	if(s[1].Trim()=='#'||s[1].Trim()==''||lh.indexOf('/?')!=-1||s[1].Trim().indexOf('index')>-1||s[1].Trim().indexOf('default')>-1)
	return true;
	return false;*/
	
	return true;
}

function cxs(id, dis) {
	if(dis == '') {
		$('cxpic_1').style.display = 'none';
		$('cxpic_1').parentNode.className = 'rank_1 overflow_h';
		$('cxpic_' + id).parentNode.className = 'rank_' + id;
	} else {
		$('cxpic_1').style.display = '';
		$('cxpic_1').parentNode.className = 'rank_1';
		$('cxpic_' + id).parentNode.className = 'overflow_h rank_' + id;
	}
	$('cxpic_' + id).style.display = dis;
}


/*init();
coop();*/

