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
		<li class="active"><a href="${ctx}/oa/travel/">出差申请列表</a></li>
		<shiro:hasPermission name="oa:travel:edit"><li><a href="${ctx}/oa/travel/form">出差申请添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="travel" action="${ctx}/oa/travel/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>申请日期：</label>
				<input name="applyDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${travel.applyDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<%-- <li><label>开始日期：</label>
				<input name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${travel.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束日期：</label>
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${travel.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li> --%>
			<li><label>申请类型：</label>
				<form:select path="applyType" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('travel_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>申请人</th>
				<th>申请日期</th>
				<th>申请类型</th>
				<th>开始日期</th>
				<th>结束日期</th>
				<th>目的地</th>
				<th>事由</th>
				<th>实际开始时间</th>
				<th>实际结束时间</th>
				<th>创建者</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="oa:travel:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="travel">
			<tr>
				<td>
					${travel.createBy.name}
				</td>
				<td><a href="${ctx}/oa/travel/form?id=${travel.id}">
					<fmt:formatDate value="${travel.applyDate}" pattern="yyyy-MM-dd"/>
				</a></td>
				<td>
					${fns:getDictLabel(travel.applyType, 'travel_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${travel.beginDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${travel.endDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${travel.destination}
				</td>
				<td>
					${travel.reason}
				</td>
				<td>
					<fmt:formatDate value="${travel.realityStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${travel.realityEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${travel.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${travel.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${travel.remarks}
				</td>
				<shiro:hasPermission name="oa:travel:edit"><td>
    				<a href="${ctx}/oa/travel/form?id=${travel.id}">修改</a>
					<a href="${ctx}/oa/travel/delete?id=${travel.id}" onclick="return confirmx('确认要删除该出差申请吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>