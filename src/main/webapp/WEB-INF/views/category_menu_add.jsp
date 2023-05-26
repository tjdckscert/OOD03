<%-- 
    Document   : main_menu
    Created on : 2022. 6. 10., 오후 3:15:45
    Author     : skylo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page errorPage="error_page/show_error.jsp" %>

<!DOCTYPE html>

<!-- 제어기에서 처리하면 로직 관련 소스 코드 제거 가능!
<jsp:useBean id="pop3" scope="page" class="deu.cse.spring_webmail.model.Pop3Agent" />
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
%>
-->

<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>카테고리 추가</title>
        <link type="text/css" rel="stylesheet" href="css/button.css" />
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <link type="text/css" rel="stylesheet" href="css/categoryadd.css" />
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>
        
        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>
        <div class="container" style="width: 400px;">
            <h2 style="text-align: inherit;">카테고리 추가</h2> 
            <form method="POST" action="category_menu_addcategory">
                <div class="group">      
                    <input type="text" name="categoryName" required >
                    <span class="highlight"></span>
                    <span class="bar"></span>
                    <label>카테고리 이름</label>
                </div>
                <!--<a href="" class="btn-gradient green mini">추가</a>-->    
                <input type="submit" class="btn-gradient green mini" value="추가">
            </form>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>

