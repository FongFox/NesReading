package com.nesreading.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.nesreading.model.*;
import com.nesreading.service.OrderService;
import com.nesreading.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nesreading.service.BookService;
import com.nesreading.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientController {
    private final BookService bookService;
    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;

    public ClientController(BookService bookService, CartService cartService, UserService userService, OrderService orderService) {
        this.bookService = bookService;
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
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
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "sort", required = false) String sort) {
        double defaultMinPrice = 0;
        double defaultMaxPrice = 1000000;

        // Xử lý các giá trị rỗng hoặc null
        if (title != null && title.isEmpty())
            title = null;
        if (author != null && author.isEmpty())
            author = null;
        Double minPriceValue = (minPrice != null && minPrice >= 0) ? minPrice : defaultMinPrice;
        Double maxPriceValue = (maxPrice != null && maxPrice > 0) ? maxPrice : defaultMaxPrice;

        // Xử lý sắp xếp
        String sortOption = (sort != null && !sort.isEmpty()) ? sort : "titleAsc";
        Sort sortOrder;
        switch (sortOption) {
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
                sortOrder = Sort.by("title").ascending();
                break;
        }

        Pageable pageable = PageRequest.of(page - 1, 20, sortOrder);
        Page<Book> bookListPage;

        // Gọi hàm lọc hoặc lấy tất cả sách
        if (title != null || author != null || minPriceValue != null || maxPriceValue != null) {
            bookListPage = bookService.handleFetchFilteredBook(pageable, title, author, minPriceValue, maxPriceValue);
        } else {
            bookListPage = bookService.handleFetchAllBooks(pageable);
        }

        // Đưa vào model nếu có giá trị hợp lệ
        model.addAttribute("bookList", bookListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookListPage.getTotalPages());
        model.addAttribute("range",
                IntStream.rangeClosed(1, bookListPage.getTotalPages()).boxed().collect(Collectors.toList()));
        model.addAttribute("sort", sortOption);
        if (title != null)
            model.addAttribute("title", title);
        if (author != null)
            model.addAttribute("author", author);
        if (minPriceValue != null)
            model.addAttribute("minPrice", minPriceValue);
        if (maxPriceValue != null)
            model.addAttribute("maxPrice", maxPriceValue);

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

    // ================== Shopping Cart & Checkout (Start) ====================
    @PostMapping("add-to-cart")
    public String handleAddToCart(
            HttpServletRequest request,
            @RequestParam("bookId") int bookId,
            @RequestParam("quantity") int bookQuantity) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);

        this.cartService.handleAddBookToCart(session ,user, bookId, bookQuantity);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("cart")
    public String getShoppingCartPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);

        Cart cart = this.cartService.handleFetchCartByUser(user);
        if(cart == null) {
            cart = this.cartService.handleCreateCart(user);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("bookTotalPrice", cart.getTotalPrice());
        model.addAttribute("subPrice", cart.getSubPrice());
        model.addAttribute("finalPrice", cart.getFinalPrice());

        return "client/shopping-cart";
    }

    @PostMapping("cart/delete/{id}")
    public String handleDeleteCartItem(@PathVariable int id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        this.cartService.handleDeleteProductFromCartById(id, session);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("cart/delete-all")
    public String handleDeleteAllCartItem(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        this.cartService.handleDeleteAllCartItem(session);
        return "redirect:/cart";
    }

    @PostMapping("checkout")
    public String getCheckoutConfirmPage(
            HttpServletRequest request,
            @RequestParam(name = "cartId") int cartId,
            Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);
        String userFullName = user.getFirstName() + " " + user.getLastName();

        Cart cart = this.cartService.handleFetchCartById(cartId);
        // Reload page if cart not found!
        if(cart == null) {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        Order tempOrder = new Order();
        tempOrder.setReceiverName(userFullName);
        tempOrder.setReceiverPhoneNumber(user.getPhoneNumber());
        tempOrder.setReceiverAddress(user.getAddress());

        model.addAttribute("tempOrder", tempOrder);
        model.addAttribute("cart", cart);
        model.addAttribute("bookTotalPrice", cart.getTotalPrice());
        model.addAttribute("subPrice", cart.getSubPrice());
        model.addAttribute("finalPrice", cart.getFinalPrice());

        return "client/checkout-confirm";
    }

    @PostMapping("place-an-order")
    public String handlePlaceOrder(
            HttpServletRequest request,
            @ModelAttribute("tempOrder") Order requestOrder) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);

        this.orderService.handlePlaceOrder(session ,requestOrder, user);

        return "client/checkout-success";
    }

    @GetMapping("orders")
    public String getUserOrderPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);
        if(user == null) {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        model.addAttribute("orderList", this.orderService.handleFetchAllOrderByUser(user));

        return "client/order";
    }

    @GetMapping("orders/{id}")
    public String getUserOrderDetail(@PathVariable int id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);
        if(user == null) {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        Order order = this.orderService.handleFetchOrderById(id);
        List<OrderItem> orderItemList = order.getOrderItems();

        model.addAttribute("order", order);
        model.addAttribute("orderItemList",orderItemList);
        model.addAttribute("bookTotalPrice",  order.getTotalPrice());
        model.addAttribute("subPrice",  order.getSubPrice());
        model.addAttribute("finalPrice",  order.getFinalPrice());

        return "client/order-detail";
    }

    @PostMapping("/orders/confirm-received")
    public String confirmOrderReceived(@RequestParam("orderId") int orderId, RedirectAttributes redirectAttributes) {
        Order order = orderService.handleFetchOrderById(orderId);
        if (order != null && order.getStatus() == 3) {
            order.setStatus(4); // Đổi trạng thái thành 'Completed'
            orderService.handleSaveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Order has been marked as completed!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Order cannot be updated!");
        }

        return "redirect:/orders/" + orderId;
    }
    // ================== Shopping Cart & Checkout (End) ======================

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

    // ================== Account Page (Start) ====================
    @GetMapping("profile")
    public String getAccountPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("email");
        User user = this.userService.handleFetchUserByEmail(userEmail);
        if(user == null) {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }

        model.addAttribute("user", user);

        return "client/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser, RedirectAttributes redirectAttributes) {
        try {
            userService.handleUpdateUser(updatedUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile.");
        }
        return "redirect:/profile";
    }
    // ================== Account Page (End) ======================
}


