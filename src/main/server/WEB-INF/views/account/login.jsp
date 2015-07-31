<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>登录页</title>
    <!-- FAVICONS -->
    <link rel="shortcut icon" href="${ctx}/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${ctx}/static/favicon.ico" type="image/x-icon">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>

    <link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
    <link href="${ctx}/static/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet"/>
    <script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>

</head>

<body>

<div class="container">
    <%@ include file="/WEB-INF/layouts/header.jsp" %>
    <div id="content">
        <form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal">
            <%
                String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
                if (error != null) {
            %>
            <div class="alert alert-error input-medium controls">
                <button class="close" data-dismiss="alert">×</button>
                登录失败，请重试.
            </div>
            <%
                }
            %>
            <div class="control-group">
                <label for="username" class="control-label">账号:</label>

                <div class="controls">
                    <input type="text" id="username" name="username" value="${username}" class="input-medium required"/>
                </div>
            </div>
            <div class="control-group">
                <label for="password" class="control-label">密码:</label>

                <div class="controls">
                    <input type="password" id="password" name="password" class="input-medium required"/>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <label class="checkbox" for="rememberMe"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我</label>
                    <input id="submit_btn" class="btn btn-success" type="submit" value="登录"/>
                    <button class="btn" type="button" disabled>注册</button>
                </div>
            </div>
        </form>
    </div>
    <%@ include file="/WEB-INF/layouts/footer.jsp" %>
</div>

<script>
    $(document).ready(function () {
        $("#loginForm").validate();
    });
</script>
</body>
</html>
