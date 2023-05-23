/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 휴지통 페이지 이동
     * @return 
     */
    @GetMapping("/trash_mail")
    public String trash() {
        return "trash_mail";
    }
}
