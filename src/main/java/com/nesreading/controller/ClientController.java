package com.nesreading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nesreading.service.BookService;
import com.nesreading.service.UserService;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ClientController {
    private final BookService bookService;

    public ClientController(BookService bookService) {
        this.bookService = bookService;
    }

    // ================== Home (Start) ====================
    @GetMapping("")
    public String getHomePage() {
      return "client/home";
    }
    // ================== Home (End) ======================

    // ================== Book (Start) ====================
    @GetMapping("shop")
    public String getBookListPage(Model model) {
        model.addAttribute("bookList", bookService.handleFetchAllBooks());
        return "client/shop";
    }

    @GetMapping("shop/{id}")
    public String getBookDetailPage(@PathVariable int id, Model model) {
        if(bookService.handleCheckBookExist(id)) {
            return "redirect:/shop";
        }

        model.addAttribute("dbBook", bookService.handleFetchBookById(id));
        return "client/book-detail";
    }
    // ================== Book (End) ======================

    // ================== Shopping Cart (Start) ====================
    @GetMapping("carts")
    public String getShoppingCartPage() {
      return "client/shopping-cart";
    }
    // ================== Shopping Cart (End) ======================

    // ================== Checkout (Start) ====================
    @GetMapping("checkout")
    public String getCheckoutPage() {
      return "client/checkout";
    }

    @PostMapping("checkout")
    public String handleCheckout() {
      return "redirect:/checkout/success";
    }

    @GetMapping("checkout/success")
    public String getCheckoutSuccessPage() {
      return "client/checkout-success";
    }
    // ================== Checkout (End) ======================
    
    // ================== About (Start) ====================
    @GetMapping("about")
    public String getAboutPage() {
      return "client/about";
    }
    // ================== About (End) ======================

    // ================== Contact (Start) ====================
    @GetMapping("contact")
    public String getContactPage() {
      return "client/contact";
    }
    // ================== Contact (End) ======================
}
