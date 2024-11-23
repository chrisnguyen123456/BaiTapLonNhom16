package org.example.baitapbig.controller;

import jakarta.servlet.http.HttpSession;
import org.example.baitapbig.model.Account;
import org.example.baitapbig.model.Category;
import org.example.baitapbig.model.Book;
import org.example.baitapbig.service.CategoryService;
import org.example.baitapbig.service.BookService;
import org.example.baitapbig.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            Account userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
//            Integer countCart = cartService.getCountCart(userDtls.getId());
//            m.addAttribute("countCart", countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllCategory();
        m.addAttribute("categorys", allActiveCategory);
    }
    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddBook(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/addproduct";
    }

    @GetMapping("/category")
    public String loadCategory(Model model) {
        model.addAttribute("categorys", categoryService.getAllCategory());
        return "admin/Category";
    }
    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/editcategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                               HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());

        if (existCategory) {
            session.setAttribute("errorMsg", "Category Name already exists");
        } else {

            Category saveCategory = categoryService.saveCategory(category);

            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "Not saved ! internal server error");
            } else {

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("succMsg", "Saved successfully");
            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        Boolean deleteCategory = categoryService.deleteCategory(id);

        if (deleteCategory) {
            session.setAttribute("succMsg", "category delete success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {

            oldCategory.setName(category.getName());
            oldCategory.setImageName(imageName);
        }

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if (!ObjectUtils.isEmpty(updateCategory)) {

            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
                        + file.getOriginalFilename());

                // System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("succMsg", "Category update success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/category";
    }

    @PostMapping("/saveProduct")
    public String saveBook(@ModelAttribute Book book, @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        book.setImage(imageName);
        Book saveBook = bookService.saveBook(book);

        if (!ObjectUtils.isEmpty(saveBook)) {

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                    + image.getOriginalFilename());

            System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product Saved Success");
        }
        else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewBook(Model m) {
        m.addAttribute("products", bookService.getAllBooks());
        return "admin/Product";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteBook(@PathVariable int id, HttpSession session) {
        Boolean deleteBook = bookService.deleteBook(id);
        if (deleteBook) {
            session.setAttribute("succMsg", "Product delete success");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editBook(@PathVariable int id, Model m) {
        Book product = bookService.getBookById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        m.addAttribute("product", product);
        m.addAttribute("categories", categoryService.getAllCategory());
        return "admin/editproduct";
    }


    @PostMapping("/updateProduct")
    public String updateBook(@ModelAttribute Book book, @RequestParam("file") MultipartFile image,
                                HttpSession session, Model m) {
        Book updateBook = bookService.updateBook(book, image);
        if (!ObjectUtils.isEmpty(updateBook)) {
            session.setAttribute("succMsg", "Product update success");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }

        return "redirect:/admin/products";
    }
    @GetMapping("/users")
    public String getAllUsers(Model m) {
        List<Account> users = userService.findByRole("ROLE_USER");
        m.addAttribute("users", users);
        return "/admin/users";
    }
    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id, HttpSession session) {
        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            session.setAttribute("succMsg", "Account Status Updated");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/users";
    }
}
