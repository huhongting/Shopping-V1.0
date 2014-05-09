domain=document.domain.split('.').slice(1).join('.');
cache = {};

function $(p,o){if(!o)o=document;return o.getElementById(p);}
function $T(p,o){if(!o)o=document;return o.getElementsByTagName(p);}
function $N(p,o){if(!o)o=document;return o.getElementsByName(p);}

function $C(n,o){
    if(!o)o=document;
    var el = [];var  _el = $T('*',o)||o.all;
    for (var i=0; i<_el.length; i++ )if (_el[i].className == n )el[el.length] = _el[i];
    return el;
}

function $I(id){return $(id).innerHTML;}
function V(p){return p.value;}
function $V(p){if(!$(p))return '';return $(p).value;}
function Y(p){return p.getFullYear();}
function M(p){return p.getMonth();}
function D(p){return p.getDate();}

function getA(o,a){return eval('o.'+a)||o.getAttribute(a);}
function setA(o,a,v){return eval('o.'+a+'=v')||o.setAttribute(a,v);}
var geta=getA,seta=setA;

function RECheck(pattern, str){var r = new RegExp(pattern, "g");return r.test(str);}
function recheck(pattern, str){return RECheck(pattern, str);}

function isMail(str){var p = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@([a-z0-9-]+\\.)+[a-z0-9-]+$" ;return RECheck(p, str.toLowerCase());}
var ismail=isMail;
function isurl(str){var p = "^[a-zA-z]+://(\w+(-\w+)*)(\.(\w+(-\w+)*))*(\?\S*)?$";return RECheck(p, str.toLowerCase());}

String.prototype.Trim=function(){return this.replace(/(^\s*)|(\s*$)/g, "");};
String.prototype.trim=String.prototype.Trim;
String.prototype.ltrim = function(){return this.replace(/(^\s*)/g, "");};
String.prototype.rtrim = function(){return this.replace(/(\s*$)/g, "");};
String.prototype.check = function(pattern){var r = new RegExp(pattern, "g");return r.test(this);};
String.prototype.left=function(n){return   this.substr(0,n);};
String.prototype.right=function(n){return   this.substr(this.length-n);}; 
String.prototype.mid = function(starnum,endnum){return this.substr(starnum,endnum);};

String.prototype.div = function(num){
var j = 0,i = 0,a = new Array();
for(;j<this.length;j++){
if(a[i] == null) a[i] = '';
a[i] += this.charAt(j);
if(a[i].len()>= num) i++;
}
return a;
};

String.prototype.decode=function(){
var strArr=this.split("a");;
var strRtn="";
for (var i=strArr.length-1;i>=0;i--)
strRtn+=String.fromCharCode(eval(strArr[i]));
return strRtn;
};

String.prototype.len = function(){return this.length + this.replace(/[\x00-\xff]/g,'').length;};
String.prototype.isch = function(){return  this.check('^[\u4e00-\u9fa5\uf900-\ufa2d]+$');};
String.prototype.toDate = function(){    
var converted = Date.parse(this);   
var myDate = new Date(converted);   
if (isNaN(myDate)){    
var arys= this.split('-');   
myDate = new Date(arys[0],--arys[1],arys[2]);   
}
return myDate;   
};

Date.prototype.dateDiff = function(strInterval, dtEnd){    
var dtStart = this;   
if (typeof dtEnd == 'string' ){ //如果是字符串转换为日期型    
dtEnd = dtEnd.toDate(); 
}   
switch (strInterval) {    
case 's' :return parseInt((dtEnd - dtStart) / 1000); 
case 'n' :return parseInt((dtEnd - dtStart) / (60*1000));   
case 'h' :return parseInt((dtEnd - dtStart) / (60*60*1000));   
case 'd' :return parseInt((dtEnd - dtStart) / (24*60*60*1000));   
case 'w' :return parseInt((dtEnd - dtStart) / (86400000 * 7));   
case 'm' :return (dtEnd.getMonth()+1)+((dtEnd.getFullYear()-dtStart.getFullYear())*12)-(dtStart.getMonth()+1);   
case 'y' :return dtEnd.getFullYear()-dtStart.getFullYear();   
}
};

String.prototype.query = function(){
var a = null;
if(this.indexOf("?")!=-1)
a = this.substr(this.indexOf("?") + 1).toLowerCase().split("&"); 
else
{
if(this.indexOf(".jsp/")!=-1)
a = this.split('.jsp/')[1].toLowerCase().split("/"); 
else
if(this.indexOf(".jsp/")!=-1)
a = this.split('.jsp/')[1].toLowerCase().split("/"); 
else
if(this.indexOf("&")!=-1)
a = this.substr(this.indexOf("&") + 1).toLowerCase().split("&"); 

}

var p = {};
if(a)
{
for(var i = 0; i < a.length; i++) { 
var b = a[i].split("="); 
p[b[0]]=b[1];
} 
}
return p;
};

Number.prototype.rd = function(b){return( Math.round(this * Math.pow (10 , b)) / Math.pow(10 , b));};

function Round(a_Num , a_Bit){return( Math.round(a_Num * Math.pow (10 , a_Bit)) / Math.pow(10 , a_Bit));}


//Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
var rest = this.slice((to || from) + 1 || this.length);
this.length = from < 0 ? this.length + from : from;
return this.push.apply(this, rest);
};

