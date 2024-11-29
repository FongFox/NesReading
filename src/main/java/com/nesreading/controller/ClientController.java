package com.nesreading.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.nesreading.domain.Cart;
import com.nesreading.domain.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nesreading.domain.Book;
import com.nesreading.service.BookService;
import com.nesreading.service.CartService;

@Controller
public class ClientController {
    private final BookService bookService;
    private final CartService cartService;

    public ClientController(BookService bookService, CartService cartService) {
        this.bookService = bookService;
        this.cartService = cartService;
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
    public String getBookListPage(
            Model model,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "sort", required = false) String sort) {
        String sortOption = sort == null ? "titleAsc" : sort;
        Sort sortOrder;
        switch (sortOption) {
            case "titleAsc":
                sortOrder = Sort.by("title").ascending();
                break;
            case "titleDesc":
                sortOrder = Sort.by("title").descending();
                break;
            case "priceAsc":
                sortOrder = Sort.by("price").ascending();
                break;
            case "priceDesc":
                sortOrder = Sort.by("price").descending();
                break;
            default:
                sortOrder = Sort.unsorted();
                break;
        }

        if (title != null && title.isEmpty()) title = null;
        if (author != null && author.isEmpty()) author = null;
        Double minPriceValue = (minPrice != null && !minPrice.isNaN()) ? Double.valueOf(minPrice) : null;
        Double maxPriceValue = (maxPrice != null && !maxPrice.isNaN()) ? Double.valueOf(maxPrice) : null;
        
        Pageable pageable = PageRequest.of(page - 1, 20, sortOrder);
        Page<Book> bookListPage;

        if (title != null || author != null || minPrice != null || maxPrice != null) {
            bookListPage = bookService.handleFetchFilteredBook(pageable, title, author, minPrice, maxPrice);
        } else {
            bookListPage = bookService.handleFetchAllBooks(pageable);
        }

        List<Book> bookList = bookListPage.getContent();
        int bookListTotalPage = bookListPage.getTotalPages();

        model.addAttribute("bookList", bookList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookListTotalPage);
        model.addAttribute("range", IntStream.rangeClosed(1, bookListTotalPage).boxed().collect(Collectors.toList()));
        model.addAttribute("sort", sort); // Truyền giá trị sort vào model để Thymeleaf sử dụng

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
    // @GetMapping("carts")
    // public String getShoppingCartPage() {
    //   return "client/shopping-cart";
    // }

    // @PostMapping("add-book-to-cart/{id}")
    // public String addBookToCart(@PathVariable long id) {
    //   return "redirect:/shop";
    // }
    // ================== Shopping Cart (End) ======================

    // ================== Checkout (Start) ====================
    @GetMapping("add-to-cart")
    public String addToCart(
            @RequestParam("bookId") int bookId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {
        try {
            cartService.addBookToCart(bookId, quantity, session);
            return "redirect:/shop";
        } catch (IllegalArgumentException e) {
            // Handle errors such as book not found or insufficient stock
            session.setAttribute("error", e.getMessage());
            return "redirect:/shop";
        }
    }

    // For N amount of only one type of book
    @PostMapping("order-now")
    public String orderNow(
            @RequestParam("bookId") int bookId,
            @RequestParam("quantity") int quantity,
            Model model) {
        Book book = bookService.handleFetchBookById(bookId);
        double totalPrice = book.getPrice() * quantity;

        // Pass only the selected book and quantity
        model.addAttribute("selectedBook", book.getTitle());
        model.addAttribute("quantity", quantity);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("isSingleBook", true); // Flag to indicate single book order
        return "client/checkout";
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
