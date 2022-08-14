<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=utf-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>文件下载演示</title>
    <%--    引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function (){
            //给下载按钮添加单击事件
            $("#fileDownLoadBtn").click(function (){
                //发送文件下载请求
                window.location.href="workbench/activity/fileDownload";
            })
        })
    </script>
</head>
<body>
    <input type="button" value="下载" id="fileDownLoadBtn">
</body>
</html>
