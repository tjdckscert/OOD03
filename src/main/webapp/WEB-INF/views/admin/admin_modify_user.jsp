<%-- 
    Document   : modify_user.jsp
    Author     : jun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType" %>
<%@page errorPage="../error_page/show_error.jsp" %>

<!DOCTYPE html>

<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>비밀번호 수정 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        <script type="text/javascript">
            function getConfirmResult() {
                var result = confirm("사용자를 정말로 수정하시겠습니까?");
                return result;
            }
        </script>
    </head>
    <body>
        <jsp:include page="../header.jspf" />

        <div id="sidebar">
            <jsp:include page="sidebar_admin_previous_menu.jsp" />
        </div>

        <div id="main">
            <h2> 비밀번호 수정 메뉴입니다. </h2> <br>
            <form name="ModifyUser" action="modify_user.do" method="POST">
                <table border="0" align="left" summary="비밀번호수정">
                    <tr>
                        <td>새 비밀번호:</td>
                        <td> <input type="password" id="password" name="password">  </td>
                    </tr>
                    <tr>
                        <td>새 비밀번호 확인:</td>
                        <td> <input type="password" id="confirmPassword" name="confirmPassword"> </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="변경하기" name="modify" onClick ="return getConfirmResult()"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <jsp:include page="../footer.jspf" />
    </body>
</html>