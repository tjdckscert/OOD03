<%--
  Created by IntelliJ IDEA.
  User: QQQ
  Date: 2023-05-14
  Time: 오후 3:25
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page errorPage="../error_page/show_error.jsp" %>

<!DOCTYPE html>

<html lang="ko">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>휴지통 메일 보기 화면</title>
  <link type="text/css" rel="stylesheet" href="../css/main_style.css" />
</head>
<body>
<%@include file="../header.jspf"%>

<div id="sidebar">
  <a href="/webmail/trash_mail"> 이전 메뉴로 </a>
</div>

<div id="msgBody">
  ${msg}
</div>
<hr>
<%@include file="../footer.jspf"%>
</body>
</html>