function keydown(fun,c1,c2,c3){
var ev = SearchEvent();
if(ev!=null){
if(ev.keyCode==13){
fun(c1,c2,c3);
return true;                                   
}
}
else
alert('NULL');
return false; 
}

function SearchEvent(){ 
if(document.all)
return window.event;
var func=SearchEvent.caller; 
while(func!=null){ //alert(func);
var arg0=func.arguments[0]; 
if(arg0) { //alert(arg0.constructor);
if(arg0.constructor==Event||arg0.constructor==MouseEvent||arg0.constructor==KeyboardEvent)
return arg0; 
} 
func=func.caller; 
} 
return null; 
} 

SearchEvent=function(){
if(document.all)return window.event; 
var e;
var c=arguments.callee.caller;
try{
while(c){
e=c.arguments[0];
if(e instanceof Event)return e;
c=c.caller;           
}
}catch(x){
}
return null;
};


var gete=SearchEvent;

function echo(p){document.write(p);	}

window.nav = function(){
var t=b_t();
if(navigator.userAgent.toUpperCase().indexOf('MSIE 7')!=-1) t='ie7';
return t;
};


function Request(strName) { var p = window.document.location.href.query()[strName]; return p==null?'':p; }


String.prototype.include = function(s){
var p=false;
for(var i=0;i<s.length;i++){
if(this.indexOf(s[i])!=-1){p=true;break;}
}
return p;
};
//dhtml
if(typeof(HTMLElement)!="undefined"){   
HTMLElement.prototype.contains   =   function(obj){   
while(obj!=null   &&   typeof(obj.tagName)!="undefind"){
if(obj==this)   
return   true;   
obj=obj.parentNode;
}   
return   false;   
};   
} 

//Event

if(window.addEventListener){  
FixPrototypeForGecko();  
}  
function   FixPrototypeForGecko(){  

HTMLElement.prototype.__defineGetter__("runtimeStyle",function(){return   this.style;  } );  
window.constructor.prototype.__defineGetter__("event",gete);  
Event.prototype.__defineGetter__("srcElement",function(){  return   this.target;  }  );  
Event.prototype.__defineGetter__("fromElement",function(){   
var node = null;   
if   (this.type   ==   "mouseover")   
node   =   this.relatedTarget;   
else   if   (this.type   ==   "mouseout")   
node   =   this.target;   
if   (!node)   return;   
while   (node.nodeType   !=   1)   node   =   node.parentNode;   
return   node;   
});

Event.prototype.__defineGetter__("toElement",function(){   
var   node = null;   
if   (this.type   ==   "mouseout")   
node   =   this.relatedTarget;   
else   if   (this.type   ==   "mouseover")   
node   =   this.target;   
if   (!node)   return;   
while   (node.nodeType   !=   1)   node   =   node.parentNode;   
return   node;   
});

} 

