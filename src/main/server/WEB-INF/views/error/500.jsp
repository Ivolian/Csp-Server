<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%	
	//设置返回码200，避免浏览器自带的错误页面
	response.setStatus(200);
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(exception.getMessage(), exception);
%>

<!DOCTYPE html>
<html>
<head>
	<title>500 - 系统内部错误</title>
    <!-- FAVICONS -->
    <link rel="shortcut icon" href="${ctx}/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${ctx}/static/favicon.ico" type="image/x-icon">
</head>

<body>
	<h2><%= exception.getMessage()%></h2>
    <p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>
