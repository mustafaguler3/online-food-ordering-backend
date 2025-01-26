package com.example.food_ordering.util;

import com.example.food_ordering.entities.DiscountCode;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.entities.Restaurant;
import com.example.food_ordering.enums.DiscountType;
import com.example.food_ordering.repository.DiscountCodeRepository;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

//@Component
public class DataSeed implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final DiscountCodeRepository discountCodeRepository;

    public DataSeed(RestaurantRepository restaurantRepository, ProductRepository productRepository, DiscountCodeRepository discountCodeRepository) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.discountCodeRepository = discountCodeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedData();

        DiscountCode discountCode1 = new DiscountCode();
        discountCode1.setCode("SUMMER10");
        discountCode1.setDiscountValue(10); // %10 indirim
        discountCode1.setActive(true);
        discountCode1.setType(DiscountType.PERCENTAGE); // Yüzde indirim
        discountCode1.setValidFrom(LocalDateTime.now().minusDays(1)); // Geçerlilik başlama zamanı
        discountCode1.setValidUntil(LocalDateTime.now().plusMonths(1)); // Geçerlilik bitiş zamanı
        discountCodeRepository.save(discountCode1); // Veritabanına kaydet

        DiscountCode discountCode2 = new DiscountCode();
        discountCode2.setCode("FLAT50");
        discountCode2.setDiscountValue(50); // 50 TL indirim
        discountCode2.setActive(true);
        discountCode2.setType(DiscountType.AMOUNT); // Sabit indirim
        discountCode2.setValidFrom(LocalDateTime.now().minusDays(1)); // Geçerlilik başlama zamanı
        discountCode2.setValidUntil(LocalDateTime.now().plusMonths(2)); // Geçerlilik bitiş zamanı
        discountCodeRepository.save(discountCode2); // Veritabanına kaydet
    }

    private void seedData() {
        if (restaurantRepository.count() == 0) {
            List<Restaurant> restaurants = createRestaurants();
            restaurantRepository.saveAll(restaurants);
        }
    }

    private List<Restaurant> createRestaurants() {
        Restaurant r1 = new Restaurant();
        r1.setName("Dominos");
        r1.setRating(4.5);
        r1.setLocation("Istanbul");
        r1.setDistance(2.4);
        r1.setDeliveryTime(20);
        r1.setBestSeller(true);
        r1.setDiscountDescription("upto $2 50% OFF");
        r1.setDiscountPercent(50.0);
        //r1.setMaxDiscountAmount(2.0);
        r1.setRestaurantIcon("domino.png");

        Product p1 = new Product();
        p1.setName("Sucuk Sever Pizza");
        p1.setDescription("Bol malzemeli sucuklu pizza");
        p1.setPrice(15.99);
        p1.setFoodImageUrls(Arrays.asList("sucuk-sever.png", "sucuk-sever2.jpg"));
        p1.setRestaurant(r1);

        Product p2 = new Product();
        p2.setName("Garlic Bread");
        p2.setDescription("Tereyağlı sarımsaklı ekmek");
        p2.setPrice(5.99);
        p2.setFoodImageUrls(Arrays.asList("sarimsak-ekmek.png", "sarimsak-ekmek2.png"));
        p2.setRestaurant(r1);

        r1.setProducts(Arrays.asList(p1, p2));

        // Similar for other restaurants (r2, r3, ..., r10) and their products
        Restaurant r2 = new Restaurant();
        r2.setName("Burger King");
        // Set other fields for r2 and its products...

        // Repeat for 8 more restaurants (r3 to r10)...

        return Arrays.asList(r1, r2 /*, r3, r4, ..., r10 */);
    }
}
