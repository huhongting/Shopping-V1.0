<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="right_c">
	<h3>修改密码</h3>
	<div class="my_order">
       	<table border="0" cellpadding="0" cellspacing="0" class="order_ret_q mTop10 mLeft10" width="100%" style="font-size: 13px">
        	<tr>
            		<td class="t_right"><span>*</span>原密码：</td>
               	<td><input class="input_o" id="pass" name="pass" value='' type="password" /></td>
           	</tr>
           	<tr>
            		<td class="t_right"><span>*</span>新密码：</td>
               	<td><input class="input_o" id="newpass" name="newpass" type="password" /></td>
           	</tr>
           	<tr>
            		<td class="t_right"><span>*</span>确认密码：</td>
               	<td><input class="input_o" id="repass" name="repass" type="password" /></td>
           	</tr>
           	<tr>
           		<td></td>
                <td><input type="submit" value="保存" onclick="changepass()" /></td>
            </tr>
            <tr>
           		<td></td>
                <td><span id="msg"></span></td>
            </tr>
       	</table>
    </div>
</div>
