package com.example.food_ordering.util;

import com.example.food_ordering.dto.*;
import com.example.food_ordering.entities.*;
import com.example.food_ordering.enums.OrderStatus;
import com.example.food_ordering.enums.PaymentStatus;
import com.example.food_ordering.repository.*;
import com.example.food_ordering.response.OrderResponseDto;
import com.example.food_ordering.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DTOConverter {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private UserRepository userRepository;

    public Product toProduct(ProductDto productDto) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setId(productDto.getId());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        product.setFoodImageUrls(productDto.getFoodImageUrls());

        return product;
    }

    public ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setDescription(product.getDescription());
        productDto.setCategory(product.getCategory().getName());
        productDto.setId(product.getId());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setName(product.getName());
        productDto.setFoodImageUrls(product.getFoodImageUrls());
        productDto.setRestaurantId(product.getRestaurant().getId());

        return productDto;
    }

    public Restaurant toRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantDto.getId());
        restaurant.setName(restaurantDto.getName());
        restaurant.setRating(restaurantDto.getRating());
        restaurant.setBestSeller(restaurantDto.isBestSeller());
        restaurant.setDiscountDescription(restaurantDto.getDiscountDescription());
        restaurant.setDiscountPercent(restaurantDto.getDiscountPercent());
        restaurant.setDistance(restaurantDto.getDistance());
        restaurant.setDeliveryTime(restaurantDto.getDeliveryTime());
        //restaurant.setMaxDiscountAmount(restaurantDto.getMaxDiscountAmount());
        restaurant.setRestaurantIcon(restaurantDto.getRestaurantIcon());


        return restaurant;
    }

    public RestaurantDto toRestaurantDto(Restaurant restaurant) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setRating(restaurant.getRating());
        restaurantDto.setBestSeller(restaurant.isBestSeller());
        restaurantDto.setDiscountDescription(restaurant.getDiscountDescription());
        restaurantDto.setDiscountPercent(restaurant.getDiscountPercent());
        restaurantDto.setDistance(restaurant.getDistance());
        restaurantDto.setLocation(restaurant.getLocation());
        restaurantDto.setDeliveryTime(restaurant.getDeliveryTime());
        //restaurantDto.setMaxDiscountAmount(restaurant.getMaxDiscountAmount());
        restaurantDto.setRestaurantIcon(restaurant.getRestaurantIcon());

        List<ProductDto> products = restaurant.getProducts().stream().map(this::toProductDto).toList();

        restaurantDto.setProducts(products);

        return restaurantDto;
    }

    public Basket toBasketEntity(BasketDto basketDto) {
        Basket basket = new Basket();
        basket.setId(basketDto.getId());
        basket.setTotalPrice(basketDto.getTotalPrice());
        basket.setUser(toEntity(basketDto.getUser()));
        basket.setBasketItems(basketDto.getItems().stream().map(this::toBasketItemEntity)
                .collect(Collectors.toList()));

        return basket;
    }

    public BasketDto toBasketDto(Basket basket){
        BasketDto basketDto = new BasketDto();
        basketDto.setId(basket.getId());
        basketDto.setTotalPrice(basket.getTotalPrice());
        basketDto.setUser(toDto(basket.getUser()));
        basketDto.setGrandTotal(basket.getGrandTotal());
        List<BasketItemDto> items =
                basket.getBasketItems()
                        .stream()
                        .map(this::toBasketItemDto).collect(Collectors.toList());
        basketDto.setItems(items);
        basketDto.setCreatedAt(basket.getCreatedAt());
        basketDto.setUpdatedAt(basket.getUpdatedAt());
        basketDto.setDiscount(basket.getDiscount());
        basketDto.setStatus("Active");
        basketDto.setTax(basket.getTax());

        return basketDto;
    }

    public BasketItem toBasketItemEntity(BasketItemDto basketItemDto) {
        // Retrieve the Product using the productId from the DTO
        Product product = productRepository.findById((long) basketItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        // Retrieve the Basket using the basketId from the DTO
        Basket basket = basketRepository.findById(basketItemDto.getBasketId())
                .orElseThrow(() -> new ResourceNotFoundException("Basket not found."));

        // Create the BasketItem entity
        BasketItem basketItem = new BasketItem();
        basketItem.setId(basketItemDto.getId());
        basketItem.setProduct(product);
        basketItem.setUnitPrice(basketItemDto.getUnitPrice());
        basketItem.setQuantity(basketItemDto.getQuantity());
        basketItem.setDiscount(basketItemDto.getDiscount());
        basketItem.setTotalPrice(basketItemDto.getTotalPrice());
        basketItem.setBasket(basket);

        return basketItem;
    }

    public BasketItemDto toBasketItemDto(BasketItem basketItem){
        BasketItemDto basketItemDto = new BasketItemDto();
        basketItemDto.setId(basketItem.getId());
        if(basketItem.getProduct() != null) {
            ProductDto productDto = toProductDto(basketItem.getProduct());
            basketItemDto.setProductId(basketItem.getProduct().getId());
            basketItemDto.setProductName(productDto.getName());
            basketItemDto.setProductImage(
                    productDto.getFoodImageUrls() != null && !productDto.getFoodImageUrls().isEmpty()
                    ? productDto.getFoodImageUrls().get(0) : null
            );
        }

        basketItemDto.setDescription(basketItem.getProduct().getDescription());
        basketItemDto.setUnitPrice(basketItem.getUnitPrice());
        basketItemDto.setQuantity(basketItem.getQuantity());
        if(basketItem.getBasket() != null){
            basketItemDto.setBasketId(basketItem.getBasket().getId());
        }
        basketItemDto.setDiscount(basketItem.getDiscount());
        basketItemDto.setTotalPrice(basketItem.getTotalPrice());
        return basketItemDto;
    }

    public AddressDto toAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setAddressLine1(address.getAddressLine1());
        addressDto.setAddressLine2(address.getAddressLine2());
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setZipCode(address.getZipCode());
        addressDto.setFirstName(address.getFirstName());
        addressDto.setState(address.getState());
        addressDto.setLastName(address.getLastName());
        addressDto.setPhone(address.getPhone());
        addressDto.setType(address.getType());
        User user = userRepository.findById(address.getUser().getId()).get();
        addressDto.setUserId(user.getId());

        return addressDto;
    }
    public Address toAddressEntity(AddressDto addressDto) {
        Address address = new Address();
        address.setId(addressDto.getId());
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setZipCode(addressDto.getZipCode());
        address.setFirstName(addressDto.getFirstName());
        address.setState(addressDto.getState());
        address.setLastName(addressDto.getLastName());
        address.setPhone(addressDto.getPhone());
        address.setType(addressDto.getType());
        User user = new User();
        user.setId(addressDto.getUserId());
        address.setUser(user);
        return address;
    }


    public User toEntity(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setProfileImage(userDto.getProfileImage());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        List<Address> addresses = userDto.getAddresses()
                .stream()
                .map(addressDto -> toAddressEntity(addressDto))
                .toList();
        user.setAddresses(addresses);

        return user;
    }

    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        List<AddressDto> addressDtoList =
                user.getAddresses()
                        .stream()
                        .map(addressDto -> toAddressDto(addressDto))
                        .toList();

        List<SavedCardDto> savedCardDtoList =
                user.getCards()
                                .stream()
                                        .map(card -> toSavedCardDto(card))
                                                .toList();

        userDto.setCards(savedCardDtoList);
        userDto.setAddresses(addressDtoList);

        return userDto;
    }


    public OrderItemDto toOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setProductName(orderItem.getProduct().getName());
        orderItemDto.setOrderId(orderItem.getOrder().getId());
        orderItemDto.setProductId(orderItem.getProduct().getId());
        orderItemDto.setUnitPrice(orderItem.getUnitPrice());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setTotalPrice(orderItem.getTotalPrice());

        orderItemDto.setTaxRate(orderItem.getTaxRate());
        orderItemDto.setDiscountPercentage(orderItem.getDiscountPercentage());

        return orderItemDto;
    }

    public OrderItem toOrderItemEntity(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDto.getId());

        Order order = orderRepository.findById(orderItemDto.getOrderId()).get();

        orderItem.setOrder(order);
        Product product = productRepository.findById(orderItemDto.getProductId()).orElse(null);
        orderItem.setProduct(product);
        orderItem.setUnitPrice(orderItemDto.getUnitPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setTotalPrice(orderItemDto.getTotalPrice());

        return orderItem;
    }
    public List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());
    }

    public List<OrderItem> toOrderItemEntityList(List<OrderItemDto> orderItemDtos) {
        return orderItemDtos.stream()
                .map(this::toOrderItemEntity)
                .collect(Collectors.toList());
    }

    public SavedCard toSavedCard(SavedCardDto savedCardDto) {
        SavedCard savedCard = new SavedCard();
        savedCard.setId(savedCardDto.getId());
        savedCard.setMaskedCardNumber(savedCardDto.getCardNumber());
        savedCard.setExpiryDate(savedCardDto.getExpiryDate());
        savedCard.setCvv(savedCardDto.getCvv());
        savedCard.setCardHolderName(savedCardDto.getCardHolderName());
        Optional<User> user = userRepository.findById(savedCardDto.getUserId());

        savedCard.setUser(user.get());
        return savedCard;
    }

    public SavedCardDto toSavedCardDto(SavedCard savedCard) {
        SavedCardDto savedCardDto = new SavedCardDto();
        savedCardDto.setId(savedCard.getId());
        savedCardDto.setCardHolderName(savedCard.getCardHolderName());
        savedCardDto.setCardNumber(savedCard.getMaskedCardNumber());
        savedCardDto.setExpiryDate(savedCard.getExpiryDate());
        savedCardDto.setCvv(savedCard.getCvv());
        savedCardDto.setUserId(savedCard.getUser().getId());
        return savedCardDto;
    }
    public List<SavedCardDto> toSavedCardDtoList(List<SavedCard> savedCardDtoList) {
        return savedCardDtoList.stream()
               .map(this::toSavedCardDto)
               .collect(Collectors.toList());
    }

    public List<SavedCard> toSavedCardEntityList(List<SavedCardDto> savedCardDtoList) {
        return savedCardDtoList.stream()
               .map(this::toSavedCard)
               .collect(Collectors.toList());
    }

    public Payment toPaymentEntity(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setId(paymentDto.getId());
        payment.setAmountPaid(paymentDto.getAmountPaid());
        payment.setCurrency(paymentDto.getCurrency());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setPaymentReferenceNumber(paymentDto.getPaymentReferenceNumber());
        User user = userRepository.findById(paymentDto.getUserId()).get();
        payment.setUser(user);

        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setOrder(orderRepository.findById((long) paymentDto.getId()).get());
        payment.setCardNumber(paymentDto.getCardNumber());
        payment.setExpiryDate(paymentDto.getExpirationDate());
        payment.setCvv(paymentDto.getCvv());
        payment.setCardHolderName(paymentDto.getCardHolderName());
        payment.setStatus(paymentDto.getStatus());

        return payment;
    }
    public OrderDto mapToOrderDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto orderDto = new OrderDto();
        orderDto.setRestaurantId((long) order.getRestaurant().getId());
        orderDto.setId(order.getId());
        orderDto.setOrderReferenceNumber(order.getOrderReferenceNumber());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getStatus() != null ? OrderStatus.valueOf(order.getStatus().name()) : null);
        orderDto.setOrderDate(order.getOrderDate());

        // OrderItem'ları dönüştür
        if (order.getItems() != null) {
            orderDto.setItems(
                    order.getItems()
                            .stream()
                            .map(this::toOrderItemDto)
                            .collect(Collectors.toList())
            );
        }

        if (order.getUser() != null) {
            orderDto.setUserId(order.getUser().getId());
        }


        if (order.getPayments() != null && !order.getPayments().isEmpty()) {
            orderDto.setPayments(mapToPaymentDtoList(order.getPayments()));
        }



        // Basket bilgisi
        if (order.getBasket() != null) {
            orderDto.setBasketId(Long.valueOf(order.getBasket().getId()));
        }

        // Shipping Address
        if (order.getShippingAddress() != null) {
            orderDto.setAddressId(order.getShippingAddress().getId());
            orderDto.setShippingAddress(toAddressDto(order.getShippingAddress()));
        }



        return orderDto;
    }
    public Order mapToOrderEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setOrderReferenceNumber(orderDto.getOrderReferenceNumber());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setStatus(orderDto.getStatus() != null ?
                OrderStatus.valueOf(String.valueOf(orderDto.getStatus())) : null);
        order.setOrderDate(orderDto.getOrderDate());

        if (orderDto.getItems() != null) {
            order.setItems(
                    orderDto.getItems()
                            .stream()
                            .map(this::toOrderItemEntity)
                            .collect(Collectors.toList())
            );

            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }

        if (orderDto.getUserId() != null) {
            User user = new User();
            user.setId(orderDto.getUserId());
            order.setUser(user);
        }

        if (orderDto.getPayments() != null && !orderDto.getPayments().isEmpty()) {
            List<Payment> paymentList = mapToPaymentEntityList(orderDto.getPayments());
            paymentList.forEach(payment -> payment.setOrder(order)); // Her ödeme nesnesine siparişi ata
            order.setPayments(paymentList);
        }

        if (orderDto.getBasketId() != null) {
            Basket basket = new Basket();
            basket.setId(Math.toIntExact(orderDto.getBasketId()));
            order.setBasket(basket);
        }

        // Shipping Address
        if (orderDto.getShippingAddress() != null) {
            order.setShippingAddress(toAddressEntity(orderDto.getShippingAddress()));
        }


        return order;
    }

    public List<PaymentDto> mapToPaymentDtoList(List<Payment> payments) {
        if (payments == null || payments.isEmpty()) {
            return Collections.emptyList();
        }
        return payments.stream().map(payment -> mapToPaymentDto(payment)).collect(Collectors.toList());
    }

    public List<Payment> mapToPaymentEntityList(List<PaymentDto> paymentDtos) {
        if (paymentDtos == null || paymentDtos.isEmpty()) {
            return Collections.emptyList();
        }
        return paymentDtos.stream().map(payment-> mapToPaymentEntity(payment)).collect(Collectors.toList());
    }


    public static PaymentDto mapToPaymentDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder() != null ? payment.getOrder().getId() : null);
        dto.setCurrency(payment.getCurrency());
        dto.setUserId(payment.getUser() != null ? payment.getUser().getId() : null);
        dto.setPaymentReferenceNumber(payment.getPaymentReferenceNumber());
        dto.setStatus(payment.getStatus() != null ? PaymentStatus.valueOf(payment.getStatus().name()) : null);
        dto.setAmountPaid(payment.getAmountPaid());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());

        // Kredi kartı bilgileri
        dto.setCardNumber(payment.getCardNumber());
        dto.setCardHolderName(payment.getCardHolderName());
        dto.setExpirationDate(payment.getExpiryDate());
        dto.setCvv(payment.getCvv());

        return dto;
    }
    public Payment mapToPaymentEntity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(dto.getId());

        // Order
        if (dto.getOrderId() != null) {
            Order order = new Order();
            order.setId(dto.getOrderId());
            payment.setOrder(order);
        }

        payment.setCurrency(dto.getCurrency());

        // User
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            payment.setUser(user);
        }

        payment.setPaymentReferenceNumber(dto.getPaymentReferenceNumber());
        payment.setStatus(dto.getStatus() != null ? PaymentStatus.valueOf(String.valueOf(dto.getStatus())) : null);
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentDate(dto.getPaymentDate());

        // Kredi kartı bilgileri
        payment.setCardNumber(dto.getCardNumber());
        payment.setCardHolderName(dto.getCardHolderName());
        payment.setExpiryDate(dto.getExpirationDate());
        payment.setCvv(dto.getCvv());

        return payment;
    }

    public OrderResponseDto toOrderResponseDto(OrderDto orderDto) {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(orderDto.getId());
        response.setOrderReferenceNumber(orderDto.getOrderReferenceNumber());
        response.setTotalAmount(orderDto.getTotalAmount());
        response.setStatus(orderDto.getStatus());
        response.setOrderDate(orderDto.getOrderDate());
        response.setUserId(orderDto.getUserId());
        Restaurant restaurant = restaurantRepository.findById(orderDto.getRestaurantId()).get();
        response.setRestaurantIcon(restaurant.getRestaurantIcon());
        response.setRestaurantName(restaurant.getName());
        response.setRestaurantId(orderDto.getRestaurantId());

        for (OrderItemDto orderItem : orderDto.getItems()){
            response.setTaxRate(orderItem.getTaxRate());
            response.setDiscountPercentage(orderItem.getDiscountPercentage());
        }


        response.setRestaurantAddress(restaurant.getLocation());
        response.setDeliveryAddress(orderDto.getShippingAddress().getAddressLine1() +"," +orderDto.getShippingAddress().getCity()+"/"+orderDto.getShippingAddress().getCountry());
        response.setOrderItems(orderDto.getItems());


        return response;
    }
}






















