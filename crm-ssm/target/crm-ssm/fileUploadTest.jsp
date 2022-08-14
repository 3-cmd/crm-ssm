<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=utf-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>文件上传演示</title>
    <%--    引入jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
</head>
<body>
    <form action="workbench/activity/fileUpLoadTest" method="post" id="uploadForm" enctype="multipart/form-data">
        <input type="file" name="myFile"><br>
        <input type="text" name="userName"><br>
        <input type="submit" value="提交文件">
    </form>
</body>
</html>