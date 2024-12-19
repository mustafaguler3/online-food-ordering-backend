package com.example.food_ordering.util;

import com.example.food_ordering.dto.*;
import com.example.food_ordering.entities.*;
import com.example.food_ordering.repository.BasketRepository;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.RestaurantRepository;
import com.example.food_ordering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                .collect(Collectors.toSet()));

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

        return basketDto;
    }

    public BasketItem toBasketItemEntity(BasketItemDto basketItemDto){
        BasketItem basketItem = new BasketItem();
        basketItem.setId(basketItemDto.getId());
        Optional<Product> product = productRepository.findById(basketItemDto.getProductId());
        basketItem.setProduct(product.get());
        basketItem.setQuantity(basketItemDto.getQuantity());

        Basket basket = basketRepository.findById(basketItemDto.getBasketId()).get();
        basketItem.setBasket(basket);


        return basketItem;
    }

    public BasketItemDto toBasketItemDto(BasketItem basketItem){
        BasketItemDto basketItemDto = new BasketItemDto();
        basketItemDto.setId(basketItem.getId());
        if(basketItem.getProduct() != null) {
            ProductDto productDto = toProductDto(basketItem.getProduct());
            basketItemDto.setProductId((long) basketItem.getProduct().getId());
            basketItemDto.setProductName(productDto.getName());
            basketItemDto.setProductImage(
                    productDto.getFoodImageUrls() != null && !productDto.getFoodImageUrls().isEmpty()
                    ? productDto.getFoodImageUrls().get(0) : null
            );
        }

        basketItemDto.setPrice(basketItem.getUnitPrice());
        basketItemDto.setQuantity(basketItem.getQuantity());
        if(basketItem.getBasket() != null){
            basketItemDto.setBasketId(basketItem.getBasket().getId());
        }

        basketItemDto.setDiscount(basketItem.getDiscount());
        basketItemDto.setTotalPrice(basketItem.getTotalPrice());
        return basketItemDto;
    }

    public AddressDto addressDto(Address address) {
        AddressDto addressDto = new AddressDto();
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
    public Address address(AddressDto addressDto) {
        Address address = new Address();
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
                .map(addressDto -> address(addressDto))
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
                        .map(addressDto -> addressDto(addressDto))
                        .toList();

        userDto.setAddresses(addressDtoList);

        return userDto;
    }
}






















