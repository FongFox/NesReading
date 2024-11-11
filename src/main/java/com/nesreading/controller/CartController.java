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
        cartService.addToCart(bookId, quantity, session);
        return "redirect:/view-cart";
    }

    @GetMapping("/cart/remove-item")
    public String removeItem(@RequestParam("itemId") int itemId, HttpSession session) {
        cartService.removeItemFromCart(itemId, session);
        return "redirect:/view-cart";
    }

    // Phương thức hiển thị giỏ hàng
    @GetMapping("/view-cart")
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        return "/client/shopping-cart";
    }

    // Đảm bảo giỏ hàng luôn có sẵn trong model
    @ModelAttribute("cart")
    public Cart getCart(HttpSession session) {
        return cartService.getCart(session);
    }
}
