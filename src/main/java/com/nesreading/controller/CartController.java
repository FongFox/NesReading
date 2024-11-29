package com.nesreading.controller;

import com.nesreading.domain.Cart;
import com.nesreading.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // Xử lý yêu cầu thêm sách vào giỏ hàng
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("bookId") int bookId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) {
        cartService.addBookToCart(bookId, quantity, session);
        return "redirect:/view-cart";
    }

    // Phương thức xóa danh mục hàng theo id của nó
    @GetMapping("/cart/remove-item")
    public String removeItem(@RequestParam("itemId") int itemId, HttpSession session) {
        try {
            System.out.println("Cart before removal: " + cartService.getCart(session).getCartItems());
            cartService.removeItemFromCart(itemId, session);
            System.out.println("Cart after removal: " + cartService.getCart(session).getCartItems());
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage()); // Optional error handling
        }
        return "redirect:/view-cart";
    }

    // Phương thức hiển thị giỏ hàng
    @GetMapping("/view-cart")
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        return "/client/shopping-cart";
    }

    @PostMapping("/order-now-bulk")
    public String proceedToCheckout(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);

        // Pass all cart items
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("isSingleBook", false); // Flag to indicate cart checkout
        return "client/checkout";
    }


    // Đảm bảo giỏ hàng luôn có sẵn trong model
    @ModelAttribute("cart")
    public Cart getCart(HttpSession session) {
        return cartService.getCart(session);
    }
}
