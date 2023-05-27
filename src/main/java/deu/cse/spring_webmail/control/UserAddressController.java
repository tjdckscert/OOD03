/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UserAddressController
 *
 * @author sooye
 */
public class UserAddressController {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            final String JdbcDriver = "org.mariadb.jdbc.Driver";
            final String JdbcUrl = "jdbc:mariadb://localhost:3306/webmail";
            final String User = "root";
            final String Password = "suyeon";

            try {
                // 1. JDBC  드라이버 적재
                Class.forName(JdbcDriver);
                // 2. Connection 객체 생성
                Connection conn = DriverManager.getConnection(JdbcUrl, User, Password);
                // 3. PreparedStatement 객체 생성
                String sql = "insert into addrbook values(?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // 4. SQL문 완성
                request.setCharacterEncoding("UTF-8"); //한글 인식
                String email=request.getParameter("email");  //주키 (PK)
                if(!(email==null) && !email.equals("")){
                    String name = request.getParameter("name");
                    String phone = request.getParameter("phone");
                    
                    pstmt.setString(1,email);
                    pstmt.setString(2,name);
                    pstmt.setString(3,phone);
                    
                    // 5. SQL 질의 결과 활용
                    pstmt.executeUpdate();
                }
                
                // 6. 자원 해제   
                pstmt.close();
                conn.close();
                
                response.sendRedirect("address.jsp");
            }catch(Exception ex){
                out.println("오류가 발생했습니다. (발생 오류: "+ex.getMessage() + ")");
                out.println("<br/> <a href=\"address.jsp\"초기 화면으로 가기</a>");
                
            }
        }finally{
            out.close();
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }
    
    public String getServletInfo(){
        return "SQL INSERT문을 사용한 DB 갱신";
    }
}
