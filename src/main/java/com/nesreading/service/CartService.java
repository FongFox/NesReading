package com.nesreading.service;

import com.nesreading.model.Book;
import com.nesreading.model.Cart;
import com.nesreading.model.CartItem;
import com.nesreading.model.User;
import com.nesreading.repository.CartItemRepository;
import com.nesreading.repository.CartRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final BookService bookService;
    private final UserService userService;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, BookService bookService, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public Cart handleCreateCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setSum(0);
        newCart.setTotalPrice(0);
        newCart.setSubPrice(0);
        newCart.setFinalPrice(0);
        return this.cartRepository.save(newCart);
    }

    public void handleAddBookToCart(HttpSession session ,User user, int bookId, int bookQuantity) {
        Book book = this.bookService.handleFetchBookById(bookId);

        Cart cart = this.cartRepository.findByUser(user);
        if (cart == null) { cart = this.handleCreateCart(user); }

        CartItem cartItem = this.cartItemRepository.findByCartAndBook(cart, book);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setBookPrice(book.getPrice());
            cartItem.setQuantity(bookQuantity);
// ================================ Old Code =================================
//            cartItem.setTotalPrice(cartItem.calculateTotalPrice());
//            cart.setSum(cart.getSum() + 1);
//            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getTotalPrice());
//            cart.handleCalculateSubAndFinalPrice(cart.getTotalPrice());
//            session.setAttribute("cartSum", cart.getSum());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
// ================================ Old Code =================================
//            cartItem.setTotalPrice(cartItem.calculateTotalPrice());
//            cart.setTotalPrice(cart.getTotalPrice() + cartItem.getTotalPrice());
        }

        cartItem.setTotalPrice(cartItem.calculateTotalPrice());
        this.cartItemRepository.save(cartItem);

        double newTotalPrice = 0;
        for (CartItem item : cart.getCartItems()) {
            newTotalPrice += item.getTotalPrice();
        }
        cart.setTotalPrice(newTotalPrice);
        cart.handleCalculateSubAndFinalPrice(newTotalPrice);
        cart.setSum(cart.getCartItems().size());

        session.setAttribute("cartSum", cart.getSum());
        this.cartRepository.save(cart);
    }

    public Cart handleFetchCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void handleDeleteProductFromCartById(int id, HttpSession session) {
        User user = this.userService.handleFetchUserByEmail((String) session.getAttribute("email"));
        Cart cart = this.cartRepository.findByUser(user);

        CartItem cartItem = this.cartItemRepository.findById(id).orElse(null);
        if (cartItem != null) {
            this.cartItemRepository.delete(cartItem);

            double newTotalPrice = 0;
            for (CartItem item : cart.getCartItems()) {
                newTotalPrice += item.getTotalPrice();
            }
            cart.setTotalPrice(newTotalPrice);
            cart.handleCalculateSubAndFinalPrice(newTotalPrice);
            cart.setSum(cart.getCartItems().size());

            session.setAttribute("cartSum", cart.getSum());
            this.cartRepository.save(cart);
        }
    }

    public void handleDeleteAllCartItem(HttpSession session) {
        User user = this.userService.handleFetchUserByEmail((String) session.getAttribute("email"));
        Cart cart = this.cartRepository.findByUser(user);

        this.cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();

        cart.setSum(0);
        cart.setTotalPrice(0);
        cart.setSubPrice(0);
        cart.setFinalPrice(0);

        session.setAttribute("cartSum", 0);

        this.cartRepository.save(cart);
// ================================ Old Code =================================
//        this.cartItemRepository.deleteAll();
//        if(!this.cartItemRepository.existsByCart(cart)) {
//            List<CartItem> cartItems = new ArrayList<>();
//            cart.setCartItems(cartItems);
//        }
//        this.cartRepository.delete(cart);
//        Cart newCart = this.handleCreateCart(user);
//        cart.setSum(0);
//        session.setAttribute("cartSum", cart.getSum());
    }

    public Cart handleFetchCartById(int cartId) { return this.cartRepository.findById(cartId).orElse(null); }

    public void clearCart(Cart cart, HttpSession session) {
        this.cartItemRepository.deleteAll(cart.getCartItems());
        this.cartRepository.delete(cart);

        Cart newCart = this.handleCreateCart(cart.getUser());
        session.setAttribute("cartSum", newCart.getSum());
// =========================================== Old Code ================================================
//        User user = this.userService.handleFetchUserByEmail((String) session.getAttribute("email"));
//        cartItemRepository.deleteAll(cart.getCartItems());
//        cart.getCartItems().clear();
//        this.cartRepository.delete(cart);
//        Cart newCart = this.handleCreateCart(user);
//        session.setAttribute("cartSum", newCart.getSum());
//        cartRepository.save(cart);
    }
}

