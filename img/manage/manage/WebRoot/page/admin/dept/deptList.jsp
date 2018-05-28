<%@page import="com.hxy.core.dept.entity.Dept"%>
<%@page import="com.hxy.core.dept.service.DeptService"%>
<%@page import="com.hxy.system.SpringManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/authority" prefix="authority"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
	        + path + "/";
	DeptService deptService = SpringManager.getBean("deptService", DeptService.class);
	List<Dept> list = deptService.getSchool();
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title></title>
<jsp:include page="../../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var deptTree;
	$(function() {
		deptTree = $('#deptTree')
				.tree(
						{
							url : '${pageContext.request.contextPath}/dept/deptAction!getNewDeptTree.action',
							animate : true,
							onContextMenu : function(e, node) {
								e.preventDefault();
								$(this).tree('select', node.target);
								if($('#deptTree').tree('getSelected').attributes.type=="class"){
								$('#ss').menu('show', {
									left : e.pageX,
									top : e.pageY
								});
                                 }
							},
							onBeforeLoad : function(node, param) {
								parent.$.messager.progress({
									text : '数据加载中....'
								});
							},
							onLoadSuccess : function(node, data) {
								parent.$.messager.progress('close')
							}
						});
	});
	function addRoot() {
		var dialog = parent
				.modalDialog({
					title : '新增学校',
					iconCls : 'ext-icon-note_add',
					url : '${pageContext.request.contextPath}/page/admin/dept/addDept.jsp?id=' + 0,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}

	function editDeptName() {
		var id = $('#deptTree').tree('getSelected').id;
		var parentId = $('#deptTree').tree('getSelected').pid;
		if (id.length == 6) {
			title = '编辑学校';
		}
		if (id.length == 10) {
			title = '编辑院系';
		}
		if (id.length == 16) {
			title = '编辑班级';
		}
		var dialog = parent
				.modalDialog({
					title : title,
					iconCls : 'ext-icon-note_edit',
					url : '${pageContext.request.contextPath}/page/admin/dept/editDept.jsp?id='
							+ id + '&pid=' + parentId,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}

	function aadd() {
		var id = $('#deptTree').tree('getSelected').id;
		var level = $('#deptTree').tree('getSelected').attributes.level;
		var dialog = parent
				.modalDialog({
					title : '新增院系',
					iconCls : 'ext-icon-note_add',
					url : '${pageContext.request.contextPath}/page/admin/dept/addaDept.jsp?id='
							+ id + '&level=' + level,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}
	function addAlias() {
		var id = $('#deptTree').tree('getSelected').id;
		var level = $('#deptTree').tree('getSelected').attributes.level;
		var parentId = $('#deptTree').tree('getSelected').pid;
		var title;
		var url = "";
		if (id.length == 6) {
			title = '新增学校曾用名';
			url = '${pageContext.request.contextPath}/page/admin/dept/addDeptAlias.jsp?id='
					+ id + '&level=' + level + '&pid=' + parentId;
		}
		if (id.length == 10) {
			title = '新增院系曾用名';
			url = '${pageContext.request.contextPath}/page/admin/dept/addDepartAlias.jsp?id='
					+ id + '&level=' + level + '&pid=' + parentId;
		}
		var dialog = parent.modalDialog({
			title : title,
			iconCls : 'ext-icon-note_add',
			url : url,
			buttons : [ {
				text : '保存',
				iconCls : 'ext-icon-save',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(
							dialog, deptTree, parent.$);
				}
			} ]
		});
	}

	function removeDept() {
		var id = $('#deptTree').tree('getSelected').id;
		parent.$.messager
				.confirm(
						'确认',
						'您确定要删除此记录？',
						function(r) {
							if (r) {
								$
										.ajax({
											url : '${pageContext.request.contextPath}/dept/deptAction!delete.action',
											data : {
												deptId : id
											},
											dataType : 'json',
											success : function(result) {
												if (result.success) {
													if (id.length == 6) {
														refrensh();
													} else {
														$('#deptTree').tree(
																'reload');
													}
													parent.$.messager.alert(
															'提示', result.msg,
															'info');
												} else {
													parent.$.messager.alert(
															'提示', result.msg,
															'error');
												}
											},
											beforeSend : function() {
												parent.$.messager.progress({
													text : '数据提交中....'
												});
											},
											complete : function() {
												parent.$.messager
														.progress('close');
											}
										});
							}
						});
	}

	function importFun() {
		var dialog = parent
				.modalDialog({
					title : '导入院系',
					iconCls : 'ext-icon-import_customer',
					url : '${pageContext.request.contextPath}/page/admin/dept/importFun.jsp',
					buttons : [ {
						text : '确定',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}
	function importDept() {
		var dialog = parent
				.modalDialog({
					title : '导入机构',
					iconCls : 'ext-icon-import_customer',
					url : '${pageContext.request.contextPath}/page/admin/dept/importDept.jsp',
					buttons : [ {
						text : '确定',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}
	function exportFun() {
		$
				.ajax({
					url : '${pageContext.request.contextPath}/dept/deptAction!exportData.action',
					data: '',
					dataType : 'json',
					success : function(result) {
						if (result.success) {
							if (result.msg != "") {
							    $('#exportResult')
										.html(
												"<a id='mf' href='"+result.msg+"'>导出结果下载</a>");
								parent.parent.$.messager.alert('提示',
										"导出成功,请在导出结果处下载导出结果", 'info');
							} else {
								parent.parent.$.messager.alert('提示', "无数据导出",
										'info');
							}
						} else {
							parent.parent.$.messager.alert('提示', result.msg,
									'error');
						}
					},
					beforeSend : function() {
						$('#mf').remove();
						parent.parent.$.messager.progress({
							text : '数据导出中....'
						});
					},
					complete : function() {
						parent.parent.$.messager.progress('close');
					}
				});
	}
function exportDept() {
		$
				.ajax({
					url : '${pageContext.request.contextPath}/dept/deptAction!exportDept.action',
					data: '',
					dataType : 'json',
					success : function(result) {
						if (result.success) {
							if (result.msg != "") {
							    $('#exportResult')
										.html(
												"<a id='mf' href='"+result.msg+"'>导出结果下载</a>");
								parent.parent.$.messager.alert('提示',
										"导出成功,请在导出结果处下载导出结果", 'info');
							} else {
								parent.parent.$.messager.alert('提示', "无数据导出",
										'info');
							}
						} else {
							parent.parent.$.messager.alert('提示', result.msg,
									'error');
						}
					},
					beforeSend : function() {
						$('#mf').remove();
						parent.parent.$.messager.progress({
							text : '数据导出中....'
						});
					},
					complete : function() {
						parent.parent.$.messager.progress('close');
					}
				});
	}
	function refrensh() {
		var panel = parent.$('#mainTabs').tabs('getSelected').panel('panel');
		var frame = panel.find('iframe');
		try {
			if (frame.length > 0) {
				for ( var i = 0; i < frame.length; i++) {
					frame[i].contentWindow.document.write('');
					frame[i].contentWindow.close();
					frame[i].src = frame[i].src;
				}
				if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
					try {
						CollectGarbage();
					} catch (e) {
					}
				}
			}
		} catch (e) {
		}
	}

	function addDept() {
		var id = $('#deptTree').tree('getSelected').id;
		var dialog = parent
				.modalDialog({
					title : '增加组织',
					iconCls : 'ext-icon-cog',
					url : '${pageContext.request.contextPath}/page/admin/dept/addDept.jsp?id='
							+ id,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}
	function setBelong() {
		var id = $('#deptTree').tree('getSelected').id;
		var dialog = parent
				.modalDialog({
					title : '设置归属',
					iconCls : 'ext-icon-cog',
					url : '${pageContext.request.contextPath}/page/admin/dept/belongDept.jsp?id='
							+ id,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, deptTree, parent.$);
						}
					} ]
				});
	}

	function myDept() {
		var id = $('#deptTree').tree('getSelected').id;
		var dialog = parent
				.modalDialog({
					title : '查看属于我的机构',
					iconCls : 'ext-icon-note',
					url : '${pageContext.request.contextPath}/page/admin/dept/myDept.jsp?id='
							+ id
				});
	}
</script>
</head>

<body class="easyui-layout" data-options="border:false">
	<div data-options="region:'west',border:false" style="width: 100px;">
		<%
			if (list == null || list.size() == 0) {
		%>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="新增机构">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'ext-icon-note_add',plain:true"
				onclick="addRoot();" style="margin-left: 5px; margin-top: 1px;">新增学校</a>
		</authority:authority>
		<br />
		<%
			}
		%>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="院系导入">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'ext-icon-import_customer',plain:true"
				onclick="importFun();" style="margin-left: 5px; margin-top: 1px;">院系导入</a>
		</authority:authority>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="机构导入">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'ext-icon-import_customer',plain:true"
				onclick="importDept();" style="margin-left: 5px; margin-top: 1px;">机构导入</a>
		</authority:authority>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="院系导出">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'ext-icon-import_customer',plain:true"
				onclick="exportFun();" style="margin-left: 5px; margin-top: 1px;">院系导出</a>
		</authority:authority>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="机构导出">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				data-options="iconCls:'ext-icon-import_customer',plain:true"
				onclick="exportDept();" style="margin-left: 5px; margin-top: 1px;">机构导出</a>
		</authority:authority>
	    <authority:authority role="${sessionScope.user.role}"
			authorizationCode="结果导出"  >
		<span id="exportResult"  style="margin-left: 5px; margin-top: 5px;"></span>
		</authority:authority>

	</div>
	<div data-options="region:'center',border:false">
		<ul id="deptTree"></ul>
	</div>
	<div id="ss" class="easyui-menu" style="width: 150px;">
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="新增机构">
			<div onclick="addDept()" data-options="iconCls:'ext-icon-note_add'">
				新增组织</div>
		</authority:authority>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="设置机构">
			<div onclick="setBelong()" data-options="iconCls:'ext-icon-cog'">
				设置归属</div>
		</authority:authority>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="查看机构">
			<div onclick="myDept()" data-options="iconCls:'ext-icon-note'">
				查看组织</div>
		</authority:authority>
		<authority:authority role="${sessionScope.user.role}"
			authorizationCode="删除机构">
			<div onclick="removeDept()"
				data-options="iconCls:'ext-icon-note_delete'">删除</div>
		</authority:authority>
	</div>
</body>
</html>
