<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="editDictForm" method="post">
	<table align="center" cellpadding="0" cellspacing="0" class="tlh-table">
		<tr>
			<td style="width:90px;" align="right">字典名称：</td>
				<td>
					<input name="dictModel.dictId"  type="hidden" value="<s:property value='dictModel.dictId'/>">
					<input name="dictModel.dictName"  class="easyui-validatebox" data-options="required:'true'" value="<s:property value='dictModel.dictName'/>"/>
				</td>
					
		</tr>
		<tr>
			<td style="width:90px;" align="right">值：</td>
				<td>
					<input name="dictModel.dictValue"  class="easyui-validatebox"  data-options="validType:'numbers',required:'true'" value="<s:property value='dictModel.dictValue'/>"/>
				</td>
					
		</tr>
	</table>
</form>