// ======================================== Old Code =======================================
//    // Add book to the cart
//    public void addBookToCart(int bookId, int quantity, HttpSession session) {
//        Cart cart = getCart(session);
//        Book book = bookRepository.findById(bookId).orElse(null);
//
//        if (book == null) {
//            throw new IllegalArgumentException("Book not found");
//        }
//
//        // Check stock availability
//        if (quantity > book.getStock()) {
//            throw new IllegalArgumentException("Requested quantity exceeds available stock");
//        }
//
//        // Check if the book already exists in the cart
//        CartItem existingItem = cart.getCartItems().stream()
//                .filter(item -> item.getBook().getId() == book.getId())
//                .findFirst()
//                .orElse(null);
//
//        if (existingItem != null) {
//            // Update quantity
//            int newQuantity = existingItem.getQuantity() + quantity;
//            if (newQuantity > book.getStock()) {
//                throw new IllegalArgumentException("Requested quantity exceeds available stock");
//            }
//            existingItem.setQuantity(newQuantity);
//        } else {
//            // Add new cart item
//            CartItem cartItem = new CartItem(quantity, book, cart);
//            cartItem.setPrice(book.getPrice());
//            cart.getCartItems().add(cartItem);
//        }
//
//        updateCartTotal(cart);
//        session.setAttribute("cart", cart);
//
//        System.out.println("Adding item: Book ID = " + bookId + ", Quantity = " + quantity);
//        System.out.println("Cart before adding item: " + cart.getCartItems());
//        System.out.println("Debugging cart: " + cart.toString());
//
//    }
//
//    // Remove item from the cart
//    public void removeItemFromCart(int itemId, HttpSession session) {
//        Cart cart = getCart(session); // Retrieve the cart from the session
//
//        System.out.println("Cart before removing item: " + cart.getCartItems());
//
//        // Use removeIf to remove the CartItem with the specified ID
//        boolean itemRemoved = cart.getCartItems().removeIf(item -> item.getId() == itemId);
//
//        if (!itemRemoved) {
//            throw new IllegalArgumentException("Item not found in the cart.");
//        }
//
//        System.out.println("Item removed: " + itemRemoved);
//
//        // Recalculate the total price after removing the item
//        cart.setTotalPrice(cart.getCartItems().stream()
//                .mapToDouble(CartItem::getTotalPrice)
//                .sum());
//
//        System.out.println("Cart after removing item: " + cart.getCartItems());
//
//        // Save the updated cart back to the session
//        session.setAttribute("cart", cart);
////        System.out.println("Debugging cart: " + cart.toString());
//    }
//
//    // Update total price of the cart
//    private void updateCartTotal(Cart cart) {
//        cart.setTotalPrice(cart.getCartItems().stream()
//                .mapToDouble(CartItem::getTotalPrice)
//                .sum());
//    }
//
//    // Get cart from session, create a new one if not exists
//    public Cart getCart(HttpSession session) {
//        Cart cart = (Cart) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new Cart();
//            session.setAttribute("cart", cart);
//        }
//        return cart;
//    }
//
//    // Clear the cart after successful checkout
//    public void clearCart(HttpSession session) {
//        session.removeAttribute("cart");
//    }


//public Cart handleCreateCart(User user) {
//    Cart newCart = new Cart();
//    newCart.setUser(user);
//    newCart.setSum(0);
//
//    return this.cartRepository.save(newCart);
//}
//
//public void handleAddBookToCart(User user, int bookId, int quantity) {
//    // Find cart by user
//    Cart cart = this.handleFetchCartByUser(user);
//    if (cart == null) {
//        cart = this.handleCreateCart(user);
//    }
//
//    // Find Book by id
//    Book book = this.bookService.handleFetchBookById(bookId);
//    if (book != null) {
//        CartItem cartItem = this.cartItemRepository.findByCartAndBook(cart, book);
//
//        if (cartItem == null) {
//            cartItem = new CartItem();
//            cartItem.setCart(cart);
//            cartItem.setBook(book);
//            cartItem.setBookPrice(book.getPrice());
//            cartItem.setQuantity(1);
//            cartItem.setTotalPrice(cartItem.calculateTotalPrice());
//        } else {
//            cartItem.setQuantity(cartItem.getQuantity() + 1);
//            cartItem.setTotalPrice(cartItem.calculateTotalPrice());
//        }
//
//        this.cartItemRepository.save(cartItem);
//    }
//}
//
//public Cart handleFetchCartByUser(User user) {
//    return this.cartRepository.findByUser(user);
//}
