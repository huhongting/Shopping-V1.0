<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<div class="right_c">
	<h3>账户信息</h3>
	<div class="my_order">
	<form id="uform" action="view/user/account/info?cmd=save" method="post">
       	<table border="0" cellpadding="0" cellspacing="0" class="order_ret_q mTop10 mLeft10" width="100%" style="font-size: 13px">
        	<tr>
            	<td class="t_right"><span>*</span>邮箱地址：</td>
               	<td><input class="input_o"  name="email" type="text" value="${currentUser.email}" readonly="readonly"/></td>
           	</tr>
           	<tr>
            	<td class="t_right">手机号码：</td>
            	<td><input class="input_o" name="mobile" type="text" value="${currentUser.contactInfo.mobile}"/></td>
            		<td class="t_right">座机号码：</td>
               	<td><input class="input_o"  name="phone" type="text" value="${currentUser.contactInfo.phone}"/></td>
           	</tr>
           	<tr>
            		<td class="t_right">地址：</td>
               	<td width="200"><input class="input_o"  name="address" type="text" value="${currentUser.contactInfo.address}"/></td>
            	<td class="t_right">邮编：</td>
            	<td><input class="input_o" name="postcode" type="text" value="${currentUser.contactInfo.postalcode}"/></td>
           	</tr>
            <tr>
            		<td class="t_right">注册时间：</td>
               	<td><input class="input_o"  name="regtime" type="text" value="<fmt:formatDate value='${currentUser.regTime}' type='both' />" readonly="readonly"/></td>
            	<td class="t_right">上次登入：</td>
            	<td><input class="input_o" name="lastlogin" type="text" value="<fmt:formatDate value='${user.lastLogin}' type='both' />" readonly="readonly"/></td>
           	</tr>
       	</table>
       	<table border="0" cellpadding="0" cellspacing="0" class="order_ret_q mTop10 mLeft10" width="100%" style="font-size: 13px">
        	<tr>
            	<td class="t_right" style="width:65">其他说明：</td>
               	<td class="book_type"><textarea name="note" id="note" rows=2 cols="60" style="resize:none;">${currentUser.note}</textarea></td>
           	</tr>
           	<tr>
           		<td></td>
                <td><input type="button" onclick="submitForm('uform', 'right')" value="保存" /></td>
            </tr>
       	</table>
       	</form>
    </div>
</div>
