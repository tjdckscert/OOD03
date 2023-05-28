<%-- 
    Document   : redirect
    Created on : 2023. 5. 19., 오전 2:04:08
    Author     : tjdckscert
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="error_page/show_error.jsp" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>알림창</title>
</head>
<body>
<script>
    alert('${msg}');
    location.href='<c:out value="${pageContext.request.contextPath}"/>${url}';
</script>
</body>
</html>