// ================================ Old Code =================================
//@GetMapping("/cart")
//    public String viewCart(HttpSession session, Model model) {
//        model.addAttribute("cart", cartService.getCart(session));
//        return "/client/shopping-cart";
//    }
//
//    @PostMapping("add-to-cart")
//    public String addToCart(@RequestParam("bookId") int bookId,
//                            @RequestParam("quantity") int quantity,
//                            HttpSession session) {
//        System.out.println("Received bookId: " + bookId);
//        System.out.println("Received quantity: " + quantity);
//        cartService.addBookToCart(bookId, quantity, session);
//        return "/client/shopping-cart";
//    }
//
//    @GetMapping("/cart/remove-item")
//    public String removeItem(@RequestParam("itemId") int itemId, HttpSession session) {
//        try {
//            System.out.println("Cart before removal: " + cartService.getCart(session).getCartItems());
//            cartService.removeItemFromCart(itemId, session);
//            System.out.println("Cart after removal: " + cartService.getCart(session).getCartItems());
//        } catch (IllegalArgumentException e) {
//            session.setAttribute("error", e.getMessage()); // Optional error handling
//        }
//        return "/client/shopping-cart";
//    }
//
//    @ModelAttribute("cart")
//    public Cart getCart(HttpSession session) {
//        return cartService.getCart(session);
//    }


// For N amount of only one type of book
//@PostMapping("order-now")
//public String orderNow(
//        @RequestParam("bookId") int bookId,
//        @RequestParam("quantity") int quantity,
//        Model model) {
//    System.out.println("Received bookId: " + bookId);
//    System.out.println("Received quantity: " + quantity);
//
//    Book book = bookService.handleFetchBookById(bookId);
//    double totalPrice = book.getPrice() * quantity;
//
//    // Add data to the model
//    model.addAttribute("selectedBook", book.getTitle());
//    model.addAttribute("quantity", quantity);
//    model.addAttribute("totalPrice", totalPrice);
//    model.addAttribute("isSingleItem", true);
//
//    return "client/checkout";
//}
//
//// For the entire shopping cart
//@PostMapping("order-now-bulk")
//public String proceedToCheckout(HttpSession session, Model model) {
//    Cart cart = cartService.getCart(session);
//
//    // Pass all cart items
//    model.addAttribute("cartItems", cart.getCartItems());
//    model.addAttribute("totalPrice", cart.getTotalPrice());
//    model.addAttribute("isSingleItem", false); // Flag to indicate single book order
//    return "client/checkout";
//}

//@PostMapping("add-to-cart")
//public String handleAddToCart(
//        HttpServletRequest request,
//        @RequestParam("bookId") int bookId,
//        @RequestParam("quantity") int quantity
//) {
//    HttpSession session = request.getSession(false);
//    String userEmail = (String) session.getAttribute("email");
//    User user = this.userService.handleFetchUserByEmail(userEmail);
//
//    this.cartService.handleAddBookToCart(user, bookId, quantity);
//
//    String referer = request.getHeader("Referer");
//    return "redirect:" + referer;
//}
//
