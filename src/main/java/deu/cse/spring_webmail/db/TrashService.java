package deu.cse.spring_webmail.db;

import deu.cse.spring_webmail.dto.TrashDTO;
import deu.cse.spring_webmail.model.SmtpAgent;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class TrashService {

    @Autowired
    private HttpSession session;

    @Autowired
    private TrashRepository trashRepository;
        
    @Autowired
    private ServletContext ctx;
    
    @Value("${file.upload_folder}")
    private String UPLOAD_FOLDER;

    public List<TrashDTO> getTrashListByToAddress(String userId) {
        List<Trash> trashList = trashRepository.findByToAddress(userId);
        List<TrashDTO> trashDTOList = new ArrayList<>();
        for (Trash trash : trashList) {
            TrashDTO trashDTO = new TrashDTO(
                    trash.getId(),
                    trash.getMsgId(),
                    trash.getFromAddress(),
                    trash.getSubject(),
                    trash.getSentDate()
            );
            trashDTOList.add(trashDTO);
        }
        return trashDTOList;
    }

    /**
     * 휴지통에서 메일 완전 삭제 수행
     *
     * @param trashId 휴지통 메일 id
     */
    @Transactional
    public void deleteMail(Long trashId) {
        trashRepository.deleteById(trashId);
    }

    /**
     * 메일 가져오기
     *
     * @param trashId
     * @param request
     * @return
     */
    public String getMessage(Long trashId, HttpServletRequest request) {
        Optional<Trash> optionalTrash = trashRepository.findById(trashId);
        String userId = (String) session.getAttribute("userid");
        if (optionalTrash.isPresent()) {
            Trash trash = optionalTrash.get();
            return getFormatMessage(trash, userId);
        }
        return null;
    }

    public String getFormatMessage(Trash trash, String userId) { //상세보기
        StringBuilder buffer = new StringBuilder();

        buffer.append("보낸 사람: " + trash.getFromAddress() + " <br>");
        buffer.append("받은 사람: " + trash.getToAddress() + " <br>");
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + trash.getCcAddress() + " <br>");
        buffer.append("보낸 날짜: " + trash.getSentDate() + " <br>");
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + trash.getSubject() + " <br> <hr>");

        buffer.append(trash.getBody());

        String attachedFile = trash.getFileName();
        if (attachedFile != null) {
            buffer.append("<br> <hr> 첨부파일: <a href=download"
                    + "?userid=" + userId
                    + "&filename=" + attachedFile.replaceAll(" ", "%20")
                    + " target=_top> " + attachedFile + "</a> <br>");
        }

        return buffer.toString();
    }
    
    /**
     * 휴지통 메일을 복구하는 기능
     * @param trashId
     * @return 
     */
    public boolean restoreMail(Long trashId) {
        Optional<Trash> optionalTrash = trashRepository.findById(trashId);
        if (optionalTrash.isPresent()) {
            Trash trash = optionalTrash.get();
            return restoreMessage(trash.getToAddress(),trash.getFromAddress(), trash.getCcAddress(), trash.getSubject(), trash.getBody(), null);
        }
        return false;
    }
    
    private boolean restoreMessage(String to,String from, String cc, String subject, String body, MultipartFile upFile) {
        boolean status = false;

        // 3. HttpSession 객체에서 메일 서버, 메일 사용자 ID 정보 얻기
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");

        // 4. SmtpAgent 객체에 메일 관련 정보 설정
        SmtpAgent agent = new SmtpAgent(host, userid);
        agent.setTo(to);
        agent.setFrom(from);
        agent.setCc(cc);
        agent.setSubj(subject);
        agent.setBody(body);
        String fileName = null;

        if (fileName != null && !"".equals(fileName)) {
            log.debug("sendMessage: 파일({}) 첨부 필요", fileName);
            File f = new File(ctx.getRealPath(UPLOAD_FOLDER) + File.separator + fileName);
            agent.setFile1(f.getAbsolutePath());
        }

        // 5. 메일 전송 권한 위임
        if (agent.sendMessageByTrash()) {//
            status = true;
        }
        return status;
    }
}
