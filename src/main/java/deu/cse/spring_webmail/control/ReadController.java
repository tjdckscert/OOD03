/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.Repository.InboxRepository;
import deu.cse.spring_webmail.model.ImportantMessageAgent;
import deu.cse.spring_webmail.db.Trash;
import deu.cse.spring_webmail.db.TrashRepository;
import deu.cse.spring_webmail.model.Pop3Agent;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * @author Prof.Jong Min Lee
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class ReadController {

    private ImportantMessageAgent importantMessageAgent = ImportantMessageAgent.getInstance();
    private static final Logger logger = Logger.getLogger(ReadController.class.getName());
    
    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private InboxRepository InboxRepository;
    @Value("${file.download_folder}")
    private String DOWNLOAD_FOLDER;

    @Autowired
    private TrashRepository trashRepository;

    @GetMapping("/show_message")
    public String showMessage(@RequestParam Integer msgid, @RequestParam Boolean isread, @RequestParam String mailIndex, Model model) {
        log.debug("download_folder = {}", DOWNLOAD_FOLDER);
        if (!isread) {
            Inbox inbox = InboxRepository.findById(Integer.parseInt(mailIndex)).orElse(null);
            inbox.setIsRead(true);
            InboxRepository.save(inbox);
        }
        Pop3Agent pop3 = new Pop3Agent();
        pop3.setHost((String) session.getAttribute("host"));
        pop3.setUserid((String) session.getAttribute("userid"));
        pop3.setPassword((String) session.getAttribute("password"));
        pop3.setRequest(request);

        String msg = pop3.getMessage(msgid);
        session.setAttribute("sender", pop3.getSender());  // 220612 LJM - added
        session.setAttribute("subject", pop3.getSubject());
        session.setAttribute("body", pop3.getBody());
        model.addAttribute("msg", msg);
        return "/read_mail/show_message";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("userid") String userId,
            @RequestParam("filename") String fileName) {
        log.debug("userid = {}, filename = {}", userId, fileName);
        try {
            log.debug("userid = {}, filename = {}", userId, MimeUtility.decodeText(fileName));
        } catch (UnsupportedEncodingException ex) {
            log.error("error");
        }

        // 1. 내려받기할 파일의 기본 경로 설정
        String basePath = ctx.getRealPath(DOWNLOAD_FOLDER) + File.separator + userId;

        // 2. 파일의 Content-Type 찾기
        Path path = Paths.get(basePath + File.separator + fileName);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
            log.debug("File: {}, Content-Type: {}", path.toString(), contentType);
        } catch (IOException e) {
            log.error("downloadDo: 오류 발생 - {}", e.getMessage());
        }

        // 3. Http 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment").filename(fileName, StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        // 4. 파일을 입력 스트림으로 만들어 내려받기 준비
        Resource resource = null;
        try {
            resource = new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            log.error("downloadDo: 오류 발생 - {}", e.getMessage());
        }
        if (resource == null) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/delete_mail.do")
    public String deleteMailDo(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs) {
        log.debug("delete_mail.do: msgid = {}", msgId);

        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        String password = (String) session.getAttribute("password");

        Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        pop3.setRequest(request);

        Map<String, String> messageInfo = pop3.getInfoByDeleteMessage(msgId);
        boolean deleteSuccessful = pop3.deleteMessage(msgId, true);
        if (deleteSuccessful) {
            attrs.addFlashAttribute("msg", "메시지 삭제를 성공하였습니다.");
            // trash 테이블에 삭제한 메시지 정보 추가
            Trash trash = new Trash();
            trash.setMsgId(msgId);
            trash.setToAddress(messageInfo.get("toAddress"));
            trash.setFromAddress(messageInfo.get("fromAddress"));
            trash.setCcAddress(messageInfo.get("ccAddress"));
            trash.setSubject(messageInfo.get("subject"));
            trash.setSentDate(messageInfo.get("sentDate"));
            trash.setBody(messageInfo.get("body"));
            trash.setFileName(messageInfo.get("fileName"));
            trashRepository.save(trash);
        } else {
            attrs.addFlashAttribute("msg", "메시지 삭제를 실패하였습니다.");
        }

        return "redirect:main_menu";
    }
    // 중요 메일
     @GetMapping("/Important_mail")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        int select = Integer.parseInt( request.getParameter("menu"));
        
        switch (select) {
            
            // 중요 메일 삭제
            case CommandType.DELETE_MAIL_COMMAND_IN_IMPORTANT:   
                try (PrintWriter out = response.getWriter()) {
                deleteMessage(request);
                int msgid = Integer.parseInt(request.getParameter("msgid"));

                boolean isSuccess = importantMessageAgent.removeMessage(msgid);
                if (isSuccess) {
                    out.println("<script>alert('중요 메일이 삭제되었습니다.');location.href='Important_mail.jsp'</script>");
                    importantMessageAgent.updateMsgId(msgid);
                } else {
                    out.println("<script>alert('중요 메일 삭제를 실패했습니다.');location.href='Important_mail.jsp'</script>");
                }
            }
            break;
            
            // 중요 메일 설정
            case CommandType.SET_IMPORTANT: 
                try (PrintWriter out = response.getWriter()) {
                int msgid = Integer.parseInt(request.getParameter("msgid"));
                if (importantMessageAgent.addMessage(msgid)) {
                    //bookmarking 성공
                    out.println(/*"userid : "+userid+"님, "+msgid+"번 메일*/"<script>alert('중요 메일 설정되었습니다.');location.href='main_menu.jsp'</script>");
                } else {
                    out.println("<script>alert('중요 메일 설정이 실패했습니다.');location.href='main_menu.jsp'</script>");
                }
            } catch (Exception ex) {
                PrintWriter out = response.getWriter();
                out.println("ReadmailHandler.cancelImportant error : " + ex);
            }
            break;
            
            // 중요 메일 취소
            case CommandType.CANCLE_IMPORTANT: 
                try (PrintWriter out = response.getWriter()) {
                int msgid = Integer.parseInt( request.getParameter("msgid"));
                logger.log(Level.INFO,"request.getParameter msgid  : " + Integer.toString(msgid));
                if (importantMessageAgent.removeMessage(msgid)) {
                    //bookmarking 성공
                    out.println("<script>alert('중요 메일 설정이 취소되었습니다.');location.href='Important_mail.jsp'</script>");
                } else {
                    out.println("<script>alert('중요 메일 설정 취소가 실패했습니다.');location.href='Important_mail.jsp'</script>");
                }
            } catch (Exception ex) {
                PrintWriter out = response.getWriter();
                out.println("ReadmailHandler.cancelImportant error : " + ex);
            }
            break;
            default:
                try (PrintWriter out = response.getWriter()) {
                out.println("없는 메뉴입니다");
            }
            break;
        }
    }
    private boolean deleteMessage(HttpServletRequest request) {
        int msgid = Integer.parseInt( request.getParameter("msgid"));

        HttpSession httpSession = request.getSession();
        String host = (String) httpSession.getAttribute("host");
        String userid = (String) httpSession.getAttribute("userid");
        String password = (String) httpSession.getAttribute("password");

       
        Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        return pop3.deleteMessage(msgid, true);
    }
}
