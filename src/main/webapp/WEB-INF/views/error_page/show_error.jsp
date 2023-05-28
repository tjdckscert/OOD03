<%-- 
    Document   : show_error
    Created on : 2023. 3. 16., 오전 10:45:51
    Author     : QQQ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, inital-scale=1.0"> 
        <title>오류 원인</title>
    </head>
    <body>
        오류 원인은 &quot;<span style="color: red;" ><%= exception.getMessage()%></span>&quot;입니다.

    <jsp:include page="../main_footer.jspf" />
    </body>
</html>