function startrnd(n,pre,shownum){
var p = Math.round(Math.random()*1024)%n;
for(var i=0;i<shownum;i++){
$(pre+p).style.display='';
p++;
p %= n;
}
}
function rndarray(list){
var p = Math.round(Math.random()*1024)%list.length;
var a=[];
for(var i=0;i<list.length;i++){
a.push(list[p]);
p++;
p %= list.length;
}
return a;
}


 



function makeRequest(url) {
var ajax = false;
ajax = ajaxobj();
ajax.open('GET', url, false);//同步
ajax.setRequestHeader("Cache-Control","no-cache");
ajax.send(null);
return ajax.responseText;
}

var makerequest=makeRequest;

function getajax(url,id,method,tm) {
var ajax = null,m_timer=null;
if(typeof(tm)=="function")	m_timer=window.setTimeout(tm,60000);
ajax = ajaxobj();


if(method=='POST'){
var u = url.split('?');
ajax.open('POST', u[0], true);
ajax.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=gb2312'); 
ajax.send(u[1]);
}
else{
ajax.open('GET', url, true);
ajax.setRequestHeader("Cache-Control","no-cache");
ajax.setRequestHeader("If-Modified-Since","0");
ajax.send(null);
}

ajax.onreadystatechange = function(){
if (ajax.readyState == 4) {
try
{
    if (ajax.status == 200)
    {
    if(typeof(id)=="string")
        $(id).innerHTML = ajax.responseText;
    else
        id(ajax.responseText);
    if(m_timer!=null) window.clearTimeout(m_timer);
    } 
    else 
    {
        return ('There was a problem with the request.');
    }
}
catch(e){try{id('0');}catch(e){}}
finally{ajax=null;}
}
};	
}

function ajaxobj(){
var ajax = false;
if (window.XMLHttpRequest) {
ajax = new XMLHttpRequest();
if (ajax.overrideMimeType) {
ajax.overrideMimeType('text/xml');
}
}
else if(window.ActiveXObject) {
var versions = ['Microsoft.XMLHTTP', 'MSXML.XMLHTTP', 'Microsoft.XMLHTTP', 'Msxml2.XMLHTTP.7.0', 'Msxml2.XMLHTTP.6.0', 'Msxml2.XMLHTTP.5.0', 'Msxml2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP'];
for(var i=0; i<versions.length; i++) {
try {
ajax = new ActiveXObject(versions[i]);
if(ajax) break;
} catch(e) {}
}
}
if (!ajax) {
alert('Giving up :( Cannot create an XMLHTTP instance');
return false;
}
return ajax;
}

function getradiov(o){
for(var i=0;i<o.length;i++)
if(o[i].checked==true) return o[i].value;
return '';
}

//form to json
function getform(fm){
var p = {},radio={};
elmt=fm.elements;
for(var C=0;C<elmt.length;C++){

if(elmt[C].type)
switch(elmt[C].type.toLowerCase()){
    case 'radio':radio[elmt[C].name]='';break;
    case 'checkbox':if(elmt[C].checked==true)p[elmt[C].name]=elmt[C].value;else p[elmt[C].name]=''; break;
    default:
    p[elmt[C].name]=elmt[C].value;break;
}
else
p[elmt[C].name]=elmt[C].value;
}
for(var i in radio){
p[i]=getradiov($N(i));
}
return p;
}

function extend(name,fn){
    if(document.all){
        var _getElementById=document.getElementById;
        document.getElementById=function(id) {
            var _elem=_getElementById(id);
            if(!_elem) return null;
            _elem[name]=fn;
            return _elem;
        };
    } else{
        HTMLElement.prototype[name]=fn;
    }
}


extend('show',function(){this.style.display='';});
extend('hide',function(){this.style.display='none';});



if(!window.top) document.write('!top');
if(!window.top.location) document.write('!top.location');
if(!window.self.location) document.write('!self.location');
