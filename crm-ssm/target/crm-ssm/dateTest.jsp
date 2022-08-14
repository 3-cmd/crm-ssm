<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=utf-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
<%--    引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--    引入trapbootsr框架--%>
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%--    日历插件--%>
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript">
        $(function (){
            //当容器加载完成,对容器调用工具函数
            $("#dateTest").datetimepicker({
                language:'zh-CN',
                format:'yyyy-mm-dd',//日期格式
                minView:'month',//最小视图 月份
                initialDate:new Date(),//初始化日历的默认选择时间
                autoclose:true,//设置完日期日历自动关闭
            });
        });
    </script>
</head>

<body>
    <input type="text" id="dateTest">
</body>
</html>
