package com.example.food_ordering.response;

import com.example.food_ordering.dto.AddressDto;
import com.example.food_ordering.dto.OrderItemDto;
import com.example.food_ordering.dto.PaymentDto;
import com.example.food_ordering.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private String orderReferenceNumber;
    private Double totalAmount;
    private OrderStatus status;
    private Date orderDate;
    private Long userId;
    private Long paymentId;
    private String restaurantName;
    private Long restaurantId;
    private String restaurantIcon;
    private String deliveryAddress;
    private String restaurantAddress;
    private List<OrderItemDto> orderItems;

    private Double discountPercentage;
    private Double taxRate;
}
