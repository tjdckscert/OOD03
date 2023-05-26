<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page errorPage="error_page/show_error.jsp" %>

<!DOCTYPE html>


<jsp:useBean id="pop3" scope="page" class="deu.cse.spring_webmail.model.Pop3Agent" />
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    pop3.setHost((String) session.getAttribute("host"));
    pop3.setUserid((String) session.getAttribute("userid"));
    pop3.setPassword((String) session.getAttribute("password"));
%>

<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>휴지통 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
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

        <div id="main">
            <h2> 휴지통</h2>
            <table summary="휴지통">
                <caption>휴지통 메일</caption>
                <tr>
                    <th> No.</th>
                    <th> 보낸사람</th>
                    <th> 제목</th>
                    <th> 보낸날짜</th>
                    <th> 복구</th>
                    <th> 삭제</th>
                </tr>
                <c:forEach var="trash"  items="${trashList}"  varStatus="status">
                    <tr>
                        <td id="no">${status.index + 1}</td>
                        <td id ="sender">${trash.sender}</td>
                        <td id ="subject"><a href="trash_mail/${trash.id}">${trash.subject}</a></td>
                        <td id ="date">${trash.sentDate}</td>
                        <td id ="restore"><a href="trash_mail/restore/${trash.id}" onclick="return confirm('정말로 복구하시겠습니까?')">복구</a> </td>
                        <td id ="delete"><a href="trash_mail/delete/${trash.id}" onclick="return confirm('정말로 삭제하시겠습니까?')">삭제</a> </td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <%@include file="footer.jspf"%>
    </body>
</html>