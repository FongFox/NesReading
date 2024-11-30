package com.nesreading.service;

import com.nesreading.domain.Book;
import com.nesreading.domain.Cart;
import com.nesreading.domain.CartItem;
import com.nesreading.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

@Service
public class CartService {

    @Autowired
    private BookRepository bookRepository;

    // Thêm sách vào giỏ hàng
    public void addToCart(int bookId, int quantity, HttpSession session) {
        Cart cart = getCart(session);
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        // Kiểm tra xem CartItem với Book này đã tồn tại trong Cart chưa
        CartItem existingItem = cart.getCartItems().stream()
                                    .filter(item -> item.getBook().getId() == book.getId()) // Sửa lỗi tại đây
                                    .findFirst()
                                    .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem(quantity, book, cart);
            cartItem.setPrice(book.getPrice());
            cart.getCartItems().add(cartItem);
        }

        // Cập nhật lại tổng giá trị giỏ hàng và lưu vào session
        updateCartTotal(cart);
        session.setAttribute("cart", cart);
    }

    public void removeItemFromCart(int itemId, HttpSession session) {
        Cart cart = getCart(session); // Lấy giỏ hàng từ session

        // Xóa item chỉ có id tương ứng với itemId
        cart.getCartItems().removeIf(item -> item.getId() == itemId);
        
        // Cập nhật lại tổng giá trị giỏ hàng
        updateCartTotal(cart);

        // Cập nhật lại giỏ hàng trong session để lưu trạng thái mới
        session.setAttribute("cart", cart);
    }

    // Cập nhật tổng giá trị giỏ hàng
    private void updateCartTotal(Cart cart) {
        cart.setTotalPrice(cart.getCartItems().stream()
                              .mapToDouble(CartItem::getTotalPrice)
                              .sum());
    }

    // Phương thức lấy giỏ hàng từ session (nếu chưa có thì tạo mới)
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
