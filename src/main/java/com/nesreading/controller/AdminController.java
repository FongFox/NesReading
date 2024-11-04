package com.nesreading.controller;

import com.nesreading.domain.Book;
import com.nesreading.domain.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.nesreading.service.BookService;
import com.nesreading.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
	private final UserService userService;
	private final BookService bookService;

	public AdminController(UserService userService, BookService bookService) {
		this.userService = userService;
		this.bookService = bookService;
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
	public String handleCreateUser(
			@ModelAttribute("newUser") @Valid User newUser,
			BindingResult newUserBindingResult,
			@RequestParam("newUserFile") MultipartFile file
	) {
		List<FieldError> errors = newUserBindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			System.out.println (">>> " + error.getField() + " - " + error.getDefaultMessage());
		}

		// validate
		if (newUserBindingResult.hasErrors()) {
			return "admin/user/create";
		}

		userService.handleCreateUser(newUser, file);

		return "redirect:/admin/users";
	}

	@GetMapping("users/{id}")
	public String getAdminUserDetailPage(@PathVariable int id, Model model) {
		if(userService.handleCheckExistedUser(id)) {
			return "redirect:/admin/users";
		}

		User dbUser = userService.handleFetchUserById(id);
		model.addAttribute("dbUser", dbUser);

		return "admin/user/detail";
	}

	@GetMapping("users/update/{id}")
	public String getAdminUserUpdatePage(@PathVariable int id, Model model) {
		if(userService.handleCheckExistedUser(id)) {
			return "redirect:/admin/users";
		}

		User dbUser = userService.handleFetchUserById(id);
		model.addAttribute("tempUser", dbUser);

		return "admin/user/update";
	}

	@PostMapping("users/update")
	public String handleUpdateUser(
			@ModelAttribute("tempUser") @Valid User tempUser,
			BindingResult tempUserBindingResult,
			@RequestParam("tempUserFile") MultipartFile file
	) {
		List<FieldError> errors = tempUserBindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			System.out.println (">>> " + error.getField() + " - " + error.getDefaultMessage());
		}

		// validate
		if (tempUserBindingResult.hasErrors()) {
			if (tempUser.getPassword() == null || tempUser.getPassword().isEmpty()) {
				userService.handleUpdateUser(tempUser, file);
			} else {
				return "admin/user/update";
			}
		}

		return "redirect:/admin/users";
	}

	@PostMapping("users/delete/{id}")
	public String handleDeleteUser(@PathVariable int id) {
		userService.handleDeleteUserById(id);
		return "redirect:/admin/users";
	}
	// ================== User (End) ======================

	// ================== Book (Start) ====================
	@GetMapping("books")
	public String getAdminBookViewPage(Model model) {
		model.addAttribute("bookList", bookService.handleFetchAllBooks());

		return "admin/book/view";
	}

	@GetMapping("books/create")
	public String getAdminBookCreatePage(Model model) {
		model.addAttribute("newBook", new Book());

		return "admin/book/create";
	}

	@PostMapping("books/create")
	public String handleCreateBook(
			@ModelAttribute("newBook") @Valid Book newBook,
			BindingResult newBookbindingResult,
			@RequestParam("newBookFile") MultipartFile file
	) {
		List<FieldError> errors = newBookbindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			System.out.println (">>> " + error.getField() + " - " + error.getDefaultMessage());
		}

		// validate
		if (newBookbindingResult.hasErrors()) {
			return "admin/book/create";
		}

		bookService.handleCreateBook(newBook, file);

		return "redirect:/admin/books";
	}

	@GetMapping("books/{id}")
	public String getAdminBookDetailPage(@PathVariable int id, Model model) {
		if(bookService.handleCheckBookExist(id)) {
			return "redirect:/admin/books";
		}

		model.addAttribute("dbBook", bookService.handleFetchBookById(id));

		return "admin/book/detail";
	}

	@GetMapping("books/update/{id}")
	public String getAdminBookUpdatePage(@PathVariable int id, Model model) {
		if(bookService.handleCheckBookExist(id)) {
			return "redirect:/admin/books";
		}

		model.addAttribute("tempBook", bookService.handleFetchBookById(id));

		return "admin/book/update";
	}

	@PostMapping("books/update")
	public String handleUpdateBook(
			@ModelAttribute("tempBook") @Valid Book tempBook,
			BindingResult tempBookbindingResult,
			@RequestParam("tempBookFile") MultipartFile file) {
//		System.out.println("Test: " + tempBook.toString());
		List<FieldError> errors = tempBookbindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			System.out.println (">>> " + error.getField() + " - " + error.getDefaultMessage());
		}

		// validate
		if (tempBookbindingResult.hasErrors()) {
			return "admin/book/update";
		}

		bookService.handleUpdateBook(tempBook, file);

		return "redirect:/admin/books";
	}

	@PostMapping("books/delete/{id}")
	public String handleDeleteBook(@PathVariable int id) {
		bookService.handleDeleteBookById(id);

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
