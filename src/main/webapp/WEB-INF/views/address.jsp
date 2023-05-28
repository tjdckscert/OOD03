<%-- 
    Document   : address
    Author     : sooye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>

<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>주소록</title>
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
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>

        <br> <br>
        
        <h1>주소록</h1>
        
        <form  method="POST" >
            <table border="1">
                <thead>
                    <tr>
                        <th>선택</th>
                        <th>아이디</th>  
                        <th>이름</th>
                        <th>이메일</th>
                        <th>전화번호</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="row" items="${dataRows}">
                        <tr>
                            <td><input type="checkbox" name="delete_addr" value="${row.id}"></td>
                            <td>${row.id}</td>
                            <td>${row.name}</td>
                            <td>${row.email}</td>
                            <td>${row.phone}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <input type="submit" value="삭제" onclick="javascript: form.action='deleteAddr.do';" >
        </form>



        <div id="main">
            ${messageList}
        </div>

        <a href="insert_address.jsp">주소록 추가</a>

        <%@include file="footer.jspf"%>
    </body>

    <script>

        function checkButton() {
            if (!confirm("주소록을 삭제하시겠습니까")) {
                return false;
            } else {
                return true;
            }
        }
    </script>

</html>
        
  
            
<!--            <c:catch var="errorReason">
                <mytags:address user="root" password="suyeon" schema="webmail" table="addrbook" />
            </c:catch>
            ${empty errorReason ? "<noerror />" : errorReason}<! 오류 원인 출력 
            <br/>
            -->
      