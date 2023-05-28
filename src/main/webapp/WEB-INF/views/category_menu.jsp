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
        <title>주메뉴 화면</title>
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
        
        <!-- 메시지 삭제 링크를 누르면 바로 삭제되어 실수할 수 있음. 해결 방법은? -->
        <div id="main">
            <h2 style="text-align: inherit;">카테고리 목록 </h2>
            <c:choose>
                <c:when test="${size eq 0}">
                    <h3 style="color: #F05B5B; font-size: x-large;"> 등록된 카테고리가 없습니다.</h3>
                </c:when>
                <c:otherwise>            
                    <ul class="pagination">
                    <c:forEach var="pNo" begin="1" end="${size}" step="1">
                            <li><a href="/webmail/category_menu?categoryName=${list[pNo-1]}" aria-controls="dataTable" data-dt-idx="1" tabindex="0" class="page-link">${list[pNo-1]}</a></li>                    
                    </c:forEach>
                    </ul>
                    ${messageList}
            </c:otherwise>        
            </c:choose>
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html>
