package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.*;
import com.example.food_ordering.entities.*;
import com.example.food_ordering.enums.OrderStatus;
import com.example.food_ordering.enums.PaymentStatus;
import com.example.food_ordering.exceptions.AddressNotFoundException;
import com.example.food_ordering.exceptions.PaymentException;
import com.example.food_ordering.repository.*;
import com.example.food_ordering.response.OrderResponseDto;
import com.example.food_ordering.service.*;
import com.example.food_ordering.util.CurrentUserProvider;
import com.example.food_ordering.util.DTOConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressService addressService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;


    @Transactional
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        UserDetailsImpl currentUser = currentUserProvider.getCurrentUserDetails();

        User managedUser = userRepository.findById(currentUser.user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BasketDto basket = basketService.findBasketByUserId();

        if (basket == null || basket.getItems().isEmpty()) {
            throw new IllegalArgumentException("Basket is empty or not found");
        }

        // 1️⃣ Sipariş oluştur
        Order order = initializeOrder(orderDto);

        // 2️⃣ Sepet ürünlerini siparişe çevir
        List<OrderItem> orderItems =
                basket.getItems()
                        .stream()
                .map(basketItem -> convertBasketItemToOrderItem(dtoConverter.toBasketItemEntity(basketItem), order))
                .collect(Collectors.toList());

        order.setItems(orderItems);

        for (OrderItem orderItem : orderItems) {
            order.setRestaurant(orderItem.getProduct().getRestaurant());

            orderItem.setTaxRate(5.0);
            orderItem.setDiscountPercentage(5.0);
            orderItem.getTotalPrice();
        }



        Address address;
        if (orderDto.getAddressId() != null && orderDto.getAddressId() > 0) {
            address = addressRepository.findById(orderDto.getAddressId())
                    .orElseThrow(() -> new AddressNotFoundException("Address not found with ID: " + orderDto.getAddressId()));
        } else {

            address = new Address();
            address.setAddressLine1(orderDto.getShippingAddress().getAddressLine1());
            address.setAddressLine2(orderDto.getShippingAddress().getAddressLine2());
            address.setUser(managedUser);
            address.setCity(orderDto.getShippingAddress().getCity());
            address.setState(orderDto.getShippingAddress().getState());
            address.setZipCode(orderDto.getShippingAddress().getZipCode());
            address.setCountry(orderDto.getShippingAddress().getCountry());
            address.setFirstName(orderDto.getShippingAddress().getFirstName());
            address.setLastName(orderDto.getShippingAddress().getLastName());
            address = addressRepository.save(address);
        }

        order.setShippingAddress(address);
        order.setTotalAmount(basket.getGrandTotal());

        Order savedOrder = orderRepository.save(order);

        if (savedOrder.getId() == null) {
            throw new RuntimeException("Order was not saved successfully");
        }
        System.out.println("Saved Order ID: " + savedOrder.getId());

        List<PaymentDto> paymentDtos = orderDto.getPayments();
        boolean paymentSuccessful = true;

        if (paymentDtos != null && !paymentDtos.isEmpty()) {
            for (PaymentDto paymentDto : paymentDtos) {
                boolean success = paymentService.processPayment(paymentDto, dtoConverter.mapToOrderDto(savedOrder));
                if (!success) {
                    paymentSuccessful = false;
                    break;
                }
            }
        }

        if (paymentSuccessful) {
            savedOrder.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(savedOrder);
            basketService.clearBasket();
        } else {
            savedOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(savedOrder);
            throw new PaymentException("Payment failed, order not completed");
        }
        return orderDto;
    }

    private OrderItem convertBasketItemToOrderItem(BasketItem basketItem,Order order) {
        System.out.println("Converting BasketItem to OrderItem");
        System.out.println("BasketItem Unit Price: " + basketItem.getUnitPrice());
        System.out.println("BasketItem Total Price: " + basketItem.getTotalPrice());

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(basketItem.getProduct());
        orderItem.setQuantity(basketItem.getQuantity());
        orderItem.setUnitPrice(basketItem.getUnitPrice());
        orderItem.setTotalPrice(basketItem.getTotalPrice());

        return orderItem;
    }

    private Order initializeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());
        order.setOrderReferenceNumber(UUID.randomUUID().toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        order.setUser(userDetails.user);
        order.setBasket(userDetails.user.getBasket());




        return order;
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        return null;
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String status) {
        return null;
    }


    @Override
    public List<OrderResponseDto> getUserOrders() {
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();

        List<Order> orders = orderRepository.findOrdersByUserId(userDetails.user.getId());

        if (orders == null) {
            throw new RuntimeException("No orders found for user: " + userDetails.getUsername());
        }

        List<OrderDto> orderDtos =
                orders.stream().map(orderDto -> dtoConverter.mapToOrderDto(orderDto)).toList();

        List<OrderResponseDto> orderResponseDtos =
                orderDtos.stream()
                        .map(orderDto -> dtoConverter.toOrderResponseDto(orderDto)).toList();

        return orderResponseDtos;
    }

    @Override
    public OrderResponseDto getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("No orders found for user: " + orderId);
        }
        OrderDto orderDto = dtoConverter.mapToOrderDto(order);
        return dtoConverter.toOrderResponseDto(orderDto);
    }
}





















