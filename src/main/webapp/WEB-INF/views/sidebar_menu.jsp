<%-- 
    Document   : new_sidebar_menu
    Created on : 2023. 5. 10, 오후 3:25:30
    Author     : tjdckscert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="deu.cse.spring_webmail.control.CommandType"%>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>웹메일 시스템 메뉴</title>
    </head>
    <body>
        <br> <br>        
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>
        <p> <a href="main_menu"> 수신 메일 : 일반 </a> </p>                
        <p> <a href="category_menu"> 수신 메일 : 카테고리</a> </p> 
        <p> <a href="isread_mail"> 수신 여부 확인하기</a> </p> 
        <p> <a href="write_mail"> 메일 쓰기 </a> </p> 
        <p><a href="modify_user"> 비밀번호 수정</a> </p>  
        <p><a href="trash_mail"> 휴지통</a> </p>
        <p> <a href="Important_mail"> 중요메일함 </a> </p>
        <p> <a href="address"> 주소록 </a> </p>
        <p> <a href="login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
        <br>
        <br>       
        <p> <a href="category_menu_add"> 카테고리 추가 </a> </p>                 
        <p> <a href="category_menu_delete"> 카테고리 삭제 </a> </p> 
    </body>
</html>
