<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出差申请管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/travel/findTask">任务列表</a></li>
	</ul>
	<%-- <form:form id="searchForm" modelAttribute="travel" action="${ctx}/oa/travel/findTask" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>申请日期：</label>
				<input name="applyDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${travel.applyDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>开始日期：</label>
				<input name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${travel.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束日期：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${travel.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>申请类型：</label>
				<form:select path="applyType" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('travel_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form> --%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>任务名称</th>
				<th>任务办理人</th>
				<th>创建时间</th>
				<shiro:hasPermission name="oa:travel:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="task">
			<tr>
				<td><a href="${ctx}/oa/travel/form?id=${task.id}">
					${task.name}
				</a></td>
				<td>
					${task.assignee}
				</td>
				<td>
					<fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd"/>
				</td>
				<shiro:hasPermission name="oa:travel:edit"><td>
    				<a href="${ctx}/oa/travel/form?id=${travel.id}">办理任务</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>