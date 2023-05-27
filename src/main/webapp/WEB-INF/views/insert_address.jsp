<%-- 
    Document   : insert_address
    Author     : suyeon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<html  lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>주소록 추가 폼</title>
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
        <hr/>

        <form action="insertAddr.do" method="POST" name="insert" onsubmit="return validateForm()" required>
            <table border = "0">
                <caption>주소록 추가</caption>
                <thead>
                    <tr>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>이름</td>
                        <td><input type="text" name="name" size="20" /></td>

                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td><input type="text" name="email" size="20" /></td>
                    </tr>
                    <tr>
                        <td>전화번호</td>
                        <td><input type="text" name="phone" size="20" /></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <center>
                                <input type="submit" value="추가" /><!-- 주소록 등록 -->
                                <input type="reset" value="초기화" />
                            </center>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
        
        <div id="main">
        ${messageList}
    </div>

    <%@include file="footer.jspf"%>
    
    </body>
</html>

<script>
    function validateForm() {
        var x = document.forms["insert"]["name"].value;
        var y = document.forms["insert"]["email"].value;
        var y = document.forms["insert"]["phone"].value;
        
        if (x == "") {
            alert("이름을 입력해주세요.");
            return false;
        }
        if (y == "") {
            alert("이메일을 입력해주세요.");
            return false;
        }
    }
</script>