<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%    String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";%>
<html>
<head>
	<title>login</title>
</head>

<body>
<script type="text/javascript">
	window.location.href="/settings/qx/user/toLogin.do";
</script>
</body>
</html>