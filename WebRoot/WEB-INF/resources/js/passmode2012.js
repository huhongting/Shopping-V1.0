function chk(t){//遍历input if checked
	for(i=0;i<t.length;i++)
	if(t[i].checked) return true;
	return false;
}
function setstr(n,c,str,n1){
	var t,t1,f=0,o;
	t = $(n);
	if(t==null){t=$N(n);f=1;}
	o = $(n+'_m');
	if(eval(c)){
		if(o){o.innerHTML = str;alert(str);}
		//if(f==1)
		//t[0].focus();
		//else
		//t.focus();
		return false;
	}
	if(o)o.innerHTML = '';
	f=0;
	if(n1!=null){
		t1 = $(n1);
		if(t1==null){t1=$N(n1);f=1;}
		//if(f==1)
			//t1[0].focus();
		//else
			//t1.focus();
	}
	return true;
}
function disp(a,v){
	for(i=0;i<a.length;i++){
	if(typeof(a[i])=="string")a[i]=$(a[i]);
	if(a[i]&&(i==v))
		a[i].style.display='';
	else 
		a[i].style.display='none';
	}
}
function getsname(iname,vl,iname2){//指定值的input是否选中且其下层展开
	var p = $N(iname),a,t;
	for(i=0;i<p.length;i++)
		if(p[i].checked){
			a = p[i].value;
			break;
		}
	if(a==vl){
	if(iname2) t = $(iname2);
	else t = $(iname+'_sub');
	if(t)t.style.display='';
	return true;
	}
	else return false;
}
var r = '<img src="/resources/images/item_r.png"  />';
var w = '<img src="/resources/images/item_w.png"  />';
 
var l = '<img src="/resources/images/load.gif"/> ';
function msg(id,str){
		$(id+'_m').innerHTML = w+str;
}

 