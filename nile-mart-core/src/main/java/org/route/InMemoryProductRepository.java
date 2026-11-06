package org.route;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryProductRepository implements ProductRepository{

    private final HashMap<String, Product> store = new HashMap<>();


    @PostConstruct
    void saveProducts() {
        Product p1 = Product.builder()
                .name("Smartphone X5 128GB")
                .sku("PH-X5-128")
                .price(BigDecimal.valueOf(16000))
                .category("Electronics")
                .isActive(true)
                .stockQuantity(12)
                .build();
        Product p2 = Product.builder()
                .name("Fast Charger 20W")
                .sku("ACC-CHG-20W")
                .price(BigDecimal.valueOf(650))
                .category("Laptop")
                .isActive(true)
                .stockQuantity(40)
                .build();

        Product p3 =  Product.builder()
                .name("Wireless Earbuds")
                .sku("ACC-BUDS-6")
                .price(BigDecimal.valueOf(1100))
                .category("Electronics")
                .isActive(true)
                .stockQuantity(8)
                .build();

        Product p4=  Product.builder()
                .name("Cotton Galabeya")
                .sku("FSH-GAL-001")
                .price(BigDecimal.valueOf(900))
                .category("Fashion")
                .isActive(true)
                .stockQuantity(25)
                .build();

        Product p5=  Product.builder()
                .name("Non-stick Frying Pan 28cm")
                .sku("HOM-PAN-28")
                .price(BigDecimal.valueOf(1200))
                .category("Home & Kitchen")
                .isActive(true)
                .stockQuantity(3)
                .build();

        Product p6=  Product.builder()
                .name("Wooden Spice Rack")
                .sku("HOM-SPC-RCK")
                .price(BigDecimal.valueOf(750))
                .category("Home & Kitchen")
                .isActive(true)
                .stockQuantity(10)
                .build();

        Product p7=  Product.builder()
                .name("Sun Cream SPF50")
                .sku("BTY-SUN-50")
                .price(BigDecimal.valueOf(350))
                .category("Beauty")
                .isActive(true)
                .stockQuantity(0)
                .build();
        Product p8=  Product.builder()
                .name("The Cairo Trilogy")
                .sku("BK-CAI-TRI")
                .price(BigDecimal.valueOf(450))
                .category("Books")
                .isActive(true)
                .stockQuantity(15)
                .build();



        store.put(p1.getSku(), p1);
        store.put(p2.getSku(), p2);
        store.put(p3.getSku(), p3);
        store.put(p4.getSku(), p4);
        store.put(p5.getSku(), p5);
        store.put(p6.getSku(), p6);
        store.put(p7.getSku(), p7);
        store.put(p8.getSku(), p8);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(store.get(sku));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public void save(Product product) {
        store.put(product.getSku(), product);
    }

}
