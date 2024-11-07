package com.nesreading.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nesreading.domain.Book;
import com.nesreading.service.BookService;

@Controller
public class ClientController {
    private final BookService bookService;

    public ClientController(BookService bookService) {
        this.bookService = bookService;
    }

    // ================== Home (Start) ====================
    @GetMapping("")
    public String getHomePage(Model model) {
        model.addAttribute("bestSellerBookList", bookService.handleFetchBestSellerBooks());
        model.addAttribute("featureBookList", bookService.handleFetchRandomBooks());

        return "client/home";
    }
    // ================== Home (End) ======================

    // ================== Book (Start) ====================
    @GetMapping("shop")
    public String getBookListPage(Model model, @RequestParam("page") int page) {
        Pageable pageable = PageRequest.of(page - 1, 20);
        Page<Book> bookListPage = bookService.handleFetchAllBooks(pageable);
        List<Book> bookList = bookListPage.getContent();
        int bookListTotalPage = bookListPage.getTotalPages();

        model.addAttribute("bookList", bookList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookListTotalPage);
        model.addAttribute("range",
                IntStream.rangeClosed(1, bookListTotalPage).boxed().collect(Collectors.toList()));

        return "client/shop";
    }

    @GetMapping("shop/{id}")
    public String getBookDetailPage(@PathVariable int id, Model model) {
        if (bookService.handleCheckBookExist(id)) {
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

    // ================== Book-Detail (Start) ====================
    @GetMapping("book-detail")
    public String getBookdetailPage() {
        return "client/book-detail";
    }
    // ================== Book-Detail (End) ======================

    // ================== Blog (Start) ====================
    @GetMapping("blog")
    public String getBlogPage() {
        return "client/blog";
    }
    // ================== Blog (End) ======================

    // ================== Post (Start) ====================
    @GetMapping("post")
    public String getPostPage() {
        return "client/post";
    }
    // ================== Post (End) ======================
}
