/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.control.CommandType;
import jakarta.mail.Message;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author skylo
 */
@Slf4j
@RequiredArgsConstructor
public class MessageFormatter {
    @NonNull private String userid;  // 파일 임시 저장 디렉토리 생성에 필요
    private HttpServletRequest request = null;
    
    // 220612 LJM - added to implement REPLY
    @Getter private String sender;
    @Getter private String subject;
    @Getter private String body;


    public String getMessageTable(Message[] messages) {
        StringBuilder buffer = new StringBuilder();

        // 메시지 제목 보여주기
        buffer.append("<table>");  // table start
        buffer.append("<tr> "
                + " <th> No. </td> "
                + " <th> 보낸 사람 </td>"
                + " <th> 제목 </td>     "
                + " <th> 보낸 날짜 </td>   "
                + " <th> 삭제 </td>   "
                + " <th> 중요메일 </td>   "
                + " </tr>");

        for (int i = messages.length - 1; i >= 0; i--) {
            MessageParser parser = new MessageParser(messages[i], userid);
            parser.parse(false);  // envelope 정보만 필요
            // 메시지 헤더 포맷
            // 추출한 정보를 출력 포맷 사용하여 스트링으로 만들기
            buffer.append("<tr> "
                    + " <td id=no>" + (i + 1) + " </td> "
                    + " <td id=sender>" + parser.getFromAddress() + "</td>"
                    + " <td id=subject> "
                    + " <a href=show_message?msgid=" + (i + 1) + " title=\"메일 보기\"> "
                    + parser.getSubject() + "</a> </td>"
                    + " <td id=date>" + parser.getSentDate() + "</td>"
                    + " <td id=delete>"
                    + "<a href=delete_mail.do"
                    + "?msgid=" + (i + 1) + "> 삭제 </a>" + "</td>"
                    + " <td id=setBookmarking>"  // 중요 메일 설정
                    + "<a href=ReadMail.do?menu="
                    + CommandType.SET_IMPORTANT //-----------//
                    + "&msgid=" + (i + 1) + "> 설정 </a>" + "</td>"        
                    + " </tr>");
        }
        buffer.append("</table>");

        return buffer.toString();
//        return "MessageFormatter 테이블 결과";
    }

    public String getMessage(Message message) {
        StringBuilder buffer = new StringBuilder();

        // MessageParser parser = new MessageParser(message, userid);
        MessageParser parser = new MessageParser(message, userid, request);
        parser.parse(true);
        
        sender = parser.getFromAddress();
        subject = parser.getSubject();
        body = parser.getBody();

        buffer.append("보낸 사람: " + parser.getFromAddress() + " <br>");
        buffer.append("받은 사람: " + parser.getToAddress() + " <br>");
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + parser.getCcAddress() + " <br>");
        buffer.append("보낸 날짜: " + parser.getSentDate() + " <br>");
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + parser.getSubject() + " <br> <hr>");

        buffer.append(parser.getBody());

        String attachedFile = parser.getFileName();
        if (attachedFile != null) {
            buffer.append("<br> <hr> 첨부파일: <a href=download"
                    + "?userid=" + this.userid
                    + "&filename=" + attachedFile.replaceAll(" ", "%20")
                    + " target=_top> " + attachedFile + "</a> <br>");
        }

        return buffer.toString();
    }
    
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
     public String getImportantMessageTable(ArrayList<Message> importantMessages) {
        ImportantMessageAgent importantMessageAgent = ImportantMessageAgent.getInstance();
        ArrayList<Integer> msgIdList = importantMessageAgent.getMsgIdList();
        StringBuilder buffer = new StringBuilder();

        // 메시지 제목 보여주기
        buffer.append("<table>");  // table start
        buffer.append("<tr> "
                + " <th> No. </td> "
                + " <th> 보낸 사람 </td>"
                + " <th> 제목 </td>     "
                + " <th> 보낸 날짜 </td>   "
                + " <th> 메일삭제 </td>   "
                + " <th> 중요메일</td>   "
                + " </tr>");

        for (int i = importantMessages.size() - 1; i >= 0; i--) {

            try {
                MessageParser parser = new MessageParser(importantMessages.get(i), this.userid);
                parser.parse(false);  // envelope 정보만 필요
                // 메시지 헤더 포맷
                // 추출한 정보를 출력 포맷 사용하여 스트링으로 만들기
                buffer.append("<tr> "
                        + " <td id=no>" + msgIdList.get(i) + " </td> "
                        + " <td id=sender>" + parser.getFromAddress() + "</td>"
                        + " <td id=subject> "
                        + " <a href=show_message.jsp?msgid=" + msgIdList.get(i) + " title=\"메일 보기\"> "
                        + parser.getSubject() + "</a> </td>"
                        + " <td id=date>" + parser.getSentDate() + "</td>"
                        + " <td id=delete>"
                        + "<a href=ReadMail.do?menu="
                        + CommandType.DELETE_MAIL_COMMAND_IN_IMPORTANT 
                        + "&msgid=" + msgIdList.get(i) + "> 삭제 </a>" + "</td>"
                        + " <td id=cancelBookmarking>"
                        + "<a href=ReadMail.do?menu="
                        + CommandType.CANCLE_IMPORTANT
                        + "&msgid=" + msgIdList.get(i) + "> 취소 </a>" + "</td>"
                        + " </tr>");

            } catch (Exception ex) {
                Logger.getLogger(MessageFormatter.class.getName()).log(Level.FINE, null, ex);
                buffer.append("<br><h1>" + ex + "</h1><br>");
            }
            System.out.println(Integer.toString(i) + " :                                                       trying to get messages ");

        }
        buffer.append("</table>");
        return buffer.toString();
    }
}
