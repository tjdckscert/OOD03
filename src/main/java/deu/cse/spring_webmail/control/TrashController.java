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

/**
 *
 * @author QQQ
 */
@Controller
@Slf4j
@PropertySource("classpath:/system.properties")
public class TrashController {

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
}
