package com.nesreading.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nesreading.domain.Book;
import com.nesreading.domain.Cart;
import com.nesreading.service.BookService;
import com.nesreading.service.CartService;

import jakarta.servlet.http.HttpSession;

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
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        return "/client/shopping-cart";
    }

    @PostMapping("add-to-cart")
    public String addToCart(@RequestParam("bookId") int bookId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) {
        System.out.println("Received bookId: " + bookId);
        System.out.println("Received quantity: " + quantity);
        cartService.addBookToCart(bookId, quantity, session);
        return "/client/shopping-cart";
    }

    @GetMapping("/cart/remove-item")
    public String removeItem(@RequestParam("itemId") int itemId, HttpSession session) {
        try {
            System.out.println("Cart before removal: " + cartService.getCart(session).getCartItems());
            cartService.removeItemFromCart(itemId, session);
            System.out.println("Cart after removal: " + cartService.getCart(session).getCartItems());
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage()); // Optional error handling
        }
        return "/client/shopping-cart";
    }

    @ModelAttribute("cart")
    public Cart getCart(HttpSession session) {
        return cartService.getCart(session);
    }
    // ================== Shopping Cart (End) ======================

    // ================== Checkout (Start) ====================

    // For N amount of only one type of book
    @PostMapping("order-now")
    public String orderNow(
            @RequestParam("bookId") int bookId,
            @RequestParam("quantity") int quantity,
            Model model) {
        System.out.println("Received bookId: " + bookId);
        System.out.println("Received quantity: " + quantity);

        Book book = bookService.handleFetchBookById(bookId);
        double totalPrice = book.getPrice() * quantity;

        // Add data to the model
        model.addAttribute("selectedBook", book.getTitle());
        model.addAttribute("quantity", quantity);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("isSingleItem", true);

        return "client/checkout";
    }

    // For the entire shopping cart
    @PostMapping("order-now-bulk")
    public String proceedToCheckout(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);

        // Pass all cart items
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("isSingleItem", false); // Flag to indicate single book order
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
    public String getBookDetailPage() {
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
