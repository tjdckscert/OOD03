/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.db.TrashService;
import deu.cse.spring_webmail.dto.TrashDTO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author QQQ
 */
@Controller
@Slf4j
@PropertySource("classpath:/system.properties")
public class TrashController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TrashService trashService;

    /**
     * 사용자 아이디에 맞는 휴지통 리스트 표시
     *
     * @return 휴지통 페이지 이동
     */
    @GetMapping("/trash_mail")
    public String trash(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userid");

        List<TrashDTO> trashDTOList = trashService.getTrashListByToAddress(userId);
        model.addAttribute("start", trashDTOList.size());
        model.addAttribute("trashList", trashDTOList);
        return "trash_mail";
    }

    /**
     * 휴지통 메일 완전 삭제 수행
     *
     * @param trashId 휴지통 메일 id
     * @return 휴지통으로 리다이렉트
     */
    @GetMapping("/trash_mail/delete/{trashId}")
    public String deleteMail(@PathVariable("trashId") Long trashId, RedirectAttributes attrs) {
        trashService.deleteMail(trashId);
        attrs.addFlashAttribute("msg", "메일이 완전 삭제 되었습니다.");
        return "redirect:/trash_mail";
    }

    /**
     * 휴지통에서 메일 읽기 수행 페이지 이동
     *
     * @return 휴지통 해당 메일 상세 보기
     */
    @GetMapping("/trash_mail/{trashId}")
    public String showTrashMail(@PathVariable("trashId") Long trashId, Model model) {
        String msg = trashService.getMessage(trashId, request);
        model.addAttribute("msg", msg);
        return "/read_mail/show_trash_message";
    }
    
    /**
     * 휴지통에서 해당 메일을 메일함으로 복구 기능 
     * @return 사용자 메인페이지로 리다이렉트
     */
    @GetMapping("/trash_mail/restore/{trashId}")
    public String restoreMail(@PathVariable("trashId") Long trashId, RedirectAttributes attrs) {
        boolean sendSuccessful = trashService.restoreMail(trashId);
        trashService.deleteMail(trashId);
        if (sendSuccessful) {
            attrs.addFlashAttribute("msg", "메일 복구 성공했습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메일 복구 실패했습니다.");
        }

        return "redirect:/main_menu";
    }
}
