package com.nesreading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
  // ================== Admin Login (Start) ====================
  @GetMapping("admin/login")
  public String getAdminLoginPage() {
    return "auth/admin-login";
  }

  @PostMapping("admin/login")
  public String handleAdminLogin() {
    return "redirect:/admin/login";
  }
  // ================== Admin Login (End) ======================

  // ================== Client Login (Start) ====================
  @GetMapping("login")
  public String getLoginPage() {
    return "auth/login";
  }

  @PostMapping("login")
  public String handleLogin() {
    return "redirect:/login";
  }
  // ================== Client Login (End) ======================

  // ================== Client Register (Start) ====================
  @GetMapping("register")
  public String getRegisterPage() {
    return "auth/register";
  }
  
  @PostMapping("register")
  public String handleRegister() {
    return "redirect:/login";
  }
  // ================== Client Register (End) ======================
}
