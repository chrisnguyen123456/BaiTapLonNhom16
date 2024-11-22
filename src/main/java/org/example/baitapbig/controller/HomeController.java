package org.example.baitapbig.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.baitapbig.model.Account;
import org.example.baitapbig.model.Category;
import org.example.baitapbig.model.Book;
import org.example.baitapbig.service.CartService;
import org.example.baitapbig.service.CategoryService;
import org.example.baitapbig.service.BookService;
import org.example.baitapbig.service.UserService;
import org.example.baitapbig.until.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
@Controller
public class HomeController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
//    @Autowired
//    private CartService cartService;



    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", bookService.getAllBooks());
        return "index";
    }
    @GetMapping("/signin")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/product/{id}")
    public String bookDetail(@PathVariable int id , Model model) {
        model.addAttribute("product", bookService.getBookById(id));
        return "view_product";
    }
    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            Account userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);

        }

        List<Category> allActiveCategory = categoryService.getAllCategory();
        m.addAttribute("categorys", allActiveCategory);
    }
    @GetMapping("/products")
    public String books(Model m, @RequestParam(value = "category", defaultValue = "") String category) {

        List<Category> categories = categoryService.getAllCategory();
        List<Book> products = bookService.getAllBooks();
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);
        m.addAttribute("paramValue", category);
        return "product";
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute Account user, @RequestParam("img") MultipartFile file, HttpSession session, HttpServletRequest request)
            throws IOException {

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        Account saveUser = userService.saveUser(user);

        // send mail code verification set isEnable=true
        String resetToken = UUID.randomUUID().toString();
        userService.updateUserResetToken(user.getEmail(), resetToken);
        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                        + file.getOriginalFilename());

//				System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Register successfully");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/register";
    }
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        Account userByEmail = userService.getUserByEmail(email);

        if (ObjectUtils.isEmpty(userByEmail)) {
            session.setAttribute("errorMsg", "Invalid email");
        } else {

            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);

            // Generate URL :
            // http://localhost:8080/reset-password?token=sfgdbgfswegfbdgfewgvsrg

            String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

            Boolean sendMail = commonUtil.sendMail(url, email);

            if (sendMail) {
                session.setAttribute("succMsg", "Please check your email..Password Reset link sent");
            } else {
                session.setAttribute("errorMsg", "Somethong wrong on server ! Email not send");
            }
        }

        return "redirect:/forgot-password";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, HttpSession session, Model m) {

        Account userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            m.addAttribute("msg", "Your link is invalid or expired !!");
            return "message";
        }
        userByToken.setIsEnable(true);
        userByToken.setResetToken(null);
        userService.updateUser(userByToken);
        m.addAttribute("msg", "Email verified successfully");
        return "message";
    }

    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String token, HttpSession session, Model m) {

        Account userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            m.addAttribute("msg", "Your link is invalid or expired !!");
            return "message";
        }
        m.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
                                Model m) {

        Account userByToken = userService.getUserByToken(token);
        if (userByToken == null) {
            m.addAttribute("errorMsg", "Your link is invalid or expired !!");
            return "message";
        } else {
            userByToken.setPassword(passwordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);
            // session.setAttribute("succMsg", "Password change successfully");
            m.addAttribute("msg", "Password change successfully");

            return "message";
        }

    }

}
