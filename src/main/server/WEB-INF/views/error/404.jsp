<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%response.setStatus(200);%>

<!DOCTYPE html>
<html>
<head>
	<title>404 - 页面不存在</title>
    <!-- FAVICONS -->
    <link rel="shortcut icon" href="${ctx}/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${ctx}/static/favicon.ico" type="image/x-icon">
</head>

<body>
	<h2>404 - 页面不存在.</h2>
	<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>