<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=utf-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--	日历插件--%>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<!--分页显示的前端插件-->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>
<script type="text/javascript">
	$(function(){
		//给删除按钮添加单击时间
		$("#deleteActivityBtn").click(function (){
			//收集参数
			//获取所有别选中的checkbox
			var checkedIds=$("#tBody input[type='checkbox']:checked");
			if (checkedIds.size()==0){
				alert("请选择要删除的数据")
				return;
			}
			if (window.confirm("确定删除吗?")){
				//var ids="";
				var ids=[];
				$.each(checkedIds,function (index){
					//ids+="id="+this.value+"&";
					ids[index]=$(this).val()
				})
				//从下表为参数1的字符串截取到下标为参数2的字符串为止(左闭右开)
				//ids=ids.substring(0,ids.length-1);
				$.ajax({
					url:'workbench/activity/deleteActivityByIds',
					type:'post',
					data:{ids:ids},
					dataType:'json',
					traditional:true,
					success:function (data){
						if (data.code==1){
							queryActivityByPage(1,$("#pageDiv").bs_pagination('getOption','rowsPerPage'));
							alert(data.msg);
						}else {
							alert(data.msg);
						}
					}
				})
			}
		})
		//添加按钮和修改按钮的日历功能
		$(".myDate").datetimepicker({
			language:'zh-CN',
			format:'yyyy-mm-dd',//日期格式
			minView:'month',//最小视图 月份
			initialDate:new Date(),//初始化日历的默认选择时间
			autoclose:true,//设置完日期日历自动关闭
			todayBtn:true, //显示当天的按钮
			clearBtn:true //显示清空按钮
		});
		//给创建按钮添加单击事件
		$("#createActivityBtn").click(function () {
					//新增表单重置
					$("#createActivityForm").get(0).reset();
					//弹出模态窗口
					$("#createActivityModal").modal("show");
				});
			//给保存按钮添加单击事件
			$("#saveBtn").click(function () {
				//收集参数,将表单中的参数传递到后端
				var owner=$("#create-marketActivityOwner").val();
				var name=$.trim($("#create-marketActivityName").val());
				var startDate=$("#create-startDate").val();
				var endDate=$("#create-endDate").val();
				var cost=$.trim($("#create-cost").val());
				var description=$("#create-description").val();
				//表单验证
				if (owner==""){
					alert("所有者不能为空");
					return;
				}
				if (name==""){
					alert("名称不能为空");
					return;
				}
				if (startDate!="" && endDate!=""){
					//使用字符串的大小来替代日期的大小
					start=new Date(startDate);
					end=new Date(endDate);
					if (start>end){
						alert("开始日期必须比结束日期小");
						return;
					}
				}
				//定义非负整数的正则表达式
				var regu = /^(([1-9]\d*)|0)$/;
				if (cost!=""){
					if (!regu.test(cost)){
						alert("成本为非负整数");
						return;
					}
				}
				$.ajax({
					url:'/workbench/activity/saveCreateActivity',
					data: {
						"owner": owner,
						"name": name,
						"startdate": startDate,
						"enddate" : endDate,
						"budgetcost" : cost,
						"description": description
					},
					type: 'post',
					dataType: 'json',
					success:function (data){
						if (data.code==1){
							//关闭模态窗口
							$("#createActivityModal").modal("hide");
							//创建成功后,刷新列表,重新显示数据
							queryActivityByPage(1,$("#pageDiv").bs_pagination('getOption','rowsPerPage'));
						}else {
							alert(data.msg);
						}
					}
				})
			})
		//当加载页面时进行分页查询,当页面初始加载采用默认的参数
		queryActivityByPage(1,10);
		//给查询按钮添加单击事件
		$("#queryActivityBtn").click(function (){
			//显示第一页并且指定显示几条的数据信息
			queryActivityByPage(1,$("#pageDiv").bs_pagination('getOption','rowsPerPage'));
		});
			//给全选选择框添加单击事件
			$("#checkAll").click(function (){
				/* if (this.checked){
				 	$("#tBody input[type='checkbox]").prop("checked",true);
				 }else {
				 	$("#tBody input[type='checkbox]").prop("checked",false);
				 }*/
				$("#tBody input[type='checkbox']").prop("checked",this.checked);
			});

			//无法使用
			/*$("#tBody input[type='checkbox']").click(function () {
				//判断列表中的所有checkbox都选中,则"全选"按钮都选中
				//如果选中的大小与该checkbox的总大小一样,那么证明全部选中,设置全选按钮为true
				if ($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
					$("#checkAll").prop("checked",true);
				}else{
					//否则,全选按钮为false
					$("#checkAll").prop("checked",false);
				}
			})*/
		$("#tBody").on("click","input[type='checkbox']",function () {
				//判断列表中的所有checkbox都选中,则"全选"按钮都选中
				//如果选中的大小与该checkbox的总大小一样,那么证明全部选中,设置全选按钮为true
				if ($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
					$("#checkAll").prop("checked",true);
				}else{
					//否则,全选按钮为false
					$("#checkAll").prop("checked",false);
				}
			})

			//给修改按钮添加单击事件
			$("#editActivityBtn").click(function (){
				//获取列表中被选中的checkbox
				var checkId=$("#tBody input[type='checkbox']:checked");
				if (checkId.size()==0){
					alert("请选择要修改的市场活动");
					return;
				}
				if (checkId.size()>1){
					alert("只能选择一条数据")
					return;
				}
				var id=checkId.val();
				$.ajax({
					url:'workbench/activity/getActivityById',
					data:{
						id:id
					},
					dataType:'json',
					type:'post',
					success:function (data){
						//把市场活动的修改信息显示在模态窗口上
						$("#edit-id").val(data.data.id);
						$("#edit-ActivityOwner").val(data.data.owner);
						$("#edit-marketActivityName").val(data.data.name);
						$("#edit-startDate").val(data.data.startdate);
						$("#edit-endDate").val(data.data.enddate);
						$("#edit-cost").val(data.data.budgetcost);
						$("#edit-description").val(data.data.description);
						//弹出模态窗口
						$("#editActivityModal").modal("show");
					}
				})
			});
			//给更新按钮添加单击事件
			$("#saveEditActivityBtn").click(function (){
				//收集参数
				var id=$("#edit-id").val();
				var owner=$("#edit-ActivityOwner").val();
				var name=$.trim($("#edit-marketActivityName").val());
				var startdate=$("#edit-startDate").val();
				var enddate=$("#edit-endDate").val();
				var cost=$.trim($("#edit-cost").val());
				var description=$.trim($("#edit-description").val());
				//表单验证
				if (owner==""){
					alert("所有者不能为空");
					return;
				}
				if (name==""){
					alert("名称不能为空");
					return;
				}
				if (startdate!="" && enddate!=""){
					//使用字符串的大小来替代日期的大小
					start=new Date(startdate);
					end=new Date(enddate);
					if (start>end){
						alert("开始日期必须比结束日期小");
						return;
					}
				}
				//定义非负整数的正则表达式
				var regu = /^(([1-9]\d*)|0)$/;
				if (cost!=""){
					if (!regu.test(cost)){
						alert("成本为非负整数");
						return;
					}
				}
				$.ajax({
					url:'/workbench/activity/saveEditActivity',
					data: {
						id:id,
						owner: owner,
						name: name,
						startdate: startdate,
						enddate : enddate,
						budgetcost : cost,
						description: description
					},
					type: 'post',
					dataType: 'json',
					success:function (data){
						if (data.code==1){
							//关闭模态窗口
							$("#editActivityModal").modal("hide");
							//创建成功后,刷新列表,重新显示数据
							queryActivityByPage(1,$("#pageDiv").bs_pagination('getOption','rowsPerPage'));
						}else {
							alert(data.msg);
						}
					}
				})
			})
		});

	//封装函数,分页条件查询的函数
	function queryActivityByPage(pageNumber,pageSize){
		//收集参数
		var name=$("#query-name").val();
		var owner=$("#query-owner").val();
		var startdate=$("#query-startDate").val();
		var enddate=$("#query-endDate").val();
		//var pageNumber=1;
		//var pageSize=10;
		//发送请求
		$.ajax({
			url:'workbench/activity/getActivityByPage',
			data: {
				name:name ,
				owner:owner,
				startdate:startdate,
				enddate:enddate,
				pageNumber:pageNumber,
				pageSize:pageSize
			},
			type: 'post',
			dataType: 'json',
			success :function (data){
				//$("#totalRows").text(data.data.total);
				//遍历查询出来的数据
				var htmlStr="";
				$.each(data.data.list,function (index,object) {
					htmlStr+="<tr class=\"active\">";
					htmlStr+="<td><input type=\"checkbox\" value=\""+object.id+"\" /></td>";
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='detail.html';\">"+object.name+"</a></td>";
					htmlStr+="<td>"+object.owner+"</td>";
					htmlStr+="<td>"+object.startdate+"</td>";
					htmlStr+="<td>"+object.enddate+"</td>";
					htmlStr+="</tr>";
				});
				$("#tBody").html(htmlStr);
				//取消全选框
				$("#checkAll").prop("checked",false)
				//计算总页数
				var totalPages=1;
				if (data.data.total%pageSize==0){
					totalPages=data.data.total/pageSize;
				}else {
					totalPages=parseInt(data.data.total/pageSize)+1;
				}
				//添加分页的前端插件
				$("#pageDiv").bs_pagination({
					currentPage:pageNumber,//当前页号,相当于pageNumber
					rowsPerPage:pageSize,//每页显示的条数,相当于pageSize
					totalRows:data.data.total,//显示的总条数总条数
					totalPages: totalPages,//总页数,必填参数
					visiblePageLinks: 5,//最多可以显示的卡片数
					showGoToPage: true,//是否显示"跳转到"部分
					showRowsPerPage: true,//是否显示"每页显示的条数"
					showRowsInfo: true,//是否显示记录的信息
					onChangePage:function (event,pageObj){
						queryActivityByPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				})
			}
		});
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="u">
									  <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" readonly class="form-control myDate" id="create-startDate">
							</div>
							<label for="create-endDate"  class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" readonly class="form-control myDate" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn" >保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="editActivityForm" class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-ActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-ActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option id="${u.name}" value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" readonly class="form-control myDate" id="edit-startDate" value="2020-10-10">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" readonly class="form-control myDate" id="edit-endDate" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="query-owner" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control myDate" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control myDate" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="queryActivityBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span > 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"  /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
				<div id="pageDiv">

				</div>
			</div>

			
<%--			<div style="height: 50px; position: relative;top: 30px;">--%>
<%--				<div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRows"></b>条记录</button>--%>
<%--				</div>--%>
<%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--					<div class="btn-group">--%>
<%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--							10--%>
<%--							<span class="caret"></span>--%>
<%--						</button>--%>
<%--						<ul class="dropdown-menu" role="menu">--%>
<%--							<li><a href="#">20</a></li>--%>
<%--							<li><a href="#">30</a></li>--%>
<%--						</ul>--%>
<%--					</div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--				</div>--%>
<%--				<div style="position: relative;top: -88px; left: 285px;">--%>
<%--					<nav>--%>
<%--						<ul class="pagination">--%>
<%--							<li class="disabled"><a href="#">首页</a></li>--%>
<%--							<li class="disabled"><a href="#">上一页</a></li>--%>
<%--							<li class="active"><a href="#">1</a></li>--%>
<%--							<li><a href="#">2</a></li>--%>
<%--							<li><a href="#">3</a></li>--%>
<%--							<li><a href="#">4</a></li>--%>
<%--							<li><a href="#">5</a></li>--%>
<%--							<li><a href="#">下一页</a></li>--%>
<%--							<li class="disabled"><a href="#">末页</a></li>--%>
<%--						</ul>--%>
<%--					</nav>--%>
<%--				</div>--%>
<%--			</div>--%>
			
		</div>
		
	</div>
</body>
</html>