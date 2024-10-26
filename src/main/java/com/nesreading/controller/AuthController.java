package com.nesreading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
  // ================== Login (Start) ====================
  @GetMapping("login")
  public String getLoginPage() {
    return "auth/login";
  }
  // ================== Login (End) ======================

  // ================== Register (Start) ====================
  @GetMapping("register")
  public String getRegisterPage() {
    return "auth/register";
  }
  
  @PostMapping("register")
  public String handleRegister() {
    return "redirect:/login";
  }
  // ================== Register (End) ======================
}
