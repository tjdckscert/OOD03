<%-- 
<%-- 
    Document   : main_menu
    Created on : 2022. 6. 10., 오후 3:15:45
    Author     : tjdckscert
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
            <h2 style="text-align: inherit;"> 수신 여부 확인 </h2>
        <div class="dataTables_paginate paging_simple_numbers" id="dataTable_paginate">
            <ul class="pagination">
            <!-- Previous 시작 -->            
            <c:choose>
			<c:when test="${list.startPage<6}">
                                <li><a href="/webmail/isread_mail?currentPage=1" aria-controls="dataTable" data-dt-idx="0" tabindex="0" class="page-link">Previous</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="/webmail/isread_mail?currentPage=${list.startPage-5 }" aria-controls="dataTable" data-dt-idx="0" tabindex="0" class="page-link">Previous</a></li>
			</c:otherwise>
            </c:choose>
            
            <!-- Previous 끝 -->
            <!-- Page번호 시작 -->
            <c:forEach var="pNo" begin="${list.startPage }" end="${list.endPage }" step="1">
                    <li class="paginate_button page-item  <c:if test='${param.currentPage eq pNo }'>active</c:if>"><a href="/webmail/isread_mail?currentPage=${pNo }" aria-controls="dataTable" data-dt-idx="1" tabindex="0" class="page-link">${pNo }</a></li>                    
            </c:forEach>
            <!-- Page번호 끝 -->
            <!-- Next 시작 -->
            <c:choose>
			<c:when test="${list.endPage>=list.totalPages }">
                                <li><a href="/webmail/isread_mail?currentPage=${list.startPage}" aria-controls="dataTable" data-dt-idx="7" tabindex="0" class="page-link">Next</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="/webmail/isread_mail?currentPage=${list.startPage+5 }" aria-controls="dataTable" data-dt-idx="7" tabindex="0" class="page-link">Next</a></li>
			</c:otherwise>
            </c:choose>            
            
            <!-- Next 끝 -->            
        </ul>
            ${messageList}
        </div>
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html>
