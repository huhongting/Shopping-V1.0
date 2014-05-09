<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String basePath = path+request.getContextPath()+"/";
String extPath = path + "/ext.4.2/";
pageContext.setAttribute("extpath", extPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title>网站业务系统管理平台</title>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style type="text/css">
	p {
	    margin:5px;
	}
	.help {
	    background-image:url(${extpath}examples/shared/icons/fam/help.png);
	}
	.exit {
	    background-image:url(${extpath}examples/shared/icons/fam/exit.png);
	}
	.user {
	    background-image:url(${extpath}examples/shared/icons/fam/user_suit.png);
	}
	.cog {
	    background-image:url(${extpath}examples/shared/icons/fam/application_view_list.png);
	}
	.add {
	    background-image:url(${extpath}examples/shared/icons/fam/add.gif);
	}
	.del {
	    background-image:url(${extpath}examples/shared/icons/fam/delete.gif);
	}
	.search {
	    background-image:url(${extpath}examples/shared/icons/fam/search.png);
	}
	.edit {
	    background-image:url(${extpath}examples/shared/icons/fam/plugin.gif);
	}
	.go_into {
	    background-image:url(${extpath}examples/shared/icons/fam/go_into.png);
	}
	.go_out {
	    background-image:url(${extpath}examples/shared/icons/fam/go_out.png);
	}
	.refresh {
	    background-image:url(${extpath}examples/shared/icons/fam/reset.png);
	}
	.recom {
	    background-image:url(${extpath}examples/shared/icons/fam/recom.png);
	}
	._recom {
	    background-image:url(${extpath}examples/shared/icons/fam/_recom.png);
	}
	.advance {
	    background-image:url(${extpath}examples/shared/icons/fam/cog.png);
	}
	A:link {
		COLOR: #6CF; TEXT-DECORATION: none
	}
	A:visited {
		COLOR: #6CF; TEXT-DECORATION: none
	}
	A:hover {
		COLOR: #777; TEXT-DECORATION: none
	}
</style>
<link rel="stylesheet" type="text/css" href="${extpath}resources/css/ext-all.css">
<script type="text/javascript" src="${extpath}bootstrap.js"></script>
<script type="text/javascript" src="${extpath}locale/ext-lang-zh_CN.js"></script>

<script type="text/javascript" src="/resources/control/admin/js/app.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/MenuSortPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/BookTypeTreePanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/PressPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/DelBookPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/BookForm.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/BookPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/UserPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/DelUserPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/PicPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/PicForm.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/WaitConfirmOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/OrderForm.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/OrderItemPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/OrderMsgPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/CancelOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/WaitPaymentOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/WaitProductOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/WaitDeliverOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/DeliveredOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/ReceivedOrderPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/NewItemPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/OrderSearchPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/OrderSearchView.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/EmpSearchView.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/EmpSearchPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/EmployeePanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/EmpInfo.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/DelEmployeePanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/CommentPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/PermissionView.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/IndexPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/NotificationPanel.js" charset="gbk"></script>
<script type="text/javascript" src="/resources/control/admin/js/EmpDatePanel.js" charset="gbk"></script>
<script type="text/javascript">
Ext.Loader.setConfig({enabled:true});
Ext.Loader.setPath('Ext.ux', '../../ext.4.2/examples/ux');
Ext.require([
	'Ext.grid.*',
	'Ext.toolbar.Paging',
	'Ext.util.*',
	'Ext.data.*',
	'Ext.ux.form.SearchField'
]);
</script>

</head>
<body oncontextmenu="return false">
</body>
</html>

