package com.nesreading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ClientController {
    // ================== Home (Start) ====================
    @GetMapping("")
    public String getHomePage() {
      return "client/home";
    }
    // ================== Home (End) ======================

    // ================== Book (Start) ====================
    @GetMapping("shop")
    public String getBookListPage() {
      return "client/shop";
    }

    @GetMapping("shop/{id}")
    public String getBookDetailPage() {
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
