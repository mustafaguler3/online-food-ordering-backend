package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.dto.BasketItemDto;
import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.dto.OrderItemDto;
import com.example.food_ordering.entities.*;
import com.example.food_ordering.enums.OrderStatus;
import com.example.food_ordering.enums.PaymentStatus;
import com.example.food_ordering.exceptions.AddressNotFoundException;
import com.example.food_ordering.exceptions.PaymentException;
import com.example.food_ordering.repository.AddressRepository;
import com.example.food_ordering.repository.OrderRepository;
import com.example.food_ordering.repository.PaymentRepository;
import com.example.food_ordering.response.OrderResponseDto;
import com.example.food_ordering.service.*;
import com.example.food_ordering.util.DTOConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private BasketService basketService;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private AddressRepository addressRepository;


    @Transactional
    @Override
    public void createOrder(OrderDto orderDto) {
        // 1. Kullanıcının aktif sepetini al
        BasketDto basket = basketService.findBasketByUserId();
        if (basket == null || basket.getItems().isEmpty()) {
            throw new IllegalArgumentException("Basket is empty or not found");
        }
        // 2. Sipariş oluşturma
        Order order = initializeOrder(orderDto);
        // 3. Sepet ürünlerini siparişe ekleme
        List<OrderItemDto> orderItems =
                basket.getItems()
                        .stream()
                .map(basketItem ->
                        convertBasketItemToOrderItem(basketItem, order))
                .collect(Collectors.toList());

        order.setItems(dtoConverter.toOrderItemEntityList(orderItems));

        Address address;
        if (orderDto.getAddressId() > 0) {
            address = addressRepository.findById(orderDto.getAddressId())
                    .orElseThrow(() -> new AddressNotFoundException("Address not found with ID: " + orderDto.getAddressId()));
        } else {
            address = new Address();
            address.setAddressLine1(orderDto.getShippingAddress().getAddressLine1());
            address.setAddressLine2(orderDto.getShippingAddress().getAddressLine2());
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
        order.setItems(dtoConverter.toOrderItemEntityList(orderItems));
        // 4. Siparişi kaydetme
        Order savedOrder = orderRepository.save(order);

        // 5. Ödeme işlemi
        boolean payment = paymentService.processPayment(orderDto.getPayment(), savedOrder);
        // 6. Sipariş durumunu güncelleme
        if (payment) {
            savedOrder.setStatus(OrderStatus.PROCESSING);

            orderRepository.save(savedOrder);
            // 7. Sepeti temizleme
            basketService.clearBasket();
        } else {
            savedOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(savedOrder);
            throw new PaymentException("Payment failed, order not completed");
        }

    }
    private OrderItemDto convertBasketItemToOrderItem(BasketItemDto basketItem, Order order) {
        System.out.println("Converting BasketItem to OrderItem");
        System.out.println("BasketItem Unit Price: " + basketItem.getUnitPrice());
        System.out.println("BasketItem Total Price: " + basketItem.getTotalPrice());

        OrderItemDto orderItem = new OrderItemDto();
        orderItem.setOrderId(basketItem.getBasketId());

        orderItem.setProductId(basketItem.getProductId());
        orderItem.setQuantity(basketItem.getQuantity());
        orderItem.setUnitPrice(basketItem.getUnitPrice());
        orderItem.setTotalPrice(basketItem.getTotalPrice());

        return orderItem;
    }

    private Order initializeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
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
}





















