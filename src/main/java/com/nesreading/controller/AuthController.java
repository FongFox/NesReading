package com.nesreading.controller;

import com.nesreading.domain.User;
import com.nesreading.dto.RegisterDTO;
import com.nesreading.mapper.UserMapper;
import com.nesreading.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // ================== Login (Start) ====================
    @GetMapping("login")
    public String getLoginPage() {
        return "auth/login";
    }
    // ================== Login (End) ======================

    // ================== Register (Start) ====================
    @GetMapping("register")
    public String getRegisterPage(Model model) {
        model.addAttribute("newUser", new RegisterDTO());

        return "auth/register";
    }

    @PostMapping("register")
    public String handleRegister(
            @ModelAttribute("newUser") @Valid RegisterDTO registerDTO,
            BindingResult registerDtoBindingResult) {
        List<FieldError> errors = registerDtoBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>> " + error.getField() + " - " + error.getDefaultMessage());
        }

        // validate
        if (registerDtoBindingResult.hasErrors()) {
            return "auth/register";
        }

        User user = userMapper.registerDtoToUser(registerDTO);

        userService.handleCreateUser(user);

        return "redirect:/login";
    }
    // ================== Register (End) ======================

    @GetMapping("/access-deny")
    public String getAccessDenyPage() {
        return "other/access-deny";
    }
}
