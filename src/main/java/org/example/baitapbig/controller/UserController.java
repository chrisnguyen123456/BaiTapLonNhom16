package org.example.baitapbig.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.baitapbig.model.*;
import org.example.baitapbig.service.CartService;
import org.example.baitapbig.service.CategoryService;
import org.example.baitapbig.service.OrderService;
import org.example.baitapbig.service.UserService;
import org.example.baitapbig.until.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @GetMapping("/success")
    public String success() {
        return "user/success";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            Account userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            m.addAttribute("countCart", countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {

        Account user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    private Account getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        Account userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m) {
        Account user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        m.addAttribute("user", user);

        // Kiểm tra và xử lý tên
        String[] nameParts = user.getName().split(" ");
        if (nameParts.length > 1) {
            m.addAttribute("firstName", nameParts[0]);
            m.addAttribute("lastName", nameParts[nameParts.length - 1]); // Lấy họ là phần cuối cùng
        } else {
            m.addAttribute("firstName", nameParts[0]); // Tên đầy đủ
            m.addAttribute("lastName", ""); // Không có họ
        }

        // Tính toán các giá trị liên quan đến carts
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);

            Long summary = carts.stream().mapToLong(c -> c.getTotalPrice().longValue()).sum();
            m.addAttribute("summary", summary);

            Double totalOrder = totalOrderPrice + 5 + 10;
            m.addAttribute("totalOrder", totalOrder);
        }

        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p, HttpServletRequest request1)
            throws MessagingException, UnsupportedEncodingException {
        // System.out.println(request);
        Account user = getLoggedInUserDetails(p);
        boolean or = orderService.saveOrder(user.getId(), request);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        double totalOrderPrice = 15;
        if (or) {
            List<BookOrder> order = orderService.getAllOrder(user.getId());
            for (BookOrder o : order) {
                totalOrderPrice += o.getPrice();
            }
            String url = CommonUtil.generateUrl(request1) + "/bill/" + user.getId();
            Boolean sendMail = commonUtil.sendMailOrder(url, user.getEmail(), carts, totalOrderPrice, order);
            if (sendMail) {
                cartService.deleteCartByUser(user.getId());
            }
        }

        return "/user/success";
    }
}
