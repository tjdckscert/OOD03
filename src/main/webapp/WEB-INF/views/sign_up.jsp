<%-- 
    Document   : sign_up
    Created on : 2023. 4. 29., 오후 12:07:26
    Author     : QQQ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page errorPage="error_page/show_error.jsp" %>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, inital-scale=1.0">
        <title>회원가입</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <link type="text/css" rel="stylesheet" href="css/sign_up.css" />
        <script>
            <c:if test="${!empty msg}">
            alert("${msg}");
            </c:if>
        </script>
    </head>
    <body>
        <%@include file="header.jspf"%>
        <div id="signup_form">
            <form method="POST" action="signup.do" name="SignUp" >
                <table>
                    <tr>
                        <th> 정보 </th>
                        <th> 입력 </th>
                    </tr>
                    <tr>
                        <td style="text-align: right;">아이디 :</td>
                        <td><input type="text" name="id"></td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">비밀번호 :</td>
                        <td><input type="password" name="pw"></td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">비밀번호 확인 :</td>
                        <td><input type="password" name="check_pw"></td>
                    </tr>
                </table>
                <br>
                <input type="submit" value="가입">&nbsp;&nbsp;&nbsp;
                <input type="button" value="취소" onclick="location.href = '${pageContext.request.contextPath}'">
            </form>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>