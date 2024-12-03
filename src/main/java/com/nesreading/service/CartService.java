package com.nesreading.service;

import com.nesreading.domain.Book;
import com.nesreading.domain.Cart;
import com.nesreading.domain.CartItem;
import com.nesreading.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private BookRepository bookRepository;

    // Add book to the cart
    public void addBookToCart(int bookId, int quantity, HttpSession session) {
        Cart cart = getCart(session);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        // Check stock availability
        if (quantity > book.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        // Check if the book already exists in the cart
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId() == book.getId())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > book.getStock()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock");
            }
            existingItem.setQuantity(newQuantity);
        } else {
            // Add new cart item
            CartItem cartItem = new CartItem(quantity, book, cart);
            cartItem.setPrice(book.getPrice());
            cart.getCartItems().add(cartItem);
        }

        updateCartTotal(cart);
        session.setAttribute("cart", cart);

        System.out.println("Adding item: Book ID = " + bookId + ", Quantity = " + quantity);
        System.out.println("Cart before adding item: " + cart.getCartItems());
        System.out.println("Debugging cart: " + cart.toString());

    }

    // Remove item from the cart
    public void removeItemFromCart(int itemId, HttpSession session) {
        Cart cart = getCart(session); // Retrieve the cart from the session

        System.out.println("Cart before removing item: " + cart.getCartItems());

        // Use removeIf to remove the CartItem with the specified ID
        boolean itemRemoved = cart.getCartItems().removeIf(item -> item.getId() == itemId);

        if (!itemRemoved) {
            throw new IllegalArgumentException("Item not found in the cart.");
        }

        System.out.println("Item removed: " + itemRemoved);

        // Recalculate the total price after removing the item
        cart.setTotalPrice(cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());

        System.out.println("Cart after removing item: " + cart.getCartItems());

        // Save the updated cart back to the session
        session.setAttribute("cart", cart);
        System.out.println("Debugging cart: " + cart.toString());
    }

    // Update total price of the cart
    private void updateCartTotal(Cart cart) {
        cart.setTotalPrice(cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum());
    }

    // Get cart from session, create a new one if not exists
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // Clear the cart after successful checkout
    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }
}
