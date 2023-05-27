/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.entity.Category;
import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.Repository.CategoryRepository;
import deu.cse.spring_webmail.Repository.InboxRepository;
import deu.cse.spring_webmail.model.MailPageing;
import deu.cse.spring_webmail.model.NewMakeTable;
import deu.cse.spring_webmail.model.Pop3Agent;
import deu.cse.spring_webmail.model.UserAdminAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * 초기 화면과 관리자 기능(사용자 추가, 삭제)에 대한 제어기
 *
 * @author skylo
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class SystemController {

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private InboxRepository inbox;
    @Autowired
    private CategoryRepository category;

    @Value("${root.id}")
    private String ROOT_ID;
    @Value("${root.password}")
    private String ROOT_PASSWORD;
    @Value("${admin.id}")
    private String ADMINISTRATOR;  //  = "admin";
    @Value("${james.control.port}")
    private Integer JAMES_CONTROL_PORT;
    @Value("${james.host}")
    private String JAMES_HOST;

    @GetMapping("/")
    public String index() {
        log.debug("index() called...");
        session.setAttribute("host", JAMES_HOST);
        session.setAttribute("debug", "false");

        return "/index";
    }

    @RequestMapping(value = "/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginDo(@RequestParam Integer menu) {
        String url = "";
        log.debug("로그인 처리: menu = {}", menu);
        switch (menu) {
            case CommandType.LOGIN:
                String host = (String) request.getSession().getAttribute("host");
                String userid = request.getParameter("userid");
                String password = request.getParameter("passwd");

                // Check the login information is valid using <<model>>Pop3Agent.
                Pop3Agent pop3Agent = new Pop3Agent(host, userid, password);
                boolean isLoginSuccess = pop3Agent.validate();

                // Now call the correct page according to its validation result.
                if (isLoginSuccess) {
                    if (isAdmin(userid)) {
                        // HttpSession 객체에 userid를 등록해 둔다.
                        session.setAttribute("userid", userid);
                        // response.sendRedirect("admin_menu.jsp");
                        url = "redirect:/admin_menu";
                    } else {
                        // HttpSession 객체에 userid와 password를 등록해 둔다.
                        session.setAttribute("userid", userid);
                        session.setAttribute("password", password);
                        // response.sendRedirect("main_menu.jsp");
                        url = "redirect:/main_menu";  // URL이 http://localhost:8080/webmail/main_menu 이와 같이 됨.
                        // url = "/main_menu";  // URL이 http://localhost:8080/webmail/login.do?menu=91 이와 같이 되어 안 좋음
                    }
                } else {
                    // RequestDispatcher view = request.getRequestDispatcher("login_fail.jsp");
                    // view.forward(request, response);
                    url = "redirect:/login_fail";
                }
                break;
            case CommandType.LOGOUT:
                session.invalidate();
                url = "redirect:/";  // redirect: 반드시 넣어야만 컨텍스트 루트로 갈 수 있음
                break;
            default:
                break;
        }
        return url;
    }

    @GetMapping("/login_fail")
    public String loginFail() {
        return "login_fail";
    }

    protected boolean isAdmin(String userid) {
        boolean status = false;

        if (userid.equals(this.ADMINISTRATOR)) {
            status = true;
        }

        return status;
    }

    @GetMapping("/main_menu")
    public String mainmenu(Model model, @RequestParam(defaultValue = "1") int currentPage) {
        List<Inbox> maillists = inbox.findByRecipientsOrderByLastUpdated(session.getAttribute("userid").toString() + "@localhost");
        String messageList = NewMakeTable.makeMainTable(maillists, currentPage);
        int total = maillists.size();
        model.addAttribute("list", new MailPageing(total, currentPage, 7, 5, maillists));
        model.addAttribute("total", total);
        model.addAttribute("messageList", messageList);
        return "main_menu";
    }

    @GetMapping("/isread_mail")
    public String isreadmenu(Model model, @RequestParam(defaultValue = "1") int currentPage) {
        List<Inbox> maillist = inbox.findBySenderOrderByLastUpdated((String) session.getAttribute("userid") + "@localhost");
        String messageList = NewMakeTable.makeIsReadTable(maillist, currentPage);
        int total = maillist.size();
        log.info(String.valueOf(total) + " is size");
        model.addAttribute("list", new MailPageing(total, currentPage, 7, 5, maillist));
        model.addAttribute("total", total);
        model.addAttribute("messageList", messageList);
        return "isread_mail";
    }

    @GetMapping("/category_menu")
    public String categorymenu(Model model, @RequestParam(defaultValue = "") String categoryName) {
        List<Inbox> maillists = inbox.findByRecipientsOrderByLastUpdated(session.getAttribute("userid").toString() + "@localhost");
        List<String> categorylist = category.findUserCategory(session.getAttribute("userid").toString());
        //log.info(String.valueOf(categorylist.size()));
        String messageList = "";
        if (categoryName.equals("")) {
            messageList = NewMakeTable.makeCategoryTable(maillists, categorylist);
        } else {
            messageList = NewMakeTable.makeCategoryTable(maillists, categoryName);
        }
        String list = NewMakeTable.makeCategoryTable(maillists, categoryName);
        model.addAttribute("messageList", messageList);
        model.addAttribute("list", categorylist);
        model.addAttribute("size", categorylist.size());
        return "category_menu";
    }

    @GetMapping("/category_menu_add")
    public String categorymenuadd() {
        return "category_menu_add";
    }

    @PostMapping("/category_menu_addcategory")
    public String categorymenuadddo(Model model) {
        String categoryName = request.getParameter("categoryName");
        String useUserID = session.getAttribute("userid").toString();
        log.info((new Category(null, categoryName, useUserID)).toString());
        category.save(new Category(null, categoryName, useUserID));
        model.addAttribute("msg", "카테고리 추가가 완료 되었습니다.");
        return "category_menu_add";
    }

    @GetMapping("/category_menu_delete")
    public String categorymenudelete(Model model) {
        List<Category> categoryLists = category.findByUseUserID(session.getAttribute("userid").toString());
        String catagoryList = NewMakeTable.makeCategoryNameTable(categoryLists);
        model.addAttribute("catagoryList", catagoryList);
        return "category_menu_delete";
    }

    @GetMapping("/category_menu_deletecategory")
    public String categorymenudeletedo(@RequestParam Integer Cindex, Model model) {
        log.info(String.valueOf(Cindex));
        category.deleteById(Cindex);
        model.addAttribute("msg", "카테고리 삭제가 완료 되었습니다.");
        return "redirect:category_menu_delete";
    }

    @GetMapping("/admin_menu")
    public String adminMenu(Model model) {
        log.debug("root.id = {}, root.password = {}, admin.id = {}",
                ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

        model.addAttribute("userList", getUserList());
        return "admin/admin_menu";
    }

    @GetMapping("/add_user")
    public String addUser() {
        return "admin/add_user";
    }

    @PostMapping("/add_user.do")
    public String addUserDo(@RequestParam String id, @RequestParam String password,
            RedirectAttributes attrs) {
        log.debug("add_user.do: id = {}, password = {}, port = {}",
                id, password, JAMES_CONTROL_PORT);

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

            // if (addUser successful)  사용자 등록 성공 팦업창
            // else 사용자 등록 실패 팝업창
            if (agent.addUser(id, password)) {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 성공하였습니다.", id));
            } else {
                attrs.addFlashAttribute("msg", String.format("사용자(%s) 추가를 실패하였습니다.", id));
            }
        } catch (Exception ex) {
            log.error("add_user.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return "redirect:/admin_menu";
    }

    @GetMapping("/delete_user")
    public String deleteUser(Model model) {
        log.debug("delete_user called");
        model.addAttribute("userList", getUserList());
        return "admin/delete_user";
    }

    /**
     *
     * @param selectedUsers <input type=checkbox> 필드의 선택된 이메일 ID. 자료형: String[]
     * @param attrs
     * @return
     */
    @PostMapping("delete_user.do")
    public String deleteUserDo(@RequestParam String[] selectedUsers, RedirectAttributes attrs) {
        log.debug("delete_user.do: selectedUser = {}", List.of(selectedUsers));

        try {
            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);
            agent.deleteUsers(selectedUsers);  // 수정!!!
        } catch (Exception ex) {
            log.error("delete_user.do : 예외 = {}", ex);
        }

        return "redirect:/admin_menu";
    }

    private List<String> getUserList() {
        String cwd = ctx.getRealPath(".");
        UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);
        List<String> userList = agent.getUserList();
        log.debug("userList = {}", userList);

        //(주의) root.id와 같이 '.'을 넣으면 안 됨.
        userList.sort((e1, e2) -> e1.compareTo(e2));
        return userList;
    }

    @GetMapping("/img_test")
    public String imgTest() {
        return "img_test/img_test";
    }

    /**
     * https://34codefactory.wordpress.com/2019/06/16/how-to-display-image-in-jsp-using-spring-code-factory/
     *
     * @param imageName
     * @return
     */
    @RequestMapping(value = "/get_image/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageName) {
        try {
            String folderPath = ctx.getRealPath("/WEB-INF/views/img_test/img");
            return getImageBytes(folderPath, imageName);
        } catch (Exception e) {
            log.error("/get_image 예외: {}", e.getMessage());
        }
        return new byte[0];
    }

    private byte[] getImageBytes(String folderPath, String imageName) {
        ByteArrayOutputStream byteArrayOutputStream;
        BufferedImage bufferedImage;
        byte[] imageInByte;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bufferedImage = ImageIO.read(new File(folderPath + File.separator + imageName));
            String format = imageName.substring(imageName.lastIndexOf(".") + 1);
            ImageIO.write(bufferedImage, format, byteArrayOutputStream);
            byteArrayOutputStream.flush();
            imageInByte = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return imageInByte;
        } catch (FileNotFoundException e) {
            log.error("getImageBytes 예외: {}", e.getMessage());
        } catch (Exception e) {
            log.error("getImageBytes 예외: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 회원가입 이동
     *
     * @return 회원가입 페이지
     */
    @GetMapping("/sign_up")
    public String signUp() {
        return "/sign_up";
    }

    /**
     * 회원 가입 수행
     *
     * @param id 아이디
     * @param pw 비밀번호
     * @param check_pw 비밀번호 확인
     * @return
     */
    @PostMapping("/signup.do")
    public String signUpDo(@RequestParam String id, @RequestParam String pw, @RequestParam String check_pw, RedirectAttributes attrs) {
        log.debug("signup.do: id = {}, password = {}, check-password = {}, port = {}",
                id, pw, check_pw, JAMES_CONTROL_PORT);

        String url = "redirect:/";
        try {

            String cwd = ctx.getRealPath(".");
            UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd,
                    ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);

            String passwordErrorMessage = PasswordValidator.validatePassword(pw);

            if (pw.equals(check_pw)) {
                // 비밀번호 유효성(규칙) 검사
                if (passwordErrorMessage == null) {
                    if (agent.addUser(id, pw)) {
                        attrs.addFlashAttribute("msg", String.format("회원가입에 성공하였습니다."));
                    } else {
                        attrs.addFlashAttribute("msg", String.format("이미 사용자가 존재합니다."));
                        url += "sign_up";
                    }
                } else {
                    attrs.addFlashAttribute("msg", passwordErrorMessage);
                    url += "sign_up";
                }
            } else {
                attrs.addFlashAttribute("msg", String.format("비밀번호가 일치하지 않습니다."));
                url += "sign_up";
            }

        } catch (Exception ex) {
            log.error("sign_up.do: 시스템 접속에 실패했습니다. 예외 = {}", ex.getMessage());
        }

        return url;
    }

    /**
     * 관리자 비밀번호 변경 페이지 이동
     */
    @GetMapping("/admin_modify_user")
    public String adminModifyUser(Model model) {
        log.debug("admin_modify_user called");
        return "admin/admin_modify_user";
    }

    /**
     * 사용자 비밀번호 변경 페이지 이동
     */
    @GetMapping("/modify_user")
    public String modifyUser(Model model) {
        log.debug("modify_user called");
        return "/modify_user";
    }

    
    /**
     * 비밀번호 변경 수행
     * @param password 비밀번호
     * @param confirmPassword 비밀번호 확인
     * @return 리다이렉트
     */
    @PostMapping("modify_user.do")
    public String modifyUserDo(@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes attrs) {
        String userId = (String) session.getAttribute("userid");

        String url = userId.equals("admin") ? "redirect:/admin_menu" : "redirect:/main_menu";

        String passwordErrorMessage = PasswordValidator.validatePassword(password);

        String msg = null;
        if (!password.equals(confirmPassword)) {
            msg = "비밀번호와 확인 비밀번호가 일치하지 않습니다.";
            attrs.addFlashAttribute("msg", msg);
        } else {
            // 비밀번호 유효성(규칙) 검사
            if (passwordErrorMessage == null) {
                try {
                    String cwd = ctx.getRealPath(".");
                    UserAdminAgent agent = new UserAdminAgent(JAMES_HOST, JAMES_CONTROL_PORT, cwd, ROOT_ID, ROOT_PASSWORD, ADMINISTRATOR);
                    agent.setPassword(userId, password);
                    msg = String.format("사용자(%s) 비밀번호를 변경하였습니다. 다시 로그인 해주세요", userId);
                    attrs.addFlashAttribute("msg", msg);
                    session.invalidate();
                    url = "redirect:/";  // redirect: 반드시 넣어야만 컨텍스트 루트로 갈 수 있음
                } catch (Exception ex) {
                    msg = String.format("사용자(%s) 비밀번호 변경에 실패하였습니다.", userId);
                    attrs.addFlashAttribute("msg", msg);
                    log.error("modify_user.do : 예외 = {}", ex);
                }
            } else {
                attrs.addFlashAttribute("msg", passwordErrorMessage);
            }
        }
        return url;
    }
}
