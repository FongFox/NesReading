package com.nesreading.controller;

import com.nesreading.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nesreading.service.AuthorService;
import com.nesreading.service.BookCategoryService;
import com.nesreading.service.BookService;
import com.nesreading.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final UserService userService;
	private final BookService bookService;
	private final BookCategoryService bookCategoryService;
	private final AuthorService authorService;

	public AdminController(UserService userService, BookService bookService, BookCategoryService bookCategoryService,
			AuthorService authorService) {
		this.userService = userService;
		this.bookService = bookService;
		this.bookCategoryService = bookCategoryService;
		this.authorService = authorService;
	}

	//================== Dashboard (Start) ====================
	@GetMapping("")
	public String getAdminDashboardPage() {
		return "admin/dashboard/view";
	}
	//================== Dashboard (End) ======================

	// ================== User (Start) ====================
	@GetMapping("users")
	public String getAdminUserViewPage(Model model) {
		model.addAttribute("userList", userService.handleFetchAllUsers());
		return "admin/user/view";
	}
	
	@GetMapping("users/create")
	public String getAdminUserCreatePage(Model model) {
		model.addAttribute("newUser", new User());
		return "admin/user/create";
	}

	@PostMapping("users/create")
	public String handleCreateUser(@ModelAttribute("newUser") User newUser) {
		userService.handleCreateUser(newUser);
		return "redirect:/admin/users";
	}

	@GetMapping("users/{id}")
	public String getAdminUserDetailPage(@PathVariable int id, Model model) {
		User dbUser = userService.handleFetchUserById(id).orElse(null);

		if (dbUser == null) {
			return "redirect:/admin/users";
		}

		model.addAttribute("dbUser", dbUser);
		return "admin/user/detail";
	}

	@GetMapping("users/update/{id}")
	public String getAdminUserUpdatePage(@PathVariable int id, Model model) {
		User dbUser = userService.handleFetchUserById(id).orElse(null);

		if (dbUser == null) {
			return "redirect:/admin/users";
		}

		model.addAttribute("tempUser", dbUser);
		return "admin/user/update";
	}

	@PostMapping("users/update")
	public String handleUpdateUser(@ModelAttribute("tempUser") User tempUser) {
		userService.handleUpdateUser(tempUser);
		return "redirect:/admin/users";
	}

	@PostMapping("users/delete/{id}")
	public String handleDeleteUser(@PathVariable int id) {
		userService.handleDeleteUserById(id);
		return "redirect:/admin/users";
	}
	// ================== User (End) ======================

	// ================== Book Category (Start) ====================
	@GetMapping("book-categories")
	public String getAdminBookCategoryViewPage() {
		return "admin/book-category/view";
	}

	@GetMapping("book-categories/create")
	public String getAdminBookCategoryCreatePage() {
		return "admin/book-category/create";
	}

	@PostMapping("book-categories/create")
	public String handleCreateBookCategory() {
		return "redirect:/admin/book-categories";
	}

	@GetMapping("book-categories/{id}")
	public String getAdminBookCategoryDetailPage(@PathVariable int id) {
		return "admin/book-category/detail";
	}

	@GetMapping("book-categories/update/{id}")
	public String getAdminBookCategoryUpdatePage(@PathVariable int id) {
		return "admin/book-category/update";
	}

	@PostMapping("book-categories/update")
	public String handleUpdateBookCategory() {
		return "redirect:/admin/book-categories";
	}

	@PostMapping("book-categories/delete/{id}")
	public String handleDeleteBookCategory(@PathVariable int id) {
		return "redirect:/admin/book-categories";
	}
	// ================== Book Category (End) ======================

	// ================== Book (Start) ====================
	@GetMapping("books")
	public String getAdminBookViewPage() {
		return "admin/book/view";
	}

	@GetMapping("books/create")
	public String getAdminBookCreatePage() {
		return "admin/book/create";
	}

	@PostMapping("books/create")
	public String handleCreateBook() {
		return "redirect:/admin/books";
	}

	@GetMapping("books/{id}")
	public String getAdminBookDetailPage(@PathVariable int id) {
		return "admin/book/detail";
	}

	@GetMapping("books/update/{id}")
	public String getAdminBookUpdatePage(@PathVariable int id) {
		return "admin/book/update";
	}

	@PostMapping("books/update")
	public String handleUpdateBook() {
		return "redirect:/admin/books";
	}

	@PostMapping("books/delete/{id}")
	public String handleDeleteBook(@PathVariable int id) {
		return "redirect:/admin/books";
	}
	// ================== Book (End) ======================

	// ================== Order (Start) ====================
	@GetMapping("orders")
	public String getAdminOrderViewPage() {
		return "admin/order/view";
	}

	@GetMapping("orders/{id}")
	public String getAdminOrderDetailPage(@PathVariable int id) {
		return "admin/order/detail";
	}
	// ================== Order (End) ======================
}
