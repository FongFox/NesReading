package com.nesreading.service;

import com.nesreading.model.*;
import com.nesreading.repository.OrderItemRepository;
import com.nesreading.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final CartService cartService;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.userService = userService;
    }

    public void handlePlaceOrder(HttpSession session, Order order , User user) {
        Cart cart = user.getCart();

        order.setStatus(0);
        order.setUser(user);
        this.orderRepository.save(order);

        // convert cart item into order item
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setBookPrice(cartItem.getBookPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(orderItem.calculateTotalPrice());

            order.setTotalPrice(order.getTotalPrice() + orderItem.getTotalPrice());
            order.handleCalculateSubAndFinalPrice(order.getTotalPrice());

            this.orderItemRepository.save(orderItem);
        }

        this.orderRepository.save(order);
        this.cartService.clearCart(cart, session);
    }
}
