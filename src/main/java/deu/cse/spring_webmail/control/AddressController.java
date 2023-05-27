/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author sooye
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class AddressController {

    @GetMapping("/addr_book")
    public String addrBook(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        return "/address";
    }

    @GetMapping("/insert_address")
    public String insertAddr() {
        return "/insert_address";
    }

    @PostMapping("/insertAddr.do")
    public String insertAddressBook(HttpServletRequest request, @RequestParam("name") String name, @RequestParam("email") String phone, @RequestParam("phone") String email, RedirectAttributes attrs) {
        HttpSession session = request.getSession();

        String str;

        try {

            attrs.addFlashAttribute("msg", "주소록이 추가되었습니다.");
            str = "redirect:/addr_book";
        } catch (Exception ex) {
            log.error("insert_address.error: {}", ex.getMessage());

            attrs.addFlashAttribute("msg", "이미 추가하였거나 존재하지 않습니다.");
            str = "redirect:/insert_addr";
        }
        return str;
    }

    @PostMapping("/deleteAddr.do")
    public String deleteMailDo(@RequestParam("delete_addr") String id, RedirectAttributes attrs) {
        log.debug("delete_mail.do: msgid = {}", id);

        long[] indexes = Stream.of(id.split(",")).mapToLong(Long::parseLong).toArray();

        attrs.addFlashAttribute("msg", "주소록이 삭제되었습니다.");

        return "redirect:/addr_book";
    }
}
